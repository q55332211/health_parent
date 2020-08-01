package com.itheima.health.exception;

/**
 * @Description: 自定义异常类
 * @Date: Create in 17:12 2020/7/30
 */
public class HealthException extends RuntimeException {

    public HealthException(String message) {
        super(message);
    }
}
