package com.lhamster.facade;

import com.lhamster.request.MessageRequest;
import com.lhamster.response.result.Response;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/9
 */
public interface UserFacade {
    /*检查手机号是否已存在*/
    Boolean checkPhone(String phone);

    /*发送验证码*/
    Response sendMessage(MessageRequest messageRequest);
}
