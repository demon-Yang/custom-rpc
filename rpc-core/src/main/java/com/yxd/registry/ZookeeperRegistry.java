package com.yxd.registry;

import org.apache.curator.framework.CuratorFramework;

import java.net.InetSocketAddress;

/**
 * @Description：服务注册中心
 * @Date 2020/12/23 14:18
 * @Author YXD
 * @Version 1.0
 */
public class ZookeeperRegistry {
    /**
     * 注册服务
     * @param rpcServiceName
     * @param inetSocketAddress
     */
    public static void registryService(String rpcServiceName, InetSocketAddress inetSocketAddress) {
        String servicePath = "/" + rpcServiceName + inetSocketAddress.toString();
        ZookeeperFactory.createPersistentNode(servicePath);
    }
}
