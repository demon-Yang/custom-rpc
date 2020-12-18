package com.yxd.registry;

import org.apache.curator.framework.CuratorFramework;

/**
 * @Description：zookeeper注册中心
 * @Date 2020/12/18 14:06
 * @Author YXD
 * @Version 1.0
 */
public class ZookeeperFactory {
    private static final int BASE_SLEEP_TIME = 1000;
    private static final int MAX_RETRIES = 3;
    private static final String ZK_REGISTER_ROOT_PATH = "/custom-rpc";
    private static final String defaultZookeeperAddress = "127.0.0.1:2181";
    private static CuratorFramework zkClient;

    public static void createZkClient(String path) {

    }
}

