package com.lhamster.facadeImpl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lhamster.entity.OrgComments;
import com.lhamster.entity.OrgNews;
import com.lhamster.entity.OrgOrganization;
import com.lhamster.entity.OrgUser;
import com.lhamster.facade.OrgNewsFacade;
import com.lhamster.request.OrgCommentRequest;
import com.lhamster.request.OrgCreateNewsRequest;
import com.lhamster.request.OrgNewsPageRequest;
import com.lhamster.request.OrgUpdateNewsRequest;
import com.lhamster.response.OrgComInfoResponse;
import com.lhamster.response.OrgNewsInfoResponse;
import com.lhamster.response.OrgNewsListInfoResponse;
import com.lhamster.response.result.Response;
import com.lhamster.service.OrgCommentsService;
import com.lhamster.service.OrgNewsService;
import com.lhamster.service.OrgOrganizationService;
import com.lhamster.service.OrgUserService;
import com.lhamster.util.TencentCOSUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/17
 */
@Slf4j
@Service
@Component
@RequiredArgsConstructor
public class OrgNewsFacadeImpl implements OrgNewsFacade {
    private final OrgBoardFacadeImpl orgBoardFacade;
    private final OrgNewsService orgNewsService;
    private final OrgCommentsService orgCommentsService;
    private final OrgOrganizationService orgOrganizationService;
    private final OrgUserService orgUserService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response create(File localFile, String fileName, OrgCreateNewsRequest orgCreateNewsRequest, Long userId) {
        // 检查当前登录用户权限
        orgBoardFacade.checkIdentity(orgCreateNewsRequest.getOrgId(), userId);
        // 文件上传
        String avatarUrl = null;
        if (Objects.nonNull(localFile) && StrUtil.isNotBlank(fileName)) {
            avatarUrl = TencentCOSUtil.uploadObject(localFile, "organization-newsAvatar/" + fileName);
        }
        // 浏览量初始值
        orgNewsService.save(OrgNews.builder()
                .createAt(LocalDateTime.now())
                .newAvatar(avatarUrl)
                .newContent(orgCreateNewsRequest.getContent())
                .newOrganizationId(orgCreateNewsRequest.getOrgId())
                .newTitle(orgCreateNewsRequest.getTitle())
                .newViews(0)
                .build());
        return new Response(Boolean.TRUE, "新闻发布成功");
    }

