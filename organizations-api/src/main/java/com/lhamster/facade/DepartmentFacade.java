package com.lhamster.facade;

import com.lhamster.entity.OrgDepartment;
import com.lhamster.request.CreateDepartmentRequest;
import com.lhamster.request.UpdateDepartmentRequest;
import com.lhamster.response.DepartmentInfoResponse;
import com.lhamster.response.result.Response;

import java.util.List;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/13
 */
public interface DepartmentFacade {
    /*创建部门*/
    Response createDepaetment(CreateDepartmentRequest createDepartmentRequest, Long userId);

    /*删除部门*/
    Response deleteDepartment(Long orgId, Long depId, Long userId);

    /*更新部门名称*/
    Response updateDepartment(UpdateDepartmentRequest updateDepartmentRequest, Long userId);

    /*部门列表*/
    Response<List<OrgDepartment>> departs(Long orgId, Long userId);

    /*部门详情*/
    Response<DepartmentInfoResponse> departDetail(Long orgId, Long depId, Long userId);
}
