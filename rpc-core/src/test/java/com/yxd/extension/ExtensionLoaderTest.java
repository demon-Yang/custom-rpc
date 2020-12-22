package com.yxd.extension;

import com.yxd.loadbalance.LoadBalance;
import com.yxd.util.LogbackUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Description：扩展测试类
 * @Date 2020/12/22 17:11
 * @Author YXD
 * @Version 1.0
 */
class ExtensionLoaderTest {

    @Test
    void getExtensionLoader() {
        LoadBalance loadBalance = (LoadBalance) ExtensionLoader.getExtensionLoader(LoadBalance.class).getExtension("loadbalance");
        LogbackUtil.info(loadBalance.getClass().getSimpleName());
    }
}