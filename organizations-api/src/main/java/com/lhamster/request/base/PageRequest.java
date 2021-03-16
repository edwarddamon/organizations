package com.lhamster.request.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/14
 */
@Data
@ApiModel(value = "分页入参")
@NoArgsConstructor
@AllArgsConstructor
public class PageRequest implements Serializable {
    @ApiModelProperty(value = "当前页", notes = "默认第一页")
    private Integer pageNo = 1;

    @ApiModelProperty(value = "每页数目", notes = "默认每页10条数据")
    private Integer pageSize = 10;
}
