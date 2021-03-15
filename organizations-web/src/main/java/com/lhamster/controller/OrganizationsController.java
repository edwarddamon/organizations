package com.lhamster.controller;

import com.lhamster.facade.OrganizationFacade;
import com.lhamster.request.*;
import com.lhamster.response.OrgApplicationListInfoResponse;
import com.lhamster.response.OrgOrganizationInfoResponse;
import com.lhamster.response.exception.ServerException;
import com.lhamster.response.OrgOrganizationListInfoResponse;
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
import java.util.*;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/11
 */
@Slf4j
@RestController
@Api(value = "社团")
@RequestMapping(value = "/organizations/web/organization")
public class OrganizationsController {
    @Reference
    private OrganizationFacade organizationFacade;

    /*图片格式*/
    private static final List<String> suffix = new ArrayList<>(Arrays.asList(".jpg", ".jpeg", ".png", ".gif"));

    @PostMapping("/page")
    @ApiOperation(value = "社团信息分页")
    public Response<List<OrgOrganizationListInfoResponse>> page(@Validated @RequestBody OrganizationPageRequest organizationPageRequest) {
        return organizationFacade.page(organizationPageRequest);
    }

    @PostMapping("/myPage")
    @ApiOperation(value = "我的社团信息分页")
    public Response<List<OrgOrganizationListInfoResponse>> yPage(@RequestBody MyOrganizationPageRequest myOrganizationPageRequest,
                                                                 @RequestHeader(JwtTokenUtil.AUTH_HEADER_KEY) String token) {
        return organizationFacade.myPage(myOrganizationPageRequest, JwtTokenUtil.getUserId(token));
    }

    @GetMapping("/orgDetail/{orgId}")
    @ApiOperation(value = "社团信息详情")
    public Response<OrgOrganizationInfoResponse> orgDetail(@PathVariable("orgId") Long orgId) {
        return organizationFacade.myOrganizationDetail(orgId);
    }

