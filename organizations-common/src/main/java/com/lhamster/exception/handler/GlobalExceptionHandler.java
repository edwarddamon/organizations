package com.lhamster.exception.handler;

import com.lhamster.response.exception.ServerException;
import com.lhamster.response.result.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;

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
        e.printStackTrace();
        return new Response(e.getResult(), e.getMsg());
    }

    /*
     * 校验异常捕捉
     * */
    @ExceptionHandler(value = {BindException.class, ValidationException.class, MethodArgumentNotValidException.class})
    public Response handleParameterVerificationException(Exception e) {
        String msg = null;
        /// BindException
        if (e instanceof BindException) {
            // getFieldError获取的是第一个不合法的参数(P.S.如果有多个参数不合法的话)
            FieldError fieldError = ((BindException) e).getFieldError();
            if (fieldError != null) {
                msg = fieldError.getDefaultMessage();
            }
            e.printStackTrace();
            /// MethodArgumentNotValidException
        } else if (e instanceof MethodArgumentNotValidException) {
            BindingResult bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
            // getFieldError获取的是第一个不合法的参数(P.S.如果有多个参数不合法的话)
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                msg = fieldError.getDefaultMessage();
            }
            e.printStackTrace();
            /// ValidationException 的子类异常ConstraintViolationException
        } else if (e instanceof ConstraintViolationException) {
            /*
             * ConstraintViolationException的e.getMessage()形如
             *     {方法名}.{参数名}: {message}
             *  这里只需要取后面的message即可
             */
            msg = e.getMessage();
            if (msg != null) {
                int lastIndex = msg.lastIndexOf(':');
                if (lastIndex >= 0) {
                    msg = msg.substring(lastIndex + 1).trim();
                }
            }
            e.printStackTrace();
            /// ValidationException 的其它子类异常
        } else {
            msg = "处理参数时异常";
        }
        // 打印异常信息
        log.error("[异常信息]：" + msg);
        return new Response(Boolean.FALSE, msg);
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
