package com.yxd.annotation;

import java.lang.annotation.*;

/**
 * @Description：rpc服务端注解
 * @Date 2020/12/23 15:02
 * @Author YXD
 * @Version 1.0
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcService {
}
