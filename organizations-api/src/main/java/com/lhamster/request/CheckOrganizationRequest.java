package com.lhamster.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/12
 */
@ApiModel(value = "审批社团入参")
@Data
public class CheckOrganizationRequest implements Serializable {
    @ApiModelProperty(value = "社团id", example = "1", required = true)
    @NotNull(message = "社团id不能为空")
    private Long id;

    @ApiModelProperty(value = "审批结果", example = "0:同意申请，1:拒绝申请", required = true)
    @NotNull(message = "审批结果不能为空")
    private Integer result;

    @ApiModelProperty(value = "拒绝理由", example = "社团发展较好，请慎重考虑", notes = "这里是拒绝申请注销的理由")
    private String reason;
}