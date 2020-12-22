package com.yxd.loadbalance.impl;

import com.yxd.loadbalance.AbstractLoadBalance;

import java.util.List;
import java.util.Random;

/**
 * @Description：随机策略
 * @Date 2020/12/22 17:01
 * @Author YXD
 * @Version 1.0
 */
public class RandomLoadBalance extends AbstractLoadBalance {
    @Override
    public String doSelect(List<String> serviceAddresses, String rpcServiceName) {
        Random random = new Random();
        return serviceAddresses.get(random.nextInt(serviceAddresses.size()));
    }
}
