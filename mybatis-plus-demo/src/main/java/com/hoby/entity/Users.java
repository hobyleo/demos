package com.hoby.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * (Users)表实体类
 *
 * @author liaozh
 * @since 2024-07-27
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class Users extends Model<Users> {

    private static final long serialVersionUID = 1L;
    

    private Integer id;
    

    private String email;
    

    private String hashedPassword;
    

    private Integer isActive;
    
    @Override
    public Serializable pkVal() {
        return this.id;
    }
}

