package com.lhamster.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@ApiModel(value = "活动列表透出")
@Data
@Builder
public class OrgActivityListInfoResponse implements Serializable {
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
     * 浏览量
     */
    private Integer actViews;

    /**
     * 社团id
     */
    private Long actOrganizationId;

    /**
     * 社团名
     */
    private String actOrganizationName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;
}
