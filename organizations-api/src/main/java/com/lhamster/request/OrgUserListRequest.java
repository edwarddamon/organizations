package com.lhamster.request;

import com.lhamster.request.base.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "社团用户列表入参")
public class OrgUserListRequest extends PageRequest {
    @ApiModelProperty(value = "社团id")
    @NotNull(message = "社团id不能为空")
    private Long orgId;

    @ApiModelProperty(value = "用户名", notes = "用于模糊查询", example = "edward")
    private String username;
}
