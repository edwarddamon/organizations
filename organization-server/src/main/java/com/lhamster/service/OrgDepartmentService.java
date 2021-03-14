package com.lhamster.service;

import com.lhamster.entity.OrgDepartment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lhamster.response.OrgUserInfoResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Edward
 * @since 2021-03-09
 */
public interface OrgDepartmentService extends IService<OrgDepartment> {

    /*检查用户身份*/
    Integer checkIdentity(String orgId, Long userId);

    void updateIt(OrgDepartment orgDepartment);

    /*获取用户信息*/
    OrgUserInfoResponse getUserInfo(Long orgId, String identity, boolean b);
}
