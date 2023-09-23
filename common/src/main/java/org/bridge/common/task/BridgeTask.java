package org.bridge.common.task;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bridge.common.contracts.ChainUtil;
import org.bridge.common.dao.ConfigBridgetDao;
import org.bridge.common.dao.ConfigDao;
import org.bridge.common.dao.DepositRecordDao;
import org.bridge.common.entity.ConfigBridge;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlockNumber;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class BridgeTask {

    @Resource
    ChainUtil chainUtil;
    @Resource
    ConfigBridgetDao configBridgetDao;
    @Resource
    DepositRecordDao depositRecordDao;

    @Resource
    ConfigDao configDao;

    final Set<Integer> IdSet = new HashSet<>();
    ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 6, 2, TimeUnit.SECONDS, new ArrayBlockingQueue<>(2), new ThreadPoolExecutor.DiscardOldestPolicy());

    @Scheduled(fixedRate = 5000)
    public void syncDepositEvent() throws Exception {
        configDao.checkConfigFile();

        log.info("syncDepositEvent() start");
        List<ConfigBridge> bridges = configBridgetDao.list(Wrappers.<ConfigBridge>lambdaQuery()
                .eq(ConfigBridge::getStatus, ConfigBridge.Status.Valid));
        bridges.forEach(bridge -> {
            synchronized (IdSet) {
                if (IdSet.contains(bridge.getId())) {
                    return;
                }
                IdSet.add(bridge.getId());

                executor.submit(() -> {
                    try {
                        syncEvents(bridge);
                    } catch (Exception e) {
                        log.error("syncDepositEvent()", e);
                    } finally {
                        IdSet.remove(bridge.getId());
                    }
                });
            }
        });
        log.info("syncDepositEvent() end");
    }

    public void syncEvents(ConfigBridge bridge) throws Exception {
        if (StringUtils.isBlank(bridge.getBridgeAddress())) {
            return;
        }
        Web3j web3j = chainUtil.getWeb3j(bridge.getChainId());
        EthBlockNumber ethBlockNumber = web3j.ethBlockNumber().send();
        BigInteger currentBlock = ethBlockNumber.getBlockNumber();

        BigInteger blockStep = BigInteger.valueOf(bridge.getBlockStep());
        if (blockStep.compareTo(BigInteger.ZERO) <= 0) {
            log.error("syncEvents() error blockStep:{}", blockStep);
            return;
        }
        currentBlock = currentBlock.subtract(BigInteger.valueOf(bridge.getBlockDelay()));
        log.info("syncEvents() bridge.id:{}, blockStep:{}, currentBlock:{}", bridge.getId(), blockStep, currentBlock);

        BigInteger fromBlock;
        if (bridge.getListenToBlock() <= 0) {
            fromBlock = currentBlock.subtract(BigInteger.ONE);
        } else {
            fromBlock = BigInteger.valueOf(bridge.getListenToBlock()).add(BigInteger.ONE);
        }
        while (fromBlock.compareTo(currentBlock) <= 0) {
            BigInteger toBlock = fromBlock.add(blockStep);
            if (toBlock.compareTo(currentBlock) > 0) {
                toBlock = currentBlock.add(BigInteger.ONE);
            }
            log.info("syncEvents() bridge.id:{}, fromBlock:{}, toBlock:{}", bridge.getId(), fromBlock, toBlock);
            depositRecordDao.syncAndSave(bridge, web3j, fromBlock, toBlock);
            fromBlock = toBlock;
        }
    }
}
