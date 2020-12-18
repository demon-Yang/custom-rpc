package com.yxd.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description：日志工具类
 * @Date 2020/11/13 16:56
 * @Author YXD
 * @Version 1.0
 */
public class LogbackUtil {
    private static final Logger logger = LoggerFactory.getLogger(com.yxd.util.LogbackUtil.class);

    public static void info(String content, Object... arguments) {
        logger.info(content, arguments);
    }

    public static void error(String content, Object... arguments) {
        logger.error(content, arguments);
    }
}
