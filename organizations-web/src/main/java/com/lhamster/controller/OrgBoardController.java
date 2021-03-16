package com.lhamster.controller;

import com.lhamster.facade.OrgBoardFacade;
import com.lhamster.request.OrgBoardRequest;
import com.lhamster.request.OrgUpdateBoardRequest;
import com.lhamster.response.OrgBoardLIstInfoResponse;
import com.lhamster.response.result.Response;
import com.lhamster.util.JwtTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/16
 */
@Slf4j
@RestController
@Api(value = "公告")
@RequestMapping(value = "/organizations/web/boards")
public class OrgBoardController {
    @Reference
    private OrgBoardFacade orgBoardFacade;

    @GetMapping(value = {"/board/org/{orgId}", "/board/boa/{boaId}"})
    @ApiOperation(value = "公告列表", notes = "两个参数只能传一个；传orgId：查询公告列表；传boaId：查询公告详情")
    public Response<List<OrgBoardLIstInfoResponse>> board(@PathVariable(value = "orgId", required = false) Long orgId,
                                                          @PathVariable(value = "boaId", required = false) Long boaId) {
        return orgBoardFacade.getBoradList(orgId, boaId);
    }

    @PostMapping("/board")
    @ApiOperation(value = "发布公告", notes = "社团核心管理人员才可发布")
    public Response board(@Validated @RequestBody OrgBoardRequest orgBoardRequest,
                          @RequestHeader(JwtTokenUtil.AUTH_HEADER_KEY) String token) {
        return orgBoardFacade.board(orgBoardRequest, JwtTokenUtil.getUserId(token));
    }

    @PutMapping("/board")
    @ApiOperation(value = "更新公告", notes = "社团核心管理人员才可发布")
    public Response updateBoard(@Validated @RequestBody OrgUpdateBoardRequest orgUpdateBoardRequest,
                                @RequestHeader(JwtTokenUtil.AUTH_HEADER_KEY) String token) {
        return orgBoardFacade.updateBoard(orgUpdateBoardRequest, JwtTokenUtil.getUserId(token));
    }

    @DeleteMapping("/board/{boaId}")
    @ApiOperation(value = "删除公告", notes = "社团核心管理人员才可发布")
    public Response updateBoard(@PathVariable("boaId") Long boaId,
                                @RequestHeader(JwtTokenUtil.AUTH_HEADER_KEY) String token) {
        return orgBoardFacade.deleteBoard(boaId, JwtTokenUtil.getUserId(token));
    }
}
