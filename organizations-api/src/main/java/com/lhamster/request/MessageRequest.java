package com.lhamster.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/9
 */
@Data
@ApiModel(value = "验证码请求")
public class MessageRequest implements Serializable {
    @ApiModelProperty(value = "手机号", required = true)
    private String phone;

    @ApiModelProperty(value = "类型", example = "0:注册;1:修改密码", required = true)
    private Integer type;
}
