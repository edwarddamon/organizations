package com.lhamster.controller;

import com.lhamster.facade.OrgNewsFacade;
import com.lhamster.request.*;
import com.lhamster.response.OrgNewsInfoResponse;
import com.lhamster.response.OrgNewsListInfoResponse;
import com.lhamster.response.exception.ServerException;
import com.lhamster.response.result.Response;
import com.lhamster.util.FileUtil;
import com.lhamster.util.JwtTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;


/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/17
 */
@Slf4j
@RestController
@Api(value = "消息通知")
@RequestMapping(value = "/organizations/web/news")
public class OrgNewsController {
    @Reference
    private OrgNewsFacade newsFacade;

    @GetMapping("/detail/{newId}")
    @ApiOperation(value = "新闻详情")
    public Response<OrgNewsInfoResponse> detail(@PathVariable("newId") Long newId) {
        if (log.isDebugEnabled()) {
            log.debug("[入参]：{}", newId);
        }
        return newsFacade.detail(newId);
    }

    @PostMapping("/page")
    @ApiOperation(value = "新闻分页")
    public Response<List<OrgNewsListInfoResponse>> page(@RequestBody OrgNewsPageRequest orgNewsPageRequest) {
        if (log.isDebugEnabled()) {
            log.debug("[入参]：{}", orgNewsPageRequest);
        }
        return newsFacade.page(orgNewsPageRequest);
    }

    @GetMapping("/list/{orgId}")
    @ApiOperation(value = "新闻列表")
    public Response<List<OrgNewsListInfoResponse>> orgPage(@PathVariable("orgId") Long orgId) {
        if (log.isDebugEnabled()) {
            log.debug("[入参]：{}", orgId);
        }
        return newsFacade.list(orgId);
    }

    @PostMapping("/create")
    @ApiOperation(value = "发布新闻")
    public Response create(@RequestParam(value = "avatar", required = false) MultipartFile avatar,
                           @Validated OrgCreateNewsRequest orgCreateNewsRequest,
                           @RequestHeader(JwtTokenUtil.AUTH_HEADER_KEY) String token,
                           HttpSession session) {
        if (log.isDebugEnabled()) {
            log.debug("[文件]：{}", avatar);
            log.debug("[入参]：{}", orgCreateNewsRequest);
        }
        // 获取新文件名
        String fileName = null;
        File localFile = null;
        if (Objects.nonNull(avatar) && !avatar.isEmpty()) {
            fileName = FileUtil.randomFileName(avatar.getOriginalFilename());
            // 即将写入磁盘的地址
            localFile = new File(session.getServletContext().getRealPath("/") + fileName);
            // 将MultipartFile转为File从内存中写入磁盘
            try {
                avatar.transferTo(localFile);
            } catch (IOException e) {
                e.printStackTrace();
                throw new ServerException(Boolean.FALSE, "文件写入磁盘失败");
            }
        }
        return newsFacade.create(localFile, fileName, orgCreateNewsRequest, JwtTokenUtil.getUserId(token));
    }

    @PostMapping("/update")
    @ApiOperation(value = "更新新闻")
    public Response update(@RequestParam(value = "newAvatar", required = false) MultipartFile newAvatar,
                           @Validated OrgUpdateNewsRequest orgUpdateNewsRequest,
                           @RequestHeader(JwtTokenUtil.AUTH_HEADER_KEY) String token,
                           HttpSession session) {
        if (log.isDebugEnabled()) {
            log.debug("[文件]：{}", newAvatar);
            log.debug("[入参]：{}", orgUpdateNewsRequest);
        }
        // 获取新文件名
        String fileName = null;
        File localFile = null;
        if (Objects.nonNull(newAvatar) && !newAvatar.isEmpty()) {
            fileName = FileUtil.randomFileName(newAvatar.getOriginalFilename());
            // 即将写入磁盘的地址
            localFile = new File(session.getServletContext().getRealPath("/") + fileName);
            // 将MultipartFile转为File从内存中写入磁盘
            try {
                newAvatar.transferTo(localFile);
            } catch (IOException e) {
                e.printStackTrace();
                throw new ServerException(Boolean.FALSE, "文件写入磁盘失败");
            }
        }
        return newsFacade.update(localFile, fileName, orgUpdateNewsRequest, JwtTokenUtil.getUserId(token));
    }

    @PostMapping("/comment")
    @ApiOperation(value = "发布新闻评论")
    public Response comment(@Validated @RequestBody OrgCommentRequest orgCommentRequest,
                            @RequestHeader(JwtTokenUtil.AUTH_HEADER_KEY) String token) {
        return newsFacade.comment(orgCommentRequest, JwtTokenUtil.getUserId(token));
    }

    @PutMapping("/comment/{comId}/{status}")
    @ApiOperation(value = "用户点赞评论", notes = "comId传评论id，status传点赞和取消点赞（true点赞;false取消点赞）")
    public Response comment(@PathVariable("comId") Long comId,
                            @PathVariable("status") Boolean status) {
        return newsFacade.good(comId, status);
    }

    @DeleteMapping("/new/{newId}")
    @ApiOperation(value = "新闻删除", notes = "新闻下面的所有评论都会一起删除")
    public Response delete(@PathVariable("newId") Long newId,
                           @RequestHeader(JwtTokenUtil.AUTH_HEADER_KEY) String token) {
        return newsFacade.delete(newId, JwtTokenUtil.getUserId(token));
    }
}
