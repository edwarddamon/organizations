package com.lhamster.facadeImpl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.lhamster.entity.*;
import com.lhamster.facade.OrganizationFacade;
import com.lhamster.request.*;
import com.lhamster.response.*;
import com.lhamster.response.exception.ServerException;
import com.lhamster.response.result.Response;
import com.lhamster.service.*;
import com.lhamster.util.TencentCOSUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Server;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private final OrgUserService orgUserService;
    private final OrgApplicationService orgApplicationService;
    private final OrgTransService orgTransService;

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

    @Override
    public void checkIdentity(String orgId, Long userId) {
        Integer count = orgDepartmentService.checkIdentity(orgId, userId);
        if (count < 1) {
            throw new ServerException(Boolean.FALSE, "权限不足");
        }
    }

    /*
     * 检查当前用户是否为社联主席或社联管理员身份
     * */
    private void checkSheLian(Long userId) {
        int count = orgUserRoleRelService.count(new QueryWrapper<OrgUserRoleRel>()
                .eq("rel_user_id", userId)
                .in("rel_role_id", 3, 4));
        if (count < 1) {
            throw new ServerException(Boolean.FALSE, "权限不足");
        }
    }

    @Override
    public Response star(Long orgId, Integer star, Long userId) {
        // 检查当前用户身份｛社联管理员｝
        this.checkSheLian(userId);
        // 设置社团星级
        OrgOrganization organization = orgOrganizationService.getById(orgId);
        if (star < 3 || star > 5) {
            throw new ServerException(Boolean.FALSE, "社团星级只能为3-5");
        }
        organization.setOrganStar(star);
        orgOrganizationService.updateById(organization);
        return new Response(Boolean.TRUE, "社团星级设置成功");
    }

    @Override
    public Response<List<OrgOrganizationListInfoResponse>> page(OrganizationPageRequest organizationPageRequest) {
        String orderField = null;
        // 1:最新；2:星级
        if (organizationPageRequest.getSortBy().equals(1)) {
            orderField = "create_at";
        }
        if (organizationPageRequest.getSortBy().equals(2)) {
            orderField = "organ_star";
        }
        QueryWrapper<OrgOrganization> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("organ_status", organizationPageRequest.getStatus()).orderByDesc(orderField);
        if (StrUtil.isNotBlank(organizationPageRequest.getName())) {
            queryWrapper.like("organ_name", organizationPageRequest.getName());
        }
        Page<OrgOrganization> page = orgOrganizationService.page(new Page<>(organizationPageRequest.getPageNo(),
                organizationPageRequest.getPageSize()), queryWrapper);
        List<OrgOrganization> records = page.getRecords();
        List<OrgOrganizationListInfoResponse> resList = new ArrayList<>();
        records.forEach(record -> {
            // 查询社长id，并找到对应社长名称
            String username = orgUserService.getById(orgDepartmentService.getOne(new QueryWrapper<OrgDepartment>()
                    .eq("dep_organization_id", record.getOrganId())
                    .eq("dep_name", "社长")).getDepMinisterId()).getUserUsername();
            resList.add(OrgOrganizationListInfoResponse.builder()
                    .organAvatar(record.getOrganAvatar())
                    .organId(record.getOrganId())
                    .organIntroduction(record.getOrganIntroduction())
                    .organName(record.getOrganName())
                    .orgMinisterName(username)
                    .build());
        });
        return new Response<List<OrgOrganizationListInfoResponse>>(Boolean.TRUE, "查询成功", (int) page.getTotal(), resList);
    }

    @Override
    public Response<List<OrgOrganizationListInfoResponse>> myPage(MyOrganizationPageRequest myOrganizationPageRequest, Long userId) {
        com.github.pagehelper.Page<Object> page = PageHelper.startPage(myOrganizationPageRequest.getPageNo(), myOrganizationPageRequest.getPageSize());
        return orgUserOrganizationRelService.getMyOrganizations(myOrganizationPageRequest, userId);
    }

    @Override
    public Response<OrgOrganizationInfoResponse> myOrganizationDetail(Long orgId) {
        // 查询社团信息
        OrgOrganization organization = orgOrganizationService.getById(orgId);
        // 社团图片转集合
        List<String> pictureList = new ArrayList<>();
        String detailAvatar = organization.getOrganIntroductionDetailAvatar();
        while (StrUtil.isNotBlank(detailAvatar)) {
            if (!detailAvatar.contains(",")) {
                pictureList.add(detailAvatar);
                detailAvatar = null;
                break;
            }
            pictureList.add(detailAvatar.substring(0, detailAvatar.indexOf(",")));
            detailAvatar = detailAvatar.substring(detailAvatar.indexOf(",") + 1);
        }
        // 社长
        OrgUserInfoResponse ministerInfo = orgDepartmentService.getUserInfo(orgId, "社长", true);
        // 副社长
        OrgUserInfoResponse vice1 = orgDepartmentService.getUserInfo(orgId, "副社长", true);
        OrgUserInfoResponse vice2 = orgDepartmentService.getUserInfo(orgId, "副社长", false);
        // 团支书
        OrgUserInfoResponse secretary = orgDepartmentService.getUserInfo(orgId, "团支书", true);
        // 组装透出
        return new Response<>(Boolean.TRUE, "查询成功", OrgOrganizationInfoResponse.builder()
                .organAvatar(organization.getOrganAvatar())
                .organId(organization.getOrganId())
                .organIntroduction(organization.getOrganIntroduction())
                .organIntroductionDetail(organization.getOrganIntroductionDetail())
                .organName(organization.getOrganName())
                .organStar(organization.getOrganStar())
                .organIntroductionDetailAvatars(pictureList)
                .minister(ministerInfo)
                .secretary(secretary)
                .viceMinisters(Lists.newArrayList(vice1, vice1))
                .build());
    }

    @Override
    public Response apply(OrgApplicationRequest orgApplicationRequest, Long userId) {
        orgApplicationService.save(OrgApplication.builder()
                .appUserId(userId)
                .appApplicationReason(orgApplicationRequest.getReason())
                .appOrgId(orgApplicationRequest.getOrgId())
                .appStatus("undetermined")
                .createAt(LocalDateTime.now())
                .build());
        return new Response(Boolean.TRUE, "申请成功，请等待神团管理人员审批");
    }

    @Override
    public Response judge(OrgJudgeApplicationRequest orgJudgeApplicationRequest, Long userId) {
        OrgApplication application = orgApplicationService.getById(orgJudgeApplicationRequest.getAppId());
        // 检查当前用户权限
        this.checkIdentity(String.valueOf(application.getAppOrgId()), userId);
        // 同意 -> 关系表新建用户
        if (orgJudgeApplicationRequest.getRes()) {
            // 申请表状态改为AGREE
            application.setAppStatus("AGREE");
            orgUserOrganizationRelService.save(OrgUserOrganizationRel.builder()
                    .createAt(LocalDateTime.now())
                    .relUserId(application.getAppUserId())
                    .relOrganizationId(application.getAppOrgId())
                    .relRoleId(2L)
                    .build());
        } else {
            // 拒绝 -> 给出理由
            application.setAppStatus("REFUSE");
            application.setAppRefuseReason(orgJudgeApplicationRequest.getReason());
        }
        application.setUpdateAt(LocalDateTime.now());
        orgApplicationService.updateById(application);
        return new Response(Boolean.TRUE, "审批成功");
    }

    @Override
    public Response<List<OrgApplicationListInfoResponse>> applyList(Long orgId, Long userId) {
        List<OrgApplicationListInfoResponse> applicationListInfoResponses = new ArrayList<>();
        // orgId不为null，查询orgId对应社团的所有审批申请
        if (Objects.nonNull(orgId)) {
            List<OrgApplication> applicationList = orgApplicationService.list(new QueryWrapper<OrgApplication>()
                    .eq("app_org_id", orgId)
                    .eq("app_status", "undetermined")
                    .orderByDesc("create_at"));
            // 添加对应的用户信息
            applicationList.forEach(app -> {
                OrgUser user = orgUserService.getById(app.getAppUserId());
                applicationListInfoResponses.add(OrgApplicationListInfoResponse.builder()
                        .appId(app.getAppId())
                        .appApplicationReason(app.getAppApplicationReason())
                        .appRefuseReason(app.getAppRefuseReason())
                        .appStatus(app.getAppStatus())
                        .appUserId(user.getUserId())
                        .appUserAvatar(user.getUserAvatar())
                        .appUserName(user.getUserUsername())
                        .createAt(app.getCreateAt())
                        .build());
            });
        } else {
            // orgId为null，查询当前登录用户所有状态的申请
            List<OrgApplication> applicationList = orgApplicationService.list(new QueryWrapper<OrgApplication>()
                    .eq("app_user_id", userId)
                    .orderByDesc("create_at"));
            // 添加对应的社团信息
            applicationList.forEach(app -> {
                OrgOrganization organization = orgOrganizationService.getById(app.getAppOrgId());
                applicationListInfoResponses.add(OrgApplicationListInfoResponse.builder()
                        .appId(app.getAppId())
                        .appApplicationReason(app.getAppApplicationReason())
                        .appRefuseReason(app.getAppRefuseReason())
                        .appStatus(app.getAppStatus())
                        .appOrgAvatar(organization.getOrganAvatar())
                        .appOrgId(organization.getOrganId())
                        .appOrgName(organization.getOrganName())
                        .createAt(app.getCreateAt())
                        .build());
            });
        }
        return new Response<List<OrgApplicationListInfoResponse>>(Boolean.TRUE, "查询成功", applicationListInfoResponses);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response quitOrganization(Long orgId, Long userId) {
        // 判断是否有职位
        int count = orgDepartmentService.count(new QueryWrapper<OrgDepartment>()
                .eq("dep_organization_id", orgId)
                .and(req -> req.eq("dep_minister_id", userId)
                        .or()
                        .eq("dep_vice_minister_id", userId)));
        // 有 -> 无法退出社团
        if (count > 0) {
            throw new ServerException(Boolean.FALSE, "您在该社团任职，请先联系社团管理员解除职位");
        }
        // 无 -> 解除当前登录用户和社团的关系
        orgUserOrganizationRelService.remove(new QueryWrapper<OrgUserOrganizationRel>()
                .eq("rel_organization_id", orgId)
                .eq("rel_user_id", userId)
                .eq("rel_role_id", 2));
        return new Response(Boolean.TRUE, "您已成功退出该社团");
    }

    /**
     * 检查当前用户是否为社长或财务
     *
     * @param orgId
     * @param userId
     */
    private void fundCheck(Long orgId, Long userId) {
        int count = orgDepartmentService.count(new QueryWrapper<OrgDepartment>()
                .eq("dep_organization_id", orgId)
                .in("dep_name", "社长", "财务")
                .eq("dep_minister_id", userId));
        if (count < 1) {
            throw new ServerException(Boolean.FALSE, "权限不足");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response fund(OrgFundRequest orgFundRequest, Long userId) {
        // 检查当前登录用户是否为社团社长或财务
        this.fundCheck(orgFundRequest.getOrgId(), userId);
        OrgOrganization organization = orgOrganizationService.getById(orgFundRequest.getOrgId());
        // 剩下的经费
        Long funds = organization.getOrganFunds();
        if (funds + orgFundRequest.getFunds() < 0) {
            throw new ServerException(Boolean.FALSE, "社团经费不足");
        }
        funds += orgFundRequest.getFunds();
        try {
            // 生成经费流水
            orgTransService.save(OrgTrans.builder()
                    .createAt(LocalDateTime.now())
                    .traChangeAmount(orgFundRequest.getFunds())
                    .traAmount(funds)
                    .traReason(orgFundRequest.getReason())
                    .traOrgId(orgFundRequest.getOrgId())
                    .build());
            // 更新社团经费
            organization.setOrganFunds(funds);
            organization.setUpdateAt(LocalDateTime.now());
            orgOrganizationService.updateById(organization);
            return new Response(Boolean.TRUE, "修改经费成功");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServerException(Boolean.FALSE, "修改经费失败");
        }
    }

    @Override
    public Response<Long> getFund(Long orgId, Long userId) {
        // 检查当前登录用户是否为社团社长或财务
        this.fundCheck(orgId, userId);
        return new Response<Long>(Boolean.TRUE, "查询成功", orgOrganizationService.getById(orgId).getOrganFunds());
    }

    @Override
    public Response<List<OrgTransInfoResponse>> pageTrans(OrgFundTransRequest orgFundTransRequest, Long userId) throws ParseException {
        if (log.isDebugEnabled()) {
            log.debug("[入参：]：{}", orgFundTransRequest);
        }
        // 检查当前用户身份
        this.fundCheck(orgFundTransRequest.getOrgId(), userId);
        // 返回
        QueryWrapper<OrgTrans> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tra_org_id", orgFundTransRequest.getOrgId());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (StrUtil.isNotBlank(orgFundTransRequest.getStartTime())) {
            queryWrapper.gt("create_at", format.parse(orgFundTransRequest.getStartTime()));
        }
        if (StrUtil.isNotBlank(orgFundTransRequest.getEndTime())) {
            queryWrapper.lt("create_at", format.parse(orgFundTransRequest.getEndTime()));
        }
        queryWrapper.orderByDesc("create_at");
        Page<OrgTrans> page = orgTransService.page(new Page<>(orgFundTransRequest.getPageNo(), orgFundTransRequest.getPageSize()), queryWrapper);
        // 封装
        List<OrgTransInfoResponse> infoResponses = new ArrayList<>();
        page.getRecords().forEach(tran -> {
            infoResponses.add(OrgTransInfoResponse.builder()
                    .createAt(tran.getCreateAt())
                    .traAmount(tran.getTraAmount())
                    .traChangeAmount(tran.getTraChangeAmount())
                    .traId(tran.getTraId())
                    .traReason(tran.getTraReason())
                    .build());
        });
        return new Response<List<OrgTransInfoResponse>>(Boolean.TRUE, "查询成功", (int) page.getTotal(), infoResponses);
    }
}
