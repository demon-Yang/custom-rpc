package com.yxd.exception;

import com.yxd.util.LogbackUtil;

/**
 * @Description：自定义异常类
 * @Date 2020/11/13 15:40
 * @Author YXD
 * @Version 1.0
 */
public class BaseException extends RuntimeException {
    public BaseException(String message) {
        super(message);
        LogbackUtil.error(message);
    }
}
