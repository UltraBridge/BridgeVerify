package org.bridge.common.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.bridge.common.mapper.WithdrawConfirmationMapper;
import org.springframework.stereotype.Component;
import org.bridge.common.entity.WithdrawConfirmation;

@Component
public class WithdrawConfirmationDao extends ServiceImpl<WithdrawConfirmationMapper, WithdrawConfirmation> {

}
