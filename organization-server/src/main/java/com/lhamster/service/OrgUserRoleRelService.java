package com.lhamster.service;

import com.lhamster.entity.OrgUserRoleRel;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lhamster.response.OrgRoleInfoResponse;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Edward
 * @since 2021-03-09
 */
public interface OrgUserRoleRelService extends IService<OrgUserRoleRel> {

    /*社联管理员列表*/
    List<OrgRoleInfoResponse> roleList(String name);

    /*获取关系*/
    OrgUserRoleRel get(Long userId, long l);
}
