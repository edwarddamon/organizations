package com.lhamster.facadeImpl;

import com.lhamster.entity.OrgLimit;
import com.lhamster.facade.DemoFacade;
import com.lhamster.service.OrgLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/8
 */
@Service
@org.apache.dubbo.config.annotation.Service
public class DemoFacadeImpl implements DemoFacade {
    @Autowired
    private OrgLimitService orgLimitService;

    @Override
    public void say(String name) {
        List<OrgLimit> list = orgLimitService.list();
        System.out.println(list);
        System.out.println("你好" + name);
    }
}
