package com.lhamster.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/16
 */
@Data
@Builder
@ApiModel(value = "加减经费入参")
public class OrgFundRequest implements Serializable {
    @ApiModelProperty(value = "社团id", required = true)
    @NotNull(message = "社团id不能为空")
    private Long orgId;

    @ApiModelProperty(value = "经费", notes = "单位是分，加传正数，减传负数", required = true)
    @NotNull(message = "经费不能为空")
    private Long funds;

    @ApiModelProperty(value = "经费变动原因")
    private String reason;
}
