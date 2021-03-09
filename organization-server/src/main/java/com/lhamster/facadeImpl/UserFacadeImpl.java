package com.lhamster.facadeImpl;

import com.lhamster.exception.util.SmsUtils;
import com.lhamster.facade.UserFacade;
import com.lhamster.request.MessageRequest;
import com.lhamster.response.result.Response;
import com.lhamster.service.OrgUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

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
}
