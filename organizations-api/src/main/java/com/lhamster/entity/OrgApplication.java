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
public class OrgApplication implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 入社申请id
     */
    @TableId(value = "app_id", type = IdType.AUTO)
    private Long appId;

    /**
     * 申请状态（undetermined、AGREE、REFUSE）
     */
    private String appStatus;

    /**
     * 申请理由
     */
    private String appApplicationReason;

    /**
     * 拒绝理由
     */
    private String appRefuseReason;

    /**
     * 申请的社团id
     */
    private Long appOrgId;

    /**
     * 申请用户id
     */
    private Long appUserId;

    private LocalDateTime createAt;

    private String createBy;

    private LocalDateTime updateAt;

    private String updateBy;


}
