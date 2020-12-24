package com.yxd.remote.server;

import com.yxd.remote.constant.RpcConstant;
import com.yxd.remote.entity.RpcMessage;
import com.yxd.remote.entity.RpcRequest;
import com.yxd.remote.entity.RpcResponse;
import com.yxd.remote.enums.MessageTypeEnum;
import com.yxd.remote.enums.RpcResponseCodeEnum;
import com.yxd.util.LogbackUtil;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

/**
 * @Description：netty服务处理消息
 * @Date 2020/12/24 16:47
 * @Author YXD
 * @Version 1.0
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg instanceof RpcMessage) {
                byte messageType = ((RpcMessage) msg).getMessageType();
                int requestId = ((RpcMessage) msg).getRequestId();
                if (messageType == MessageTypeEnum.REQUEST_TYPE.getCode()) {
                    RpcMessage rpcMessage = RpcMessage.builder()
                            .setMessageType(MessageTypeEnum.HEARTBEAT_PONG.getCode())
                            .setCodec(RpcConstant.KYRO)
                            .setCompress(RpcConstant.GZIP)
                            .setRequestId(requestId)
                            .setData(RpcConstant.PONG)
                            .build();
                    ctx.writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                } else {
                    RpcRequest rpcRequest = (RpcRequest) ((RpcMessage) msg).getData();
                    // Execute the target method (the method the client needs to execute) and return the method result
                    Object result = RpcRequestHandler.handle(rpcRequest);
                    if (ctx.channel().isActive() && ctx.channel().isWritable()) {
                        RpcResponse<Object> rpcResponse = RpcResponse.success(result, rpcRequest.getRequestId());
                        RpcMessage rpcMessage = RpcMessage.builder()
                                .setMessageType(MessageTypeEnum.RESPONSE_TYPE.getCode())
                                .setCodec(RpcConstant.KYRO)
                                .setCompress(RpcConstant.GZIP)
                                .setRequestId(requestId)
                                .setData(rpcResponse)
                                .build();
                        ctx.writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                    } else {
                        RpcResponse<Object> rpcResponse = RpcResponse.fail(RpcResponseCodeEnum.FAIL);
                        RpcMessage rpcMessage = RpcMessage.builder()
                                .setMessageType(MessageTypeEnum.RESPONSE_TYPE.getCode())
                                .setCodec(RpcConstant.KYRO)
                                .setCompress(RpcConstant.GZIP)
                                .setRequestId(requestId)
                                .setData(rpcResponse)
                                .build();
                        ctx.writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                    }
                }

            }
        } finally {
            //Ensure that ByteBuf is released, otherwise there may be memory leaks
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                LogbackUtil.info("idle check happen, so close the connection");
                ctx.close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LogbackUtil.error("server catch exception");
        cause.printStackTrace();
        ctx.close();
    }
}
