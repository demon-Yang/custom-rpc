package com.yxd.remote.codec;

import com.yxd.remote.compress.GzipCompress;
import com.yxd.remote.constant.RpcConstant;
import com.yxd.remote.entity.RpcMessage;
import com.yxd.remote.enums.MessageTypeEnum;
import com.yxd.util.KryoUtil;
import com.yxd.util.LogbackUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description：rpc请求内容编码器
 * @Date 2020/12/24 11:07
 * @Author YXD
 * @Version 1.0
 */
public class RpcMessageEncoder extends MessageToByteEncoder<RpcMessage> {
    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger(0);
    /**
     * custom protocol decoder
     *   0     1     2     3     4        5     6     7     8         9          10      11     12  13  14   15 16
     *   +-----+-----+-----+-----+--------+----+----+----+------+-----------+-------+----- --+-----+-----+-------+
     *   |   magic   code        |version | full length         | messageType| codec|compress|    RequestId       |
     *   +-----------------------+--------+---------------------+-----------+-----------+-----------+------------+
     *   |                                                                                                       |
     *   |                                         body                                                          |
     *   |                                                                                                       |
     *   |                                        ... ...                                                        |
     *   +-------------------------------------------------------------------------------------------------------+
     * 4B  magic code（魔法数）   1B version（版本）   4B full length（消息长度）    1B messageType（消息类型）
     * 1B compress（压缩类型） 1B codec（序列化类型）    4B  requestId（请求的Id）
     * body（object类型数据）
     *
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcMessage rpcMessage, ByteBuf out) {
        try {
            out.writeBytes(RpcConstant.MAGIC_NUMBER);
            out.writeByte(RpcConstant.VERSION);
            // leave a place to write the value of full length
            out.writerIndex(out.writerIndex() + 4);
            byte messageType = rpcMessage.getMessageType();
            out.writeByte(messageType);
            out.writeByte(rpcMessage.getCodec());
            out.writeByte(RpcConstant.GZIP);
            out.writeInt(ATOMIC_INTEGER.getAndIncrement());
            // build full length
            byte[] bodyBytes = null;
            int fullLength = RpcConstant.HEAD_LENGTH;
            // if messageType is not heartbeat message,fullLength = head length + body length
            if (messageType != MessageTypeEnum.HEARTBEAT_PING.getCode()
                    && messageType != MessageTypeEnum.HEARTBEAT_PONG.getCode()) {
                // serialize the object
                bodyBytes = KryoUtil.serialize(rpcMessage.getData());
                // compress the bytes
                bodyBytes = GzipCompress.compress(bodyBytes);
                fullLength += bodyBytes.length;
            }
            if (bodyBytes != null) {
                out.writeBytes(bodyBytes);
            }
            int writeIndex = out.writerIndex();
            out.writerIndex(writeIndex - fullLength + RpcConstant.MAGIC_NUMBER.length + 1);
            out.writeInt(fullLength);
            out.writerIndex(writeIndex);
        } catch (Exception e) {
            LogbackUtil.error("rpc的request编码错误 {}", e.toString());
        }
    }
}
