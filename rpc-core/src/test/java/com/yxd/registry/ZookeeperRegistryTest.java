package com.yxd.registry;

import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Description：zookeeper注册测试
 * @Date 2020/12/23 16:47
 * @Author YXD
 * @Version 1.0
 */
class ZookeeperRegistryTest {

    @Test
    void registryService() {
        ZookeeperRegistry.registryService("com.yxd.test", new InetSocketAddress("127.0.0.1", 8080));
    }
}