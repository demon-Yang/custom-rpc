package com.yxd.spring;

import com.yxd.annotation.RpcScan;
import com.yxd.annotation.RpcService;
import com.yxd.util.LogbackUtil;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;

/**
 * @Description：自定义注入bean
 * @Date 2020/12/23 15:37
 * @Author YXD
 * @Version 1.0
 */
public class CustomScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {
    private static final String BASE_PACKAGE_ATTRIBUTE_NAME = "basePackage";
    private ResourceLoader resourceLoader;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
        //get the attributes and values ​​of RpcScan annotation
        AnnotationAttributes rpcScanAnnotationAttributes = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(RpcScan.class.getName()));
        String[] rpcScanBasePackages = new String[0];
        if (rpcScanAnnotationAttributes != null) {
            // get the value of the basePackage property
            rpcScanBasePackages = rpcScanAnnotationAttributes.getStringArray(BASE_PACKAGE_ATTRIBUTE_NAME);
        }
        if (rpcScanBasePackages.length == 0) {
            rpcScanBasePackages = new String[]{((StandardAnnotationMetadata) annotationMetadata).getIntrospectedClass().getPackage().getName()};
        }
        // Scan the RpcService annotation
        CustomScanner rpcServiceScanner = new CustomScanner(registry, RpcService.class);
        rpcServiceScanner.setResourceLoader(resourceLoader);
        int rpcServiceCount = rpcServiceScanner.scan(rpcScanBasePackages);
        LogbackUtil.info("rpcServiceScanner扫描的数量 【{}】", rpcServiceCount);
    }
}
