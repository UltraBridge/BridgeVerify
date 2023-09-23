package org.bridge.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ConfigBridge implements Serializable {

    private static final long serialVersionUID = 1L;

    public interface Status {
        int Invalid = 0;
        int Valid = 1;
    }

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Long chainId;

    private Long targetChainId;

    private String bridgeAddress;

    private BigDecimal feeRate;

    private String gasPrice;

    private String gasLimit;

    private Long blockStep;

    private Long listenToBlock;
    private Long blockDelay;

    private Integer rpcConfirmRequire;

    private Integer status;

    private Date createTime;

    private Date updateTime;
}
