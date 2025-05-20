package com.qcdfz.fengaiagent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qcdfz.fengaiagent.model.entity.TodoItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * @version v1.0.0
 * @belongsProject: feng-ai-agent
 * @belongsPackage: com.qcdfz.fengaiagent.mapper
 * @author: fgh
 * @description: 待办事项Mapper接口
 * @createTime: 2025-05-17 20:51
 */
@Mapper
public interface TodoItemMapper extends BaseMapper<TodoItem> {
}