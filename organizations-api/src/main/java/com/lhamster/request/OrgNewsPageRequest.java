package com.lhamster.request;

import com.lhamster.request.base.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "新闻分页入参")
public class OrgNewsPageRequest extends PageRequest {
    @ApiModelProperty(value = "新闻标题")
    private String title;
}
