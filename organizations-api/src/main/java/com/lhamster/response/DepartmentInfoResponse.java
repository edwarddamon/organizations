package com.lhamster.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/13
 */
@ApiModel(value = "部门详情透出")
@Data
@Builder
public class DepartmentInfoResponse implements Serializable {
    /**
     * 部门id
     */
    private Long depId;

    /**
     * 部门名
     */
    private String depName;

    /**
     * 部长
     */
    private OrgUserInfoResponse depMinister;

    /**
     * 副部长
     */
    private OrgUserInfoResponse depViceMinister;

    /**
     * 社团id
     */
    private Long depOrganizationId;
}
