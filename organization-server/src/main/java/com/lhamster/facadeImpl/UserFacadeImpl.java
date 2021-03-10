package com.lhamster.facadeImpl;

import com.lhamster.entity.OrgUser;
import com.lhamster.exception.util.SmsUtils;
import com.lhamster.facade.UserFacade;
import com.lhamster.request.MessageRequest;
import com.lhamster.request.RegisterRequest;
import com.lhamster.response.exception.ServerException;
import com.lhamster.response.result.Response;
import com.lhamster.service.OrgUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.apache.ibatis.annotations.Result;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/9
 */
@Slf4j
@Component
@Service
@RequiredArgsConstructor
public class UserFacadeImpl implements UserFacade {
    private final OrgUserService orgUserService;
    private final RedisTemplate redisTemplate;

    @Override
    public Boolean checkPhone(String phone) {
        return orgUserService.getByPhone(phone);
    }

    @Override
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
    public Response register(RegisterRequest registerRequest) {
        // 去redis缓存检查code是否存在，对比是否过期
        if (!redisTemplate.opsForHash().hasKey(registerRequest.getPhone(), "code")
                || !redisTemplate.opsForHash().hasKey(registerRequest.getPhone(), "time")) {
            throw new ServerException(Boolean.FALSE, "缓存中不存在验证码或时间，请重新发送验证码");
        }
        String redisCode = (String) redisTemplate.opsForHash().get(registerRequest.getPhone(), "code");
        long redisTime = (long) redisTemplate.opsForHash().get(registerRequest.getPhone(), "time");
        if (!StringUtils.isEmpty(redisCode) && !StringUtils.isEmpty(redisTime)) { // 读取到了存入redis的该手机号的信息
            // 判断该信息是否过期
            long nowTime = new Date().getTime();
            if (nowTime - redisTime < 120 * 1000) { // 没过期：对比redisCode和前端传递的code是否一致
                if (redisCode.equals(registerRequest.getCode())) {// 一致
                    //将手机号和密码存入数据库，注册成功
                    try {
                        OrgUser orgUser = new OrgUser();
                        orgUser.setUserUsername(registerRequest.getUsername());
                        orgUser.setUserPassword(registerRequest.getPassword());
                        orgUser.setUserPhone(registerRequest.getPhone());
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
}
