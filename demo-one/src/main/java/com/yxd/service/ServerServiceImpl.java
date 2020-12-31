package com.yxd.service;

import com.yxd.annotation.RpcService;
import com.yxd.util.LogbackUtil;

/**
 * @Description：测试接口实现层
 * @Date 2020/12/31 10:13
 * @Author YXD
 * @Version 1.0
 */
@RpcService
public class ServerServiceImpl implements ServerService {
    @Override
    public String testDemoOneServer(String content) {
        LogbackUtil.info("----测试demoOne服务端----");
        return "----返回demoOne服务端----";
    }
}
