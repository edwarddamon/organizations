package com.lhamster.facadeImpl;

import cn.hutool.core.util.StrUtil;
import com.lhamster.entity.OrgComments;
import com.lhamster.entity.OrgNews;
import com.lhamster.facade.OrgBoardFacade;
import com.lhamster.facade.OrgNewsFacade;
import com.lhamster.request.OrgCommentRequest;
import com.lhamster.request.OrgCreateNewsRequest;
import com.lhamster.request.OrgUpdateNewsRequest;
import com.lhamster.response.result.Response;
import com.lhamster.service.OrgCommentsService;
import com.lhamster.service.OrgNewsService;
import com.lhamster.util.TencentCOSUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Objects;

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
}
