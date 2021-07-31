package com.liangqiang.manager.exception;

import com.liangqiang.manager.utils.Result;
import com.liangqiang.manager.utils.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        StringBuilder errorMsg = new StringBuilder();
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        for (int i = 0; i < errors.size(); i++) {
            ObjectError error = errors.get(i);
            if (error instanceof FieldError) {
                errorMsg.append("[").append(((FieldError) error).getField()).append("]");
            }
            errorMsg.append(error.getDefaultMessage());
            if (i != errors.size() - 1) errorMsg.append(",");
        }
        return ResultCode.ERROR_PARAM.bindMsg(errorMsg.toString()).bindResult();
    }

    @ExceptionHandler(value = Exception.class)
    public Result<Void> handler(Exception e) {
        log.error(e.getMessage(), e);
        Result<Void> result = ResultCode.SYS_ERROR.bindResult();
        return result.bindMsg(result.getMsg() + " | " + e.getMessage());
    }

    @ExceptionHandler(BaseException.class)
    public Result<Void> handler(BaseException e) {
        ResultCode code = ResultCode.CUSTOMIZE_ERROR.bindMsg(e.getMessage());
        if (e.code != null) {
            code = e.code;
        }
        log.error(code.bindResult().toString());
        return code.bindResult();
    }

    @ExceptionHandler(value = NoHandlerFoundException.class)
    public Result<Void> handler(NoHandlerFoundException e) {
        Result<Void> result = ResultCode.CUSTOMIZE_ERROR.bindResult();
        return result.bindMsg(e.getMessage());
    }
}
