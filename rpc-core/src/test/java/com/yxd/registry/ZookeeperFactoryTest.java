package com.yxd.registry;

import com.yxd.util.LogbackUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Description：测试zookeeper客户端
 * @Date 2020/12/21 15:19
 * @Author YXD
 * @Version 1.0
 */
class ZookeeperFactoryTest {

    @Test
    void createPersistentNode() {
        ZookeeperFactory.createPersistentNode("/com.yxd.testService/127.0.0.1:8080");
    }

    @Test
    void getChildrenNodes() {
        List<String> childrenNodes = ZookeeperFactory.getChildrenNodes("/com.yxd.testService");
        for (String node : childrenNodes) {
            LogbackUtil.info(node);
        }
    }

    @Test
    void clearRegistry() {
        ZookeeperFactory.clearRegistry();
    }
}