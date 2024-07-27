package com.hoby.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hoby.mapper.UploadRecordMapper;
import com.hoby.entity.UploadRecord;
import com.hoby.service.UploadRecordService;
import org.springframework.stereotype.Service;

/**
 * (UploadRecord)表服务实现类
 *
 * @author liaozh
 * @since 2024-07-27
 */
@Service("uploadRecordService")
public class UploadRecordServiceImpl extends ServiceImpl<UploadRecordMapper, UploadRecord> implements UploadRecordService {

}

