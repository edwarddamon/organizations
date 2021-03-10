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
@ApiModel(value = "注册入参")
public class RegisterRequest implements Serializable {
    @ApiModelProperty(value = "用户名", example = "edward", required = true)
    private String username;

    @ApiModelProperty(value = "密码", example = "123456789", required = true)
    private String password;

    @ApiModelProperty(value = "手机号", example = "13612345678", required = true)
    private String phone;

    @ApiModelProperty(value = "验证码", example = "123456", required = true)
    private String code;
}
