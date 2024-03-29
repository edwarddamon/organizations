package com.lhamster.mapper;

import com.lhamster.entity.OrgUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lhamster.request.LoginRequest;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Edward
 * @since 2021-03-09
 */
public interface OrgUserMapper extends BaseMapper<OrgUser> {

    OrgUser getByPhone(String phone);

    OrgUser login(LoginRequest loginRequest);
}
