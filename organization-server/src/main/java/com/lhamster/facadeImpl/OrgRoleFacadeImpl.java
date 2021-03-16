package com.lhamster.facadeImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lhamster.entity.OrgUserRoleRel;
import com.lhamster.facade.OrgRoleFacade;
import com.lhamster.response.OrgRoleInfoResponse;
import com.lhamster.response.exception.ServerException;
import com.lhamster.response.result.Response;
import com.lhamster.service.OrgUserRoleRelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/16
 */
@Slf4j
@Service
@Component
@RequiredArgsConstructor
public class OrgRoleFacadeImpl implements OrgRoleFacade {
    private final OrgUserRoleRelService orgUserRoleRelService;

    private void checkRole(Long userId, Long... params) {
        // 检查是否为社联主席或社联管理员
        int count2 = orgUserRoleRelService.count(new QueryWrapper<OrgUserRoleRel>()
                .eq("rel_user_id", userId)
                .in("rel_role_id", params));
        if (count2 < 1) {
            throw new ServerException(Boolean.FALSE, "权限不足");
        }
    }

    @Override
    public Response<List<OrgRoleInfoResponse>> role(String name, Long userId) {
        // 检查身份是否为社联主席或社联管理员
        this.checkRole(userId, 3L, 4L);
        // name不为空则添加查询条件
        return new Response<List<OrgRoleInfoResponse>>(Boolean.TRUE, "查询成功", orgUserRoleRelService.roleList(name));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<List<OrgRoleInfoResponse>> next(Long targetId, Long userId) {
        // 检查身份是否为社联主席
        this.checkRole(userId, 4L);
        // 解除当前社联主席身份
        OrgUserRoleRel rel = orgUserRoleRelService.get(userId, 4L);
        orgUserRoleRelService.removeById(rel.getRelId());
        // 委任下届社联主席
        orgUserRoleRelService.save(
                OrgUserRoleRel.builder()
                        .createAt(LocalDateTime.now())
                        .relRoleId(4L)
                        .relUserId(targetId)
                        .build());
        return new Response<>(Boolean.TRUE, "委任下届社联主席成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<List<OrgRoleInfoResponse>> nextAdmin(Long targetId, Boolean status, Long userId) {
        // 检查身份是否为社联主席
        this.checkRole(userId, 4L);
        // 任职
        if (status) {
            orgUserRoleRelService.save(OrgUserRoleRel.builder()
                    .relUserId(targetId)
                    .relRoleId(3L)
                    .createAt(LocalDateTime.now())
                    .build());
        } else {
            // 解雇
            OrgUserRoleRel rel = orgUserRoleRelService.get(targetId, 3L);
            orgUserRoleRelService.removeById(rel.getRelId());
        }
        return new Response<>(Boolean.TRUE, "任职/解雇社联管理员成功");
    }
}
