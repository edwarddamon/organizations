package com.lhamster.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/11
 */
@ApiModel(value = "用户信息透出")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrgUserInfoResponse implements Serializable {
    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "用户头像地址")
    private String userAvatar;

    @ApiModelProperty(value = "用户名")
    private String userUsername;

    @ApiModelProperty(value = "性别")
    private Integer userSex;

    @ApiModelProperty(value = "手机号")
    private String userPhone;

    @ApiModelProperty(value = "QQ号")
    private String userQq;

    @ApiModelProperty(value = "微信")
    private String userVx;

    @ApiModelProperty(value = "是否为社联管理员（主席）")
    private Boolean identity;

    /**
     * 身份
     */
    private String depName;
}
