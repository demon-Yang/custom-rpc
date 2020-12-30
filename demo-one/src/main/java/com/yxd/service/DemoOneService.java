package com.yxd.service;

import com.yxd.annotation.RpcService;
import com.yxd.util.LogbackUtil;

/**
 * @Description：测试接口层
 * @Date 2020/12/30 17:28
 * @Author YXD
 * @Version 1.0
 */
@RpcService
public class DemoOneService {
    /**
     * 测试服务端
     * @param content
     * @return
     */
    String testDemoOneServer(String content){
        LogbackUtil.info("----测试demoOne服务端----");
        return "----返回demoOne服务端----";
    }
}
