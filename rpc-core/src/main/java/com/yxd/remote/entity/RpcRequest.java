package com.yxd.remote.entity;

import java.io.Serializable;

/**
 * @Description：rpc请求体
 * @Date 2020/12/24 10:50
 * @Author YXD
 * @Version 1.0
 */
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = 8427495180817462904L;
    /**
     * 请求ID
     */
    private String requestId;
    /**
     * 请求接口
     */
    private String interfaceName;
    /**
     * 请求方法
     */
    private String methodName;
    /**
     * 请求参数
     */
    private Object[] parameters;
    /**
     * 请求参数类型
     */
    private Class<?>[] paramTypes;

    public static Builder builder() {
        return new Builder();
    }

    /**
     * 建造者模式
     * @param builder
     */
    public RpcRequest(Builder builder) {
        this.requestId = builder.requestId;
        this.interfaceName = builder.interfaceName;
        this.methodName = builder.methodName;
        this.parameters = builder.parameters;
        this.paramTypes = builder.paramTypes;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public Class<?>[] getParamTypes() {
        return paramTypes;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public void setParamTypes(Class<?>[] paramTypes) {
        this.paramTypes = paramTypes;
    }

    public static class Builder {
        private String requestId;
        private String interfaceName;
        private String methodName;
        private Object[] parameters;
        private Class<?>[] paramTypes;

        public Builder setRequestId(String requestId) {
            this.requestId = requestId;
            return this;
        }

        public Builder setInterfaceName(String interfaceName) {
            this.interfaceName = interfaceName;
            return this;
        }

        public Builder setMethodName(String methodName) {
            this.methodName = methodName;
            return this;
        }

        public Builder setParameters(Object[] parameters) {
            this.parameters = parameters;
            return this;
        }

        public Builder setParamTypes(Class<?>[] paramTypes) {
            this.paramTypes = paramTypes;
            return this;
        }

        public RpcRequest build() {
            return new RpcRequest(this);
        }
    }
}
