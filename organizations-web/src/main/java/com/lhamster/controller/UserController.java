package com.lhamster.controller;

import cn.hutool.core.util.StrUtil;
import com.lhamster.facade.UserFacade;
import com.lhamster.request.*;
import com.lhamster.response.exception.ServerException;
import com.lhamster.response.result.Response;
import com.lhamster.util.JwtTokenUtil;
import com.lhamster.util.TencentCOSUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/9
 */
@Slf4j
@RestController
@Api(value = "用户")
@RequestMapping(value = "/organizations/web/user")
public class UserController {
    @Reference
    private UserFacade userFacade;

    /*图片格式*/
    private static final List<String> suffix = new ArrayList<>(Arrays.asList(".jpg", ".jpeg", ".png", ".gif"));

    @PostMapping("/message")
    @ApiOperation(value = "发送验证码", notes = "手机号和类型都不能为空", produces = "application/json")
    public Response sendMessage(@RequestBody MessageRequest messageRequest) {
        if (StringUtils.isEmpty(messageRequest.getPhone()) || Objects.isNull(messageRequest.getType())) {
            throw new ServerException(Boolean.FALSE, "手机号或类型为空");
        }
        log.info("手机号:{},类型:{}", messageRequest.getPhone(), messageRequest.getType());
        /*根据类型区分*/
        if (messageRequest.getType().equals(0)) { // 注册
            try {
                // 检查手机号是否已经存在
                Boolean result = userFacade.checkPhone(messageRequest.getPhone());
                if (result) {// 该手机号已存在
                    return new Response(Boolean.FALSE, "该手机号已注册过了");
                } else {// 该手机号不存在,发送短信验证
                    return userFacade.sendMessage(messageRequest);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServerException(Boolean.FALSE, "信息发送失败");
            }
        } else if (messageRequest.getType().equals(1)) { // 修改密码
            return userFacade.sendMessage(messageRequest);
        } else {
            throw new ServerException(Boolean.FALSE, "信息发送失败");
        }
    }

    @PostMapping("/register")
    @ApiOperation(value = "注册", produces = "application/json")
    public Response register(@RequestBody RegisterRequest registerRequest) {
        if (StringUtils.isEmpty(registerRequest.getUsername()) || StringUtils.isEmpty(registerRequest.getPassword())
                || StringUtils.isEmpty(registerRequest.getPhone()) || StringUtils.isEmpty(registerRequest.getCode())) {
            throw new ServerException(Boolean.FALSE, "入参不能为空");
        }
        log.info("入参为：" + registerRequest);
        /*注册*/
        return userFacade.register(registerRequest);
    }

    @PostMapping("/login")
    @ApiOperation(value = "登录", produces = "application/json")
    public Response login(@RequestBody LoginRequest loginRequest) {
        if (StrUtil.hasBlank(loginRequest.getPhone()) || StrUtil.hasBlank(loginRequest.getPassword())) {
            throw new ServerException(Boolean.FALSE, "入参不能为空");
        }
        // 登录
        return userFacade.login(loginRequest);
    }

    @PostMapping("/reset")
    @ApiOperation(value = "重置密码", produces = "application/json")
    public Response reset(@RequestBody RegisterRequest registerRequest) {
        if (StringUtils.isEmpty(registerRequest.getPassword())
                || StringUtils.isEmpty(registerRequest.getPhone())
                || StringUtils.isEmpty(registerRequest.getCode())) {
            throw new ServerException(Boolean.FALSE, "入参不能为空");
        }
        log.info("入参为：{}", registerRequest);
        /*重置密码*/
        return userFacade.resetPassword(registerRequest);
    }

    @PostMapping("/changePassword")
    @ApiOperation(value = "修改密码", produces = "application/json")
    public Response changePassword(@RequestBody ChangePwdRequest changePwdRequest, @RequestHeader(JwtTokenUtil.AUTH_HEADER_KEY) String token) {
        if (StrUtil.hasBlank(changePwdRequest.getNewPwd()) || StrUtil.hasBlank(changePwdRequest.getOldPwd())) {
            throw new ServerException(Boolean.FALSE, "入参不能为空");
        }
        log.info("[入参为]：{}", changePwdRequest);
        // 修改密码
        return userFacade.changePassword(changePwdRequest, JwtTokenUtil.getUserId(token));
    }

    @PostMapping("/updateAvatar")
    @ApiOperation(value = "更换头像", notes = "上传头像")
    public Response updateAvatar(@RequestParam("file") MultipartFile file, HttpSession session, @RequestHeader(JwtTokenUtil.AUTH_HEADER_KEY) String token) {
        if (file.isEmpty()) {
            throw new ServerException(Boolean.FALSE, "上传文件为空");
        }
        String filename = file.getOriginalFilename();
        log.info("文件名:" + filename);
        // uuid生成随机的文件名
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replaceAll("-", "");
        // 新的文件名+文件后缀
        String fileSuffix = filename.substring(filename.lastIndexOf("."));
        if (!suffix.contains(fileSuffix)) { // 不是指定格式
            throw new ServerException(Boolean.FALSE, "文件格式不正确");
        }
        filename = uuid + fileSuffix;
        // 即将写入磁盘的地址
        File localFile = new File(session.getServletContext().getRealPath("/") + filename);
        // 获取用户id
        Long userId = JwtTokenUtil.getUserId(token);
        // 将MultipartFile转为File从内存中写入磁盘
        try {
            file.transferTo(localFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 更新用户头像
        return userFacade.updateAvatar(localFile, filename, userId);
    }

    @PostMapping("/updateUser")
    @ApiOperation(value = "更新用户信息", produces = "application/json")
    public Response updateUser(@RequestBody UpdateUserRequest updateUserRequest, @RequestHeader(JwtTokenUtil.AUTH_HEADER_KEY) String token) {
        log.info("[入参为]：{}", updateUserRequest);
        return userFacade.updateUser(updateUserRequest, JwtTokenUtil.getUserId(token));
    }

    @GetMapping("/info")
    @ApiOperation(value = "获取当前登录用户信息")
    public Response userInfo(@RequestHeader(JwtTokenUtil.AUTH_HEADER_KEY) String token) {
        return userFacade.getCurrentUser(JwtTokenUtil.getUserId(token));
    }

    @DeleteMapping("/cancellation")
    @ApiOperation(value = "注销当前用户")
    public Response cancellation(@RequestHeader(JwtTokenUtil.AUTH_HEADER_KEY) String token) {
        return userFacade.cancellationUser(JwtTokenUtil.getUserId(token));
    }

    @PostMapping("/test")
    public void test(Date date) {
        log.info(String.valueOf(date));
    }
}
