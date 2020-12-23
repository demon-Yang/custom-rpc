package com.yxd.annotation;

import com.yxd.spring.CustomScannerRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Description：rpc扫描
 * @Date 2020/12/23 15:06
 * @Author YXD
 * @Version 1.0
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(CustomScannerRegistrar.class)
public @interface RpcScan {
    String[] basePackage();
}
