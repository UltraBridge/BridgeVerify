package org.bridge.common.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.bridge.common.Constants;
import org.bridge.common.entity.Config;
import org.bridge.common.mapper.ConfigMapper;
import org.chain.common.base.exception.BusinessException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.util.Arrays;

@Slf4j
@Component
public class ConfigDao extends ServiceImpl<ConfigMapper, Config> {

    @Resource
    ConfigChainDao configChainDao;
    @Resource
    ConfigBridgetDao configBridgetDao;

    public Config getByName(String name, boolean throwIfNull) {
        Config config = getOne(Wrappers.<Config>lambdaQuery()
                .eq(Config::getName, name));
        if (throwIfNull && config == null) {
            throw new BusinessException("cannot find config:" + name);
        }
        return config;
    }

    public Config getByKey(Config.Keys key, boolean throwIfNull) {
        return getByName(key.name(), throwIfNull);
    }

    public Config getByKey(Config.Keys key) {
        return getByKey(key, true);
    }

    public BigDecimal getFeeRate() {
        return new BigDecimal(getByKey(Config.Keys.FeeRate).getValue());
    }

    public synchronized void checkConfigFile() throws Exception {
        for (String path : Arrays.asList(Constants.ChainConfigPath, Constants.BridgeConfigPath)) {
            Config config = getByName(path, false);
            if (config == null) {
                config = new Config();
                config.setName(path);
                config.setValue("");
                save(config);
            }
            String md5 = getFileMD5(path);
            if (!md5.equals(config.getValue())) {
                if (path.equals(Constants.ChainConfigPath)) {
                    configChainDao.reloadFromFile();
                } else {
                    configBridgetDao.reloadFromFile();
                }
                config.setValue(md5);
                updateById(config);
            }
        }
    }

    public static String getFileMD5(String file) {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            MessageDigest MD5 = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[8192];
            int length;
            while ((length = fileInputStream.read(buffer)) != -1) {
                MD5.update(buffer, 0, length);
            }
            return new String(Hex.encodeHex(MD5.digest()));
        } catch (Exception e) {
            log.error("getFileMD5() error", e);
            throw new BusinessException("error getFileMD5");
        }
    }
}
