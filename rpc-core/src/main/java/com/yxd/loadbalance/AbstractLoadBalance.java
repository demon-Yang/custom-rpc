package com.yxd.loadbalance;

import java.util.List;

/**
 * @Description：负载均衡抽象类
 * @Date 2020/12/22 16:55
 * @Author YXD
 * @Version 1.0
 */
public abstract class AbstractLoadBalance implements LoadBalance {
    @Override
    public String selectServiceAddress(List<String> serviceAddresses, String rpcServiceName) {
        if (serviceAddresses == null || serviceAddresses.size() == 0) {
            return null;
        }
        if (serviceAddresses.size() == 1) {
            return serviceAddresses.get(0);
        }
        return doSelect(serviceAddresses, rpcServiceName);
    }

    /**
     * 选择策略
     * @param serviceAddresses
     * @param rpcServiceName
     * @return
     */
    public abstract String doSelect(List<String> serviceAddresses, String rpcServiceName);
}
