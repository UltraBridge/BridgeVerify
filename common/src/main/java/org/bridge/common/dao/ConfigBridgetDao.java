package org.bridge.common.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.bridge.common.entity.ConfigBridge;
import org.bridge.common.mapper.ConfigBridgeMapper;
import org.springframework.stereotype.Component;
import org.bridge.common.Constants;
import org.bridge.common.util.ConfigBridgeCSVParser;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Component
public class ConfigBridgetDao extends ServiceImpl<ConfigBridgeMapper, ConfigBridge> {

    @Resource
    ConfigBridgeCSVParser configBridgeCSVParser;

    public void reloadFromFile() throws Exception {
        ConfigBridge update = new ConfigBridge();
        update.setStatus(ConfigBridge.Status.Invalid);
        update(update, null);

        List<ConfigBridge> list = configBridgeCSVParser.parse(Constants.BridgeConfigPath, true);
        for (ConfigBridge configBridge : list) {
            ConfigBridge found = getOne(Wrappers.<ConfigBridge>lambdaQuery()
                    .eq(ConfigBridge::getChainId, configBridge.getChainId())
                    .eq(ConfigBridge::getTargetChainId, configBridge.getTargetChainId()));
            if (found != null) {
                if (configBridge.getListenToBlock() == 0) {
                    configBridge.setListenToBlock(null);
                }
                configBridge.setId(found.getId());
                configBridge.setUpdateTime(new Date());
                updateById(configBridge);
            } else {
                configBridge.setCreateTime(new Date());
                configBridge.setUpdateTime(new Date());
                save(configBridge);
            }
        }
    }
}
