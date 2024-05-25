package com.github.deansquirrel.tools.http;

public class ResponseResult<T> {

    public static int SUCCESS = 0;
    public static int FAIL = -1;
    public static String MSG_SUCCESS = "成功";
    public static String MSG_FAIL = "失败";

    protected int code = 0;
    protected String message;
    protected T data;

    public ResponseResult() {
    }

    public ResponseResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> ResponseResult<T> success() {
        return new ResponseResult<>(ResponseResult.SUCCESS, ResponseResult.MSG_SUCCESS, null);
    }

    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<>(ResponseResult.SUCCESS, ResponseResult.MSG_SUCCESS, data);
    }

    public static <T> ResponseResult<T> success(String message, T data) {
        return new ResponseResult<>(ResponseResult.SUCCESS, message, data);
    }

    public static <T> ResponseResult<T> fail() {
        return new ResponseResult<>(ResponseResult.FAIL, ResponseResult.MSG_FAIL, null);
    }

    public static <T> ResponseResult<T> fail(String message) {
        return new ResponseResult<>(ResponseResult.FAIL, message, null);
    }

    public static <T> ResponseResult<T> fail(int code, String message) {
        return new ResponseResult<>(code, message, null);
    }
}
