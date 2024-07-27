package com.hoby.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hoby.entity.UploadRecord;

/**
 * (UploadRecord)表数据库访问层
 *
 * @author liaozh
 * @since 2024-07-27
 */
@DS("oracle")
public interface UploadRecordMapper extends BaseMapper<UploadRecord> {

}

