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
@Data
@ApiModel(value = "更新用户信息入参")
public class UpdateOrganizationRequest implements Serializable {
    @ApiModelProperty(value = "社团id", required = true)
    @NotNull
    private String orgId;

    @ApiModelProperty(value = "社团名", notes = "更新后的社团名")
    private String orgName;

    @ApiModelProperty(value = "社团详细介绍")
    private String detailIntroduction;
}
