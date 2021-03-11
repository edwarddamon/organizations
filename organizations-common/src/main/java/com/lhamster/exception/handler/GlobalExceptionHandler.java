package com.lhamster.exception.handler;

import com.lhamster.response.exception.ServerException;
import com.lhamster.response.result.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 *
 * @author edward
 * @since 2020/6/26
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理自定义的异常
     */
    @ExceptionHandler(ServerException.class)
    public Response handleException(ServerException e) {
        // 打印异常信息
        log.error("[异常信息]：" + e.getMsg());
        return new Response(e.getResult(), e.getMsg());
    }

    /**
     * 处理所有非自定义异常
     */
    @ExceptionHandler(Exception.class)
    public Response handleOtherException(Exception e) {
        //打印异常堆栈信息
        e.printStackTrace();
        // 打印异常信息
        log.error("[不可知异常]：" + e.getMessage());
        return new Response(false, e.getMessage());
    }
}
