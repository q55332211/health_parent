package com.itheima.controller;

import com.itheima.entity.Result;
import com.itheima.health.exception.HealthException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
/**
 * @Description:
 * @Date: Create in 17:15 2020/7/30
 */

/****
 * 这是一个Controller动态代理增加类 ，主要代理异常处理
 * 根据@ExceptionHandler 对指定的异常进行捕获，对应的方法进行处理
 * 使用时  抛出本类上指定的异常即可
 *    throw new HealthException(MessageConstant.CHECKITEM_IN_USE);
 */
@RestControllerAdvice
public class HealExceptionAdvice {

    //定义方法做异常处理
    //使用注解对异常进行认领
    //统一标注返回类型
    @ExceptionHandler(HealthException.class)
    public Result handleHealthException(HealthException he) {
        return new Result(false, he.getMessage());
    }

    //未知异常处理
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        e.printStackTrace();
        return new Result(false, "未知异常");
    }
}
