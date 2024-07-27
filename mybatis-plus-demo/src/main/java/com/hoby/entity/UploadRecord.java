package com.hoby.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * (UploadRecord)表实体类
 *
 * @author liaozh
 * @since 2024-07-27
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class UploadRecord extends Model<UploadRecord> {

    private static final long serialVersionUID = 1L;
    
    /**
     * 主键
     */
    private Long id;
    
    /**
     * 流水号
     */
    private String serialNumber;
    
    /**
     * 业务类型
     */
    private String command;
    
    /**
     * 状态(0:上传失败;1:上传成功)
     */
    private Integer recordStatus;
    
    /**
     * 上传成功/错误信息
     */
    private String remark;
    
    /**
     * 创建时间
     */
    private Date createdTime;
    
    /**
     * 更新时间
     */
    private Date updatedTime;
    
    @Override
    public Serializable pkVal() {
        return this.id;
    }
}

