package com.lhamster.request;

import com.lhamster.request.base.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "注册入参")
public class OrgUserRequest extends PageRequest {
    @ApiModelProperty(value = "用户名", notes = "用于模糊查询", example = "edward")
    private String username;
}
