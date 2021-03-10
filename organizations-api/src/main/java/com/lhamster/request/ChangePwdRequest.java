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
@ApiModel("修改密码入参")
public class ChangePwdRequest implements Serializable {
    @ApiModelProperty(value = "原密码", example = "123456")
    private String oldPwd;

    @ApiModelProperty(value = "新密码", example = "987654")
    private String newPwd;
}
