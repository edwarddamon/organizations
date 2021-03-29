package com.lhamster.facadeImpl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lhamster.entity.OrgActivity;
import com.lhamster.entity.OrgDepartment;
import com.lhamster.entity.OrgLimit;
import com.lhamster.entity.OrgOrganization;
import com.lhamster.facade.OrgActivityFacade;
import com.lhamster.facade.OrganizationFacade;
import com.lhamster.request.OrgCreateActivityRequest;
import com.lhamster.request.OrgFundRequest;
import com.lhamster.request.OrgUpdateActivityRequest;
import com.lhamster.response.OrgActivityInfoResponse;
import com.lhamster.response.OrgActivityListInfoResponse;
import com.lhamster.response.exception.ServerException;
import com.lhamster.response.result.Response;
import com.lhamster.service.OrgActivityService;
import com.lhamster.service.OrgDepartmentService;
import com.lhamster.service.OrgLimitService;
import com.lhamster.service.OrgOrganizationService;
import com.lhamster.util.TencentCOSUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/17
 */
@Slf4j
@Component
@Service
@RequiredArgsConstructor
public class OrgActivityFacadeImpl implements OrgActivityFacade {
    private final OrgLimitService orgLimitService;
    private final OrgActivityService orgActivityService;
    private final OrgBoardFacadeImpl orgBoardFacade;
    private final OrganizationFacade organizationFacade;
    private final OrgDepartmentService orgDepartmentService;
    private final OrgOrganizationService orgOrganizationService;

