package com.qcdfz.fengaiagent.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @version v1.0.0
 * @belongsProject: feng-ai-agent
 * @belongsPackage: com.qcdfz.fengaiagent.model.entity
 * @author: fgh
 * @description: 待办事项实体类
 * @createTime: 2025-05-17 20:49
 */
@Data
@TableName("todo_item")
public class TodoItem {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 待办事项标题
     */
    private String title;

    /**
     * 待办事项内容
     */
    private String content;

    /**
     * 状态：0-未完成，1-已完成
     */
    private Integer status;

    /**
     * 优先级：0-低，1-中，2-高
     */
    private Integer priority;

    /**
     * 截止日期
     */
    private Date dueDate;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 是否删除：0-未删除，1-已删除
     */
    @TableLogic
    private Integer isDeleted;
}
