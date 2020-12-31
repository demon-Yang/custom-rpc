package com.yxd.expose;

import com.yxd.registry.ZookeeperRegistry;
import com.yxd.remote.constant.RpcConstant;
import com.yxd.util.LogbackUtil;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description：服务端暴露服务
 * @Date 2020/12/24 18:07
 * @Author YXD
 * @Version 1.0
 */
public class RpcServiceExpose {
    public final static ConcurrentHashMap<String, Object> serviceMap = new ConcurrentHashMap<>();

    /**
     * 暴露服务
     */
    public static void exposeService(Object bean) {
        try {
            //注册到zookeeper
            String hostname = InetAddress.getLocalHost().getHostAddress();
            ZookeeperRegistry.registryService(
                    bean.getClass().getInterfaces()[0].getCanonicalName(),
                    new InetSocketAddress(hostname, RpcConstant.PORT));
            //添加到map
            serviceMap.putIfAbsent(bean.getClass().getInterfaces()[0].getCanonicalName(), bean);
        } catch (Exception e) {
            e.printStackTrace();
            LogbackUtil.error(e.toString());
        }
    }
}
