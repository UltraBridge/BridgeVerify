package org.bridge.common.util;

import lombok.extern.slf4j.Slf4j;
import org.bridge.common.entity.ConfigChain;
import org.chain.common.base.exception.BusinessException;
import org.chain.common.util.data.csv.CSVParserTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConfigChainCSVParser extends CSVParserTemplate<ConfigChain> {

    @Override
    protected ConfigChain parseRow(String[] items) {
        if (items.length < 3) {
            throw new BusinessException("parseRow() parse config.chain.file, items.length < 3");
        }
        ConfigChain configChain = new ConfigChain();
        configChain.setChainId(Long.parseLong(items[0].trim()));
        configChain.setRpc(items[1].trim());
        configChain.setType(items[2].trim());
        return configChain;
    }
}