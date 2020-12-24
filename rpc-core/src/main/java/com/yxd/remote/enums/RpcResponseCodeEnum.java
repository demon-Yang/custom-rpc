package com.yxd.remote.enums;

/**
 * @Description：rpc返回code枚举
 * @Date 2020/12/24 11:22
 * @Author YXD
 * @Version 1.0
 */
public enum RpcResponseCodeEnum {
    SUCCESS(200, "请求成功"),
    FAIL(500, "请求失败");
    /**
     * code
     */
    private final int code;
    /**
     * 信息内容
     */
    private final String message;

    RpcResponseCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
