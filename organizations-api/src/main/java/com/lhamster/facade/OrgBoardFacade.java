package com.lhamster.facade;

import com.lhamster.request.OrgBoardRequest;
import com.lhamster.request.OrgUpdateBoardRequest;
import com.lhamster.response.OrgBoardLIstInfoResponse;
import com.lhamster.response.result.Response;

import java.util.List;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/16
 */
public interface OrgBoardFacade {
    /*发布公告*/
    Response board(OrgBoardRequest orgBoardRequest, Long userId);

    /*更新公告*/
    Response updateBoard(OrgUpdateBoardRequest orgUpdateBoardRequest, Long userId);

    /*删除公告*/
    Response deleteBoard(Long boaId, Long userId);

    /*公告列表*/
    Response<List<OrgBoardLIstInfoResponse>> getBoradList(Long orgId, Long boaId);
}
