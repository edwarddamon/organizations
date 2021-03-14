package com.lhamster.service;

import com.lhamster.entity.OrgUserOrganizationRel;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lhamster.request.MyOrganizationPageRequest;
import com.lhamster.response.OrgOrganizationListInfoResponse;
import com.lhamster.response.result.Response;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Edward
 * @since 2021-03-09
 */
public interface OrgUserOrganizationRelService extends IService<OrgUserOrganizationRel> {

    Response<List<OrgOrganizationListInfoResponse>> getMyOrganizations(MyOrganizationPageRequest myOrganizationPageRequest, Long userId);
}
