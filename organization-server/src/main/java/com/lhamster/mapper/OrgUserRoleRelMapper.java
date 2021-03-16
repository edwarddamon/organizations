package com.lhamster.mapper;

import com.lhamster.entity.OrgUserRoleRel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Edward
 * @since 2021-03-09
 */
public interface OrgUserRoleRelMapper extends BaseMapper<OrgUserRoleRel> {

    /*社联管理员列表*/
    List<Map<String, Object>> list(String name);
}
