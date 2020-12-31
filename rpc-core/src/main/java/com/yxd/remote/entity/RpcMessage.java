package com.yxd.remote.entity;

/**
 * @Description：rpc请求内容
 * @Date 2020/12/24 10:51
 * @Author YXD
 * @Version 1.0
 */
public class RpcMessage {
    /**
     * 请求类型
     */
    private byte messageType;
    /**
     * 请求编码方式
     */
    private byte codec;
    /**
     * 请求压缩方式
     */
    private byte compress;
    /**
     * 请求ID
     */
    private int requestId;
    /**
     * 请求消息内容
     */
    private Object data;

    public RpcMessage() {
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * 建造者模式
     * @param builder
     */
    public RpcMessage(Builder builder) {
        this.messageType = builder.messageType;
        this.codec = builder.codec;
        this.compress = builder.compress;
        this.requestId = builder.requestId;
        this.data = builder.data;
    }

    public byte getMessageType() {
        return messageType;
    }

    public byte getCodec() {
        return codec;
    }

    public byte getCompress() {
        return compress;
    }

    public int getRequestId() {
        return requestId;
    }

    public Object getData() {
        return data;
    }

    public void setMessageType(byte messageType) {
        this.messageType = messageType;
    }

    public void setCodec(byte codec) {
        this.codec = codec;
    }

    public void setCompress(byte compress) {
        this.compress = compress;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static class Builder {
        private byte messageType;
        private byte codec;
        private byte compress;
        private int requestId;
        private Object data;

        public Builder setMessageType(byte messageType) {
            this.messageType = messageType;
            return this;
        }

        public Builder setCodec(byte codec) {
            this.codec = codec;
            return this;
        }

        public Builder setCompress(byte compress) {
            this.compress = compress;
            return this;
        }

        public Builder setRequestId(int requestId) {
            this.requestId = requestId;
            return this;
        }

        public Builder setData(Object data) {
            this.data = data;
            return this;
        }

        public RpcMessage build() {
            return new RpcMessage(this);
        }
    }
}
