package com.example.common;

/**
 * 错误码
 *
 * @author hoby
 * @since 2023-08-29
 */
public interface ResultCode {
    /**
     * 成功
     */
    int SUCCESS = 0;
    /**
     * 失败
     */
    int ERROR = -1;
    /**
     * 参数校验错误
     */
    int VALIDATE_FAILED = 4001;
    /**
     * 数据不存在
     */
    int NO_DATA = 4004;
    /**
     * 第三方接口异常
     */
    int THIRD_API_ERROR = 3000;
}