    @Override
    public Response update(File localFile, String fileName, OrgUpdateNewsRequest orgUpdateNewsRequest, Long userId) {
        // 检查身份
        OrgNews news = orgNewsService.getById(orgUpdateNewsRequest.getNewId());
        orgBoardFacade.checkIdentity(news.getNewOrganizationId(), userId);
        // 头像不为空 -> 删除旧头像 ->上传新头像
        if (Objects.nonNull(localFile) && StrUtil.isNotBlank(fileName)) {
            // 删除旧封面
            String actAvatar = news.getNewAvatar();
            if (StrUtil.isNotBlank(actAvatar)) {
                TencentCOSUtil.deletefile("organization-newsAvatar/" + actAvatar.substring(actAvatar.lastIndexOf("/") + 1));
            }
            // 更新新封面
            String newAvatar = TencentCOSUtil.uploadObject(localFile, "organization-newsAvatar/" + fileName);
            news.setNewAvatar(newAvatar);
        }
        // title不为空
        if (StrUtil.isNotBlank(orgUpdateNewsRequest.getTitle())) {
            news.setNewTitle(orgUpdateNewsRequest.getTitle());
        }
        // content不为空
        if (StrUtil.isNotBlank(orgUpdateNewsRequest.getContent())) {
            news.setNewContent(orgUpdateNewsRequest.getContent());
        }
        news.setUpdateAt(LocalDateTime.now());
        // 更新
        orgNewsService.updateById(news);
        return new Response(Boolean.TRUE, "新闻更新成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response comment(OrgCommentRequest orgCommentRequest, Long userId) {
        orgCommentsService.save(OrgComments.builder()
                .comContent(orgCommentRequest.getContent())
                .comGood(0)
                .comNewsId(orgCommentRequest.getNewId())
                .comTargetId(orgCommentRequest.getTargetUserId())
                .comUserId(userId)
                .createAt(LocalDateTime.now())
                .build());
        return new Response(Boolean.TRUE, "评论发布成功");
    }

    @Override
    public Response good(Long comId, Boolean status) {
        OrgComments orgComments = orgCommentsService.getById(comId);
        if (status) {
            orgComments.setComGood(orgComments.getComGood() + 1);
        } else {
            orgComments.setComGood(orgComments.getComGood() - 1);
        }
        orgCommentsService.updateById(orgComments);
        return new Response(Boolean.TRUE, "点赞/取消点赞成功");
    }

    @Override
    public Response<List<OrgNewsListInfoResponse>> page(OrgNewsPageRequest orgNewsPageRequest) {
        // 条件
        QueryWrapper<OrgNews> wrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(orgNewsPageRequest.getTitle())) {
            wrapper.like("new_title", orgNewsPageRequest.getTitle());
        }
        wrapper.orderByDesc("create_at", "update_at");
        Page<OrgNews> page = orgNewsService.page(new Page<>(orgNewsPageRequest.getPageNo(), orgNewsPageRequest.getPageSize()), wrapper);
        List<OrgNewsListInfoResponse> responses = new ArrayList<>();
        page.getRecords().forEach(orgNews -> {
            // 获取社团信息
            OrgOrganization organization = orgOrganizationService.getById(orgNews.getNewOrganizationId());
            responses.add(OrgNewsListInfoResponse.builder()
                    .createAt(orgNews.getCreateAt())
                    .newAvatar(orgNews.getNewAvatar())
                    .newId(orgNews.getNewId())
                    .newTitle(orgNews.getNewTitle())
                    .newViews(orgNews.getNewViews())
                    .newOrganizationId(organization.getOrganId())
                    .newOrganizationName(organization.getOrganName())
                    .build());
        });
        return new Response<List<OrgNewsListInfoResponse>>(Boolean.TRUE, "查询成功", (int) page.getTotal(), responses);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<OrgNewsInfoResponse> detail(Long newId) {
        // 查询新闻详情
        OrgNews news = orgNewsService.getById(newId);
        // 查询社团详情
        OrgOrganization organization = orgOrganizationService.getById(news.getNewOrganizationId());        // 封装新闻详情
        // 封装新闻详情
        OrgNewsInfoResponse response = OrgNewsInfoResponse.builder()
                .createAt(news.getCreateAt())
                .newAvatar(news.getNewAvatar())
                .newContent(news.getNewContent())
                .newId(news.getNewId())
                .newTitle(news.getNewTitle())
                .newViews(news.getNewViews() + 1)
                .newOrganizationId(organization.getOrganId())
                .newOrganizationName(organization.getOrganName())
                .newOrganizationAvatar(organization.getOrganAvatar())
                .build();
        // 查询所有评论
        List<OrgComInfoResponse> comInfoResponses = new ArrayList<>();
        orgCommentsService.list(new QueryWrapper<OrgComments>()
                .eq("com_news_id", newId)
                .orderByAsc("create_at"))
                .forEach(orgComments -> {
                    // 查询发布用户
                    OrgUser user = orgUserService.getById(orgComments.getComUserId());
                    // 查询目标用户
                    OrgUser targetUser = null;
                    if (!orgComments.getComTargetId().equals(-1L)) {
                        targetUser = orgUserService.getById(orgComments.getComTargetId());
                    }
                    // 封装评论
                    OrgComInfoResponse response1 = OrgComInfoResponse.builder()
                            .comId(orgComments.getComId())
                            .comContent(orgComments.getComContent())
                            .comGood(orgComments.getComGood())
                            .comNewsId(orgComments.getComNewsId())
                            .createAt(orgComments.getCreateAt())
                            .comUserId(user.getUserId())
                            .comUserAvatar(user.getUserAvatar())
                            .comUserName(user.getUserUsername())
                            .build();
                    if (Objects.nonNull(targetUser)) {
                        response1.setComTargetId(targetUser.getUserId());
                        response1.setComTargetName(targetUser.getUserUsername());
                        response1.setComTargetAvatar(targetUser.getUserAvatar());
                    }
                    comInfoResponses.add(response1);
                });
        response.setComInfoResponse(comInfoResponses);
        // 浏览量+1
        news.setNewViews(news.getNewViews() + 1);
        orgNewsService.updateById(news);
        return new Response<>(Boolean.TRUE, "查询成功", response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response delete(Long newId, Long userId) {
        OrgNews news = orgNewsService.getById(newId);
        // 检查身份
        orgBoardFacade.checkIdentity(news.getNewOrganizationId(), userId);
        // 查找出所有评论 -> 删除评论
        List<Long> idList = orgCommentsService.list(new QueryWrapper<OrgComments>()
                .eq("com_news_id", newId))
                .stream().map(OrgComments::getComId).collect(Collectors.toList());
        orgCommentsService.removeByIds(idList);
        // 删除新闻
        orgNewsService.removeById(news.getNewId());
        return new Response(Boolean.TRUE, "删除新闻成功");
    }

    @Override
    public Response<List<OrgNewsListInfoResponse>> list(Long orgId) {
        List<OrgNews> news = orgNewsService.list(new QueryWrapper<OrgNews>()
                .eq("new_organization_id", orgId)
                .orderByDesc("create_at"));
        List<OrgNewsListInfoResponse> responses = new ArrayList<>();
        news.forEach(orgNews -> {
            // 获取社团信息
            OrgOrganization organization = orgOrganizationService.getById(orgNews.getNewOrganizationId());
            responses.add(OrgNewsListInfoResponse.builder()
                    .createAt(orgNews.getCreateAt())
                    .newAvatar(orgNews.getNewAvatar())
                    .newId(orgNews.getNewId())
                    .newTitle(orgNews.getNewTitle())
                    .newViews(orgNews.getNewViews())
                    .newOrganizationId(organization.getOrganId())
                    .newOrganizationName(organization.getOrganName())
                    .build());
        });
        return new Response<List<OrgNewsListInfoResponse>>(Boolean.TRUE, "查询成功", responses);

    }
}
