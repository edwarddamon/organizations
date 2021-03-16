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
 * @date 2021/3/16
 */
@Data
@ApiModel(value = "更新公告入参")
public class OrgUpdateBoardRequest implements Serializable {
    @ApiModelProperty(value = "公告id", required = true)
    @NotNull(message = "公告id不能为空")
    private Long boaId;

    @ApiModelProperty(value = "公告内容", required = true)
    @NotBlank(message = "公告内容不能为空")
    private String content;
}