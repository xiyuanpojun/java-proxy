package com.liangqiang.manager.utils;

public enum ResultCode {
    CUSTOMIZE_ERROR(-2, "前方道路拥挤，请稍后再试"),
    SYS_ERROR(-1, "前方路滑，请稍后再试"),
    SUCCESS(0, "成功"),
    MISS_PARAM(10001, "缺少参数"),
    ERROR_PARAM(10002, "参数解析错误"),
    INVALID_PARAM(10003, "无效参数"),
    DATABASE_ERROR(10004, "数据库异常"),
    DATABASE_NULL(10005, "没有记录"),
    AUTH_ERROR(10006, "未授权"),
    FORBID_ERROR(10007, "禁止操作"),
    LOGIN_ERROR(10008, "用户名或密码错误"),
    USER_NOT_FOUND(10009, "用户未注册"),
    DUPLICATE_SUBMIT(10010, "重复提交"),
    STATUS_ERROR(10015, "状态异常"),

    ;

    private int code;
    private String msg;

    ResultCode(int code, String msg) {
        bindCode(code).bindMsg(msg);
    }

    public ResultCode bindMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public ResultCode bindCode(int code) {
        this.code = code;
        return this;
    }

    public <T> Result<T> bindResult(T data) {
        return new Result<T>().bindCode(this.code).bindMsg(this.msg).bindData(data);
    }

    public <T> Result<T> bindResult() {
        return new Result<T>().bindCode(this.code).bindMsg(this.msg);
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }

}
