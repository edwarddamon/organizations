package com.lhamster.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lhamster.entity.OrgUserRoleRel;
import com.lhamster.mapper.OrgUserRoleRelMapper;
import com.lhamster.response.OrgRoleInfoResponse;
import com.lhamster.service.OrgUserRoleRelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Edward
 * @since 2021-03-09
 */
@Service
@RequiredArgsConstructor
public class OrgUserRoleRelServiceImpl extends ServiceImpl<OrgUserRoleRelMapper, OrgUserRoleRel> implements OrgUserRoleRelService {
    private final OrgUserRoleRelMapper orgUserRoleRelMapper;

    @Override
    public List<OrgRoleInfoResponse> roleList(String name) {
        List<Map<String, Object>> resList = orgUserRoleRelMapper.list(name);
        // 封装
        List<OrgRoleInfoResponse> roleList = new ArrayList<>();
        resList.forEach(res -> {
            OrgRoleInfoResponse response = OrgRoleInfoResponse.builder()
                    .userId((Long) res.get("user_id"))
                    .userAvatar((String) res.get("user_avatar"))
                    .userName((String) res.get("user_username"))
                    .userPhone((String) res.get("user_phone"))
                    .build();
            if (((Long) res.get("rel_role_id")).equals(4L)) {
                response.setUserIdentity("社联主席");
            } else {
                response.setUserIdentity("社联管理员");
            }
            roleList.add(response);
        });
        return roleList;
    }

    @Override
    public OrgUserRoleRel get(Long userId, long l) {
        return this.getOne(new QueryWrapper<OrgUserRoleRel>()
                .eq("rel_role_id", l)
                .eq("rel_user_id", userId));
    }
}
