package com.yxd.remote.codec;

import com.yxd.remote.compress.GzipCompress;
import com.yxd.remote.constant.RpcConstant;
import com.yxd.remote.entity.RpcMessage;
import com.yxd.remote.enums.MessageTypeEnum;
import com.yxd.util.KryoUtil;
import com.yxd.util.LogbackUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.util.Arrays;

/**
 * @Description：rpc请求内容解码器
 * @Date 2020/12/24 11:07
 * @Author YXD
 * @Version 1.0
 */
public class RpcMessageDecoder extends LengthFieldBasedFrameDecoder {
    public RpcMessageDecoder() {
        // lengthFieldOffset: magic code is 4B, and version is 1B, and then full length. so value is 5
        // lengthFieldLength: full length is 4B. so value is 4
        // lengthAdjustment: full length include all data and read 9 bytes before, so the left length is (fullLength-9). so values is -9
        // initialBytesToStrip: we will check magic code and version manually, so do not strip any bytes. so values is 0
        this(RpcConstant.MAX_FRAME_LENGTH, 5, 4, -9, 0);
    }

    /**
     * @param maxFrameLength      Maximum frame length. It decide the maximum length of data that can be received.
     *                            If it exceeds, the data will be discarded.
     * @param lengthFieldOffset   Length field offset. The length field is the one that skips the specified length of byte.
     * @param lengthFieldLength   The number of bytes in the length field.
     * @param lengthAdjustment    The compensation value to add to the value of the length field
     * @param initialBytesToStrip Number of bytes skipped.
     *                            If you need to receive all of the header+body data, this value is 0
     *                            if you only want to receive the body data, then you need to skip the number of bytes consumed by the header.
     */
    public RpcMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
                             int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        Object decoded = super.decode(ctx, in);
        if (decoded instanceof ByteBuf) {
            ByteBuf frame = (ByteBuf) decoded;
            if (frame.readableBytes() >= RpcConstant.HEAD_LENGTH) {
                try {
                    return decodeFrame(frame);
                } catch (Exception e) {
                    LogbackUtil.error("rpc的request解码错误 {}", e.toString());
                } finally {
                    frame.release();
                }
            }

        }
        return decoded;
    }

    /**
     * 解析协议帧
     * @param in
     * @return
     */
    private Object decodeFrame(ByteBuf in) {
        // note: must read ByteBuf in order
        // read the first 4 bit, which is the magic number, and compare
        int len = RpcConstant.MAGIC_NUMBER.length;
        byte[] tmp = new byte[len];
        in.readBytes(tmp);
        for (int i = 0; i < len; i++) {
            if (tmp[i] != RpcConstant.MAGIC_NUMBER[i]) {
                throw new IllegalArgumentException("Unknown magic code: " + Arrays.toString(tmp));
            }
        }
        // read the version and compare
        byte version = in.readByte();
        if (version != RpcConstant.VERSION) {
            throw new RuntimeException("version isn't compatible" + version);
        }
        int fullLength = in.readInt();
        // build RpcMessage object
        byte messageType = in.readByte();
        byte codecType = in.readByte();
        byte compressType = in.readByte();
        int requestId = in.readInt();
        RpcMessage rpcMessage = RpcMessage.builder()
                .setCodec(codecType)
                .setRequestId(requestId)
                .setCompress(compressType)
                .setMessageType(messageType).build();
        if (messageType == MessageTypeEnum.HEARTBEAT_PING.getCode()) {
            rpcMessage.setData(RpcConstant.PING);
        } else if (messageType == MessageTypeEnum.HEARTBEAT_PONG.getCode()) {
            rpcMessage.setData(RpcConstant.PONG);
        } else {
            int bodyLength = fullLength - RpcConstant.HEAD_LENGTH;
            if (bodyLength > 0) {
                byte[] bs = new byte[bodyLength];
                in.readBytes(bs);
                // decompress the bytes
                bs = GzipCompress.decompress(bs);
                // deserialize the object
                if (messageType == MessageTypeEnum.REQUEST_TYPE.getCode()) {
                    rpcMessage.setData(KryoUtil.deserialize(bs));
                } else if (messageType == MessageTypeEnum.RESPONSE_TYPE.getCode()) {
                    rpcMessage.setData(KryoUtil.deserialize(bs));
                } else {
                    rpcMessage.setData(null);
                }
            }
        }
        return rpcMessage;
    }
}
