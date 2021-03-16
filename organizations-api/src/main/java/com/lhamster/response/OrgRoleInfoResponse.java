package com.lhamster.response;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/16
 */
@ApiModel(value = "用户信息透出")
@Data
@Builder
public class OrgRoleInfoResponse implements Serializable {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户头像地址
     */
    private String userAvatar;

    /**
     * 用户头名
     */
    private String userName;

    /**
     * 用户手机号
     */
    private String userPhone;

    /**
     * 用户身份
     */
    private String userIdentity;
}
