package com.yxd.remote.entity;

import com.yxd.remote.enums.RpcResponseCodeEnum;

import java.io.Serializable;

/**
 * @Description：rpc返回体
 * @Date 2020/12/24 10:51
 * @Author YXD
 * @Version 1.0
 */
public class RpcResponse<T> implements Serializable {
    private static final long serialVersionUID = -2765079785021686232L;
    /**
     * 返回请求的ID
     */
    private String requestId;
    /**
     * 返回code
     */
    private Integer code;
    /**
     * 返回信息提示
     */
    private String message;
    /**
     * 返回信息内容
     */
    private T data;

    public RpcResponse() {}

    public String getRequestId() {
        return requestId;
    }

    public RpcResponse<T> setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public RpcResponse<T> setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public RpcResponse<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return data;
    }

    public RpcResponse<T> setData(T data) {
        this.data = data;
        return this;
    }

    public static <T> RpcResponse<T> success(T data, String requestId) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setCode(RpcResponseCodeEnum.SUCCESS.getCode());
        response.setMessage(RpcResponseCodeEnum.SUCCESS.getMessage());
        response.setRequestId(requestId);
        if (null != data) {
            response.setData(data);
        }
        return response;
    }

    public static <T> RpcResponse<T> fail(RpcResponseCodeEnum rpcResponseCodeEnum) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setCode(rpcResponseCodeEnum.getCode());
        response.setMessage(rpcResponseCodeEnum.getMessage());
        return response;
    }
}
