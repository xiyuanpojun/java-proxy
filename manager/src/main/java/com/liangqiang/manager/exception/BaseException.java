package com.liangqiang.manager.exception;

import com.liangqiang.manager.utils.ResultCode;

public class BaseException extends RuntimeException {
    private static final long serialVersionUID = 6413136607285578265L;

    public ResultCode code;

    public BaseException() {
        this(ResultCode.CUSTOMIZE_ERROR.getMsg());
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String... message) {
        this(String.join(",", message));
    }

    public BaseException(ResultCode code) {
        this(code.getMsg());
        this.code = code;
    }
}
