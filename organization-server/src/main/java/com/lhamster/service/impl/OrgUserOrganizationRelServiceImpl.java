package com.lhamster.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lhamster.entity.OrgDepartment;
import com.lhamster.entity.OrgUserOrganizationRel;
import com.lhamster.mapper.OrgDepartmentMapper;
import com.lhamster.mapper.OrgUserMapper;
import com.lhamster.mapper.OrgUserOrganizationRelMapper;
import com.lhamster.request.MyOrganizationPageRequest;
import com.lhamster.response.OrgOrganizationListInfoResponse;
import com.lhamster.response.result.Response;
import com.lhamster.service.OrgUserOrganizationRelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
public class OrgUserOrganizationRelServiceImpl extends ServiceImpl<OrgUserOrganizationRelMapper, OrgUserOrganizationRel> implements OrgUserOrganizationRelService {
    private final OrgUserOrganizationRelMapper orgUserOrganizationRelMapper;
    private final OrgUserMapper orgUserMapper;
    private final OrgDepartmentMapper orgDepartmentMapper;

    @Override
    public Response<List<OrgOrganizationListInfoResponse>> getMyOrganizations(MyOrganizationPageRequest myOrganizationPageRequest, Long userId) {
        Page<OrgOrganizationListInfoResponse> page = PageHelper.startPage(myOrganizationPageRequest.getPageNo(), myOrganizationPageRequest.getPageSize());
        List<OrgOrganizationListInfoResponse> orgUserOrganizationRelList = orgUserOrganizationRelMapper.getMyOrganizations(myOrganizationPageRequest.getName(), userId);
        orgUserOrganizationRelList.forEach(res -> {
            // 根据社团id拿到社长id,根据社长id查询社长名
            String username = orgUserMapper.selectById(orgDepartmentMapper.selectOne(new QueryWrapper<OrgDepartment>()
                    .eq("dep_organization_id", res.getOrganId())
                    .eq("dep_name", "社长")).getDepMinisterId()).getUserUsername();
            res.setOrgMinisterName(username);
        });
        return new Response<List<OrgOrganizationListInfoResponse>>(Boolean.TRUE, "查询成功", (int) page.getTotal(), orgUserOrganizationRelList);
    }
}
