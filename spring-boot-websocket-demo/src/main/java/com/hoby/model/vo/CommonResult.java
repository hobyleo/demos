package com.hoby.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author liaozh
 * @since 2024-05-10
 */
@Data
@NoArgsConstructor
public class CommonResult implements Serializable {
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

    public CommonResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public CommonResult(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
