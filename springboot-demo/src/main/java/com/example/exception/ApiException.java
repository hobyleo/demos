package com.example.exception;

import com.example.common.ResultCode;
import lombok.Getter;

/**
 * @author hoby
 * @since 2023-08-29
 */
@Getter
public class ApiException extends RuntimeException {

    private final int code;
    private final String message;

    public ApiException() {
        this(ResultCode.ERROR, "接口错误");
    }

    public ApiException(String message) {
        this(ResultCode.ERROR, message);
    }

    public ApiException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        return "code=" + this.code + ", message=" + this.message;
    }
}
