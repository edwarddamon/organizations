package com.lhamster.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/17
 */
@Data
@ApiModel(value = "创建活动入参")
public class OrgCreateActivityRequest implements Serializable {
    @ApiModelProperty(value = "社团id", notes = "当前发布的社团id", required = true)
    @NotNull(message = "社团id不能为空")
    private Long orgId;

    @ApiModelProperty(value = "活动名称", required = true)
    @NotBlank(message = "活动名称不能为空")
    private String activityName;

    @ApiModelProperty(value = "活动地点", required = true)
    @NotBlank(message = "活动地点不能为空")
    private String activityAddress;

    @ApiModelProperty(value = "活动开始时间", required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "活动开始时间不能为空")
    private Date startTime;

    @ApiModelProperty(value = "活动结束时间", required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "活动结束时间不能为空")
    private Date endTime;

    @ApiModelProperty(value = "限制id", required = true)
    @NotNull(message = "限制id不能为空")
    private Long limitId;

    @ApiModelProperty(value = "活动内容", required = true)
    @NotBlank(message = "活动内容不能为空")
    private String activityContent;

    @ApiModelProperty(value = "需要的经费", notes = "单位分：如果需要50元，则传5000")
    @Min(value = 0, message = "活动社费不能小于0")
    private Long funds;
}
