package com.lhamster.facadeImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lhamster.entity.OrgBoards;
import com.lhamster.entity.OrgDepartment;
import com.lhamster.entity.OrgUser;
import com.lhamster.facade.OrgBoardFacade;
import com.lhamster.request.OrgBoardRequest;
import com.lhamster.request.OrgUpdateBoardRequest;
import com.lhamster.response.OrgBoardLIstInfoResponse;
import com.lhamster.response.exception.ServerException;
import com.lhamster.response.result.Response;
import com.lhamster.service.OrgBoardsService;
import com.lhamster.service.OrgDepartmentService;
import com.lhamster.service.OrgUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/16
 */
@Slf4j
@Component
@Service
@RequiredArgsConstructor
public class OrgBoardFacadeImpl implements OrgBoardFacade {
    private final OrgBoardsService orgBoardsService;
    private final OrgDepartmentService orgDepartmentService;
    private final OrgUserService orgUserService;

    /**
     * 检查是否为社团管理人员
     *
     * @param orgId
     * @param userId
     */
    private void checkIdentity(Long orgId, Long userId) {
        // 检查当前用户身份
        int count = orgDepartmentService.count(new QueryWrapper<OrgDepartment>()
                .eq("dep_organization_id", orgId)
                .and(req ->
                        req.eq("dep_minister_id", userId)
                                .or()
                                .eq("dep_vice_minister_id", userId)
                )
                .in("dep_name", "社长", "副社长", "团支书"));
        if (count < 1) {
            throw new ServerException(Boolean.FALSE, "权限不足");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response board(OrgBoardRequest orgBoardRequest, Long userId) {
        // 检验当前登录用户身份
        this.checkIdentity(orgBoardRequest.getOrgId(), userId);
        // 发布公告
        orgBoardsService.save(OrgBoards.builder()
                .boaContent(orgBoardRequest.getContent())
                .boaUserId(userId)
                .boaViews(0)
                .boaOrgId(orgBoardRequest.getOrgId())
                .createAt(LocalDateTime.now())
                .build());
        return new Response(Boolean.TRUE, "公告发布成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response updateBoard(OrgUpdateBoardRequest orgUpdateBoardRequest, Long userId) {
        OrgBoards boards = orgBoardsService.getById(orgUpdateBoardRequest.getBoaId());
        // 检验当前登录用户身份
        this.checkIdentity(boards.getBoaOrgId(), userId);
        // 更新
        boards.setBoaContent(orgUpdateBoardRequest.getContent());
        boards.setUpdateAt(LocalDateTime.now());
        orgBoardsService.updateById(boards);
        return new Response(Boolean.TRUE, "公告更新成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response deleteBoard(Long boaId, Long userId) {
        OrgBoards boards = orgBoardsService.getById(boaId);
        // 检验当前登录用户身份
        this.checkIdentity(boards.getBoaOrgId(), userId);
        // 删除
        try {
            orgBoardsService.removeById(boaId);
            return new Response(Boolean.TRUE, "公告删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServerException(Boolean.FALSE, "公告删除失败");
        }
    }

    @Override
    public Response<List<OrgBoardLIstInfoResponse>> getBoradList(Long orgId, Long boaId) {
        List<OrgBoardLIstInfoResponse> resList = new ArrayList<>();
        // 公告列表
        if (Objects.nonNull(orgId) && Objects.isNull(boaId)) {
            List<OrgBoards> boards = orgBoardsService.list(new QueryWrapper<OrgBoards>()
                    .eq("boa_org_id", orgId)
                    .orderByDesc("create_at"));
            boards.forEach(board -> {
                OrgUser user = orgUserService.getById(board.getBoaUserId());
                resList.add(OrgBoardLIstInfoResponse.builder()
                        .boaContent(board.getBoaContent())
                        .boaId(board.getBoaId())
                        .boaViews(board.getBoaViews())
                        .createAt(board.getCreateAt())
                        .boaUserAvatar(user.getUserAvatar())
                        .boaUserId(user.getUserId())
                        .boaUserName(user.getUserUsername())
                        .build());
            });
        } else if (Objects.isNull(orgId) && Objects.nonNull(boaId)) {
            // 公告详情
            OrgBoards orgBoards = orgBoardsService.getById(boaId);
            OrgUser user = orgUserService.getById(orgBoards.getBoaUserId());
            resList.add(OrgBoardLIstInfoResponse.builder()
                    .boaContent(orgBoards.getBoaContent())
                    .boaId(orgBoards.getBoaId())
                    .boaViews(orgBoards.getBoaViews())
                    .createAt(orgBoards.getCreateAt())
                    .boaUserAvatar(user.getUserAvatar())
                    .boaUserId(user.getUserId())
                    .boaUserName(user.getUserUsername())
                    .build());
        } else {
            throw new ServerException(Boolean.FALSE, "入参异常");
        }
        return new Response<>(Boolean.TRUE, "查询成功", resList);
    }
}
