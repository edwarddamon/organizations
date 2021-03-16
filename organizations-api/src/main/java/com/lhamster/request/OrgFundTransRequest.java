package com.lhamster.request;

import com.lhamster.request.base.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "分页查询社费入参")
public class OrgFundTransRequest extends PageRequest {
    @ApiModelProperty(value = "社团id", required = true)
    @NotNull(message = "社团id不能为空")
    private Long orgId;

    @ApiModelProperty(value = "开始时间", example = "2021-3-16 20:10:10")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String startTime;

    @ApiModelProperty(value = "结束时间", example = "2021-3-17 20:10:10")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String endTime;
}
