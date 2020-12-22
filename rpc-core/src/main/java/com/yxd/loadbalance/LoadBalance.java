package com.yxd.loadbalance;

import com.yxd.annotation.SPI;

import java.util.List;

/**
 * @Description：负载均衡接口
 * @Date 2020/12/22 16:47
 * @Author YXD
 * @Version 1.0
 */
@SPI
public interface LoadBalance {
    /**
     * 选择负载均衡策略
     * @param serviceAddresses
     * @param rpcServiceName
     * @return
     */
    String selectServiceAddress(List<String> serviceAddresses, String rpcServiceName);
}
