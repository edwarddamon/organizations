package com.lhamster.facade;

import com.lhamster.request.*;
import com.lhamster.response.result.Response;

import java.io.File;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/9
 */
public interface OrgUserFacade {
    /*检查手机号是否已存在*/
    Boolean checkPhone(String phone);

    /*发送验证码*/
    Response sendMessage(MessageRequest messageRequest);

    /*用户注册*/
    Response register(RegisterRequest registerRequest);

    /*用户登录*/
    Response login(LoginRequest loginRequest);

    /*重置密码*/
    Response resetPassword(RegisterRequest registerRequest);

    /*修改密码*/
    Response changePassword(ChangePwdRequest changePwdRequest, Long userId);

    /*更新用户头像*/
    Response updateAvatar(File localFile, String filename, Long userId);

    /*更新用户信息*/
    Response updateUser(UpdateUserRequest updateUserRequest, Long userId);

    /*获取当前登录用户的信息*/
    Response getCurrentUser(Long userId);

    /*注销当前用户*/
    Response cancellationUser(Long userId);
}
