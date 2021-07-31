package com.liangqiang.manager.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

public class Result<T> implements Serializable {
    private static final long serialVersionUID = 0L;

    private int code;
    private String msg;
    private T data;

    public Result() {
        this(ResultCode.SUCCESS);
    }

    public Result(ResultCode resultCode) {
        this(resultCode, null);
    }

    public Result(ResultCode resultCode, String message) {
        this(resultCode, message, null);
    }

    public Result(ResultCode resultCode, String message, T data) {
        bindCode(resultCode.getCode()).bindMsg(StringUtils.isEmpty(message) ? resultCode.getMsg() : message).bindData(data);
    }

    public Result<T> bindData(T data) {
        this.data = data;
        return this;
    }

    public Result<T> bindMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Result<T> bindCode(int code) {
        this.code = code;
        return this;
    }

    public T getData() {
        return data;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return this.code == ResultCode.SUCCESS.getCode();
    }

    @Override
    public String toString() {
        return JSONUtils.toJSONString(this);
    }
}
