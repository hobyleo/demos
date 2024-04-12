package com.example.advice;

import com.example.common.CommonResponse;
import com.example.common.ResultCode;
import com.example.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 全局异常捕获处理
 *
 * @author hoby
 * @since 2023-08-20
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(Exception.class)
    public CommonResponse exception(Exception e) {
        CommonResponse response = new CommonResponse(ResultCode.ERROR, "business error");
        response.setData(e.getMessage());
        log.error("common service has error: [{}]", e.getMessage(), e);
        return response;
    }

    @ExceptionHandler(ApiException.class)
    public CommonResponse apiException(ApiException e) {
        CommonResponse response = new CommonResponse(e.getCode(), e.getMessage());
        log.error("common service throws exception: [{}]", e.toString());
        return response;
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public CommonResponse missingServletRequestParameterException(MissingServletRequestParameterException e) {
        String message = String.format("缺失参数:[%s]", e.getParameterName());
        return new CommonResponse(ResultCode.VALIDATE_FAILED, message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResponse methodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String errorMessage = fieldErrors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(","));
        return new CommonResponse(ResultCode.VALIDATE_FAILED, errorMessage);
    }

    @ExceptionHandler(BindException.class)
    public CommonResponse bindException(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String errorMessage = fieldErrors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(","));
        return new CommonResponse(ResultCode.VALIDATE_FAILED, errorMessage);
    }

}
