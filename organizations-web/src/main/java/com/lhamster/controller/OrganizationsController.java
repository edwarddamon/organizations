package com.lhamster.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
