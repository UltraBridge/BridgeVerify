package org.bridge.common.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;
import org.bridge.common.entity.ConfigAddress;
import org.bridge.common.mapper.ConfigAddressMapper;

@Component
public class ConfigAddressDao extends ServiceImpl<ConfigAddressMapper, ConfigAddress> {

}
