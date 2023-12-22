package com.hoby.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author hoby
 * @since 2023-02-21
 */
@Data
@TableName("db_stock")
public class Stock {

    @TableId
    private Long id;

    private String productCode;

    private String stockCode;

    private Integer count;

    private Integer version;

}
