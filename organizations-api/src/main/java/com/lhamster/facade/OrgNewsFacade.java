package com.lhamster.facade;

import com.lhamster.request.OrgCommentRequest;
import com.lhamster.request.OrgCreateNewsRequest;
import com.lhamster.request.OrgNewsPageRequest;
import com.lhamster.request.OrgUpdateNewsRequest;
import com.lhamster.response.OrgNewsInfoResponse;
import com.lhamster.response.OrgNewsListInfoResponse;
import com.lhamster.response.result.Response;

import java.io.File;
import java.util.List;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/17
 */
public interface OrgNewsFacade {
    /*发布新闻*/
    Response create(File localFile, String fileName, OrgCreateNewsRequest orgCreateNewsRequest, Long userId);

    /*跟新新闻*/
    Response update(File localFile, String fileName, OrgUpdateNewsRequest orgUpdateNewsRequest, Long userId);

    /*发布评论*/
    Response comment(OrgCommentRequest orgCommentRequest, Long userId);

    /*点赞与取消点赞*/
    Response good(Long comId, Boolean status);

    /*新闻分页*/
    Response<List<OrgNewsListInfoResponse>> page(OrgNewsPageRequest orgNewsPageRequest);

    /*新闻详情*/
    Response<OrgNewsInfoResponse> detail(Long newId);

    /*删除新闻*/
    Response delete(Long newId, Long userId);

    /*指定社团的新闻列表*/
    Response<List<OrgNewsListInfoResponse>> list(Long orgId);
}
