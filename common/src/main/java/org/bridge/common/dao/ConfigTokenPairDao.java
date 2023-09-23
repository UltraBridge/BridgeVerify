package org.bridge.common.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;
import org.bridge.common.contracts.Bridge;
import org.bridge.common.entity.ConfigTokenPair;
import org.bridge.common.entity.DepositRecord;
import org.bridge.common.mapper.ConfigTokenPairMapper;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ConfigTokenPairDao extends ServiceImpl<ConfigTokenPairMapper, ConfigTokenPair> {

    public void syncPair(long chainId, Bridge.TokenPairEventResponse tokenPairEvent) {
        ConfigTokenPair tokenPair = new ConfigTokenPair();
        tokenPair.setChainId(chainId);
        tokenPair.setBridgeAddress(tokenPairEvent.log.getAddress());
        tokenPair.setToken1(tokenPairEvent.ownToken);
        tokenPair.setToken1Name(tokenPairEvent.ownTokenName);
        tokenPair.setMinFee(tokenPairEvent.ownWithdrawMinFee.toString());
        tokenPair.setMaxFee(tokenPairEvent.ownWithdrawMaxFee.toString());
        tokenPair.setEnabled(tokenPairEvent.enabled);
        tokenPair.setTargetChainId(tokenPairEvent.toChainId.longValue());
        tokenPair.setToken2(tokenPairEvent.toToken);
        tokenPair.setToken2Name(tokenPairEvent.toTokenName);
        tokenPair.setLastHash(tokenPairEvent.log.getTransactionHash());
        tokenPair.setUpdateTime(new Date());

        ConfigTokenPair found = getOne(Wrappers.<ConfigTokenPair>lambdaQuery()
                .eq(ConfigTokenPair::getChainId, tokenPair.getChainId())
                .eq(ConfigTokenPair::getBridgeAddress, tokenPair.getBridgeAddress())
                .eq(ConfigTokenPair::getToken1, tokenPair.getToken1()));
        if (found != null) {
            tokenPair.setId(found.getId());
            updateById(tokenPair);
        } else {
            tokenPair.setCreateTime(new Date());
            save(tokenPair);
        }
    }

    public void syncPair(long chainId, String bridgeAddress, List<Bridge.Pair> pairs) {
        List<ConfigTokenPair> pairsInDb = list(Wrappers.<ConfigTokenPair>lambdaQuery()
                .eq(ConfigTokenPair::getChainId, chainId)
                .eq(ConfigTokenPair::getBridgeAddress, bridgeAddress));
        Map<String, ConfigTokenPair> pairMap = new HashMap<>();
        pairsInDb.forEach(pair -> {
            pairMap.put(pair.getToken1(), pair);
        });

        pairs.forEach(pair -> {
            ConfigTokenPair tokenPair = new ConfigTokenPair();
            tokenPair.setChainId(chainId);
            tokenPair.setBridgeAddress(bridgeAddress);
            tokenPair.setToken1(pair.ownToken);
            tokenPair.setToken1Name(pair.ownTokenName);
            tokenPair.setMinFee(pair.ownWithdrawMinFee.toString());
            tokenPair.setMaxFee(pair.ownWithdrawMaxFee.toString());
            tokenPair.setEnabled(pair.enabled);
            tokenPair.setTargetChainId(pair.toChainId.longValue());
            tokenPair.setToken2(pair.toToken);
            tokenPair.setToken2Name(pair.toTokenName);
            tokenPair.setUpdateTime(new Date());
            ConfigTokenPair pairInDb = pairMap.get(tokenPair.getToken1());
            if (pairInDb != null) {
                tokenPair.setId(pairInDb.getId());
                updateById(tokenPair);
                pairMap.remove(tokenPair.getToken1());
            } else {
                tokenPair.setCreateTime(new Date());
                save(tokenPair);
            }
        });

        pairMap.forEach((token, pair) -> {
            removeById(pair.getId());
        });
    }

    public ConfigTokenPair getPairByDepositRecord(DepositRecord record) {
        return getOne(Wrappers.<ConfigTokenPair>lambdaQuery()
                .eq(ConfigTokenPair::getChainId, record.getChainId())
                .eq(ConfigTokenPair::getBridgeAddress, record.getBridgeAddress())
                .eq(ConfigTokenPair::getToken1, record.getToken())
                .eq(ConfigTokenPair::getEnabled, true));
    }
}
