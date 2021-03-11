package com.lhamster.controller;

import com.lhamster.facade.OrganizationFacade;
import com.lhamster.request.CreateOrganizationRequest;
import com.lhamster.response.exception.ServerException;
import com.lhamster.response.result.Response;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
}
