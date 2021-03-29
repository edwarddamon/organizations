package com.lhamster.response;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/14
 */
@ApiModel(value = "社团列表信息透出")
@Data
@Builder
public class OrgOrganizationListInfoResponse implements Serializable {
    /*
     * 社团id
     * */
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
     * 社团星级
     */
    private Integer organStar;

    /**
     * 社团简介
     */
    private String organIntroduction;

    /**
     * 社长名
     */
    private String orgMinisterName;
}
