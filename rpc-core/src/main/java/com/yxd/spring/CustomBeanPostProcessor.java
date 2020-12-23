package com.yxd.spring;

import com.yxd.annotation.RpcService;
import com.yxd.constant.SystemConstant;
import com.yxd.registry.ZookeeperRegistry;
import com.yxd.util.LogbackUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * @Description：自定义rpc注入的处理
 * @Date 2020/12/23 16:23
 * @Author YXD
 * @Version 1.0
 */
public class CustomBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(RpcService.class)) {
            try {
                //注册到zookeeper
                String hostname = InetAddress.getLocalHost().getHostAddress();
                ZookeeperRegistry.registryService(
                        bean.getClass().getCanonicalName(),
                        new InetSocketAddress(hostname, SystemConstant.PORT));
            } catch (Exception e) {
                e.printStackTrace();
                LogbackUtil.error(e.toString());
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
