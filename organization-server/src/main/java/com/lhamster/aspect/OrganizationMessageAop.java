package com.lhamster.aspect;

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
 * @date 2021/3/11
 */
@Aspect
@Component
@Slf4j
@Api(value = "社团模块通知")
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class OrganizationMessageAop {
    private final UserMessageAop userMessageAop;

    @ApiOperation(value = "社团创建消息")
    @AfterReturning(value = "execution(* com.lhamster.facadeImpl.OrganizationFacadeImpl.createOrganization(..))")
    private void updateUser(JoinPoint joinPoint) {
        // 通知表插入数据
        userMessageAop.saveMessage("社团创建成功", (Long) joinPoint.getArgs()[1]);
    }
}
