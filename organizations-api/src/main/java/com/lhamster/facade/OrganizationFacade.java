package com.lhamster.facade;

import com.lhamster.request.CreateOrganizationRequest;
import com.lhamster.response.result.Response;

import java.io.File;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/11
 */
public interface OrganizationFacade {
    /*上传社团封面*/
    Response<String> updateAvatar(File localFile, String filename);

    /*创建社团*/
    Response createOrganization(CreateOrganizationRequest createOrganizationRequest, Long userId);
}
