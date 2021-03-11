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
public class OrgOrganization implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 社团id
     */
    @TableId(value = "organ_id", type = IdType.AUTO)
    private Long organId;

    /**
     * 社团封面地址
     */
    private String organAvatar;

    /**
     * 社团名
     */
    private String organName;

    /**
     * 社费
     */
    private Long organFunds;

    /**
     * 社团简介
     */
    private String organIntroduction;

    /**
     * 社团详细介绍
     */
    private String organIntroductionDetail;

    /**
     * 社团详细介绍图片地址（数组）
     */
    private String organIntroductionDetailAvatar;

    /**
     * 社团星级（3-5对应3星-5星）
     */
    private Integer organStar;

    /**
     * 社团状态（undetermined

、normal、Cancelled）
     */
    private String organStatus;

    /**
     * 社团申请注销理由
     */
    private String organCancelReason;

    /**
     * 拒绝申请理由
     */
    private String organRefuseReason;

    private LocalDateTime createAt;

    private String createBy;

    private LocalDateTime updateAt;

    private String updateBy;


}
