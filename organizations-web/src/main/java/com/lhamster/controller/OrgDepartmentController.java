package com.lhamster.controller;

import com.lhamster.entity.OrgDepartment;
import com.lhamster.facade.OrgDepartmentFacade;
import com.lhamster.request.CreateDepartmentRequest;
import com.lhamster.request.OfficeDepartmentRequest;
import com.lhamster.request.UpdateDepartmentRequest;
import com.lhamster.response.DepartmentInfoResponse;
import com.lhamster.response.result.Response;
import com.lhamster.util.JwtTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/13
 */
@Slf4j
@RestController
@Api(value = "部门")
@RequestMapping(value = "/organizations/web/department")
public class OrgDepartmentController {
    @Reference
    private OrgDepartmentFacade departmentFacade;

    @GetMapping("/depart/{orgId}")
    @ApiOperation(value = "部门列表")
    public Response<List<OrgDepartment>> depart(@Validated @NotNull(message = "社团id不能为空") @PathVariable("orgId") Long orgId,
                                                @RequestHeader(JwtTokenUtil.AUTH_HEADER_KEY) String token) {
        return departmentFacade.departs(orgId, JwtTokenUtil.getUserId(token));
    }

    @GetMapping("/depart/{orgId}/{depId}")
    @ApiOperation(value = "部门详情")
    public Response<DepartmentInfoResponse> depart(@Validated @NotNull(message = "社团id不能为空") @PathVariable("orgId") Long orgId,
                                                   @Validated @NotNull(message = "部门id不能为空") @PathVariable("depId") Long depId,
                                                   @RequestHeader(JwtTokenUtil.AUTH_HEADER_KEY) String token) {
        return departmentFacade.departDetail(orgId, depId, JwtTokenUtil.getUserId(token));
    }

    @PostMapping(value = "/createDepartment")
    @ApiOperation(value = "新增社团部门", notes = "社长才可新增社团部门")
    public Response createDep(@Validated @RequestBody CreateDepartmentRequest createDepartmentRequest,
                              @RequestHeader(JwtTokenUtil.AUTH_HEADER_KEY) String token) {
        return departmentFacade.createDepaetment(createDepartmentRequest, JwtTokenUtil.getUserId(token));
    }

    @DeleteMapping("/delete/{orgId}/{depId}")
    @ApiOperation(value = "删除部门", notes = "社长权限")
    public Response createDep(@Validated @PathVariable("orgId") @NotNull(message = "社团id不能为空") Long orgId,
                              @Validated @PathVariable("depId") @NotNull(message = "部门id不能为空") Long depId,
                              @RequestHeader(JwtTokenUtil.AUTH_HEADER_KEY) String token) {
        return departmentFacade.deleteDepartment(orgId, depId, JwtTokenUtil.getUserId(token));
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新部门", notes = "社长权限")
    public Response update(@Validated @RequestBody UpdateDepartmentRequest updateDepartmentRequest,
                           @RequestHeader(JwtTokenUtil.AUTH_HEADER_KEY) String token) {
        return departmentFacade.updateDepartment(updateDepartmentRequest, JwtTokenUtil.getUserId(token));
    }

    @PostMapping("/office")
    @ApiOperation(value = "入职和解雇", notes = "社长权限")
    public Response office(@Validated @RequestBody OfficeDepartmentRequest officeDepartmentRequest,
                           @RequestHeader(JwtTokenUtil.AUTH_HEADER_KEY) String token) {
        return departmentFacade.office(officeDepartmentRequest, JwtTokenUtil.getUserId(token));
    }

    @PostMapping("/boss/{orgId}/{bossId}")
    @ApiOperation(value = "委任下届社长", notes = "社长权限")
    public Response boss(@Validated @NotNull(message = "社团id不能为空") @PathVariable(value = "orgId") Long orgId,
                         @Validated @NotNull(message = "下届社长id不能为空") @PathVariable(value = "bossId") Long bossId,
                         @RequestHeader(JwtTokenUtil.AUTH_HEADER_KEY) String token) {
        return departmentFacade.boss(orgId, bossId, JwtTokenUtil.getUserId(token));
    }
}
