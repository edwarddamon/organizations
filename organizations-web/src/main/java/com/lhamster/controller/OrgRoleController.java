package com.lhamster.controller;

import com.lhamster.facade.OrgRoleFacade;
import com.lhamster.response.OrgRoleInfoResponse;
import com.lhamster.response.result.Response;
import com.lhamster.util.JwtTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/16
 */
@Slf4j
@RestController
@Api(value = "社联")
@RequestMapping(value = "/organizations/web/role")
public class OrgRoleController {
    @Reference
    private OrgRoleFacade orgRoleFacade;

    @GetMapping(value = {"/role", "/role/{name}"})
    @ApiOperation(value = "社联主席、管理员列表", notes = "社联主席和社联管理员权限可查看")
    public Response<List<OrgRoleInfoResponse>> role(@PathVariable(value = "name", required = false) String name,
                                                    @RequestHeader(JwtTokenUtil.AUTH_HEADER_KEY) String token) {
        return orgRoleFacade.role(name, JwtTokenUtil.getUserId(token));
    }

    @PostMapping("/role/{targetId}")
    @ApiOperation(value = "委任下届社联主席", notes = "社联主席权限")
    public Response<List<OrgRoleInfoResponse>> role(@PathVariable(value = "targetId") Long targetId,
                                                    @RequestHeader(JwtTokenUtil.AUTH_HEADER_KEY) String token) {
        return orgRoleFacade.next(targetId, JwtTokenUtil.getUserId(token));
    }

    @PostMapping("/role/{targetId}/{status}")
    @ApiOperation(value = "任职/解雇社联管理员", notes = "社联主席权限：任职:status=true；解雇:status=false")
    public Response<List<OrgRoleInfoResponse>> role(@PathVariable(value = "targetId") Long targetId,
                                                    @PathVariable(value = "status") Boolean status,
                                                    @RequestHeader(JwtTokenUtil.AUTH_HEADER_KEY) String token) {
        return orgRoleFacade.nextAdmin(targetId, status, JwtTokenUtil.getUserId(token));
    }
}
