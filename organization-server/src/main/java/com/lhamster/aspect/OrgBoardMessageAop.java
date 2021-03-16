package com.lhamster.aspect;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lhamster.entity.OrgBoards;
import com.lhamster.entity.OrgOrganization;
import com.lhamster.entity.OrgUserOrganizationRel;
import com.lhamster.request.CreateOrganizationRequest;
import com.lhamster.request.OrgBoardRequest;
import com.lhamster.request.OrgUpdateBoardRequest;
import com.lhamster.service.OrgBoardsService;
import com.lhamster.service.OrgOrganizationService;
import com.lhamster.service.OrgUserOrganizationRelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/16
 */
@Aspect
@Component
@Slf4j
@Api(value = "社团公告通知")
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
public class OrgBoardMessageAop {
    private final OrgUserOrganizationRelService orgUserOrganizationRelService;
    private final OrgUserMessageAop userMessageAop;
    private final OrgOrganizationService orgOrganizationService;
    private final OrgBoardsService orgBoardsService;

    @ApiOperation(value = "社团公告发布消息")
    @AfterReturning(value = "execution(* com.lhamster.facadeImpl.OrgBoardFacadeImpl.board(..))")
    private void board(JoinPoint joinPoint) {
        OrgOrganization organization = orgOrganizationService.getById(((OrgBoardRequest) joinPoint.getArgs()[0]).getOrgId());
        // 通知该社团所有人
        List<Long> userIds = orgUserOrganizationRelService.list(new QueryWrapper<OrgUserOrganizationRel>()
                .eq("rel_organization_id", organization.getOrganId())
                .eq("rel_role_id", 2))
                .stream().map(OrgUserOrganizationRel::getRelUserId).distinct().collect(Collectors.toList());
        String content = ((OrgBoardRequest) joinPoint.getArgs()[0]).getContent();
        String finContent = content.length() > 6 ? content.substring(0, 6) : content;
        userIds.forEach(userId -> {
            userMessageAop.saveMessage("[" + organization.getOrganName() + "] 发布了新公告，内容：" + finContent + "...", userId);
        });
    }

    @ApiOperation(value = "社团公告更新消息")
    @AfterReturning(value = "execution(* com.lhamster.facadeImpl.OrgBoardFacadeImpl.updateBoard(..))")
    private void updateBoard(JoinPoint joinPoint) {
        OrgUpdateBoardRequest boardRequest = (OrgUpdateBoardRequest) joinPoint.getArgs()[0];
        OrgBoards boards = orgBoardsService.getById(boardRequest.getBoaId());
        OrgOrganization organization = orgOrganizationService.getById(boards.getBoaOrgId());
        // 通知该社团所有人
        List<Long> userIds = orgUserOrganizationRelService.list(new QueryWrapper<OrgUserOrganizationRel>()
                .eq("rel_organization_id", organization.getOrganId())
                .eq("rel_role_id", 2))
                .stream().map(OrgUserOrganizationRel::getRelUserId).distinct().collect(Collectors.toList());
        String content = boardRequest.getContent();
        String finContent = content.length() > 6 ? content.substring(0, 6) : content;
        userIds.forEach(userId -> {
            userMessageAop.saveMessage("[" + organization.getOrganName() + "] 更新了公告，内容：" + finContent + "...", userId);
        });
    }
}
