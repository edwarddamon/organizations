package com.lhamster.aspect;

import com.lhamster.entity.OrgMessage;
import com.lhamster.mapper.OrgUserMapper;
import com.lhamster.request.RegisterRequest;
import com.lhamster.service.OrgMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/10
 */
@Aspect
@Component
@Slf4j
@Api(value = "用户模块通知")
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class OrgUserMessageAop {
    private final OrgMessageService orgMessageService;
    private final OrgUserMapper orgUserMapper;

    /*
     * 提取公共代码
     * */
    public void saveMessage(String msg, Long userId) {
        OrgMessage orgMessage = OrgMessage.builder()
                .mesContent(msg)
                .mesStatus("UNREAD")
                .mesTargetId(userId)
                .createAt(LocalDateTime.now())
                .build();
        orgMessageService.save(orgMessage);
    }

    @ApiOperation(value = "手机验证码重置密码通知")
    @AfterReturning(value = "execution(* com.lhamster.facadeImpl.OrgUserFacadeImpl.resetPassword(..))")
    private void resetPasswordAop(JoinPoint joinPoint) {
        // 通知表插入数据
        Long userId = orgUserMapper
                .getByPhone(((RegisterRequest) joinPoint.getArgs()[0])
                        .getPhone())
                .getUserId();
        saveMessage("密码重置成功", userId);
    }

    @ApiOperation(value = "修改密码")
    @AfterReturning(value = "execution(* com.lhamster.facadeImpl.OrgUserFacadeImpl.changePassword(..))")
    private void changePassword(JoinPoint joinPoint) {
        // 通知表插入数据
        saveMessage("密码修改成功", (Long) joinPoint.getArgs()[1]);
    }

    @ApiOperation(value = "更新用户信息")
    @AfterReturning(value = "execution(* com.lhamster.facadeImpl.OrgUserFacadeImpl.updateUser(..))")
    private void updateUser(JoinPoint joinPoint) {
        // 通知表插入数据
        saveMessage("用户信息更新成功", (Long) joinPoint.getArgs()[1]);
    }
}
