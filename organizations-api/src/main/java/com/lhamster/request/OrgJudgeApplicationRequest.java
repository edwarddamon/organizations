package com.lhamster.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/15
 */
@Data
@ApiModel(value = "审批入社申请入参")
public class OrgJudgeApplicationRequest implements Serializable {
    @ApiModelProperty(value = "审批id", example = "1", required = true)
    @NotNull(message = "审批id不能为空")
    private Long appId;

    @ApiModelProperty(value = "审批结果", example = "true:同意；false:拒绝", required = true)
    @NotNull(message = "审批结果不能为空")
    private Boolean res;

    @ApiModelProperty(value = "拒绝原因", example = "不给进，没理由")
    private String reason;
}
