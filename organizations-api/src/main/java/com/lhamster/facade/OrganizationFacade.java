package com.lhamster.facade;

import com.lhamster.request.CancelOrganizationRequest;
import com.lhamster.request.CheckOrganizationRequest;
import com.lhamster.request.CreateOrganizationRequest;
import com.lhamster.request.UpdateOrganizationRequest;
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
}
