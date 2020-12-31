package com.yxd.registry;

import com.yxd.util.LogbackUtil;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Description：
 * @Date 2020/12/23 16:49
 * @Author YXD
 * @Version 1.0
 */
class ZookeeperDiscoveryTest {

    @Test
    void lookupService() {
        ZookeeperDiscovery zookeeperDiscovery = new ZookeeperDiscovery("randomBalance");
        InetSocketAddress inetSocketAddress = zookeeperDiscovery.lookupService("com.yxd.test");
        LogbackUtil.info("获取服务地址【{}】", inetSocketAddress.toString());
    }
}