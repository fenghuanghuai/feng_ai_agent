package com.qcdfz.fengaiagent.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcdfz.fengaiagent.mapper.TodoItemMapper;
import com.qcdfz.fengaiagent.model.entity.TodoItem;
import com.qcdfz.fengaiagent.service.TodoItemService;
import org.springframework.stereotype.Service;

/**
 * @version v1.0.0
 * @belongsProject: feng-ai-agent
 * @belongsPackage: com.qcdfz.fengaiagent.service.impl
 * @author: fgh
 * @description: 待办事项服务实现类
 * @createTime: 2025-05-17 20:50
 */
@Service
public class TodoItemServiceImpl extends ServiceImpl<TodoItemMapper, TodoItem> implements TodoItemService {
}