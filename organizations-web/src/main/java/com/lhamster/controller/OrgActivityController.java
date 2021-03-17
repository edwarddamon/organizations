package com.lhamster.controller;

import com.lhamster.entity.OrgLimit;
import com.lhamster.facade.OrgActivityFacade;
import com.lhamster.request.OrgBoardRequest;
import com.lhamster.request.OrgCreateActivityRequest;
import com.lhamster.request.OrgUpdateActivityRequest;
import com.lhamster.response.OrgActivityInfoResponse;
import com.lhamster.response.OrgActivityListInfoResponse;
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
@Api(value = "活动")
@RequestMapping(value = "/organizations/web/activity")
public class OrgActivityController {
    @Reference
    private OrgActivityFacade orgActivityFacade;

    @GetMapping("/limit")
    @ApiOperation(value = "限制列表")
    public Response<List<OrgLimit>> board() {
        return orgActivityFacade.limit();
    }

    @GetMapping(value = {"/page", "/page/{pageNo}/{pageSize}"})
    @ApiOperation(value = "活动分页")
    public Response<List<OrgActivityListInfoResponse>> activity(@PathVariable(value = "pageNo", required = false) Integer pageNo,
                                                                @PathVariable(value = "pageSize", required = false) Integer pageSize) {
        pageNo = Objects.nonNull(pageNo) ? pageNo : 1;
        pageSize = Objects.nonNull(pageSize) ? pageSize : 10;
        return orgActivityFacade.page(pageNo, pageSize);
    }

    @GetMapping("/detail/{actId}")
    @ApiOperation(value = "活动详情")
    public Response<OrgActivityInfoResponse> activity(@PathVariable(value = "actId") Long actId) {
        return orgActivityFacade.detail(actId);
    }

    @PostMapping("/create")
    @ApiOperation(value = "创建活动", notes = "社团核心管理人员才可创建")
    public Response create(@RequestParam(value = "avatar", required = false) MultipartFile avatar,
                           @Validated OrgCreateActivityRequest orgCreateActivityRequest,
                           @RequestHeader(JwtTokenUtil.AUTH_HEADER_KEY) String token,
                           HttpSession session) {
        if (log.isDebugEnabled()) {
            log.debug("[文件]：{}", avatar);
            log.debug("[入参]：{}", orgCreateActivityRequest);
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
        return orgActivityFacade.create(localFile, fileName, orgCreateActivityRequest, JwtTokenUtil.getUserId(token));
    }


    @PostMapping("/update")
    @ApiOperation(value = "更新活动", notes = "社团核心管理人员才可更新")
    public Response create(@RequestParam(value = "newAvatar", required = false) MultipartFile newAvatar,
                           @Validated OrgUpdateActivityRequest orgUpdateActivityRequest,
                           @RequestHeader(JwtTokenUtil.AUTH_HEADER_KEY) String token,
                           HttpSession session) {
        if (log.isDebugEnabled()) {
            log.debug("[文件]：{}", newAvatar);
            log.debug("[入参]：{}", orgUpdateActivityRequest);
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
        return orgActivityFacade.update(localFile, fileName, orgUpdateActivityRequest, JwtTokenUtil.getUserId(token));
    }

    @DeleteMapping("/delete/{actId}")
    @ApiOperation(value = "取消活动", notes = "社团核心管理人员才可取消")
    public Response delete(@PathVariable("actId") Long actId,
                           @RequestHeader(JwtTokenUtil.AUTH_HEADER_KEY) String token) {
        return orgActivityFacade.delete(actId, JwtTokenUtil.getUserId(token));
    }
}
