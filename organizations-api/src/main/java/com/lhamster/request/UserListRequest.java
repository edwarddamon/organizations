package com.lhamster.request;

import com.lhamster.request.base.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/29
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "用户列表入参")
public class UserListRequest extends PageRequest {
    @ApiModelProperty(value = "用户名", notes = "用于模糊查询", example = "edward")
    private String username;
}
