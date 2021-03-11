package com.lhamster.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/11
 */
@Data
@ApiModel(value = "创建社团入参")
public class CreateOrganizationRequest implements Serializable {
    @ApiModelProperty(value = "社团封面地址", hidden = true)
    private String avatarUrl;

    @ApiModelProperty(value = "社团名称", required = true)
    @NotBlank(message = "社团名称不能为空")
    private String name;

    @ApiModelProperty(value = "社团简介", required = true)
    @NotBlank(message = "社团简介不能为空")
    private String introduction;
}
