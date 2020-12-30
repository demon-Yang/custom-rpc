package com.yxd;

import com.yxd.factory.SingletonFactory;
import com.yxd.remote.client.NettyClient;
import com.yxd.remote.server.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description：启动类
 * @Date 2020/12/30 16:39
 * @Author YXD
 * @Version 1.0
 */
@SpringBootApplication
public class DemoOneApplication {
    public static void main(String[] args){
        SpringApplication.run(DemoOneApplication.class, args);
        //开启netty服务端
        NettyServer nettyServer = SingletonFactory.getInstance(NettyServer.class);
        nettyServer.start();
        //开启netty客户端
        NettyClient nettyClient = SingletonFactory.getInstance(NettyClient.class);
        nettyClient.start();
    }
}
