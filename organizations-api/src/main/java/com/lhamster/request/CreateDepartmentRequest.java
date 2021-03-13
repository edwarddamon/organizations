package com.lhamster.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/13
 */
@ApiModel(value = "新增部门入参")
@Data
public class CreateDepartmentRequest implements Serializable {
    @ApiModelProperty(value = "社团id", required = true)
    @NotNull(message = "社团id不能为空")
    private Long orgId;

    @ApiModelProperty(value = "部门名称", required = true)
    @NotBlank(message = "部门名称不能为空")
    private String depName;
}
