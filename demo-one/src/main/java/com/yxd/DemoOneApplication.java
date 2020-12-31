package com.yxd;

import com.yxd.annotation.RpcScan;
import com.yxd.factory.SingletonFactory;
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
@RpcScan(basePackage = {"com.yxd.service"})
public class DemoOneApplication {
    public static void main(String[] args){
        SpringApplication.run(DemoOneApplication.class, args);
        //开启netty服务端
        NettyServer nettyServer = SingletonFactory.getInstance(NettyServer.class);
        nettyServer.start();
    }
}
