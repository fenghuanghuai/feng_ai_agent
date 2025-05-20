package com.qcdfz.fengaiagent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qcdfz.fengaiagent.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @version v1.0.0
 * @belongsProject: feng-ai-agent
 * @belongsPackage: com.qcdfz.fengaiagent.mapper
 * @author: fgh
 * @description: 用户Mapper接口
 * @createTime: 2025-05-17 21:31
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}