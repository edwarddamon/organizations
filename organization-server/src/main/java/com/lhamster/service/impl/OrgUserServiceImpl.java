package com.lhamster.service.impl;

import com.lhamster.entity.OrgUser;
import com.lhamster.mapper.OrgUserMapper;
import com.lhamster.request.LoginRequest;
import com.lhamster.request.RegisterRequest;
import com.lhamster.service.OrgUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Edward
 * @since 2021-03-09
 */
@Service
@RequiredArgsConstructor
public class OrgUserServiceImpl extends ServiceImpl<OrgUserMapper, OrgUser> implements OrgUserService {
    private final OrgUserMapper orgUserMapper;

    @Override
    public Boolean getByPhone(String phone) {
        OrgUser orgUser = orgUserMapper.getByPhone(phone);
        return Objects.nonNull(orgUser);
    }

    @Override
    public OrgUser login(LoginRequest loginRequest) {
        return orgUserMapper.login(loginRequest);
    }

    @Override
    public void resetPhone(RegisterRequest registerRequest) {
        OrgUser user = orgUserMapper.getByPhone(registerRequest.getPhone());
        // 更新密码
        user.setUserPassword(registerRequest.getPassword());
        user.setUpdateAt(LocalDateTime.now());
        orgUserMapper.updateById(user);
    }
}
