package com.yxd.service;

import com.yxd.util.LogbackUtil;

/**
 * @Description：测试接口层
 * @Date 2020/12/30 18:00
 * @Author YXD
 * @Version 1.0
 */
public class DemoTwoService {
    /**
     * 测试客户端
     * @param content
     * @return
     */
    String testDemoTwoClient(String content) {
        LogbackUtil.info("----测试demoOne服务端----");
        return "----返回demoOne服务端----";
    }
}
