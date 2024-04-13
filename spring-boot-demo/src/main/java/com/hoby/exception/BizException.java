package com.hoby.exception;

import com.hoby.constants.ResultCode;
import lombok.Getter;

/**
 * @author hoby
 * @since 2023-08-29
 */
@Getter
public class BizException extends RuntimeException {

    private final int code;
    private final String message;

    public BizException() {
        this(ResultCode.ERROR, "业务处理异常");
    }

    public BizException(String message) {
        this(ResultCode.ERROR, message);
    }

    public BizException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        return "code=" + this.code + ", message=" + this.message;
    }
}
