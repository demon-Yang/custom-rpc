package com.yxd.spring;

import com.yxd.annotation.RpcService;
import com.yxd.expose.ServiceExpose;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

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
            //暴露服务
            ServiceExpose.exposeService(bean);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
