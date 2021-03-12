package com.lhamster.aspect;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lhamster.entity.OrgDepartment;
import com.lhamster.entity.OrgOrganization;
import com.lhamster.entity.OrgUserRoleRel;
import com.lhamster.request.CancelOrganizationRequest;
import com.lhamster.request.CheckOrganizationRequest;
import com.lhamster.request.CreateOrganizationRequest;
import com.lhamster.service.OrgDepartmentService;
import com.lhamster.service.OrgOrganizationService;
import com.lhamster.service.OrgUserRoleRelService;
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

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

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

}
