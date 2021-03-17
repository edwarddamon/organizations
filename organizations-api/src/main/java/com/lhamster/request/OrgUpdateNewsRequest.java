package com.lhamster.request;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/18
 */

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel(value = "更新新闻入参")
public class OrgUpdateNewsRequest implements Serializable {
    @ApiModelProperty(value = "新闻id", required = true)
    @NotNull(message = "新闻id不能为空")
    private Long newId;

    @ApiModelProperty(value = "新闻标题")
    private String title;

    @ApiModelProperty(value = "新闻内容")
    private String content;
}
