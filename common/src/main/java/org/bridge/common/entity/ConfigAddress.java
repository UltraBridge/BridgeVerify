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
public class ConfigAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum Type {
        Singer, ERC20
    }

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Long chainId;

    private String type;

    private String name;

    private String address;

    private String hash;

    private BigDecimal balance;

    private Integer threshold;

    private Date notifyTime;

    private Date createTime;
}
