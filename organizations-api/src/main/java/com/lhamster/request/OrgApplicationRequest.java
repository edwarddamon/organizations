package com.lhamster.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/14
 */
@Data
@ApiModel(value = "申请入社入参")
public class OrgApplicationRequest implements Serializable {
    @ApiModelProperty(value = "社团id", example = "123456789", required = true)
    @NotNull(message = "社团id不能为空")
    private Long orgId;

    @ApiModelProperty(value = "申请理由", example = "123456789")
    private String reason;
}
