package com.qcdfz.fengaiagent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qcdfz.fengaiagent.model.entity.ChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @version v1.0.0
 * @belongsProject: feng-ai-agent
 * @belongsPackage: com.qcdfz.fengaiagent.chatmemory.mapper
 * @author: fgh
 * @description: 聊天消息Mapper接口
 * @createTime: 2024-04-30 10:00
 */
public interface ChatMessageMapper extends BaseMapper<ChatMessage> {


}
