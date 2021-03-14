package com.lhamster.mapper;

import com.lhamster.entity.OrgDepartment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Edward
 * @since 2021-03-09
 */
public interface OrgDepartmentMapper extends BaseMapper<OrgDepartment> {

    Integer checkIdentity(@Param("orgId") String orgId, @Param("userId") Long userId);

    void updateIt(OrgDepartment orgDepartment);
}
