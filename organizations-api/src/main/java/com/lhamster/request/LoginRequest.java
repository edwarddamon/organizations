package com.lhamster.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/10
 */
@Data
@ApiModel(value = "登录入参")
public class LoginRequest implements Serializable {
    @ApiModelProperty(value = "手机号", example = "13612345678", required = true)
    private String phone;

    @ApiModelProperty(value = "密码", example = "123567", required = true)
    private String password;
}
