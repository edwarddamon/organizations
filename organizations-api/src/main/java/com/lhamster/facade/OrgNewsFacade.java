package com.lhamster.facade;

import com.lhamster.request.OrgCommentRequest;
import com.lhamster.request.OrgCreateNewsRequest;
import com.lhamster.request.OrgUpdateNewsRequest;
import com.lhamster.response.result.Response;

import java.io.File;

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
}
