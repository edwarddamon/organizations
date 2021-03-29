package com.lhamster.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/14
 */
@ApiModel(value = "社团详细信息透出")
@Data
@Builder
public class OrgOrganizationInfoResponse implements Serializable {
    /**
     * 社团id
     */
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
    private List<String> organIntroductionDetailAvatars;

    /**
     * 社团星级（3-5对应3星-5星）
     */
    private Integer organStar;

    /**
     * 社长
     */
    private OrgUserInfoResponse minister;

    /**
     * 副社长
     */
    private List<OrgUserInfoResponse> viceMinisters;

    /**
     * 团支书
     */
    private OrgUserInfoResponse secretary;

    /**
     * 当前用户和该社团的关系（1：已加入，2：已申请）
     */
    private Integer status;

    /**
     * 当前用户是否是该社团的社长、副社或团支书
     */
    private Boolean identity;

    /**
     * 当前用户身份 0:社员；1:财务；2:副社或团支书：3:社长;
     */
    private Integer myJob;

    /**
     * 社团申请注销理由
     */
    private String cancelReason;
}
