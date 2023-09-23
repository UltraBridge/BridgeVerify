package org.bridge.common.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;
import org.bridge.common.entity.DepositTransaction;
import org.bridge.common.mapper.DepositTransactionMapper;

@Component
public class DepositTransactionDao extends ServiceImpl<DepositTransactionMapper, DepositTransaction> {

}
