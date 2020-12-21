package com.yxd.registry;

import com.yxd.util.LogbackUtil;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.ArrayList;
import java.util.List;

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
    private static final String zookeeperAddress = "127.0.0.1:2181";
    private static CuratorFramework zkClient;

    /**
     * 创建zookeeper永久节点
     *
     * @param path com.yxd.service.testService/127.0.0.1:2181
     */
    public static void createPersistentNode(String path) {
        //检查是否有创建zookeeper客户端
        obtainZKClient();
        //创建节点
        try {
            path = ZK_REGISTER_ROOT_PATH + path;
            if (zkClient.checkExists().forPath(path) == null) {
                zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
                LogbackUtil.info("创建zookeeper节点：【{}】", path);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogbackUtil.error(e.toString());
        }
    }

    /**
     * 获取zookeeper子节点的列表
     * @param rpcServiceName
     * @return
     */
    public static List<String> getChildrenNodes(String rpcServiceName) {
        //检查是否有创建zookeeper客户端
        obtainZKClient();
        List<String> result = new ArrayList<String>();
        String servicePath = ZK_REGISTER_ROOT_PATH + rpcServiceName;
        try {
            result = zkClient.getChildren().forPath(servicePath);
        } catch (Exception e) {
            e.printStackTrace();
            LogbackUtil.error(e.toString());
        }
        return result;
    }

    /**
     * 清空zookeeper的rpc注册信息
     */
    public static void clearRegistry() {
        //检查是否有创建zookeeper客户端
        obtainZKClient();
        //递归删除所有该节点的子节点
        deleteNode(ZK_REGISTER_ROOT_PATH);
    }

    /**
     * 递归删除zookeeper节点以及子节点
     * @param rootPath
     */
    private static void deleteNode(String rootPath) {
        try {
            List<String> nodes = zkClient.getChildren().forPath(rootPath);
            if (nodes.size() == 0) {
                zkClient.delete().forPath(rootPath);
            } else {
                for (String node : nodes) {
                    deleteNode(rootPath + "/" + node);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogbackUtil.error(e.toString());
        }
    }

    /**
     * 创建zookeeper客户端
     */
    private static void obtainZKClient() {
        //检查是否启动了客户端
        if (zkClient == null || zkClient.getState() != CuratorFrameworkState.STARTED) {
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(BASE_SLEEP_TIME, MAX_RETRIES);
            zkClient = CuratorFrameworkFactory.builder()
                    .connectString(zookeeperAddress)
                    .retryPolicy(retryPolicy)
                    .build();
            zkClient.start();
        }
    }
}

