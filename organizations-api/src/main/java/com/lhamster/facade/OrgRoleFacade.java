package com.lhamster.facade;

import com.lhamster.response.OrgRoleInfoResponse;
import com.lhamster.response.result.Response;

import java.util.List;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/16
 */
public interface OrgRoleFacade {
    /*社联管理员列表*/
    Response<List<OrgRoleInfoResponse>> role(String name, Long userId);

    /*委任下届社联主席*/
    Response<List<OrgRoleInfoResponse>> next(Long targetId, Long userId);

    /*任职/解雇社联管理员*/
    Response<List<OrgRoleInfoResponse>> nextAdmin(Long targetId, Boolean status, Long userId);
}
