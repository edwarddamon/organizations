package com.lhamster.facade;

import com.lhamster.entity.OrgLimit;
import com.lhamster.request.OrgCreateActivityRequest;
import com.lhamster.request.OrgUpdateActivityRequest;
import com.lhamster.response.OrgActivityInfoResponse;
import com.lhamster.response.OrgActivityListInfoResponse;
import com.lhamster.response.result.Response;

import java.io.File;
import java.util.List;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/17
 */
public interface OrgActivityFacade {
    /*限制列表*/
    Response<List<OrgLimit>> limit();

    /*创建活动*/
    Response create(File localFile, String fileName, OrgCreateActivityRequest orgCreateActivityRequest, Long userId);

    /*更新活动*/
    Response update(File localFile, String fileName, OrgUpdateActivityRequest orgUpdateActivityRequest, Long userId);

    /*取消活动*/
    Response delete(Long actId, Long userId);

    /*分页*/
    Response<List<OrgActivityListInfoResponse>> page(Integer pageNo, Integer pageSize, String name);

    /*活动详情*/
    Response<OrgActivityInfoResponse> detail(Long actId);

    /*指定社团的活动列表*/
    Response<List<OrgActivityListInfoResponse>> list(Long orgId);
}
