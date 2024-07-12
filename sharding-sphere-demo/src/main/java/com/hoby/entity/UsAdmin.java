package com.hoby.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 后台用户表(UsAdmin)表实体类
 *
 * @author liaozh
 * @since 2024-07-12
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class UsAdmin extends Model<UsAdmin> {

    private static final long serialVersionUID = 1L;
    
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 用户id
     */
    private Integer userId;
    
    /**
     * 地址id
     */
    private Integer addrId;
    
    /**
     * 用户编号
     */
    private String userName;
    
    @Override
    public Serializable pkVal() {
        return this.id;
    }
}

