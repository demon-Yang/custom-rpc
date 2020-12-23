package com.yxd.annotation;

import java.lang.annotation.*;

/**
 * @Description：rpc消费端注解
 * @Date 2020/12/23 15:05
 * @Author YXD
 * @Version 1.0
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcReference {
}
