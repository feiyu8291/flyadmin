package com.fly.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应对象
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseData<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 响应码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 响应时间戳
     */
    private Long timestamp;

    public ResponseData() {
        this.timestamp = System.currentTimeMillis();
    }

    public ResponseData(Integer code, String message) {
        this.code = code;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public ResponseData(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 成功响应（无数据）
     */
    public static <T> ResponseData<T> success() {
        return new ResponseData<>(200, "操作成功");
    }

    /**
     * 成功响应（带数据）
     */
    public static <T> ResponseData<T> success(T data) {
        return new ResponseData<>(200, "操作成功", data);
    }

    /**
     * 成功响应（自定义消息）
     */
    public static <T> ResponseData<T> success(String message) {
        return new ResponseData<>(200, message);
    }

    /**
     * 成功响应（自定义消息和数据）
     */
    public static <T> ResponseData<T> success(String message, T data) {
        return new ResponseData<>(200, message, data);
    }

    /**
     * 错误响应
     */
    public static <T> ResponseData<T> error(String message) {
        return new ResponseData<>(500, message);
    }

    /**
     * 错误响应（自定义错误码）
     */
    public static <T> ResponseData<T> error(Integer code, String message) {
        return new ResponseData<>(code, message);
    }
}