    @Override
    public Response<List<OrgLimit>> limit() {
        return new Response<>(Boolean.TRUE, "查询成功", orgLimitService.list());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response create(File localFile, String fileName, OrgCreateActivityRequest orgCreateActivityRequest, Long userId) {
        // 检查当前登陆用户身份
        orgBoardFacade.checkIdentity(orgCreateActivityRequest.getOrgId(), userId);
        // 创建活动
        String headPicUrl = null;
        try {
            // 上传封面地址
            if (Objects.nonNull(localFile) && StrUtil.isNotBlank(fileName)) {
                headPicUrl = TencentCOSUtil.uploadObject(localFile, "organization-activityAvatar/" + fileName);
            }
            if (Objects.nonNull(orgCreateActivityRequest.getFunds())) {
                // 获取社长id
                Long depMinisterId = orgDepartmentService.getOne(new QueryWrapper<OrgDepartment>()
                        .eq("dep_organization_id", orgCreateActivityRequest.getOrgId())
                        .eq("dep_name", "社长")).getDepMinisterId();
                // 检查扣除经费
                organizationFacade.fund(OrgFundRequest.builder()
                        .orgId(orgCreateActivityRequest.getOrgId())
                        .funds(0 - orgCreateActivityRequest.getFunds())
                        .reason("举办活动")
                        .build(), depMinisterId);
            }
            // 创建活动
            orgActivityService.save(OrgActivity.builder()
                    .createAt(LocalDateTime.now())
                    .actAddress(orgCreateActivityRequest.getActivityAddress())
                    .actAvatar(headPicUrl)
                    .actBeginTime(LocalDateTime.ofInstant(orgCreateActivityRequest.getStartTime().toInstant(), ZoneId.systemDefault()))
                    .actContent(orgCreateActivityRequest.getActivityContent())
                    .actEndTime(LocalDateTime.ofInstant(orgCreateActivityRequest.getEndTime().toInstant(), ZoneId.systemDefault()))
                    .actName(orgCreateActivityRequest.getActivityName())
                    .actLimitUserId(orgCreateActivityRequest.getLimitId())
                    .actOrganizationId(orgCreateActivityRequest.getOrgId())
                    .actFunds(orgCreateActivityRequest.getFunds())
                    .actViews(0)
                    .build());
            return new Response(Boolean.TRUE, "创建活动成功");
        } catch (ServerException e) {
            // 回滚，删除该封面
            if (StrUtil.isNotBlank(headPicUrl)) {
                TencentCOSUtil.deletefile("organization-activityAvatar/" + headPicUrl.substring(headPicUrl.lastIndexOf("/") + 1));
            }
            e.printStackTrace();
            throw new ServerException(Boolean.FALSE, e.getMsg());
        } catch (Exception e) {
            // 回滚，删除该封面
            if (StrUtil.isNotBlank(headPicUrl)) {
                TencentCOSUtil.deletefile("organization-activityAvatar/" + headPicUrl.substring(headPicUrl.lastIndexOf("/") + 1));
            }
            e.printStackTrace();
            throw new ServerException(Boolean.FALSE, "创建活动失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response update(File localFile, String fileName, OrgUpdateActivityRequest orgUpdateActivityRequest, Long userId) {
        // 获取表中活动信息
        OrgActivity activity = orgActivityService.getById(orgUpdateActivityRequest.getActId());
        if (Objects.isNull(activity)) {
            throw new ServerException(Boolean.FALSE, "活动不存在,请重新输入活动id");
        }
        // 检查当前登陆用户身份
        orgBoardFacade.checkIdentity(activity.getActOrganizationId(), userId);
        // 更新活动
        // 活动名不为空
        if (StrUtil.isNotBlank(orgUpdateActivityRequest.getActivityName())) {
            activity.setActName(orgUpdateActivityRequest.getActivityName());
        }
        // 活动地点不为空
        if (StrUtil.isNotBlank(orgUpdateActivityRequest.getActivityAddress())) {
            activity.setActAddress(orgUpdateActivityRequest.getActivityAddress());
        }
        // 活动开始时间不为空
        if (Objects.nonNull(orgUpdateActivityRequest.getStartTime())) {
            activity.setActBeginTime(LocalDateTime.ofInstant(orgUpdateActivityRequest.getStartTime().toInstant(), ZoneId.systemDefault()));
        }
        // 活动结束时间不为空
        if (Objects.nonNull(orgUpdateActivityRequest.getEndTime())) {
            activity.setActEndTime(LocalDateTime.ofInstant(orgUpdateActivityRequest.getEndTime().toInstant(), ZoneId.systemDefault()));
        }
        // 限制id不为空
        if (Objects.nonNull(orgUpdateActivityRequest.getLimitId())) {
            activity.setActLimitUserId(orgUpdateActivityRequest.getLimitId());
        }
        // 活动内容不为空
        if (Objects.nonNull(orgUpdateActivityRequest.getActivityContent())) {
            activity.setActContent(orgUpdateActivityRequest.getActivityContent());
        }
        // 经费不为空
        if (Objects.nonNull(orgUpdateActivityRequest.getFunds())) {
            // 扣除或增加经费
            // 获取社长id
            Long depMinisterId = orgDepartmentService.getOne(new QueryWrapper<OrgDepartment>()
                    .eq("dep_organization_id", activity.getActOrganizationId())
                    .eq("dep_name", "社长")).getDepMinisterId();
            // 恢复之前扣的经费
            organizationFacade.fund(OrgFundRequest.builder()
                    .orgId(activity.getActOrganizationId())
                    .funds(0 - (orgUpdateActivityRequest.getFunds() - activity.getActFunds()))
                    .reason("修改活动经费")
                    .build(), depMinisterId);
            activity.setActFunds(orgUpdateActivityRequest.getFunds());
        }
        // 文件上传不为空
        if (Objects.nonNull(localFile) && StrUtil.isNotBlank(fileName)) {
            // 删除旧封面
            String actAvatar = activity.getActAvatar();
            if (StrUtil.isNotBlank(actAvatar)) {
                TencentCOSUtil.deletefile("organization-activityAvatar/" + actAvatar.substring(actAvatar.lastIndexOf("/") + 1));
            }
            // 更新新封面
            String newAvatar = TencentCOSUtil.uploadObject(localFile, "organization-activityAvatar/" + fileName);
            activity.setActAvatar(newAvatar);
        }
        activity.setUpdateAt(LocalDateTime.now());
        orgActivityService.updateById(activity);
        return new Response(Boolean.TRUE, "更新活动信息成功");
    }

    @Override
    public Response delete(Long actId, Long userId) {
        OrgActivity activity = orgActivityService.getById(actId);
        // 检查当前用户身份
        orgBoardFacade.checkIdentity(activity.getActOrganizationId(), userId);
        // 检查当前时间和活动开始/结束时间 -> 超过时间无法结束
        if (LocalDateTime.now().isAfter(activity.getActEndTime())) {
            throw new ServerException(Boolean.FALSE, "活动已结束，无法取消");
        }
        if (LocalDateTime.now().isAfter(activity.getActBeginTime()) && LocalDateTime.now().isBefore(activity.getActEndTime())) {
            throw new ServerException(Boolean.FALSE, "活动正在进行中，无法取消");
        }
        // 有封面 -> 删除
        if (StrUtil.isNotBlank(activity.getActAvatar())) {
            TencentCOSUtil.deletefile("organization-activityAvatar/" + activity.getActAvatar().substring(activity.getActAvatar().lastIndexOf("/") + 1));
        }
        // 有经费 -> 返还经费
        if (!activity.getActFunds().equals(0L)) {
            // 获取社长id
            Long depMinisterId = orgDepartmentService.getOne(new QueryWrapper<OrgDepartment>()
                    .eq("dep_organization_id", activity.getActOrganizationId())
                    .eq("dep_name", "社长")).getDepMinisterId();
            // 恢复之前扣的经费
            organizationFacade.fund(OrgFundRequest.builder()
                    .orgId(activity.getActOrganizationId())
                    .funds(activity.getActFunds())
                    .reason("取消活动经费")
                    .build(), depMinisterId);
        }
        orgActivityService.removeById(activity.getActId());
        return new Response(Boolean.TRUE, "取消活动成功");
    }

    @Override
    public Response<List<OrgActivityListInfoResponse>> page(Integer pageNo, Integer pageSize, String name) {
        List<OrgActivityListInfoResponse> responses = new ArrayList<>();
        QueryWrapper<OrgActivity> wrapper = new QueryWrapper<OrgActivity>().orderByDesc("create_at", "update_at");
        if (StrUtil.isNotBlank(name)) {
            wrapper.like("act_name", name);
        }
        Page<OrgActivity> page = orgActivityService.page(new Page<>(pageNo, pageSize), wrapper);
        page.getRecords().forEach(orgActivity -> {
            // 获取社团名
            OrgOrganization organization = orgOrganizationService.getById(orgActivity.getActOrganizationId());
            responses.add(OrgActivityListInfoResponse.builder()
                    .actAvatar(orgActivity.getActAvatar())
                    .actId(orgActivity.getActId())
                    .actName(orgActivity.getActName())
                    .actViews(orgActivity.getActViews())
                    .createAt(orgActivity.getCreateAt())
                    .actOrganizationId(orgActivity.getActOrganizationId())
                    .actOrganizationName(organization.getOrganName())
                    .build());
        });
        return new Response<List<OrgActivityListInfoResponse>>(Boolean.TRUE, "查询成功", (int) page.getTotal(), responses);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<OrgActivityInfoResponse> detail(Long actId) {
        OrgActivity activity = orgActivityService.getById(actId);
        // 查询社团
        OrgOrganization organization = orgOrganizationService.getById(activity.getActOrganizationId());
        // 查询限制
        OrgLimit orgLimit = orgLimitService.getById(activity.getActLimitUserId());
        // 拼装
        OrgActivityInfoResponse response = OrgActivityInfoResponse.builder()
                .actAddress(activity.getActAddress())
                .actAvatar(activity.getActAvatar())
                .actBeginTime(activity.getActBeginTime())
                .actEndTime(activity.getActEndTime())
                .actContent(activity.getActContent())
                .actId(activity.getActId())
                .actName(activity.getActName())
                .actViews(activity.getActViews() + 1)
                .actFunds(activity.getActFunds())
                .createAt(activity.getCreateAt())
                .actOrganizationId(organization.getOrganId())
                .actOrganizationName(organization.getOrganName())
                .actOrganizationAvatar(organization.getOrganAvatar())
                .orgLimit(orgLimit)
                .build();
        // 更新浏览量
        activity.setActViews(activity.getActViews() + 1);
        orgActivityService.updateById(activity);
        return new Response<>(Boolean.TRUE, "查询成功", response);
    }

    @Override
    public Response<List<OrgActivityListInfoResponse>> list(Long orgId) {
        List<OrgActivityListInfoResponse> responses = new ArrayList<>();
        List<OrgActivity> activityList = orgActivityService.list(new QueryWrapper<OrgActivity>()
                .eq("act_organization_id", orgId)
                .orderByDesc("create_at"));
        activityList.forEach(orgActivity -> {
            // 获取社团名
            OrgOrganization organization = orgOrganizationService.getById(orgActivity.getActOrganizationId());
            responses.add(OrgActivityListInfoResponse.builder()
                    .actAvatar(orgActivity.getActAvatar())
                    .actId(orgActivity.getActId())
                    .actName(orgActivity.getActName())
                    .actViews(orgActivity.getActViews())
                    .createAt(orgActivity.getCreateAt())
                    .actOrganizationId(orgActivity.getActOrganizationId())
                    .actOrganizationName(organization.getOrganName())
                    .build());
        });
        return new Response<List<OrgActivityListInfoResponse>>(Boolean.TRUE, "查询成功", responses);

    }
}
