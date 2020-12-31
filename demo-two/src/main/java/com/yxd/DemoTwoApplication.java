package com.yxd;

import com.yxd.factory.SingletonFactory;
import com.yxd.remote.client.NettyClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description：启动类
 * @Date 2020/12/30 17:56
 * @Author YXD
 * @Version 1.0
 */
@SpringBootApplication
public class DemoTwoApplication {
    public static void main(String[] args){
        SpringApplication.run(DemoTwoApplication.class, args);
        //开启netty客户端
        NettyClient nettyClient = SingletonFactory.getInstance(NettyClient.class);
        nettyClient.start();
    }
}