    @PostMapping("/create")
    @ApiOperation(value = "申请创建社团")
    public Response create(@RequestParam("file") MultipartFile file,
                           @Validated CreateOrganizationRequest createOrganizationRequest,
                           HttpSession session,
                           @RequestHeader(JwtTokenUtil.AUTH_HEADER_KEY) String token) {
        log.info("[文件]：{}", file);
        log.info("[入参]：{}", createOrganizationRequest);
        if (file.isEmpty()) {
            throw new ServerException(Boolean.FALSE, "上传文件为空");
        }
        String filename = file.getOriginalFilename();
        log.info("文件名:" + filename);
        // uuid生成随机的文件名
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replaceAll("-", "");
        // 新的文件名+文件后缀
        String fileSuffix = filename.substring(filename.lastIndexOf("."));
        if (!suffix.contains(fileSuffix)) { // 不是指定格式
            throw new ServerException(Boolean.FALSE, "文件格式不正确");
        }
        filename = uuid + fileSuffix;
        // 即将写入磁盘的地址
        File localFile = new File(session.getServletContext().getRealPath("/") + filename);
        // 将MultipartFile转为File从内存中写入磁盘
        try {
            file.transferTo(localFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 上传社团封面
        Response<String> response = organizationFacade.updateAvatar(localFile, filename);
        createOrganizationRequest.setAvatarUrl(response.getData());
        // 创建社团
        return organizationFacade.createOrganization(createOrganizationRequest, JwtTokenUtil.getUserId(token));
    }

    @PostMapping("/cancel")
    @ApiOperation(value = "申请注销社团", notes = "必须是社长才能申请注销社团")
    public Response cancel(@Validated @RequestBody CancelOrganizationRequest cancelOrganizationRequest,
                           @RequestHeader(JwtTokenUtil.AUTH_HEADER_KEY) String token) {
        return organizationFacade.cancelOrganization(cancelOrganizationRequest, JwtTokenUtil.getUserId(token));
    }

    @PostMapping("/check")
    @ApiOperation(value = "审批社团创建/注销申请", notes = "必须是社联主席或社联管理员才能审批")
    public Response checkApply(@Validated @RequestBody CheckOrganizationRequest checkOrganizationRequest,
                               @RequestHeader(JwtTokenUtil.AUTH_HEADER_KEY) String token) {
        return organizationFacade.check(checkOrganizationRequest, JwtTokenUtil.getUserId(token));
    }

    @PostMapping("/updateOrganization")
    @ApiOperation(value = "更新社团信息", notes = "社团核心管理人员才可更新社团信息")
    public Response updateOrganization(@Validated UpdateOrganizationRequest updateOrganizationRequest,
                                       @RequestParam(value = "newAvatar", required = false) MultipartFile newAvatar,
                                       @RequestParam(value = "introductionAvatars", required = false) MultipartFile[] introductionAvatars,
                                       HttpSession session,
                                       @RequestHeader(JwtTokenUtil.AUTH_HEADER_KEY) String token) {
        log.info("[更新的信息]：{}", updateOrganizationRequest);
        log.info("[新封面]：{}", newAvatar);
        log.info("[社团介绍图片]：{}", Arrays.toString(introductionAvatars));
        // 检验当前登录用户身份
        organizationFacade.checkIdentity(updateOrganizationRequest.getOrgId(), JwtTokenUtil.getUserId(token));
        String newAvatarUrl = null;
        List<String> newIntroductionUrls = new ArrayList<>();
        // 新封面上传
        if (Objects.nonNull(newAvatar)) {
            String fileName = FileUtil.randomFileName(Objects.requireNonNull(newAvatar.getOriginalFilename()));
            // 即将写入磁盘的地址
            File localFile = new File(session.getServletContext().getRealPath("/") + fileName);
            // 将MultipartFile转为File从内存中写入磁盘
            try {
                newAvatar.transferTo(localFile);
            } catch (IOException e) {
                e.printStackTrace();
                throw new ServerException(Boolean.FALSE, "文件写入磁盘失败");
            }
            // 上传社团封面
            Response<String> response = organizationFacade.updateAvatar(updateOrganizationRequest.getOrgId(), localFile, fileName);
            newAvatarUrl = response.getData();
        }
        // 社团详细介绍图片上传
        // 删除旧照片地址
        organizationFacade.deleteOldIntroductionUrls(updateOrganizationRequest.getOrgId());
        // 上传新照片地址
        for (MultipartFile introductionAvatar : introductionAvatars) {
            if (Objects.nonNull(introductionAvatar)) {
                String fileName = FileUtil.randomFileName(Objects.requireNonNull(introductionAvatar.getOriginalFilename()));
                // 即将写入磁盘的地址
                File localFile = new File(session.getServletContext().getRealPath("/") + fileName);
                // 将MultipartFile转为File从内存中写入磁盘
                try {
                    introductionAvatar.transferTo(localFile);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new ServerException(Boolean.FALSE, "文件写入磁盘失败");
                }
                // 上传社团详细介绍图片
                Response<String> response = organizationFacade.updateNewIntroductionUrl(localFile, fileName);
                newIntroductionUrls.add(response.getData());
            }
        }
        // 将新的地址和信息存入数据库
        return organizationFacade.updateOrganization(updateOrganizationRequest, newAvatarUrl, newIntroductionUrls, JwtTokenUtil.getUserId(token));
    }

    @PostMapping("/star/{orgId}/{star}")
    @ApiOperation(value = "设置社团星级", notes = "社联管理员权限")
    public Response star(@PathVariable(value = "orgId") Long orgId,
                         @PathVariable(value = "star") Integer star,
                         @RequestHeader(JwtTokenUtil.AUTH_HEADER_KEY) String token) {
        return organizationFacade.star(orgId, star, JwtTokenUtil.getUserId(token));
    }

    @PostMapping("/apply")
    @ApiOperation(value = "申请加入社团")
    public Response apply(@Validated @RequestBody OrgApplicationRequest orgApplicationRequest,
                          @RequestHeader(JwtTokenUtil.AUTH_HEADER_KEY) String token) {
        return organizationFacade.apply(orgApplicationRequest, JwtTokenUtil.getUserId(token));
    }

    @PostMapping("/judge")
    @ApiOperation(value = "审批学生入社")
    public Response judge(@Validated @RequestBody OrgJudgeApplicationRequest orgJudgeApplicationRequest,
                          @RequestHeader(JwtTokenUtil.AUTH_HEADER_KEY) String token) {
        return organizationFacade.judge(orgJudgeApplicationRequest, JwtTokenUtil.getUserId(token));
    }

    @GetMapping(value = {"/appList/{orgId}", "/appList"})
    @ApiOperation(value = "入社申请列表",
            notes = "传社团id：查询社团待审批的申请；不传社团id：查询当前登录用户的所有审批")
    public Response<List<OrgApplicationListInfoResponse>> applyList(@PathVariable(value = "orgId", required = false) Long orgId,
                                                                    @RequestHeader(JwtTokenUtil.AUTH_HEADER_KEY) String token) {
        return organizationFacade.applyList(orgId, JwtTokenUtil.getUserId(token));
    }

    @DeleteMapping("/quit/{orgId}")
    @ApiOperation(value = "退出社团")
    public Response quit(@PathVariable("orgId") Long orgId,
                         @RequestHeader(JwtTokenUtil.AUTH_HEADER_KEY) String token) {
        return organizationFacade.quitOrganization(orgId, JwtTokenUtil.getUserId(token));
    }
}
