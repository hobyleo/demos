package com.hoby.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 通用响应对象定义
 *
 * @author hoby
 * @since 2023-08-20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse implements Serializable {
    /**
     * 错误码
     */
    private Integer code;
    /**
     * 错误消息
     */
    private String message;
    /**
     * 泛型响应数据
     */
    private Object data;

    public CommonResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
