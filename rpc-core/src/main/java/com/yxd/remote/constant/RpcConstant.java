package com.yxd.remote.constant;

/**
 * @Description：常量池
 * @Date 2020/12/24 14:31
 * @Author YXD
 * @Version 1.0
 */
public interface RpcConstant {
    /**
     * 端口号
     */
    int PORT = 8080;

    /**
     * 魔法数
     */
    byte[] MAGIC_NUMBER = {(byte) 'g', (byte) 'r', (byte) 'p', (byte) 'c'};

    /**
     * 版本号
     */
    byte VERSION = 1;

    /**
     * 压缩方式
     */
    byte GZIP = 0x01;

    /**
     * 序列化方式
     */
    byte KYRO = 0x01;

    /**
     * 请求头
     */
    int HEAD_LENGTH = 16;

    /**
     * ping
     */
    String PING = "ping";

    /**
     * pong
     */
    String PONG = "pong";

    /**
     * 最大的帧长度
     */
    int MAX_FRAME_LENGTH = 8 * 1024 * 1024;
}
