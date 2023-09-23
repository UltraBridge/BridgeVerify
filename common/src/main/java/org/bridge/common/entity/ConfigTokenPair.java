package org.bridge.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ConfigTokenPair implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Long chainId;

    private String bridgeAddress;

    private String token1;

    private String token1Name;

    private String minFee;

    private String maxFee;

    private Boolean enabled;

    private Long targetChainId;

    private String token2;

    private String token2Name;

    private String lastHash;

    private Date createTime;

    private Date updateTime;
}
