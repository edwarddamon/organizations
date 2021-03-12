package com.lhamster.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/12
 */
@Data
@ApiModel("申请注销社团入参")
public class CancelOrganizationRequest implements Serializable {
    @ApiModelProperty(value = "社团id", example = "1", required = true)
    @NotNull(message = "社团id不能为空")
    private Long id;

    @ApiModelProperty(value = "申请注销原因", example = "巴拉啦巴拉巴拉巴拉啦", required = true)
    @NotBlank(message = "申请注销理由不能为空")
    private String reason;
}
