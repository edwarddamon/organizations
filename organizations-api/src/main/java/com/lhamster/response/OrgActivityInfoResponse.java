package com.lhamster.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lhamster.entity.OrgLimit;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/17
 */
@ApiModel(value = "活动详情透出")
@Data
@Builder
public class OrgActivityInfoResponse implements Serializable {
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime actBeginTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
     * 经费
     */
    private Long actFunds;

    /**
     * 社团id
     */
    private Long actOrganizationId;

    /**
     * 社团名
     */
    private String actOrganizationName;

    /**
     * 社团封面地址
     */
    private String actOrganizationAvatar;

    /**
     * 人员限制id
     */
    private OrgLimit orgLimit;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;
}
