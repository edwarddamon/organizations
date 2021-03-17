package com.lhamster.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/17
 */
@Data
@ApiModel(value = "发布新闻入参")
public class OrgCreateNewsRequest implements Serializable {
    @ApiModelProperty(value = "社团id", required = true)
    @NotNull(message = "社团id不能为空")
    private Long orgId;

    @ApiModelProperty(value = "新闻标题", required = true)
    @NotBlank(message = "新闻标题不能为空")
    private String title;

    @ApiModelProperty(value = "新闻内容", required = true)
    @NotBlank(message = "新闻内容不能为空")
    private String content;
}
