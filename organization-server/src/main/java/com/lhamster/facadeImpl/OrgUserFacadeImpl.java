package com.lhamster.facadeImpl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.lhamster.entity.OrgDepartment;
import com.lhamster.entity.OrgUser;
import com.lhamster.entity.OrgUserOrganizationRel;
import com.lhamster.entity.OrgUserRoleRel;
import com.lhamster.facade.OrgRoleFacade;
import com.lhamster.request.*;
import com.lhamster.response.OrgUserInfoResponse;
import com.lhamster.service.*;
import com.lhamster.util.JwtTokenUtil;
import com.lhamster.util.SmsUtils;
import com.lhamster.facade.OrgUserFacade;
import com.lhamster.response.exception.ServerException;
import com.lhamster.response.result.Response;
import com.lhamster.util.TencentCOSUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.sql.Struct;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/9
 */
@Slf4j
@Component
@Service
@RequiredArgsConstructor
public class OrgUserFacadeImpl implements OrgUserFacade {
    private final OrgUserService orgUserService;
    private final OrgUserOrganizationRelService orgUserOrganizationRelService;
    private final OrgDepartmentService orgDepartmentService;
    private final OrgUserRoleRelService orgUserRoleRelService;
    private final RedisTemplate redisTemplate;

    @Override
    public Boolean checkPhone(String phone) {
        return orgUserService.getByPhone(phone);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response sendMessage(MessageRequest messageRequest) {
        String randomCode = SmsUtils.generateRandomCode();// 六位随机数验证码
        long currTime = new Date().getTime();// 当前时间的时间戳
        String res = SmsUtils.sendSms(randomCode, new String[]{"86" + messageRequest.getPhone()}, messageRequest.getType());
        if ("success".equals(res)) {// 发送成功
            // 以手机号为key，将随机数验证码和当前时间的时间戳存入redis中
            HashMap<String, Object> map = new HashMap<>();
            map.put("code", randomCode);
            map.put("time", currTime);
            map.put("type", messageRequest.getType());
            redisTemplate.opsForHash().putAll(messageRequest.getPhone(), map);
            log.info(messageRequest.getPhone() + "   " + randomCode + "   " + currTime);
            return new Response(Boolean.TRUE, "短信发送成功");
        } else {// 发送失败
            return new Response(Boolean.FALSE, "短信发送失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response register(RegisterRequest registerRequest) {
        // 去redis缓存检查code是否存在，对比是否过期
        if (!redisTemplate.opsForHash().hasKey(registerRequest.getPhone(), "code")
                || !redisTemplate.opsForHash().hasKey(registerRequest.getPhone(), "time")) {
            throw new ServerException(Boolean.FALSE, "缓存中不存在验证码或时间，请重新发送验证码");
        }
        String redisCode = (String) redisTemplate.opsForHash().get(registerRequest.getPhone(), "code");
        Long redisTime = (Long) redisTemplate.opsForHash().get(registerRequest.getPhone(), "time");
        if (!StringUtils.isEmpty(redisCode) && Objects.nonNull(redisTime)) { // 读取到了存入redis的该手机号的信息
            // 判断该信息是否过期
            long nowTime = new Date().getTime();
            if (nowTime - redisTime < 120 * 1000) { // 没过期：对比redisCode和前端传递的code是否一致
                if (redisCode.equals(registerRequest.getCode())) {// 一致
                    //将手机号和密码存入数据库，注册成功
                    try {
                        OrgUser orgUser = new OrgUser();
                        orgUser.setUserUsername(registerRequest.getUsername());
                        // 存储桶中的默认头像
                        String avatar = "https://lhamster-organizations-1302533254.cos.ap-nanjing.myqcloud.com/avatar/" + new Random().nextInt(3) + ".jpg";
                        orgUser.setUserAvatar(avatar);
                        orgUser.setUserPassword(registerRequest.getPassword());
                        orgUser.setUserPhone(registerRequest.getPhone());
                        orgUser.setUserStatus("NORMAL");
                        orgUser.setCreateAt(LocalDateTime.now());
                        orgUserService.save(orgUser);
                        // 删除redis中的相关信息
                        redisTemplate.delete(registerRequest.getPhone());
                        return new Response(Boolean.TRUE, "注册成功");
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new ServerException(Boolean.FALSE, "注册失败");
                    }
                } else { // 不一致
                    return new Response(Boolean.FALSE, "验证码错误");
                }
            } else { // 过期
                redisTemplate.delete(registerRequest.getPhone());
                throw new ServerException(Boolean.FALSE, "验证码已过期");
            }
        } else {
            throw new ServerException(Boolean.FALSE, "缓存验证码或时间为空，请重新发送验证码");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response login(LoginRequest loginRequest) {
        OrgUser user = orgUserService.login(loginRequest);
        // 登陆失败
        if (Objects.isNull(user)) {
            throw new ServerException(Boolean.FALSE, "用户名或密码错误");
        }
        // 登录成功
        String jwt = JwtTokenUtil.createJWT(String.valueOf(user.getUserId()), user.getUserUsername(), null);
        log.info("[JWT]：{}", jwt);
        return new Response<String>(Boolean.TRUE, "登陆成功", JwtTokenUtil.TOKEN_PREFIX + jwt);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response resetPassword(RegisterRequest registerRequest) {
        // 去redis缓存检查code是否存在，对比是否过期
        if (!redisTemplate.opsForHash().hasKey(registerRequest.getPhone(), "code")
                || !redisTemplate.opsForHash().hasKey(registerRequest.getPhone(), "time")) {
            throw new ServerException(Boolean.FALSE, "缓存中不存在验证码或时间，请重新发送验证码");
        }
        String redisCode = (String) redisTemplate.opsForHash().get(registerRequest.getPhone(), "code");
        Long redisTime = (Long) redisTemplate.opsForHash().get(registerRequest.getPhone(), "time");
        if (!StringUtils.isEmpty(redisCode) && Objects.nonNull(redisTime)) { // 读取到了存入redis的该手机号的信息
            // 判断该信息是否过期
            long nowTime = new Date().getTime();
            if (nowTime - redisTime < 120 * 1000) { // 没过期：对比redisCode和前端传递的code是否一致
                if (redisCode.equals(registerRequest.getCode())) {// 一致
                    // 将新密码存入数据库
                    try {
                        orgUserService.resetPhone(registerRequest);
                        // 删除redis中的相关信息
                        redisTemplate.delete(registerRequest.getPhone());
                        return new Response(Boolean.TRUE, "重置密码成功");
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new ServerException(Boolean.FALSE, "重置密码失败");
                    }
                } else { // 不一致
                    throw new ServerException(Boolean.FALSE, "验证码错误");
                }
            } else { // 过期
                redisTemplate.delete(registerRequest.getPhone());
                throw new ServerException(Boolean.FALSE, "验证码已过期");
            }
        } else {
            throw new ServerException(Boolean.FALSE, "缓存中验证码或时间为空，请重新发送验证码");
        }
    }

    @Override
    public Response changePassword(ChangePwdRequest changePwdRequest, Long userId) {
        OrgUser orgUser = orgUserService.getById(userId);
        // 旧密码正确
        if (orgUser.getUserPassword().equals(changePwdRequest.getOldPwd())) {
            // 修改密码
            orgUser.setUserPassword(changePwdRequest.getNewPwd());
            orgUserService.updateById(orgUser);
            return new Response(Boolean.TRUE, "密码修改成功");
        } else {
            throw new ServerException(Boolean.FALSE, "旧密码错误");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response updateAvatar(File localFile, String filename, Long userId) {
        try {
            // 新头像的地址
            String headPicUrl = TencentCOSUtil.uploadObject(localFile, "avatar/" + filename);
            // 查询旧头像地址
            OrgUser orgUser = orgUserService.getById(userId);
            // 删除旧头像
            String oldHeadPicUrl = orgUser.getUserAvatar();
            String oldFileName = oldHeadPicUrl.substring(oldHeadPicUrl.lastIndexOf("/") + 1);
            if (oldFileName.length() > 6) { // 默认的图标不删除
                TencentCOSUtil.deletefile("avatar/" + oldFileName);
            }
            // 将新头像地址插入数据库
            orgUser.setUserAvatar(headPicUrl);
            orgUserService.updateById(orgUser);
            return new Response(Boolean.TRUE, "头像更新成功");
        } catch (Exception e) {
            throw new ServerException(Boolean.FALSE, "头像更新失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response updateUser(UpdateUserRequest updateUserRequest, Long userId) {
        OrgUser orgUser = orgUserService.getById(userId);
        if (!StrUtil.hasBlank(updateUserRequest.getNickName())) {
            orgUser.setUserUsername(updateUserRequest.getNickName());
        }
        if (Objects.nonNull(updateUserRequest.getSex())) {
            orgUser.setUserSex(updateUserRequest.getSex());
        }
        if (!StrUtil.hasBlank(updateUserRequest.getQQ())) {
            orgUser.setUserQq(updateUserRequest.getQQ());
        }
        if (!StrUtil.hasBlank(updateUserRequest.getVx())) {
            orgUser.setUserVx(updateUserRequest.getVx());
        }
        orgUser.setUpdateAt(LocalDateTime.now());
        try {
            orgUserService.updateById(orgUser);
            return new Response(Boolean.TRUE, "更新用户信息成功");
        } catch (Exception e) {
            throw new ServerException(Boolean.FALSE, "更新用户信息失败");
        }
    }

    @Override
    public Response getCurrentUser(Long userId) {
        OrgUser orgUser = orgUserService.getById(userId);
        // 检查当前用户是否为社联主席或社联管理员
        int count2 = orgUserRoleRelService.count(new QueryWrapper<OrgUserRoleRel>()
                .eq("rel_user_id", userId)
                .in("rel_role_id", 3L, 4L));
        Boolean identity = Boolean.FALSE;
        if (count2 > 0) {
            identity = Boolean.TRUE;
        }
        OrgUserInfoResponse userInfoResponse = OrgUserInfoResponse.builder()
                .userId(orgUser.getUserId())
                .userAvatar(orgUser.getUserAvatar())
                .userUsername(orgUser.getUserUsername())
                .userPhone(orgUser.getUserPhone())
                .userQq(orgUser.getUserQq())
                .userVx(orgUser.getUserVx())
                .userSex(orgUser.getUserSex())
                .identity(identity)
                .build();
        return new Response<OrgUserInfoResponse>(Boolean.TRUE, "获取用户信息成功", userInfoResponse);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response cancellationUser(Long userId) {
        // 检查是否在某个社团任职
        int count = orgDepartmentService.count(new QueryWrapper<OrgDepartment>()
                .eq("dep_minister_id", userId)
                .or()
                .eq("dep_vice_minister_id", userId));
        if (count > 0) {
            throw new ServerException(Boolean.FALSE, "您正在任职，无法注销账户");
        }
        // 检查用户是否为某社团社员
        int count1 = orgUserOrganizationRelService.count(new QueryWrapper<OrgUserOrganizationRel>()
                .eq("rel_user_id", userId)
                .eq("rel_role_id", 2));
        if (count1 > 0) {
            throw new ServerException(Boolean.FALSE, "您是社员，无法注销账户");
        }
        // 检查是否为社联主席或社联管理员
        int count2 = orgUserRoleRelService.count(new QueryWrapper<OrgUserRoleRel>()
                .eq("rel_user_id", userId)
                .in("rel_role_id", 3, 4));
        if (count2 > 0) {
            throw new ServerException(Boolean.FALSE, "您是社联主席或社联管理员，无法注销账户");
        }
        // 如果都不是则允许注销
        OrgUser orgUser = orgUserService.getById(userId);
        String avatar = orgUser.getUserAvatar();
        // 更改用户状态
        orgUser.setUserPhone("-1");
        orgUser.setUserStatus("CANCEL");
        String oldFileName = avatar.substring(avatar.lastIndexOf("/") + 1);
        if (oldFileName.length() > 6) { // 默认的图标不删除
            TencentCOSUtil.deletefile("avatar/" + oldFileName);
        }
        orgUser.setUserAvatar(null);
        boolean res = orgUserService.updateById(orgUser);
        return new Response(Boolean.TRUE, "注销账户成功");
    }

    @Override
    public Response<List<OrgUserInfoResponse>> user(OrgUserRequest orgUserRequest) {
        QueryWrapper<OrgUser> queryWrapper = new QueryWrapper<>();
        if (!StrUtil.hasBlank(orgUserRequest.getUsername())) {
            queryWrapper.like("user_username", orgUserRequest.getUsername());
        }
        queryWrapper.orderByDesc("create_at");
        // 分页查询
        Page<OrgUser> page = orgUserService.page(new Page<OrgUser>(orgUserRequest.getPageNo(), orgUserRequest.getPageSize()), queryWrapper);
        List<OrgUserInfoResponse> responses = new ArrayList<>();
        page.getRecords().forEach(orgUser -> {
            responses.add(OrgUserInfoResponse.builder()
                    .userAvatar(orgUser.getUserAvatar())
                    .userUsername(orgUser.getUserUsername())
                    .userSex(orgUser.getUserSex())
                    .userPhone(orgUser.getUserPhone())
                    .userQq(orgUser.getUserQq())
                    .userVx(orgUser.getUserVx())
                    .userId(orgUser.getUserId())
                    .build());
        });
        return new Response<List<OrgUserInfoResponse>>(Boolean.TRUE, "查询成功", (int) page.getTotal(), responses);
    }

    @Override
    public Response<List<OrgUserInfoResponse>> userList(OrgUserListRequest orgUserRequest) {
        com.github.pagehelper.Page<OrgUserInfoResponse> page = PageHelper.startPage(orgUserRequest.getPageNo(), orgUserRequest.getPageSize());
        List<OrgUserInfoResponse> users = orgUserOrganizationRelService.listMyself(orgUserRequest);
        return new Response<List<OrgUserInfoResponse>>(Boolean.TRUE, "查询成功", (int) page.getTotal(), users);
    }

    @Override
    public Response<List<OrgUserInfoResponse>> list(UserListRequest orgUserRequest) {
        QueryWrapper<OrgUser> wrapper = new QueryWrapper<>();
        wrapper.eq("user_status", "NORMAL");
        if (StrUtil.isNotBlank(orgUserRequest.getUsername())) {
            wrapper.like("user_username", orgUserRequest.getUsername());
        }
        Page<OrgUser> page = orgUserService.page(new Page<>(orgUserRequest.getPageNo(), orgUserRequest.getPageSize()), wrapper);
        List<OrgUserInfoResponse> userInfoResponseArrayList = new ArrayList<>();
        page.getRecords().forEach(user -> {
            userInfoResponseArrayList.add(OrgUserInfoResponse.builder()
                    .userId(user.getUserId())
                    .userVx(user.getUserVx())
                    .userQq(user.getUserQq())
                    .userPhone(user.getUserPhone())
                    .userSex(user.getUserSex())
                    .userUsername(user.getUserUsername())
                    .userAvatar(user.getUserAvatar())
                    .build());
        });
        return new Response<List<OrgUserInfoResponse>>(Boolean.TRUE, "查询成功", (int) page.getTotal(), userInfoResponseArrayList);
    }
}
