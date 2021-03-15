package com.lhamster.facade;

import com.lhamster.request.*;
import com.lhamster.response.OrgApplicationListInfoResponse;
import com.lhamster.response.OrgOrganizationInfoResponse;
import com.lhamster.response.OrgOrganizationListInfoResponse;
import com.lhamster.response.result.Response;

import java.io.File;
import java.util.List;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/11
 */
public interface OrganizationFacade {
    /*上传社团封面*/
    Response<String> updateAvatar(File localFile, String filename);

    Response<String> updateAvatar(String orgId, File localFile, String fileName);

    void deleteOldIntroductionUrls(String orgId);

    Response<String> updateNewIntroductionUrl(File localFile, String fileName);

    /*申请创建社团*/
    Response createOrganization(CreateOrganizationRequest createOrganizationRequest, Long userId);

    /*申请注销社团*/
    Response cancelOrganization(CancelOrganizationRequest org_id, Long userId);

    /*审核社团注册/注销申请*/
    Response check(CheckOrganizationRequest checkOrganizationRequest, Long userId);

    /*更新社团信息*/
    Response updateOrganization(UpdateOrganizationRequest updateOrganizationRequest, String newAvatarUrl, List<String> newIntroductionUrls, Long userId);

    /*检验身份*/
    void checkIdentity(String orgId, Long userId);

    /*设置社团星级*/
    Response star(Long orgId, Integer star, Long userId);

    /*社团信息分页查询*/
    Response<List<OrgOrganizationListInfoResponse>> page(OrganizationPageRequest organizationPageRequest);

    /*我的社团分页*/
    Response<List<OrgOrganizationListInfoResponse>> myPage(MyOrganizationPageRequest myOrganizationPageRequest, Long userId);

    /*社团详情*/
    Response<OrgOrganizationInfoResponse> myOrganizationDetail(Long orgId);

    /*申请入社*/
    Response apply(OrgApplicationRequest orgApplicationRequest, Long userId);

    /*审批学生入社*/
    Response judge(OrgJudgeApplicationRequest orgJudgeApplicationRequest, Long userId);

    /*入社申请列表*/
    Response<List<OrgApplicationListInfoResponse>> applyList(Long orgId, Long userId);

    /*退出社团*/
    Response quitOrganization(Long orgId, Long userId);
}
