package com.lhamster.mapper;

import com.lhamster.entity.OrgUserOrganizationRel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lhamster.request.OrgUserListRequest;
import com.lhamster.response.OrgOrganizationListInfoResponse;
import com.lhamster.response.OrgUserInfoResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Edward
 * @since 2021-03-09
 */
public interface OrgUserOrganizationRelMapper extends BaseMapper<OrgUserOrganizationRel> {

    List<OrgOrganizationListInfoResponse> getMyOrganizations(@Param("name") String name, @Param("userId") Long userId);

    List<OrgUserInfoResponse> listMyself(OrgUserListRequest orgUserRequest);
}
