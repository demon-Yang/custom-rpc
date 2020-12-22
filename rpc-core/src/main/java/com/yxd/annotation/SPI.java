package com.yxd.annotation;

import java.lang.annotation.*;

/**
 * @Description：类似dubbo的spi注解
 * @Date 2020/12/22 16:06
 * @Author YXD
 * @Version 1.0
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SPI {
}
