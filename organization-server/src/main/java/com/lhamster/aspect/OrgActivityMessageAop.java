package com.lhamster.aspect;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/17
 */

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lhamster.entity.OrgActivity;
import com.lhamster.entity.OrgOrganization;
import com.lhamster.entity.OrgUserOrganizationRel;
import com.lhamster.request.CreateOrganizationRequest;
import com.lhamster.request.OrgCreateActivityRequest;
import com.lhamster.request.OrgUpdateActivityRequest;
import com.lhamster.service.OrgActivityService;
import com.lhamster.service.OrgOrganizationService;
import com.lhamster.service.OrgUserOrganizationRelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Aspect
@Component
@Slf4j
@Api(value = "社团模块通知")
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class OrgActivityMessageAop {
    private final OrgOrganizationService orgOrganizationService;
    private final OrgUserOrganizationRelService orgUserOrganizationRelService;
    private final OrgUserMessageAop orgUserMessageAop;
    private final OrgActivityService orgActivityService;

    /**
     * 通知社团所有成员
     *
     * @param msg
     * @param orgId
     */
    public void messageAllOrganizationUser(String msg, Long orgId) {
        // 获取所有用户id
        orgUserOrganizationRelService.list(new QueryWrapper<OrgUserOrganizationRel>()
                .eq("rel_organization_id", orgId)
                .eq("rel_role_id", 2L))
                .stream().map(OrgUserOrganizationRel::getRelUserId).collect(Collectors.toList())
                .forEach(userId -> {
                    // 通知所有用户
                    orgUserMessageAop.saveMessage(msg, userId);
                });

    }

    @ApiOperation(value = "创建活动")
    @AfterReturning(value = "execution(* com.lhamster.facadeImpl.OrgActivityFacadeImpl.create(..))")
    public void createActivity(JoinPoint joinPoint) {
        // 获取社团id -> 获取社团名称 -> 通知所有社团成员
        OrgCreateActivityRequest orgCreateActivityRequest = (OrgCreateActivityRequest) joinPoint.getArgs()[2];
        OrgOrganization organization = orgOrganizationService.getById(orgCreateActivityRequest.getOrgId());
        // 通知所有社团成员
        this.messageAllOrganizationUser(
                "[" + organization.getOrganName() + "] 发布了新活动：" + orgCreateActivityRequest.getActivityName(),
                orgCreateActivityRequest.getOrgId());
    }


    @ApiOperation(value = "更新活动信息")
    @AfterReturning(value = "execution(* com.lhamster.facadeImpl.OrgActivityFacadeImpl.update(..))")
    public void updateActivity(JoinPoint joinPoint) {
        // 获取社团id -> 获取社团名称 -> 通知所有社团成员
        OrgUpdateActivityRequest activityRequest = (OrgUpdateActivityRequest) joinPoint.getArgs()[2];
        OrgActivity activity = orgActivityService.getById(activityRequest.getActId());
        OrgOrganization organization = orgOrganizationService.getById(activity.getActOrganizationId());
        // 通知所有社团成员
        this.messageAllOrganizationUser(
                "[" + organization.getOrganName() + "] 更新了活动信息：" + activity.getActName(),
                activity.getActOrganizationId());
    }

    @ApiOperation(value = "取消活动信息")
    @Before(value = "execution(* com.lhamster.facadeImpl.OrgActivityFacadeImpl.delete(..))")
    public void deleteActivity(JoinPoint joinPoint) {
        // 获取社团id -> 获取社团名称 -> 通知所有社团成员
        OrgActivity activity = orgActivityService.getById((Long) joinPoint.getArgs()[0]);
        OrgOrganization organization = orgOrganizationService.getById(activity.getActOrganizationId());
        // 通知所有社团成员
        this.messageAllOrganizationUser(
                "[" + organization.getOrganName() + "] 取消了活动：" + activity.getActName(),
                activity.getActOrganizationId());
    }
}
