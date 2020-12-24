package com.yxd.remote.server;

import com.yxd.expose.ServiceExpose;
import com.yxd.remote.entity.RpcRequest;
import com.yxd.util.LogbackUtil;

import java.lang.reflect.Method;

/**
 * @Description：rpc的request处理
 * @Date 2020/12/24 17:15
 * @Author YXD
 * @Version 1.0
 */
public class RpcRequestHandler {
    /**
     *
     * @param rpcRequest
     * @return
     */
    public static Object handle(RpcRequest rpcRequest) {
        return invokeTargetMethod(rpcRequest);
    }

    /**
     * 执行方法
     * @param rpcRequest
     * @return
     */
    private static Object invokeTargetMethod(RpcRequest rpcRequest) {
        Object result = null;
        try {
            Object service = ServiceExpose.serviceMap.get(rpcRequest.getInterfaceName());
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            result = method.invoke(service, rpcRequest.getParameters());
        } catch (Exception e) {
            e.printStackTrace();
            LogbackUtil.error(e.toString());
        }
        return result;
    }
}
