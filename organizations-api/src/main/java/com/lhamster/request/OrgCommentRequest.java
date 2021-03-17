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
 * @date 2021/3/18
 */
@Data
@ApiModel(value = "发布新闻评论入参")
public class OrgCommentRequest implements Serializable {
    @ApiModelProperty(value = "新闻id", required = true)
    @NotNull(message = "新闻id不能为空")
    private Long newId;

    @ApiModelProperty(value = "目标用户id", notes = "若没有目标则传 -1", required = true)
    @NotNull(message = "目标用户id不能为空")
    private Long targetUserId;

    @ApiModelProperty(value = "评论内容", required = true)
    @NotBlank(message = "评论内容不能为空")
    private String content;

}
