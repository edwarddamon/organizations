package com.lhamster.facade;

import com.lhamster.response.OrgMessageInfoResponse;
import com.lhamster.response.result.Response;

import java.util.List;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/15
 */
public interface OrgMessageFacade {
    /*消息列表*/
    Response<List<OrgMessageInfoResponse>> msgList(Long userId);

    /*修改状态*/
    Response read(String status, Long msgId, Long userId);
}
