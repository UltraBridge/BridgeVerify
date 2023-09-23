package org.bridge.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DepositRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    public interface Status {
        int Create = 0;
        int Confirmed = 1;
    }

    public interface TaskStatus {
        int Init = 0;
        int Processing = 1;
        int Cancelled = 2;
        int Done = 3;
    }

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Long chainId;

    private String bridgeAddress;

    private String hash;

    private String token;

    private String fromAddress;

    @TableField("`to`")
    private String to;

    private String chain;

    private String value;

    private String taskHash;

    private Date depositTime;

    private String confirmHash;

    private String fee;

    private Integer status;

    private Date withdrawTime;

    private Date createTime;

    private Date updateTime;

    public BigInteger getSendValue() {
        return new BigDecimal(getValue()).toBigInteger();
    }

    public String getProof() {
        return String.format("%s_%s_%s_1", getChain(), Numeric.cleanHexPrefix(getToken()), Numeric.cleanHexPrefix(getHash()));
    }

    public byte[] getTaskHashBytes() {
        byte[] bytes = Numeric.hexStringToByteArray(getTo());
        byte[] bytes1 = Numeric.toBytesPadded(getSendValue(), 32);
        byte[] bytes2 = getProof().getBytes();

        byte[] result = new byte[bytes.length + bytes1.length + bytes2.length];
        System.arraycopy(bytes, 0, result, 0, bytes.length);
        System.arraycopy(bytes1, 0, result, bytes.length, bytes1.length);
        System.arraycopy(bytes2, 0, result, bytes.length + bytes1.length, bytes2.length);
        return Hash.sha3(result);
    }
}
