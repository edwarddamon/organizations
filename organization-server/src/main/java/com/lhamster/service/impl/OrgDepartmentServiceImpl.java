package com.lhamster.service.impl;

import com.lhamster.entity.OrgDepartment;
import com.lhamster.mapper.OrgDepartmentMapper;
import com.lhamster.service.OrgDepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public Integer checkIdentity(String orgId, Long userId) {
        return orgDepartmentMapper.checkIdentity(orgId, userId);
    }

    @Override
    public void updateIt(OrgDepartment orgDepartment) {
        orgDepartmentMapper.updateIt(orgDepartment);
    }
}
