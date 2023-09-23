package org.bridge.common.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.bridge.common.mapper.ConfigChainMapper;
import org.springframework.stereotype.Component;
import org.bridge.common.Constants;
import org.bridge.common.util.ConfigChainCSVParser;
import org.bridge.common.entity.ConfigChain;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ConfigChainDao extends ServiceImpl<ConfigChainMapper, ConfigChain> {

    @Resource
    ConfigChainCSVParser configChainCSVParser;

    public void reloadFromFile() throws Exception {
        remove(null);

        List<ConfigChain> list = configChainCSVParser.parse(Constants.ChainConfigPath, true);
        for (ConfigChain configChain : list) {
            save(configChain);
        }
    }
}
