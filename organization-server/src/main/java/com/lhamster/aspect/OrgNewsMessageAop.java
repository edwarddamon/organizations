package com.lhamster.aspect;

import com.lhamster.entity.OrgNews;
import com.lhamster.entity.OrgOrganization;
import com.lhamster.request.OrgCreateActivityRequest;
import com.lhamster.request.OrgCreateNewsRequest;
import com.lhamster.request.OrgUpdateNewsRequest;
import com.lhamster.service.OrgNewsService;
import com.lhamster.service.OrgOrganizationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/17
 */
@Aspect
@Component
@Slf4j
@Api(value = "社团新闻通知")
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class OrgNewsMessageAop {
    private final OrgActivityMessageAop orgActivityMessageAop;
    private final OrgOrganizationService orgOrganizationService;
    private final OrgNewsService orgNewsService;

    @ApiOperation(value = "发布新闻")
    @AfterReturning(value = "execution(* com.lhamster.facadeImpl.OrgNewsFacadeImpl.create(..))")
    public void create(JoinPoint joinPoint) {
        // 获取社团id -> 获取社团名称 -> 通知所有社团成员
        OrgCreateNewsRequest createNewsRequest = (OrgCreateNewsRequest) joinPoint.getArgs()[2];
        OrgOrganization organization = orgOrganizationService.getById(createNewsRequest.getOrgId());
        // 通知所有社团成员
        orgActivityMessageAop.messageAllOrganizationUser(
                "[" + organization.getOrganName() + "] 发布了新闻：" + createNewsRequest.getTitle(),
                createNewsRequest.getOrgId());
    }

    @ApiOperation(value = "更新新闻")
    @AfterReturning(value = "execution(* com.lhamster.facadeImpl.OrgNewsFacadeImpl.update(..))")
    public void update(JoinPoint joinPoint) {
        // 获取社团id -> 获取社团名称 -> 通知所有社团成员
        OrgUpdateNewsRequest orgUpdateNewsRequest = (OrgUpdateNewsRequest) joinPoint.getArgs()[2];
        OrgNews news = orgNewsService.getById(orgUpdateNewsRequest.getNewId());
        OrgOrganization organization = orgOrganizationService.getById(news.getNewOrganizationId());
        // 通知所有社团成员
        orgActivityMessageAop.messageAllOrganizationUser(
                "[" + organization.getOrganName() + "] 更新了新闻：" + news.getNewTitle(),
                news.getNewOrganizationId());
    }
}
