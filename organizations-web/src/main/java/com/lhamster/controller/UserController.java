package com.lhamster.controller;

import com.lhamster.facade.UserFacade;
import com.lhamster.request.MessageRequest;
import com.lhamster.response.exception.ServerException;
import com.lhamster.response.result.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/9
 */
@Slf4j
@RestController
@Api(value = "用户")
public class UserController {

    @Reference
    private UserFacade userFacade;

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
}
