package org.bridge.scheduler.task;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.chain.common.util.AESUtil;
import org.chain.common.util.Helper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.bridge.common.contracts.Bridge;
import org.bridge.common.contracts.BridgeStorage;
import org.bridge.common.contracts.ChainUtil;
import org.bridge.common.dao.ConfigBridgetDao;
import org.bridge.common.dao.ConfigDao;
import org.bridge.common.dao.ConfigTokenPairDao;
import org.bridge.common.dao.DepositRecordDao;
import org.bridge.common.entity.ConfigBridge;
import org.bridge.common.entity.ConfigChain;
import org.bridge.common.entity.ConfigTokenPair;
import org.bridge.common.entity.DepositRecord;
import org.web3j.abi.datatypes.Address;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tx.gas.StaticGasProvider;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class BridgeConfirmTask {

    @Resource
    ChainUtil chainUtil;
    @Resource
    ConfigBridgetDao configBridgetDao;
    @Resource
    ConfigTokenPairDao configTokenPairDao;
    @Resource
    DepositRecordDao depositRecordDao;

    @Resource
    ConfigDao configDao;

    @Scheduled(cron = "0/5 * * * * *")
    public void confirm() throws Exception {
        configDao.checkConfigFile();

        log.info("confirm() start");
        List<DepositRecord> transactionList = depositRecordDao.list(Wrappers.<DepositRecord>lambdaQuery()
                .eq(DepositRecord::getStatus, DepositRecord.Status.Create));
        transactionList.forEach(record -> {
            try {
                if (StringUtils.isBlank(record.getTo())) {
                    log.error("confirm() blank target address,{}-{}", record.getChainId(), record.getHash());
                    return;
                }
                ConfigBridge bridge = configBridgetDao.getOne(Wrappers.<ConfigBridge>lambdaQuery()
                        .eq(ConfigBridge::getChainId, record.getChainId())
                        .eq(ConfigBridge::getBridgeAddress, record.getBridgeAddress())
                        .eq(ConfigBridge::getStatus, ConfigBridge.Status.Valid));
                if (bridge == null) {
                    log.error("confirm() bridge error, record.id:{}", record.getId());
                    return;
                }
                ConfigTokenPair pair = configTokenPairDao.getPairByDepositRecord(record);
                if (pair == null) {
                    log.error("confirm() pair not found, record.id:{}", record.getId());
                    return;
                }
                ConfigBridge targetBridge = configBridgetDao.getOne(Wrappers.<ConfigBridge>lambdaQuery()
                        .eq(ConfigBridge::getChainId, pair.getTargetChainId())
                        .eq(ConfigBridge::getTargetChainId, record.getChainId())
                        .eq(ConfigBridge::getStatus, ConfigBridge.Status.Valid));
                if (targetBridge == null) {
                    log.error("confirm() target bridge error, record.id:{}", record.getId());
                    return;
                }

                StaticGasProvider gasProvider = new StaticGasProvider(new BigInteger(targetBridge.getGasPrice()), new BigInteger(targetBridge.getGasLimit()));

                List<String> keys = new ArrayList<>();
                try (BufferedReader reader = new BufferedReader(new FileReader("/d/tmp/.tmp"))) {
                    String key;
                    while ((key = reader.readLine()) != null) {
                        if (StringUtils.isBlank(key)) {
                            continue;
                        }
                        key = AESUtil.decryptCBC(key, "hello");
                        if (StringUtils.isBlank(key)) {
                            log.error("confirm() error blank key");
                            return;
                        }
                        keys.add(key);
                    }
                } catch (Exception e) {
                    log.error("confirm() error occurred in decrypt keys", e);
                    return;
                }
                if (keys.size() == 0) {
                    log.error("confirm() error must require one key");
                    return;
                }
                List<Web3j> web3jList = chainUtil.getWeb3j(targetBridge.getChainId(), ConfigChain.Type.Slave);
                for (String key : keys) {
                    if (!confirm(record, targetBridge, gasProvider, pair.getToken2(), web3jList, key)) {
                        return;
                    }
                }

                record.setStatus(DepositRecord.Status.Confirmed);
                record.setUpdateTime(new Date());
                depositRecordDao.updateById(record);
            } catch (Exception e) {
                log.error("confirm() error", e);
            }
        });
        log.info("confirm() finish");
    }

    public boolean confirm(DepositRecord record, ConfigBridge targetBridge, StaticGasProvider gasProvider, String targetToken, List<Web3j> web3jList, String key) {
        String proof = record.getProof();
        BigInteger sendValue = record.getSendValue();
        byte[] taskHash = record.getTaskHashBytes();
        Credentials credentials = Credentials.create(key);
        Bridge bridge = chainUtil.loadContract(ChainUtil.ContractEnum.Bridge, targetBridge.getChainId(), targetBridge.getBridgeAddress(), key, gasProvider);

        String storageAddress = Helper.retryIfFail(() -> bridge.getStoreAddress().send());
        BridgeStorage bridgeStorage = chainUtil.loadReadonlyContract(ChainUtil.ContractEnum.BridgeStorage, targetBridge.getChainId(), storageAddress);

        Tuple3<BigInteger, BigInteger, BigInteger> taskInfo = Helper.retryIfFail(() -> bridgeStorage.getTaskInfo(record.getTaskHashBytes()).send());
        if (taskInfo.component2().intValue() == DepositRecord.TaskStatus.Done) {
            return true;
        }
        Boolean exist = Helper.retryIfFail(() -> bridgeStorage.supporterExists(taskHash, credentials.getAddress()).send());
        if (exist) {
            return true;
        }
        try {
            TransactionReceipt transactionReceipt;
            if (Address.DEFAULT.toString().equals(targetToken)) {
                transactionReceipt = bridge.withdrawNative(record.getTo(), sendValue, proof, taskHash).send();
            } else {
                transactionReceipt = bridge.withdrawToken(targetToken, record.getTo(), sendValue, proof, taskHash).send();
            }
            if (transactionReceipt.isStatusOK()) {
                chainUtil.confirmTransaction(web3jList, transactionReceipt.getTransactionHash(), targetBridge.getRpcConfirmRequire());
                String transactionHash = transactionReceipt.getTransactionHash();
                log.info("confirm(), hash:{}, confirmHash:{}", record.getHash(), transactionHash);
                record.setConfirmHash(record.getConfirmHash() + "," + transactionHash);
                return true;
            } else {
                log.error("confirm() error, hash:{}, transactionReceipt:{}", record.getHash(), new Gson().toJson(transactionReceipt));
            }
        } catch (Exception e) {
            log.error("confirm() error", e);
        }
        return false;
    }
}
