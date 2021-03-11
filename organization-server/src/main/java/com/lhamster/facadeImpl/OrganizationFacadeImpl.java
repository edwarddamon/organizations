package com.lhamster.facadeImpl;

import com.lhamster.entity.OrgDepartment;
import com.lhamster.entity.OrgOrganization;
import com.lhamster.facade.OrganizationFacade;
import com.lhamster.request.CreateOrganizationRequest;
import com.lhamster.response.exception.ServerException;
import com.lhamster.response.result.Response;
import com.lhamster.service.OrgDepartmentService;
import com.lhamster.service.OrgOrganizationService;
import com.lhamster.util.TencentCOSUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/11
 */
@Slf4j
@Component
@Service
@RequiredArgsConstructor
public class OrganizationFacadeImpl implements OrganizationFacade {
    private final OrgOrganizationService orgOrganizationService;
    private final OrgDepartmentService orgDepartmentService;

    @Override
    public Response<String> updateAvatar(File localFile, String filename) {
        try {
            // 封面地址
            String headPicUrl = TencentCOSUtil.uploadObject(localFile, "organization-avatar/" + filename);
            return new Response<String>(Boolean.TRUE, "封面上传成功", headPicUrl);
        } catch (Exception e) {
            throw new ServerException(Boolean.FALSE, "封面上传失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response createOrganization(CreateOrganizationRequest createOrganizationRequest, Long userId) {
        try {
            // 创建社团
            OrgOrganization orgOrganization = OrgOrganization.builder()
                    .organAvatar(createOrganizationRequest.getAvatarUrl())
                    .organName(createOrganizationRequest.getName())
                    .organIntroduction(createOrganizationRequest.getIntroduction())
                    .organFunds(0L)
                    .organStar(0)
                    .organStatus("undetermined")
                    .createAt(LocalDateTime.now())
                    .build();
            orgOrganizationService.save(orgOrganization);
            // 建立社团自带的社长、副社长、团支书和财务
            ArrayList<OrgDepartment> list = new ArrayList<>();
            // 任命自己为社长
            list.add(OrgDepartment.builder().depMinisterId(userId).depName("社长").depOrganizationId(orgOrganization.getOrganId()).createAt(LocalDateTime.now()).build());
            list.add(OrgDepartment.builder().depName("副社长").depOrganizationId(orgOrganization.getOrganId()).createAt(LocalDateTime.now()).build());
            list.add(OrgDepartment.builder().depName("团支书").depOrganizationId(orgOrganization.getOrganId()).createAt(LocalDateTime.now()).build());
            list.add(OrgDepartment.builder().depName("财务").depOrganizationId(orgOrganization.getOrganId()).createAt(LocalDateTime.now()).build());
            orgDepartmentService.saveBatch(list);
            return new Response(Boolean.TRUE, "社团创建成功");
        } catch (Exception e) {
            throw new ServerException(Boolean.FALSE, "社团创建失败，[异常信息]：" + e.getMessage());
        }
    }
}
