package com.lhamster.service;

import com.lhamster.entity.OrgUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lhamster.request.LoginRequest;
import com.lhamster.request.RegisterRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Edward
 * @since 2021-03-09
 */
public interface OrgUserService extends IService<OrgUser> {

    Boolean getByPhone(String phone);

    OrgUser login(LoginRequest loginRequest);

    void resetPhone(RegisterRequest registerRequest);
}
