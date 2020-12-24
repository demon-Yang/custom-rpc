package com.yxd.remote.enums;

/**
 * @Description：消息类型枚举类
 * @Date 2020/12/24 14:40
 * @Author YXD
 * @Version 1.0
 */
public enum MessageTypeEnum {
    REQUEST_TYPE((byte)1, "request"),
    RESPONSE_TYPE((byte)2, "response"),
    HEARTBEAT_PING((byte)3, "ping"),
    HEARTBEAT_PONG((byte)4, "pong");

    /**
     * 消息类型编号
     */
    private byte code;
    /**
     * 消息类型
     */
    private String type;

    MessageTypeEnum(byte code, String type) {
        this.code = code;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public byte getCode() {
        return code;
    }
}
