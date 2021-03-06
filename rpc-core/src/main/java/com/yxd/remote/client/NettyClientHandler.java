package com.yxd.remote.client;

import com.yxd.factory.SingletonFactory;
import com.yxd.remote.constant.RpcConstant;
import com.yxd.remote.entity.RpcMessage;
import com.yxd.remote.entity.RpcResponse;
import com.yxd.remote.enums.MessageTypeEnum;
import com.yxd.util.LogbackUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

import java.net.InetSocketAddress;

/**
 * @Description：
 * @Date 2020/12/25 15:17
 * @Author YXD
 * @Version 1.0
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    private final UnprocessedRequest unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequest.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            LogbackUtil.info("返回信息: [{}]", msg);
            if (msg instanceof RpcMessage) {
                RpcMessage temp = (RpcMessage) msg;
                if (temp.getMessageType() == MessageTypeEnum.HEARTBEAT_PONG.getCode()) {
                    LogbackUtil.info("心跳pong返回 [{}]", temp.getData());
                } else if (temp.getMessageType() == MessageTypeEnum.RESPONSE_TYPE.getCode()) {
                    // 完成异步
                    unprocessedRequests.complete((RpcResponse<Object>) temp.getData());
                }
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE) {
                NettyClient nettyClient = SingletonFactory.getInstance(NettyClient.class);
                Channel channel = nettyClient.getChannel((InetSocketAddress) ctx.channel().remoteAddress());
                RpcMessage rpcMessage = RpcMessage.builder()
                        .setMessageType(MessageTypeEnum.HEARTBEAT_PING.getCode())
                        .setCodec(RpcConstant.KYRO)
                        .setCompress(RpcConstant.GZIP)
                        .setData(RpcConstant.PING)
                        .build();
                channel.writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
