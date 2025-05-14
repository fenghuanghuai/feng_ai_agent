package com.qcdfz.fengaiagent.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcdfz.fengaiagent.model.entity.ApiKey;
import com.qcdfz.fengaiagent.service.ApiKeyService;
import com.qcdfz.fengaiagent.mapper.ApiKeyMapper;
import org.springframework.stereotype.Service;

/**
* @author fgh
* @description 针对表【api_key(API密钥表)】的数据库操作Service实现
* @createDate 2025-04-30 19:55:31
*/
@Service
public class ApiKeyServiceImpl extends ServiceImpl<ApiKeyMapper, ApiKey>
    implements ApiKeyService{

}




