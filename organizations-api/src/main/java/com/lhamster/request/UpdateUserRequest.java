package com.lhamster.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/11
 */
@Data
@ApiModel(value = "更新用户信息入参")
public class UpdateUserRequest implements Serializable {

    @ApiModelProperty(value = "昵称", example = "damon")
    private String nickName;

    @ApiModelProperty(value = "性别", example = "0:女 | 1:男")
    private Integer sex;

    @ApiModelProperty(value = "QQ", example = "2810315142")
    private String QQ;

    @ApiModelProperty(value = "vx", example = "damon_edward")
    private String vx;
}
