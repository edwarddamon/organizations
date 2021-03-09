package com.lhamster.controller;

import com.lhamster.facade.DemoFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/8
 */
@RestController
@Api(value = "demo控制层")
public class DemoController {
    @Reference
    private DemoFacade demoFacade;

    @PostMapping("/test")
    @ApiOperation("测试controller")
    public void say(@RequestBody String name) {
        demoFacade.say(name);
    }
}
