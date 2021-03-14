package com.lhamster.facadeImpl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lhamster.entity.OrgDepartment;
import com.lhamster.entity.OrgUser;
import com.lhamster.entity.OrgUserOrganizationRel;
import com.lhamster.facade.DepartmentFacade;
import com.lhamster.request.CreateDepartmentRequest;
import com.lhamster.request.OfficeDepartmentRequest;
import com.lhamster.request.UpdateDepartmentRequest;
import com.lhamster.response.DepartmentInfoResponse;
import com.lhamster.response.OrgUserInfoResponse;
import com.lhamster.response.exception.ServerException;
import com.lhamster.response.result.Response;
import com.lhamster.service.OrgDepartmentService;
import com.lhamster.service.OrgUserOrganizationRelService;
import com.lhamster.service.OrgUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/13
 */
@Slf4j
@Component
@Service
@RequiredArgsConstructor
public class DepartmentFacadeImpl implements DepartmentFacade {
    private final OrgDepartmentService orgDepartmentService;
    private final OrgUserService orgUserService;
    private final OrgUserOrganizationRelService orgUserOrganizationRelService;

    private void checkIdentity(Long orgId, Long userId) {
        // 检查当前用户身份
        int count = orgDepartmentService.count(new QueryWrapper<OrgDepartment>()
                .eq("dep_organization_id", orgId)
                .eq("dep_name", "社长")
                .eq("dep_minister_id", userId));
        if (count < 1) {
            throw new ServerException(Boolean.FALSE, "权限不足");
        }
    }

    private void checkIdentity2(Long orgId, Long userId) {
        // 检查当前用户身份
        int count = orgDepartmentService.count(new QueryWrapper<OrgDepartment>()
                .eq("dep_organization_id", orgId)
                .and(req ->
                        req.eq("dep_minister_id", userId)
                                .or()
                                .eq("dep_vice_minister_id", userId)
                )
                .in("dep_name", "社长", "副社长", "团支书", "财务"));
        if (count < 1) {
            throw new ServerException(Boolean.FALSE, "权限不足");
        }
    }

    @Override
    public Response createDepaetment(CreateDepartmentRequest createDepartmentRequest, Long userId) {
        this.checkIdentity(createDepartmentRequest.getOrgId(), userId);
        // 新增部门
        OrgDepartment department = OrgDepartment.builder()
                .depName(createDepartmentRequest.getDepName())
                .depOrganizationId(createDepartmentRequest.getOrgId())
                .createAt(LocalDateTime.now())
                .build();
        orgDepartmentService.save(department);
        return new Response(Boolean.TRUE, "部门创建成功");
    }

    @Override
    public Response deleteDepartment(Long orgId, Long depId, Long userId) {
        this.checkIdentity(orgId, userId);
        // 删除部门
        boolean res = orgDepartmentService.removeById(depId);
        if (res) {
            return new Response(Boolean.TRUE, "删除部门成功");
        } else {
            throw new ServerException(Boolean.FALSE, "删除部门失败");
        }
    }

    @Override
    public Response updateDepartment(UpdateDepartmentRequest updateDepartmentRequest, Long userId) {
        // 检查身份
        this.checkIdentity(updateDepartmentRequest.getOrgId(), userId);
        // 更新名称、时间
        OrgDepartment department = orgDepartmentService.getById(updateDepartmentRequest.getDepId());
        if (!StrUtil.hasBlank(updateDepartmentRequest.getDepName())) {
            department.setDepName(updateDepartmentRequest.getDepName());
        }
        department.setUpdateAt(LocalDateTime.now());
        orgDepartmentService.updateById(department);
        return new Response(Boolean.TRUE, "部门名称更新成功");
    }

    @Override
    public Response<List<OrgDepartment>> departs(Long orgId, Long userId) {
        this.checkIdentity2(orgId, userId);
        // 获取部门列表
        return new Response<List<OrgDepartment>>(Boolean.TRUE, "获取部门列表成功",
                orgDepartmentService.list(new QueryWrapper<OrgDepartment>().eq("dep_organization_id", userId)));
    }

