package com.lhamster.aspect;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lhamster.entity.OrgDepartment;
import com.lhamster.entity.OrgOrganization;
import com.lhamster.entity.OrgUserOrganizationRel;
import com.lhamster.entity.OrgUserRoleRel;
import com.lhamster.request.CancelOrganizationRequest;
import com.lhamster.request.CheckOrganizationRequest;
import com.lhamster.request.CreateOrganizationRequest;
import com.lhamster.request.OfficeDepartmentRequest;
import com.lhamster.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/11
 */
@Aspect
@Component
@Slf4j
@Api(value = "社团模块通知")
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
public class OrganizationMessageAop {
    private final UserMessageAop userMessageAop;
    private final OrgUserRoleRelService orgUserRoleRelService;
    private final OrgOrganizationService orgOrganizationService;
    private final OrgDepartmentService orgDepartmentService;
    private final OrgUserOrganizationRelService orgUserOrganizationRelService;
    private final OrgUserService orgUserService;

    /*
     * 给社联主席和所有社联管理员发送消息
     * */
    private void messageAllAdmin(String msg) {
        // 获取所以社联管理员和社联主席的id
        List<OrgUserRoleRel> relList = orgUserRoleRelService.list(new QueryWrapper<OrgUserRoleRel>()
                .in("rel_role_id", 3, 4));
        List<Long> idsList = new ArrayList<>();
        relList.forEach(rel -> {
            idsList.add(rel.getRelUserId());
        });
        // 通知所有社联管理员和社联主席该社团申请注册
        idsList.forEach(id -> {
            userMessageAop.saveMessage(msg, id);
        });
    }

    @ApiOperation(value = "社团创建神情消息")
    @AfterReturning(value = "execution(* com.lhamster.facadeImpl.OrganizationFacadeImpl.createOrganization(..))")
    private void createOrg(JoinPoint joinPoint) {
        // 通知表插入数据
        userMessageAop.saveMessage("社团创建申请成功，请等待审核", (Long) joinPoint.getArgs()[1]);
        messageAllAdmin("[" + ((CreateOrganizationRequest) joinPoint.getArgs()[0]).getName() + "] 申请创建");
    }

    @ApiOperation(value = "社团注销申请消息")
    @AfterReturning(value = "execution(* com.lhamster.facadeImpl.OrganizationFacadeImpl.cancelOrganization(..))")
    private void cancelOrg(JoinPoint joinPoint) {
        // 通知表插入数据
        userMessageAop.saveMessage("社团注销申请成功，请等待审核", (Long) joinPoint.getArgs()[1]);
        Long id = ((CancelOrganizationRequest) joinPoint.getArgs()[0]).getId();
        messageAllAdmin("[" + orgOrganizationService.getById(id).getOrganName() + "] 申请注销");
    }

    @ApiOperation(value = "审批社团注册/注销审批")
    @Before(value = "execution(* com.lhamster.facadeImpl.OrganizationFacadeImpl.check(..))")
    private void check(JoinPoint joinPoint) {
        // 社团id
        CheckOrganizationRequest arg = (CheckOrganizationRequest) joinPoint.getArgs()[0];
        Long id = arg.getId();
        // 获取社长id
        Long depMinisterId = orgDepartmentService.getOne(new QueryWrapper<OrgDepartment>()
                .eq("dep_organization_id", id)
                .eq("dep_name", "社长")).getDepMinisterId();
        // 获取社团名称
        OrgOrganization orgOrganization = orgOrganizationService.getById(id);
        // 通知当前登录用户
        userMessageAop.saveMessage("[" + orgOrganization.getOrganName() + "] 审批" + (arg.getResult() == 0 ? "通过" : "不通过"), (Long) joinPoint.getArgs()[1]);
        // 通知社长
        userMessageAop.saveMessage("您的社团 [" + orgOrganization.getOrganName() + "] 审批" + (arg.getResult() == 0 ? "通过" : "不通过"), depMinisterId);
    }

    @ApiOperation(value = "任职和解雇通知")
    @AfterReturning(value = "execution(* com.lhamster.facadeImpl.DepartmentFacadeImpl.office(..))")
    private void office(JoinPoint joinPoint) {
        OfficeDepartmentRequest joinPointArg = (OfficeDepartmentRequest) joinPoint.getArgs()[0];
        Long userId = joinPointArg.getTargetId();
        // 获取社团名称
        String organName = orgOrganizationService.getById(joinPointArg.getOrgId()).getOrganName();
        // 获取部门名称
        OrgDepartment department = orgDepartmentService.getById(joinPointArg.getDepId());
        // 获取职位名称
        String jobName = null;
        if (department.getDepName().equals("副社长") || department.getDepName().equals("团支书") || department.getDepName().equals("财务")) {
            jobName = department.getDepName();
        } else {
            if (joinPointArg.getPosition().equals(1)) {
                jobName = department.getDepName() + "\t部长";
            } else {
                jobName = department.getDepName() + "\t副部长";
            }
        }
        userMessageAop.saveMessage("您已被" + (joinPointArg.getJudge() ? "任职" : "解雇") +
                " [" + organName + "] " + "【" + jobName + "】职位", userId);
    }

    @ApiOperation(value = "委任下届社长")
    @AfterReturning(value = "execution(* com.lhamster.facadeImpl.DepartmentFacadeImpl.boss(..))")
    private void boss(JoinPoint joinPoint) {
        OrgOrganization organization = orgOrganizationService.getById((Long) joinPoint.getArgs()[0]);
        // 通知被委任的人
        userMessageAop.saveMessage("您已被委任 [" + organization.getOrganName() + "] 【社长】职位", (Long) joinPoint.getArgs()[1]);
        // 获取新社长名称
        String username = orgUserService.getById((Long) joinPoint.getArgs()[1]).getUserUsername();
        // 通知该社团所有人
        List<Long> userIds = orgUserOrganizationRelService.list(new QueryWrapper<OrgUserOrganizationRel>()
                .eq("rel_organization_id", (Long) joinPoint.getArgs()[0])
                .eq("rel_role_id", 2))
                .stream().map(OrgUserOrganizationRel::getRelUserId).distinct().collect(Collectors.toList());
        userIds.forEach(userId -> {
            userMessageAop.saveMessage("您所在的社团 [" + organization.getOrganName() + "] 已更换【社长】,新【社长】为：" + username, userId);
        });
    }

    @ApiOperation(value = "社团星级评定")
    @AfterReturning(value = "execution(* com.lhamster.facadeImpl.OrganizationFacadeImpl.star(..))")
    public void star(JoinPoint joinPoint) {
        // 获取社团名称
        String organName = orgOrganizationService.getById((Long) joinPoint.getArgs()[0]).getOrganName();
        // 通知该社团所有人
        List<Long> userIds = orgUserOrganizationRelService.list(new QueryWrapper<OrgUserOrganizationRel>()
                .eq("rel_organization_id", (Long) joinPoint.getArgs()[0])
                .eq("rel_role_id", 2))
                .stream().map(OrgUserOrganizationRel::getRelUserId).distinct().collect(Collectors.toList());
        userIds.forEach(userId -> {
            userMessageAop.saveMessage("您所在的社团 [" + organName + "] 已重新判定星级，现在星级为：" + joinPoint.getArgs()[1] + "星级", userId);
        });
    }
}
