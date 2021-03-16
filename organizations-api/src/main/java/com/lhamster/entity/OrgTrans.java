package com.lhamster.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author Edward
 * @since 2021-03-09
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class OrgTrans implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "tra_id", type = IdType.AUTO)
    private Long traId;

    /**
     * 变动金额
     */
    private Long traChangeAmount;

    /**
     * 变动原因
     */
    private String traReason;

    /**
     * 当前余额
     */
    private Long traAmount;

    /**
     * 社团id
     */
    private Long traOrgId;

    private LocalDateTime createAt;

    private String createBy;

    private LocalDateTime updateAt;

    private String updateBy;


}
