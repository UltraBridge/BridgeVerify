package org.bridge.common.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.bridge.common.entity.*;
import org.bridge.common.mapper.DepositRecordMapper;
import org.chain.common.base.exception.BusinessException;
import org.chain.common.util.Helper;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.bridge.common.Constants;
import org.bridge.common.contracts.Bridge;
import org.bridge.common.contracts.ChainUtil;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Numeric;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DepositRecordDao extends ServiceImpl<DepositRecordMapper, DepositRecord> {

    @Resource
    ChainUtil chainUtil;
    @Resource
    ConfigBridgetDao configBridgetDao;
    @Resource
    ConfigTokenPairDao configTokenPairDao;
    @Resource
    DepositTransactionDao depositTransactionDao;
    @Resource
    WithdrawConfirmationDao confirmationDao;

    static BigDecimal FeeRateBase = BigDecimal.valueOf(10000);

    @Transactional(rollbackFor = Exception.class)
    public void syncAndSave(ConfigBridge configBridge, Web3j web3j, BigInteger fromBlock, BigInteger toBlock) throws Exception {
        String bridgeAddress = configBridge.getBridgeAddress().toLowerCase();
        Long chainId = configBridge.getChainId();
        List<Web3j> web3jList = chainUtil.getWeb3j(chainId, ConfigChain.Type.Slave);
        Bridge bridge = chainUtil.loadReadonlyContract(ChainUtil.ContractEnum.Bridge, chainId, bridgeAddress);

        List<DepositTransaction> depositTransactions = new ArrayList<>();

        List<DepositRecord> _depositRecords = new ArrayList<>();
        List<WithdrawConfirmation> _confirmations = new ArrayList<>();
        List<BigInteger> blocks = new ArrayList<>();
        if (Constants.CustomChainIds.contains(configBridge.getChainId())) {
            for (; fromBlock.compareTo(toBlock) < 0; fromBlock = fromBlock.add(BigInteger.ONE)) {
                blocks.add(fromBlock);
            }
            blocks.parallelStream().forEach(block -> {
                EthBlock ethBlock;
                try {
                    ethBlock = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(block), false).send();
                } catch (IOException e) {
                    throw new BusinessException(e);
                }
                if (ethBlock.getError() != null) {
                    throw new BusinessException("block sync error, " + ethBlock.getError().getMessage());
                }
                List<EthBlock.TransactionResult> transactions = ethBlock.getBlock().getTransactions();
                List<String> hashes = new ArrayList<>();
                for (EthBlock.TransactionResult each : transactions) {
                    EthBlock.TransactionHash transaction = (EthBlock.TransactionHash) each;
                    hashes.add(transaction.get());
                }
                Date blockTime = new Date(ethBlock.getBlock().getTimestamp().longValue() * 1000);
                hashes.parallelStream().forEach(hash -> {
                    TransactionReceipt transactionReceipt = chainUtil.getTransaction(web3j, hash);
                    if (!transactionReceipt.isStatusOK()) {
                        return;
                    }
                    transactionReceipt.setLogs(transactionReceipt.getLogs().stream().filter(log1 -> log1.getAddress().equals(bridgeAddress)).collect(Collectors.toList()));
                    List<Bridge.SetWithdrawFeeRateEventResponse> feeRateEvents = bridge.getSetWithdrawFeeRateEvents(transactionReceipt);
                    List<Bridge.DepositEventResponse> depositEvents = bridge.getDepositEvents(transactionReceipt);
                    List<Bridge.WithdrawEventResponse> withdrawEvents = bridge.getWithdrawEvents(transactionReceipt);

                    // confirm
                    if (CollectionUtils.isNotEmpty(feeRateEvents) || CollectionUtils.isNotEmpty(depositEvents) ||
                            CollectionUtils.isNotEmpty(withdrawEvents)) {
                        chainUtil.confirmTransaction(web3jList, hash, configBridge.getRpcConfirmRequire());

                        DepositTransaction depositTransaction = new DepositTransaction();
                        depositTransaction.setChainId(chainId);
                        depositTransaction.setHash(hash);
                        depositTransaction.setBlock(block.longValue());
                        depositTransaction.setBlockTime(blockTime);
                        depositTransaction.setCreateTime(new Date());
                        depositTransactions.add(depositTransaction);
                    }
                    if (CollectionUtils.isNotEmpty(feeRateEvents)) {
                        feeRateEvents.forEach(event -> {
                            configBridge.setFeeRate(new BigDecimal(event.withdrawFeeRate).divide(FeeRateBase, RoundingMode.DOWN));
                        });
                    }
                    if (CollectionUtils.isNotEmpty(depositEvents)) {
                        for (Bridge.DepositEventResponse event : depositEvents) {
                            _depositRecords.add(wrapDepositRecord(configBridge, event, blockTime));
                        }
                    }
                    if (CollectionUtils.isNotEmpty(withdrawEvents)) {
                        for (Bridge.WithdrawEventResponse event : withdrawEvents) {
                            _confirmations.add(wrapConfirmation(chainId, event, transactionReceipt.getFrom(), hash, blockTime));
                        }
                    }
                });
            });
        } else {
            Map<String, TransactionInfo> transactions = new HashMap<>();

            DefaultBlockParameter _fromBlock = DefaultBlockParameter.valueOf(fromBlock);
            DefaultBlockParameter _toBlock = DefaultBlockParameter.valueOf(toBlock.subtract(BigInteger.ONE));
            List<Bridge.SetWithdrawFeeRateEventResponse> feeRateEvents = bridge.getSetWithdrawFeeRateEvents(_fromBlock, _toBlock);
            List<Bridge.DepositEventResponse> depositEvents = bridge.getDepositEvents(_fromBlock, _toBlock);
            List<Bridge.WithdrawEventResponse> withdrawEvents = bridge.getWithdrawEvents(_fromBlock, _toBlock);
            if (CollectionUtils.isNotEmpty(feeRateEvents)) {
                for (Bridge.SetWithdrawFeeRateEventResponse event : feeRateEvents) {
                    getReceipt(transactions, event.log, web3j, web3jList, configBridge.getRpcConfirmRequire());
                    configBridge.setFeeRate(new BigDecimal(event.withdrawFeeRate).divide(FeeRateBase, RoundingMode.DOWN));
                }
            }
            if (CollectionUtils.isNotEmpty(depositEvents)) {
                for (Bridge.DepositEventResponse event : depositEvents) {
                    TransactionInfo receipt = getReceipt(transactions, event.log, web3j, web3jList, configBridge.getRpcConfirmRequire());
                    _depositRecords.add(wrapDepositRecord(configBridge, event, receipt.getBlockTime()));
                }
            }
            if (CollectionUtils.isNotEmpty(withdrawEvents)) {
                for (Bridge.WithdrawEventResponse event : withdrawEvents) {
                    TransactionInfo transactionInfo = getReceipt(transactions, event.log, web3j, web3jList, configBridge.getRpcConfirmRequire());
                    _confirmations.add(wrapConfirmation(chainId, event, transactionInfo.getReceipt().getFrom(), transactionInfo.getReceipt().getTransactionHash(), transactionInfo.getBlockTime()));
                }
            }

            for (Map.Entry<String, TransactionInfo> entry : transactions.entrySet()) {
                TransactionInfo transactionInfo = entry.getValue();
                DepositTransaction depositTransaction = new DepositTransaction();
                depositTransaction.setChainId(chainId);
                depositTransaction.setHash(transactionInfo.getReceipt().getTransactionHash());
                depositTransaction.setBlock(transactionInfo.getBlock());
                depositTransaction.setBlockTime(transactionInfo.getBlockTime());
                depositTransaction.setCreateTime(new Date());
                depositTransactions.add(depositTransaction);
            }
        }
        List<Bridge.Pair> pairs = new ArrayList<>();
        BigInteger length = Helper.retryIfFail(() -> bridge.getTokenPairCount().send());
        for (BigInteger index = BigInteger.ZERO; index.compareTo(length) < 0; index = index.add(BigInteger.ONE)) {
            BigInteger finalIndex = index;
            Bridge.Pair pair = Helper.retryIfFail(() -> bridge.getTokenPair(finalIndex).send());
            if (pair.toChainId.longValue() != 0) {
                pairs.add(pair);
            }
        }
        configTokenPairDao.syncPair(chainId, bridgeAddress, pairs);

        for (DepositTransaction depositTransaction : depositTransactions) {
            depositTransactionDao.save(depositTransaction);
        }
        for (DepositRecord record : _depositRecords) {
            log.info("saveTokenTx() chainId:{},bridgeAddress:{}, hash:{}", chainId, bridgeAddress, record.getHash());
            save(record);
        }
        for (WithdrawConfirmation confirmation : _confirmations) {
            confirmationDao.save(confirmation);
            if (confirmation.getStatus() == DepositRecord.TaskStatus.Done) {
                DepositRecord update = new DepositRecord();
                update.setWithdrawTime(confirmation.getCreateTime());
                update.setFee(confirmation.getFee());
                update(update, Wrappers.<DepositRecord>lambdaQuery()
                        .eq(DepositRecord::getTaskHash, confirmation.getTaskHash()));
            }
        }
        configBridge.setListenToBlock(toBlock.subtract(BigInteger.ONE).longValue());
        configBridgetDao.updateById(configBridge);
    }

    private WithdrawConfirmation wrapConfirmation(Long chainId, Bridge.WithdrawEventResponse event, String operator, String confirmHash, Date blockTime) {
        WithdrawConfirmation confirmation = new WithdrawConfirmation();
        confirmation.setChainId(chainId);
        confirmation.setTaskHash(Numeric.toHexString(event.taskHash));
        confirmation.setToken(event.token);
        confirmation.setTo(event.to);
        confirmation.setValue(event.value.toString());
        confirmation.setFee(event.fee.toString());
        confirmation.setProof(event.proof);
        confirmation.setStatus(event.status.intValue());
        confirmation.setOperator(operator);
        confirmation.setConfirmHash(confirmHash);
        confirmation.setCreateTime(blockTime);
        return confirmation;
    }

    private DepositRecord wrapDepositRecord(ConfigBridge configBridge, Bridge.DepositEventResponse event, Date blockTime) {
        DepositRecord record = new DepositRecord();
        record.setChainId(configBridge.getChainId());
        record.setBridgeAddress(configBridge.getBridgeAddress().toLowerCase());
        record.setHash(event.log.getTransactionHash());
        record.setToken(event.token);
        record.setFromAddress(event.from);
        record.setTo(event.to);
        record.setChain(event.chain);
        record.setValue(event.value.toString());
        record.setTaskHash(Numeric.toHexString(record.getTaskHashBytes()));
        record.setDepositTime(blockTime);
        record.setStatus(DepositRecord.Status.Create);
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        return record;
    }

    @Nullable
    private TransactionInfo getReceipt(Map<String, TransactionInfo> transactions, Log log, Web3j web3j, List<Web3j> web3jList, int rpcConfirmRequire) throws Exception {
        String transactionHash = log.getTransactionHash();
        TransactionInfo transactionInfo = transactions.get(transactionHash);
        if (transactionInfo != null) {
            return transactionInfo;
        }
        TransactionReceipt transactionReceipt = chainUtil.getTransaction(web3j, transactionHash);
        if (!transactionReceipt.isStatusOK()) {
            throw new BusinessException("transaction status error, " + transactionReceipt.getTransactionHash());
        }
        chainUtil.confirmTransaction(web3jList, transactionHash, rpcConfirmRequire);

        transactionInfo = new TransactionInfo();
        transactionInfo.setReceipt(transactionReceipt);
        transactionInfo.setBlock(log.getBlockNumber().intValue());

        EthBlock ethBlock;
        ethBlock = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(log.getBlockNumber()), false).send();
        if (ethBlock.getError() != null) {
            throw new BusinessException("block sync error, " + ethBlock.getError().getMessage());
        }
        transactionInfo.setBlockTime(new Date(ethBlock.getBlock().getTimestamp().longValue() * 1000));
        transactions.put(transactionHash, transactionInfo);
        return transactionInfo;
    }

    @Data
    static class TransactionInfo {
        TransactionReceipt receipt;
        long block;
        Date blockTime;
    }
}
