package com.lhamster.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/13
 */
@ApiModel(value = "更新部门名称")
@Data
public class UpdateDepartmentRequest implements Serializable {
    @ApiModelProperty(value = "社团id", required = true)
    @NotNull(message = "社团id不能为空")
    private Long orgId;

    @ApiModelProperty(value = "部门id", required = true)
    @NotNull(message = "部门id不能为空")
    private Long depId;

    @ApiModelProperty(value = "部门名称")
    private String depName;
}
