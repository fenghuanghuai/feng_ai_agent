package com.qcdfz.fengaiagent.tools;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qcdfz.fengaiagent.model.entity.TodoItem;
import com.qcdfz.fengaiagent.service.TodoItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @version v1.0.0
 * @belongsProject: feng-ai-agent
 * @belongsPackage: com.qcdfz.fengaiagent.tools
 * @author: fgh
 * @description: 待办事项工具类
 * @createTime: 2025-05-17 20:49
 */
@Slf4j
@Component
public class TodoTool {

    @Resource
    private TodoItemService todoItemService;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 创建待办事项
     * @param title 标题
     * @param content 内容
     * @param priority 优先级（0-低，1-中，2-高）
     * @param dueDate 截止日期
     * @param toolContext 工具上下文
     * @return 创建结果
     */
    @Tool(description = "Create a new todo item")
    public String createTodo(
            @ToolParam(description = "Todo title") String title,
            @ToolParam(description = "Todo content") String content,
            @ToolParam(description = "Priority (0-Low, 1-Medium, 2-High)") Integer priority,
            @ToolParam(description = "Due date (yyyy-MM-dd HH:mm:ss)") String dueDate,
            ToolContext toolContext) {
        try {
            Long userId = (Long)toolContext.getContext().get("userId");
            if (userId == null) {
                return "用户未登录";
            }

            TodoItem todoItem = new TodoItem();
            todoItem.setUserId(userId);
            todoItem.setTitle(title);
            todoItem.setContent(content);
            todoItem.setPriority(priority);
            todoItem.setDueDate(DATE_FORMAT.parse(dueDate));
            todoItem.setCreateTime(new Date());
            todoItem.setUpdateTime(new Date());
            todoItem.setStatus(0);

            boolean success = todoItemService.save(todoItem);
            return success ? "待办事项创建成功" : "待办事项创建失败";
        } catch (Exception e) {
            log.error("创建待办事项失败", e);
            return "创建待办事项失败：" + e.getMessage();
        }
    }

    /**
     * 更新待办事项
     * @param id 待办事项ID
     * @param title 标题
     * @param content 内容
     * @param status 状态（0-未完成，1-已完成）
     * @param priority 优先级（0-低，1-中，2-高）
     * @param dueDate 截止日期
     * @param toolContext 工具上下文
     * @return 更新结果
     */
    @Tool(description = "Update a todo item")
    public String updateTodo(
            @ToolParam(description = "Todo ID") Long id,
            @ToolParam(description = "Todo title") String title,
            @ToolParam(description = "Todo content") String content,
            @ToolParam(description = "Status (0-Not completed, 1-Completed)") Integer status,
            @ToolParam(description = "Priority (0-Low, 1-Medium, 2-High)") Integer priority,
            @ToolParam(description = "Due date (yyyy-MM-dd HH:mm:ss)") String dueDate,
            ToolContext toolContext) {
        try {
            Long userId = (Long)toolContext.getContext().get("userId");
            if (userId == null) {
                return "用户未登录";
            }

            // 验证待办事项是否属于当前用户
            TodoItem existingTodo = todoItemService.getById(id);
            if (existingTodo == null || !existingTodo.getUserId().equals(userId)) {
                return "无权操作此待办事项";
            }

            TodoItem todoItem = new TodoItem();
            todoItem.setId(id);
            todoItem.setTitle(title);
            todoItem.setContent(content);
            todoItem.setStatus(status);
            todoItem.setPriority(priority);
            todoItem.setDueDate(DATE_FORMAT.parse(dueDate));
            todoItem.setUpdateTime(new Date());

            boolean success = todoItemService.updateById(todoItem);
            return success ? "待办事项更新成功" : "待办事项更新失败";
        } catch (Exception e) {
            log.error("更新待办事项失败", e);
            return "更新待办事项失败：" + e.getMessage();
        }
    }

    /**
     * 删除待办事项
     * @param id 待办事项ID
     * @param toolContext 工具上下文
     * @return 删除结果
     */
    @Tool(description = "Delete a todo item")
    public String deleteTodo(
            @ToolParam(description = "Todo ID") Long id,
            ToolContext toolContext) {
        try {
            Long userId = (Long)toolContext.getContext().get("userId");
            if (userId == null) {
                return "用户未登录";
            }

            // 验证待办事项是否属于当前用户
            TodoItem existingTodo = todoItemService.getById(id);
            if (existingTodo == null || !existingTodo.getUserId().equals(userId)) {
                return "无权操作此待办事项";
            }

            boolean success = todoItemService.removeById(id);
            return success ? "待办事项删除成功" : "待办事项删除失败";
        } catch (Exception e) {
            log.error("删除待办事项失败", e);
            return "删除待办事项失败：" + e.getMessage();
        }
    }

    /**
     * 获取待办事项列表
     * @param page 页码
     * @param size 每页大小
     * @param status 状态（可选）
     * @param priority 优先级（可选）
     * @param toolContext 工具上下文
     * @return 待办事项列表
     */
    @Tool(description = "Get todo items list")
    public Map<String, Object> getTodoList(
            @ToolParam(description = "Page number") Integer page,
            @ToolParam(description = "Page size") Integer size,
            @ToolParam(description = "Status (0-Not completed, 1-Completed)") Integer status,
            @ToolParam(description = "Priority (0-Low, 1-Medium, 2-High)") Integer priority,
            ToolContext toolContext) {
        try {
            Long userId = (Long)toolContext.getContext().get("userId");
            if (userId == null) {
                return Map.of("error", "用户未登录");
            }

            LambdaQueryWrapper<TodoItem> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TodoItem::getIsDeleted, 0)
                  .eq(TodoItem::getUserId, userId);

            if (status != null) {
                wrapper.eq(TodoItem::getStatus, status);
            }
            if (priority != null) {
                wrapper.eq(TodoItem::getPriority, priority);
            }

            wrapper.orderByDesc(TodoItem::getCreateTime);

            Page<TodoItem> pageResult = todoItemService.page(new Page<>(page, size), wrapper);

            return Map.of(
                "total", pageResult.getTotal(),
                "items", pageResult.getRecords()
            );
        } catch (Exception e) {
            log.error("获取待办事项列表失败", e);
            return Map.of("error", "获取待办事项列表失败：" + e.getMessage());
        }
    }

    /**
     * 更新待办事项状态
     * @param id 待办事项ID
     * @param status 状态（0-未完成，1-已完成）
     * @param toolContext 工具上下文
     * @return 更新结果
     */
    @Tool(description = "Update todo item status")
    public String updateTodoStatus(
            @ToolParam(description = "Todo ID") Long id,
            @ToolParam(description = "Status (0-Not completed, 1-Completed)") Integer status,
            ToolContext toolContext) {
        try {
            Long userId = (Long)toolContext.getContext().get("userId");
            if (userId == null) {
                return "用户未登录";
            }

            // 验证待办事项是否属于当前用户
            TodoItem existingTodo = todoItemService.getById(id);
            if (existingTodo == null || !existingTodo.getUserId().equals(userId)) {
                return "无权操作此待办事项";
            }

            LambdaUpdateWrapper<TodoItem> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(TodoItem::getId, id)
                  .set(TodoItem::getStatus, status);

            boolean success = todoItemService.update(wrapper);
            return success ? "待办事项状态更新成功" : "待办事项状态更新失败";
        } catch (Exception e) {
            log.error("更新待办事项状态失败", e);
            return "更新待办事项状态失败：" + e.getMessage();
        }
    }
}
