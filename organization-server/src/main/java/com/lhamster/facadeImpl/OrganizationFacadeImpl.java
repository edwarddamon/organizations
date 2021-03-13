package com.lhamster.facadeImpl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lhamster.entity.OrgDepartment;
import com.lhamster.entity.OrgOrganization;
import com.lhamster.entity.OrgUserOrganizationRel;
import com.lhamster.entity.OrgUserRoleRel;
import com.lhamster.facade.OrganizationFacade;
import com.lhamster.request.CancelOrganizationRequest;
import com.lhamster.request.CheckOrganizationRequest;
import com.lhamster.request.CreateOrganizationRequest;
import com.lhamster.request.UpdateOrganizationRequest;
import com.lhamster.response.exception.ServerException;
import com.lhamster.response.result.Response;
import com.lhamster.service.OrgDepartmentService;
import com.lhamster.service.OrgOrganizationService;
import com.lhamster.service.OrgUserOrganizationRelService;
import com.lhamster.service.OrgUserRoleRelService;
import com.lhamster.util.TencentCOSUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private final OrgUserOrganizationRelService orgUserOrganizationRelService;
    private final OrgUserRoleRelService orgUserRoleRelService;

    @Override
    public Response<String> updateAvatar(File localFile, String filename) {
        try {
            // 上传封面地址
            String headPicUrl = TencentCOSUtil.uploadObject(localFile, "organization-avatar/" + filename);
            return new Response<String>(Boolean.TRUE, "封面上传成功", headPicUrl);
        } catch (Exception e) {
            throw new ServerException(Boolean.FALSE, "封面上传失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<String> updateAvatar(String orgId, File localFile, String fileName) {
        // 判断该社团是否有封面
        OrgOrganization orgOrganization = orgOrganizationService.getById(orgId);
        String organAvatar = orgOrganization.getOrganAvatar();
        if (!StrUtil.hasBlank(organAvatar)) {
            // 有 -> 删除该封面
            TencentCOSUtil.deletefile("organization-avatar/" + organAvatar.substring(organAvatar.lastIndexOf("/") + 1));
        }
        return updateAvatar(localFile, fileName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOldIntroductionUrls(String orgId) {
        // 判断之前是否有社团详细介绍照片
        List<String> oldIntroductionUrls = new ArrayList<>();
        OrgOrganization orgOrganization = orgOrganizationService.getById(orgId);
        String avatars = orgOrganization.getOrganIntroductionDetailAvatar();
        while (!StrUtil.hasBlank(avatars)) {
            // 有 -> 删除
            if (!avatars.contains(",")) {
                oldIntroductionUrls.add(avatars);
                break;
            }
            oldIntroductionUrls.add(avatars.substring(0, avatars.indexOf(",")));
            avatars = avatars.substring(avatars.indexOf(",") + 1);
        }
        // 删除
        if (CollectionUtil.isNotEmpty(oldIntroductionUrls)) {
            oldIntroductionUrls.forEach(url -> {
                TencentCOSUtil.deletefile("organization-introductionAvatars" + url.substring(url.lastIndexOf("/")));
            });
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<String> updateNewIntroductionUrl(File localFile, String fileName) {
        try {
            // 上传详细介绍地址
            String headPicUrl = TencentCOSUtil.uploadObject(localFile, "organization-introductionAvatars/" + fileName);
            log.info("[上传成功]：{}", headPicUrl);
            return new Response<String>(Boolean.TRUE, "图片上传成功", headPicUrl);
        } catch (Exception e) {
            throw new ServerException(Boolean.FALSE, "图片上传失败");
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
            return new Response(Boolean.TRUE, "社团创建申请成功，请等待审核");
        } catch (Exception e) {
            throw new ServerException(Boolean.FALSE, "社团创建申请失败，[异常信息]：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response cancelOrganization(CancelOrganizationRequest cancelOrganizationRequest, Long userId) {
        // 检查当前用户是否为该社团社长,不是则不能注销
        int count = orgDepartmentService.count(new QueryWrapper<OrgDepartment>()
                .eq("dep_organization_id", cancelOrganizationRequest.getId())
                .eq("dep_name", "社长")
                .eq("dep_minister_id", userId));
        if (count < 1) {
            throw new ServerException(Boolean.FALSE, "您不是该社团社长，无权注销");
        }
        // 若是，则改变社团状态为cancel
        OrgOrganization organization = orgOrganizationService.getById(cancelOrganizationRequest.getId());
        organization.setOrganStatus("cancel");
        organization.setOrganCancelReason(cancelOrganizationRequest.getReason());
        boolean res = orgOrganizationService.updateById(organization);
        if (!res) {
            throw new ServerException(Boolean.FALSE, "社团注销社团失败");
        }
        return new Response(Boolean.TRUE, "社团注销申请成功，请等待审核");
    }

    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public Response check(CheckOrganizationRequest checkOrganizationRequest, Long userId) {
        // 检查当前登录用户是否为社联主席或社联管理员
        int count = orgUserRoleRelService.count(new QueryWrapper<OrgUserRoleRel>()
                .eq("rel_user_id", userId)
                .in("rel_role_id", 3, 4));
        if (count < 1) {
            throw new ServerException(Boolean.FALSE, "权限不足");
        }
        OrgOrganization organization = orgOrganizationService.getById(checkOrganizationRequest.getId());
        organization.setUpdateAt(LocalDateTime.now());
        if ("undetermined".equals(organization.getOrganStatus())) {
            // 1、社团注册
            if (checkOrganizationRequest.getResult().equals(0)) {
                // 同意 -> 改变社团状态为normal
                organization.setOrganStatus("normal");
            } else if (checkOrganizationRequest.getResult().equals(1)) {
                // 拒绝 -> 社团状态改为dead -> 填写拒绝理由
                organization.setOrganStatus("dead");
                organization.setOrganRefuseReason(checkOrganizationRequest.getReason());
            } else {
                throw new ServerException(Boolean.FALSE, "审批结果未知");
            }
        } else if ("cancel".equals(organization.getOrganStatus())) {
            // 2、社团注销
            if (checkOrganizationRequest.getResult().equals(0)) {
                // 同意 -> 社团状态为cancelled
                organization.setOrganStatus("cancelled");
                //      -> 删除社团相关部门
                List<OrgDepartment> departmentList = orgDepartmentService.list(new QueryWrapper<OrgDepartment>()
                        .eq("dep_organization_id", organization.getOrganId()));
                List<Long> ids = new ArrayList<>();
                departmentList.forEach(depart -> {
                    ids.add(depart.getDepId());
                });
                orgDepartmentService.removeByIds(ids);
                //      -> 删除用户和社团的关系表中相关数据
                List<OrgUserOrganizationRel> rels = orgUserOrganizationRelService.list(new QueryWrapper<OrgUserOrganizationRel>()
                        .eq("rel_organization_id", organization.getOrganId()));
                rels.forEach(rel -> {
                    orgUserOrganizationRelService.removeById(rel.getRelId());
                });
            } else if (checkOrganizationRequest.getResult().equals(1)) {
                // 拒绝 -> 社团状态改为normal
                organization.setOrganStatus("normal");
                //      -> 填写拒绝理由
                organization.setOrganRefuseReason(checkOrganizationRequest.getReason());
            } else {
                throw new ServerException(Boolean.FALSE, "审批结果未知");
            }
        } else {
            throw new ServerException(Boolean.FALSE, "社团状态不符合审批要求");
        }
        orgOrganizationService.updateById(organization);
        return new Response(Boolean.TRUE, "审批成功");
    }

    @Override
    public Response updateOrganization(UpdateOrganizationRequest updateOrganizationRequest, String newAvatarUrl, List<String> newIntroductionUrls, Long userId) {
        // 检查当前登录用户身份
        Integer count = orgDepartmentService.checkIdentity(updateOrganizationRequest.getOrgId(), userId);
        if (count < 1) {
            throw new ServerException(Boolean.FALSE, "权限不足");
        }
        // 查询
        OrgOrganization organization = orgOrganizationService.getById(updateOrganizationRequest.getOrgId());
        // 新封面
        if (!StrUtil.hasBlank(newAvatarUrl)) {
            organization.setOrganAvatar(newAvatarUrl);
        }
        // 社团名
        if (!StrUtil.hasBlank(updateOrganizationRequest.getOrgName())) {
            organization.setOrganName(updateOrganizationRequest.getOrgName());
        }
        // 新介绍
        if (!StrUtil.hasBlank(updateOrganizationRequest.getDetailIntroduction())) {
            organization.setOrganIntroductionDetail(updateOrganizationRequest.getDetailIntroduction());
        }
        // 新介绍图片
        if (CollectionUtil.isNotEmpty(newIntroductionUrls)) {
            StringBuffer newUrls = new StringBuffer();
            newIntroductionUrls.forEach(url -> {
                newUrls.append(url + ",");
            });
            String val = newUrls.substring(0, newUrls.lastIndexOf(","));
            organization.setOrganIntroductionDetailAvatar(val);
        }
        // 更新
        orgOrganizationService.updateById(organization);
        return new Response(Boolean.TRUE, "更新社团信息成功");
    }
}
