package com.yxd.controller;

import com.yxd.annotation.RpcReference;
import com.yxd.service.ServerService;
import com.yxd.util.LogbackUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description：测试接口控制层
 * @Date 2020/12/31 10:26
 * @Author YXD
 * @Version 1.0
 */
@RestController
public class ClientController {

    @RpcReference
    private ServerService serverService;

    /**
     * 测试
     */
    @GetMapping("/test")
    public String demoController() {
        String result = serverService.testDemoOneServer("---调用服务端请求---");
        return result;
    }
}
