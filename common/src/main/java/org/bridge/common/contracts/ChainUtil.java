package org.bridge.common.contracts;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.bridge.common.dao.ConfigChainDao;
import org.bridge.common.entity.ConfigChain;
import org.chain.common.base.exception.BusinessException;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Numeric;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@SuppressWarnings("unchecked")
public class ChainUtil {

    static Map<String, Web3j> web3Cache = new HashMap<>();

    @Resource
    ConfigChainDao configChainDao;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ContractEnum<T extends Contract> {
        String name;
        Class<T> contract;

        public static ContractEnum<Bridge> Bridge = new ContractEnum<>("Bridge", Bridge.class);
        public static ContractEnum<BridgeStorage> BridgeStorage = new ContractEnum<>("BridgeStorage", BridgeStorage.class);
    }

    public Web3j getWeb3j(Long chainId) {
        return getWeb3j(chainId, ConfigChain.Type.Main).get(0);
    }

    public List<Web3j> getWeb3j(Long chainId, ConfigChain.Type type) {
        List<ConfigChain> configChainList = configChainDao.list(Wrappers.<ConfigChain>lambdaQuery()
                .eq(ConfigChain::getChainId, chainId)
                .eq(ConfigChain::getType, type.name()));
        if (CollectionUtils.isEmpty(configChainList)) {
            throw new BusinessException("no rpc config found for chainId:" + chainId);
        }
        ArrayList<Web3j> list = new ArrayList<>();
        for (ConfigChain configChain : configChainList) {
            list.add(getWeb3j(configChain.getRpc()));
        }
        return list;
    }

    public Web3j getWeb3j(String rpc) {
        Web3j web3j = web3Cache.get(rpc);
        if (web3j == null) {
            synchronized (this) {
                web3j = web3Cache.get(rpc);
                if (web3j == null) {
                    OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(120, TimeUnit.SECONDS)
                            .readTimeout(120, TimeUnit.SECONDS)
                            .build();
                    web3j = Web3j.build(new HttpService(rpc, client));
                    web3Cache.put(rpc, web3j);
                }
            }
        }
        return web3j;
    }

    public <T extends Contract> T loadContract(ContractEnum<T> contractEnum, long chainId, String contractAddress, String key, ContractGasProvider gasProvider) {
        Credentials credentials = Credentials.create(Numeric.cleanHexPrefix(key));
        try {
            if (gasProvider == null) {
                gasProvider = new DefaultGasProvider();
            }
            Web3j web3j = getWeb3j(chainId);
            final RawTransactionManager transactionManager = new RawTransactionManager(web3j, credentials, chainId);
            return loadContract(contractEnum, contractAddress, web3j, transactionManager, gasProvider);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends Contract> T loadContract(ContractEnum<T> contractEnum, String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) throws Exception {
        Method method = contractEnum.getContract().getMethod("load", String.class, Web3j.class, Credentials.class, ContractGasProvider.class);
        return (T) method.invoke(null, contractAddress, web3j, credentials, contractGasProvider);
    }

    public static <T extends Contract> T loadContract(ContractEnum<T> contractEnum, String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) throws Exception {
        Method method = contractEnum.getContract().getMethod("load", String.class, Web3j.class, TransactionManager.class, ContractGasProvider.class);
        return (T) method.invoke(null, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public <T extends Contract> T loadReadonlyContract(ContractEnum<T> contractEnum, long chainId, String contractAddress) {
        try {
            return loadContract(contractEnum, contractAddress, getWeb3j(chainId), Credentials.create("0"), null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public TransactionReceipt getTransaction(Web3j web3j, String hash) {
        try {
            EthGetTransactionReceipt transactionReceipt = web3j.ethGetTransactionReceipt(hash).send();
            if (transactionReceipt.getError() != null) {
                throw new BusinessException(transactionReceipt.getError().getMessage());
            }
            return transactionReceipt.getTransactionReceipt().orElseThrow(() -> new BusinessException("error tx"));
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    public void confirmTransaction(List<Web3j> web3jList, String hash, Integer rpcConfirmRequire) {
        rpcConfirmRequire = rpcConfirmRequire > 0 ? rpcConfirmRequire : (web3jList.size() + 1) >> 1;
        int success = 0;
        for (Web3j _web3j : web3jList) {
            try {
                if (getTransaction(_web3j, hash).isStatusOK()) {
                    success++;
                }
            } catch (Exception e) {
                log.error("confirm error", e);
            }
            if (success >= rpcConfirmRequire) {
                break;
            }
        }
        if (success < rpcConfirmRequire) {
            throw new BusinessException(String.format("transaction confirmed error, hash:%s", hash));
        }
    }
}