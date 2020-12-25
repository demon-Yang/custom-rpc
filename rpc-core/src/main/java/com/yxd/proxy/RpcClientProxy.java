package com.yxd.proxy;

import com.yxd.factory.SingletonFactory;
import com.yxd.remote.client.NettyClient;
import com.yxd.remote.entity.RpcRequest;
import com.yxd.remote.entity.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @Description：rpc客户端代理
 * @Date 2020/12/25 16:40
 * @Author YXD
 * @Version 1.0
 */
public class RpcClientProxy implements InvocationHandler {

    /**
     * 代理生成类
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T delegate(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest = RpcRequest.builder()
                .setRequestId(UUID.randomUUID().toString())
                .setInterfaceName(method.getDeclaringClass().getName())
                .setMethodName(method.getName())
                .setParameters(args)
                .setParamTypes(method.getParameterTypes())
                .build();
        NettyClient nettyClient = SingletonFactory.getInstance(NettyClient.class);
        CompletableFuture<RpcResponse<Object>> completableFuture =  nettyClient.sendRpcRequest(rpcRequest);
        return completableFuture.get().getData();
    }
}
