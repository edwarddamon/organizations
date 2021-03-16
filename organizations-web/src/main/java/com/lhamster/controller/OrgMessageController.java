package com.lhamster.controller;

import com.lhamster.facade.OrgMessageFacade;
import com.lhamster.response.OrgMessageInfoResponse;
import com.lhamster.response.result.Response;
import com.lhamster.util.JwtTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/15
 */
@Slf4j
@RestController
@Api(value = "消息通知")
@RequestMapping(value = "/organizations/web/message")
public class OrgMessageController {
    @Reference
    private OrgMessageFacade messageFacade;

    @GetMapping("/msg")
    @ApiOperation(value = "消息通知列表")
    public Response<List<OrgMessageInfoResponse>> msg(@RequestHeader(JwtTokenUtil.AUTH_HEADER_KEY) String token) {
        return messageFacade.msgList(JwtTokenUtil.getUserId(token));
    }

    @PutMapping(value = {"/read/{status}/{msgId}", "/read/{status}"})
    @ApiOperation(value = "一键已读/删除所有通知",
            notes = "不传msgId为全部已读或删除，传则指定通知已读或删除；已读：READ；删除：DELETED")
    public Response read(@PathVariable("status") String status,
                         @PathVariable(value = "msgId", required = false) Long msgId,
                         @RequestHeader(JwtTokenUtil.AUTH_HEADER_KEY) String token) {
        return messageFacade.read(status, msgId, JwtTokenUtil.getUserId(token));
    }
}
