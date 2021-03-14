package com.lhamster.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.lang.reflect.Member;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/13
 */
@Data
@ApiModel(value = "入职/解雇入参")
public class OfficeDepartmentRequest implements Serializable {
    @ApiModelProperty(value = "社团id", required = true)
    @NotNull(message = "社团id不能为空")
    private Long orgId;

    @ApiModelProperty(value = "部门id", required = true)
    @NotNull(message = "部门id不能为空")
    private Long depId;

    @ApiModelProperty(value = "被操作用户id", required = true)
    @NotNull(message = "被操作用户id不能为空")
    private Long targetId;

    @ApiModelProperty(value = "上任/下任", notes = "上任：true，下任：false", required = true)
    @NotNull(message = "操作结果不能为空")
    private Boolean judge;

    @ApiModelProperty(value = "职位", example = "技能部：部长：1；副部长：2", notes = "非社长、副社长、团支书和财务的时候才需要传")
    private Integer position;
}
