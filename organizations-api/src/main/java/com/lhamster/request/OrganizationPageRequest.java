package com.lhamster.request;

import com.lhamster.request.base.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Member;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "分页社团信息入参")
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationPageRequest extends PageRequest {
    @ApiModelProperty(value = "社团名称", notes = "用于模糊查询")
    private String name;

    @ApiModelProperty(value = "社团状态",
            notes = "dead:申请创建失败、undetermined:申请创建待审批、normal:正常、cancel:申请注销待审批、cancelled:已经注销",
            required = true)
    @NotBlank(message = "社团状态不能为空")
    private String status;

    @ApiModelProperty(value = "排序方式", notes = "1:最新；2:星级", required = true)
    @NotNull(message = "排序方式不能为空")
    private Integer sortBy;
}
