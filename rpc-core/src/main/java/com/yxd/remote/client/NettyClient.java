package com.yxd.remote.client;

import com.yxd.registry.ZookeeperDiscovery;
import com.yxd.remote.codec.RpcMessageDecoder;
import com.yxd.remote.codec.RpcMessageEncoder;
import com.yxd.remote.constant.RpcConstant;
import com.yxd.remote.entity.RpcMessage;
import com.yxd.remote.entity.RpcRequest;
import com.yxd.remote.entity.RpcResponse;
import com.yxd.remote.enums.MessageTypeEnum;
import com.yxd.util.LogbackUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Description：netty客户端
 * @Date 2020/12/25 15:14
 * @Author YXD
 * @Version 1.0
 */
public class NettyClient {
    private final Bootstrap bootstrap = new Bootstrap();
    private final EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
    private final Map<String, Channel> channelMap = new ConcurrentHashMap<>();

    public void start() {
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                //  If this time is exceeded or the connection cannot be established, the connection fails.
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline p = ch.pipeline();
                        // If no data is sent to the server within 15 seconds, a heartbeat request is sent
                        p.addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS));
                        p.addLast(new RpcMessageEncoder());
                        p.addLast(new RpcMessageDecoder());
                        p.addLast(new NettyClientHandler());
                    }
                });
    }

    /**
     * 发送请求
     *
     * @param rpcRequest
     * @return
     */
    public CompletableFuture<RpcResponse<Object>> sendRpcRequest(RpcRequest rpcRequest) {
        // build return value
        CompletableFuture<RpcResponse<Object>> resultFuture = new CompletableFuture<>();
        // build rpc service name by rpcRequest
        String rpcServiceName = rpcRequest.getInterfaceName();
        // get server address
        InetSocketAddress inetSocketAddress = new ZookeeperDiscovery("randomBalance").lookupService(rpcServiceName);
        // get  server address related channel
        Channel channel = getChannel(inetSocketAddress);
        if (channel != null && channel.isActive()) {
            RpcMessage rpcMessage = RpcMessage.builder()
                    .setMessageType(MessageTypeEnum.REQUEST_TYPE.getCode())
                    .setCodec(RpcConstant.KYRO)
                    .setData(rpcRequest)
                    .build();
            channel.writeAndFlush(rpcMessage).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    LogbackUtil.info("client send message: [{}]", rpcMessage);
                } else {
                    future.channel().close();
                    resultFuture.completeExceptionally(future.cause());
                    LogbackUtil.error("Send failed:", future.cause());
                }
            });
        } else {
            throw new IllegalStateException();
        }

        return resultFuture;
    }

    /**
     * 获取channel
     *
     * @param inetSocketAddress
     * @return
     */
    public Channel getChannel(InetSocketAddress inetSocketAddress) {
        String key = inetSocketAddress.toString();
        // determine if there is a connection for the corresponding address
        if (channelMap.containsKey(key)) {
            Channel channel = channelMap.get(key);
            // if so, determine if the connection is available, and if so, get it directly
            if (channel != null && channel.isActive()) {
                return channel;
            } else {
                channelMap.remove(key);
            }
        }
        // otherwise, reconnect to get the Channel
        try {
            CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
            bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    completableFuture.complete(future.channel());
                } else {
                    throw new IllegalStateException();
                }
            });
            Channel channel = completableFuture.get();
            channelMap.put(key, channel);
            return channel;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
