package com.lhamster.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
@EqualsAndHashCode(callSuper = false)
public class OrgActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "act_id", type = IdType.AUTO)
    private Long actId;

    /**
     * 活动名称
     */
    private String actName;

    /**
     * 封面地址
     */
    private String actAvatar;

    /**
     * 开始时间
     */
    private LocalDateTime actBeginTime;

    /**
     * 结束时间
     */
    private LocalDateTime actEndTime;

    /**
     * 活动地址
     */
    private String actAddress;

    /**
     * 活动内容
     */
    private String actContent;

    /**
     * 浏览量
     */
    private Integer actViews;

    /**
     * 社团id
     */
    private Long actOrganizationId;

    /**
     * 人员限制id
     */
    private Long actLimitUserId;

    /**
     * 多个社团联和举办时存储可参加社团的id
     */
    private String actLimitOrgIds;

    private LocalDateTime createAt;

    private String createBy;

    private LocalDateTime updateAt;

    private String updateBy;


}