    @Override
    public Response<DepartmentInfoResponse> departDetail(Long orgId, Long depId, Long userId) {
        // 检验身份
        checkIdentity2(orgId, userId);
        // 获取部门信息
        OrgDepartment department = orgDepartmentService.getById(depId);
        // 获取部长和副部长信息
        OrgUserInfoResponse ministerResponse = null;
        OrgUserInfoResponse viceMinisterResponse = null;
        if (Objects.nonNull(department.getDepMinisterId())) {
            OrgUser minister = orgUserService.getById(department.getDepMinisterId());
            ministerResponse = OrgUserInfoResponse.builder()
                    .userId(minister.getUserId())
                    .userAvatar(minister.getUserAvatar())
                    .userUsername(minister.getUserUsername())
                    .userSex(minister.getUserSex())
                    .userPhone(minister.getUserPhone())
                    .userQq(minister.getUserQq())
                    .userVx(minister.getUserVx())
                    .build();
        }
        if (Objects.nonNull(department.getDepViceMinisterId())) {
            OrgUser viceMinister = orgUserService.getById(department.getDepViceMinisterId());
            viceMinisterResponse = OrgUserInfoResponse.builder()
                    .userId(viceMinister.getUserId())
                    .userAvatar(viceMinister.getUserAvatar())
                    .userUsername(viceMinister.getUserUsername())
                    .userSex(viceMinister.getUserSex())
                    .userPhone(viceMinister.getUserPhone())
                    .userQq(viceMinister.getUserQq())
                    .userVx(viceMinister.getUserVx())
                    .build();

        }
        DepartmentInfoResponse infoResponse = DepartmentInfoResponse.builder()
                .depId(department.getDepId())
                .depName(department.getDepName())
                .depOrganizationId(department.getDepOrganizationId())
                .build();
        if (Objects.nonNull(ministerResponse)) {
            infoResponse.setDepMinister(ministerResponse);
        }
        if (Objects.nonNull(viceMinisterResponse)) {
            infoResponse.setDepViceMinister(viceMinisterResponse);
        }

        return new Response<>(Boolean.TRUE, "获取成功", infoResponse);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response office(OfficeDepartmentRequest officeDepartmentRequest, Long userId) {
        // 检查是否为社长
        this.checkIdentity(officeDepartmentRequest.getOrgId(), userId);
        // 查询
        OrgDepartment orgDepartment = orgDepartmentService.getById(officeDepartmentRequest.getDepId());
        // 上任
        if (officeDepartmentRequest.getJudge()) {
            // 副社长
            // 两个人满了 -> 无法继续添加
            if ("副社长".equals(orgDepartment.getDepName())) {
                if (Objects.isNull(orgDepartment.getDepMinisterId())) {
                    orgDepartment.setDepMinisterId(officeDepartmentRequest.getTargetId());
                } else if (Objects.isNull(orgDepartment.getDepViceMinisterId())) {
                    orgDepartment.setDepViceMinisterId(officeDepartmentRequest.getTargetId());
                } else {
                    throw new ServerException(Boolean.FALSE, "副社长人数已满，无法继续添加");
                }
            } else if ("团支书".equals(orgDepartment.getDepName())) {
                // 团支书
                // 有了 -> 无法继续添加
                if (Objects.nonNull(orgDepartment.getDepMinisterId())) {
                    throw new ServerException(Boolean.FALSE, "该社团已有团支书，无法再次添加");
                }
                orgDepartment.setDepMinisterId(officeDepartmentRequest.getTargetId());
            } else if ("财务".equals(orgDepartment.getDepName())) {
                // 财务
                // 有了 -> 无法继续添加
                if (Objects.nonNull(orgDepartment.getDepMinisterId())) {
                    throw new ServerException(Boolean.FALSE, "该社团已有财务，无法再次添加");
                }
                orgDepartment.setDepMinisterId(officeDepartmentRequest.getTargetId());
            } else {
                // 其他
                // 看指定职务是否有人，没人添加，有人无法继续添加
                if (Objects.isNull(officeDepartmentRequest.getPosition())) {
                    throw new ServerException(Boolean.FALSE, "入参职位为空，无法确定正副部长");
                }
                if (officeDepartmentRequest.getPosition().equals(1)) {
                    if (Objects.nonNull(orgDepartment.getDepMinisterId())) {
                        throw new ServerException(Boolean.FALSE, "部长已有人，无法再次添加");
                    }
                    orgDepartment.setDepMinisterId(officeDepartmentRequest.getTargetId());
                } else if (officeDepartmentRequest.getPosition().equals(2)) {
                    if (Objects.nonNull(orgDepartment.getDepViceMinisterId())) {
                        throw new ServerException(Boolean.FALSE, "副部长已有人，无法再次添加");
                    }
                    orgDepartment.setDepViceMinisterId(officeDepartmentRequest.getTargetId());
                } else {
                    throw new ServerException(Boolean.FALSE, "无法确定职位");
                }
            }
            orgDepartment.setUpdateAt(LocalDateTime.now());
            orgDepartmentService.updateById(orgDepartment);
        } else {
            // 下任
            // 直接删除对应的用户id即可
            if (officeDepartmentRequest.getTargetId().equals(orgDepartment.getDepMinisterId())) {
                orgDepartment.setDepMinisterId(null);
            }
            if (officeDepartmentRequest.getTargetId().equals(orgDepartment.getDepViceMinisterId())) {
                orgDepartment.setDepViceMinisterId(null);
            }
            orgDepartment.setUpdateAt(LocalDateTime.now());
            orgDepartmentService.updateIt(orgDepartment);
        }
        return new Response(Boolean.TRUE, officeDepartmentRequest.getJudge() ? "任职成功" : "解雇成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response boss(Long orgId, Long bossId, Long userId) {
        // 检验当前身份是否为社长
        this.checkIdentity(orgId, userId);
        // 更换社长
        OrgDepartment department = orgDepartmentService.getOne(new QueryWrapper<OrgDepartment>()
                .eq("dep_organization_id", orgId)
                .eq("dep_name", "社长"));
        department.setDepMinisterId(bossId);
        orgDepartmentService.updateById(department);
        // 将当前用户改为社员身份
        OrgUserOrganizationRel userRel = orgUserOrganizationRelService.getOne(new QueryWrapper<OrgUserOrganizationRel>()
                .eq("rel_organization_id", orgId)
                .eq("rel_user_id", userId));
        if (Objects.nonNull(userRel)) {
            if (Objects.nonNull(userRel.getRelRoleId()) && userRel.getRelId().equals(2L)) {
                return new Response(Boolean.TRUE, "委任下届社长成功");
            } else {
                userRel.setRelRoleId(2L);
                userRel.setUpdateAt(LocalDateTime.now());
                orgUserOrganizationRelService.updateById(userRel);
            }
        } else {
            OrgUserOrganizationRel rel = OrgUserOrganizationRel.builder()
                    .relUserId(userId)
                    .relOrganizationId(orgId)
                    .relRoleId(2L)
                    .createAt(LocalDateTime.now())
                    .build();
            orgUserOrganizationRelService.save(rel);
        }
        return new Response(Boolean.TRUE, "委任下届社长成功");
    }
}
