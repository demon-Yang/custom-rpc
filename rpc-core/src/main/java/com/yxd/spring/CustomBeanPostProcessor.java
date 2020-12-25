package com.yxd.spring;

import com.yxd.annotation.RpcReference;
import com.yxd.annotation.RpcService;
import com.yxd.expose.RpcServiceExpose;
import com.yxd.proxy.RpcClientProxy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

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
            RpcServiceExpose.exposeService(bean);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Field[] declaredFields = bean.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            RpcReference rpcReference = declaredField.getAnnotation(RpcReference.class);
            if (rpcReference != null) {
                RpcClientProxy rpcClientProxy = new RpcClientProxy();
                Object clientProxy = rpcClientProxy.delegate(declaredField.getType());
                declaredField.setAccessible(true);
                try {
                    declaredField.set(bean, clientProxy);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }
        return bean;
    }
}
