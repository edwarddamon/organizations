package com.lhamster.request;

import com.lhamster.request.base.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "分页社团信息入参")
@NoArgsConstructor
@AllArgsConstructor
public class MyOrganizationPageRequest extends PageRequest {
    @ApiModelProperty(value = "社团名称", notes = "用于模糊查询")
    private String name;
}
