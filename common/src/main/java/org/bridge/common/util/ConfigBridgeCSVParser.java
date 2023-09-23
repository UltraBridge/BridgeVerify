package org.bridge.common.util;

import lombok.extern.slf4j.Slf4j;
import org.bridge.common.entity.ConfigBridge;
import org.chain.common.base.exception.BusinessException;
import org.chain.common.util.data.csv.CSVParserTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConfigBridgeCSVParser extends CSVParserTemplate<ConfigBridge> {

    @Override
    protected ConfigBridge parseRow(String[] items) {
        if (items.length < 9) {
            throw new BusinessException("parseRow() parse config.chain.file, items.length < 9");
        }
        ConfigBridge configBridge = new ConfigBridge();
        configBridge.setChainId(Long.parseLong(items[0].trim()));
        configBridge.setTargetChainId(Long.parseLong(items[1].trim()));
        configBridge.setBridgeAddress(items[2].trim().toLowerCase());
        configBridge.setGasPrice(items[3].trim());
        configBridge.setGasLimit(items[4].trim());
        configBridge.setBlockStep(Long.parseLong(items[5].trim()));
        configBridge.setListenToBlock(Long.parseLong(items[6].trim()));
        configBridge.setBlockDelay(Long.parseLong(items[7].trim()));
        configBridge.setRpcConfirmRequire(Integer.parseInt(items[8].trim()));
        configBridge.setStatus(Integer.parseInt(items[9].trim()));
        return configBridge;
    }
}
