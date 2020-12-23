package com.yxd.registry;

import com.yxd.exception.BaseException;
import com.yxd.extension.ExtensionLoader;
import com.yxd.loadbalance.LoadBalance;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @Description：服务发现中心
 * @Date 2020/12/23 14:23
 * @Author YXD
 * @Version 1.0
 */
public class ZookeeperDiscovery {
    private final LoadBalance loadBalance;

    public ZookeeperDiscovery(String strategy) {
        this.loadBalance = (LoadBalance) ExtensionLoader.getExtensionLoader(LoadBalance.class).getExtension(strategy);
    }

    /**
     * 负载均衡
     * @param rpcServiceName
     * @return
     */
    public InetSocketAddress lookupService(String rpcServiceName) {
        List<String> childrenNodes = ZookeeperFactory.getChildrenNodes(rpcServiceName);
        if (childrenNodes == null || childrenNodes.size() == 0) {
            throw new IllegalArgumentException("There is no such service.Please check out.");
        }
        String serviceAddress = loadBalance.selectServiceAddress(childrenNodes, rpcServiceName);
        String hostname = serviceAddress.split(":")[0];
        int port = Integer.parseInt(serviceAddress.split(":")[1]);
        return new InetSocketAddress(hostname, port);
    }
}
