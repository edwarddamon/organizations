package com.lhamster.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lhamster.entity.OrgDepartment;
import com.lhamster.entity.OrgUser;
import com.lhamster.mapper.OrgDepartmentMapper;
import com.lhamster.mapper.OrgUserMapper;
import com.lhamster.response.OrgUserInfoResponse;
import com.lhamster.service.OrgDepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
public class OrgDepartmentServiceImpl extends ServiceImpl<OrgDepartmentMapper, OrgDepartment> implements OrgDepartmentService {
    private final OrgDepartmentMapper orgDepartmentMapper;
    private final OrgUserMapper orgUserMapper;

    @Override
    public Integer checkIdentity(String orgId, Long userId) {
        return orgDepartmentMapper.checkIdentity(orgId, userId);
    }

    @Override
    public void updateIt(OrgDepartment orgDepartment) {
        orgDepartmentMapper.updateIt(orgDepartment);
    }

    @Override
    public OrgUserInfoResponse getUserInfo(Long orgId, String identity, boolean b) {
        Long ministerId = orgDepartmentMapper.selectOne(new QueryWrapper<OrgDepartment>()
                .eq("dep_organization_id", orgId)
                .eq("dep_name", identity)).getDepMinisterId();
        Long depViceMinisterId = orgDepartmentMapper.selectOne(new QueryWrapper<OrgDepartment>()
                .eq("dep_organization_id", orgId)
                .eq("dep_name", identity)).getDepViceMinisterId();
        Long resId = b ? ministerId : depViceMinisterId;
        if (Objects.isNull(resId)) {
            return null;
        }
        OrgUser orgUser = orgUserMapper.selectById(resId);
        return OrgUserInfoResponse.builder()
                .userId(orgUser.getUserId())
                .userVx(orgUser.getUserVx())
                .userQq(orgUser.getUserQq())
                .userPhone(orgUser.getUserPhone())
                .userSex(orgUser.getUserSex())
                .userUsername(orgUser.getUserUsername())
                .userAvatar(orgUser.getUserAvatar())
                .build();
    }
}
