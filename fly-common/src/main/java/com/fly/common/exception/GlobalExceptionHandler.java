package com.fly.common.exception;

import com.fly.common.constant.ResponseCode;
import com.fly.common.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 业务异常
     */
    @ExceptionHandler(ResponseException.class)
    public ResponseData<?> handleResponseException(ResponseException e, HttpServletRequest request) {
        log.error("请求地址'{}',发生业务异常.", request.getRequestURI(), e);
        return ResponseData.error(e.getCode(), e.getMessage());
    }

    /**
     * 参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseData<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
                                                                 HttpServletRequest request) {
        log.error("请求地址'{}',参数校验异常.", request.getRequestURI(), e);
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return ResponseData.error(ResponseCode.BAD_REQUEST, message);
    }

    /**
     * 参数绑定异常
     */
    @ExceptionHandler(BindException.class)
    public ResponseData<?> handleBindException(BindException e, HttpServletRequest request) {
        log.error("请求地址'{}',参数绑定异常.", request.getRequestURI(), e);
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return ResponseData.error(ResponseCode.BAD_REQUEST, message);
    }

    /**
     * 运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseData<?> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        log.error("请求地址'{}',发生运行时异常.", request.getRequestURI(), e);
        return ResponseData.error(e.getMessage());
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseData<?> handleException(Exception e, HttpServletRequest request) {
        log.error("请求地址'{}',发生系统异常.", request.getRequestURI(), e);
        return ResponseData.error("系统异常，请联系管理员");
    }
}
