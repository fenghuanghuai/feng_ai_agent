package com.qcdfz.fengaiagent.controller;

import com.qcdfz.fengaiagent.app.LoveApp;
import com.qcdfz.fengaiagent.common.BaseResponse;
import com.qcdfz.fengaiagent.common.ResultUtils;
import com.qcdfz.fengaiagent.model.entity.ChatMessage;
import com.qcdfz.fengaiagent.service.ChatMessageService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * @version v1.0.0
 * @belongsProject: feng-ai-agent
 * @belongsPackage: com.qcdfz.fengaiagent.controller
 * @author: fgh
 * @description: 恋爱助手控制器
 * @createTime: 2025-04-30 21:27
 */
@RestController
@RequestMapping("loveApp")
public class LoveAppController {
    @Resource
    private LoveApp loveApp;

    @Resource
    private ChatMessageService chatMessageService;

    @GetMapping("/doChat")
    public BaseResponse<String> doChat(@RequestParam(required = false) String chatId,
                                     @RequestParam String message) {
        if (chatId == null) {
            chatId = UUID.randomUUID().toString();
        }
        // 第一轮
        String answer ;
        answer =  loveApp.doChat(message, chatId);
        return ResultUtils.success(answer);
    }

    /**
     * 获取对话历史记录
     *
     * @param chatId 会话ID
     * @param limit 获取最近的消息数量，默认10条
     * @return 对话记录列表
     */
    @GetMapping("/chatHistory")
    public BaseResponse<List<ChatMessage>> getChatHistory(@RequestParam String chatId,
                                                        @RequestParam(defaultValue = "10") Integer limit) {
        if (limit <= 0 || limit > 100) {
            limit = 10;
        }
        List<ChatMessage> messages = chatMessageService.findLatestMessages(chatId, limit);
        return ResultUtils.success(messages);
    }
    @GetMapping("/doRagChat")
    public BaseResponse<String> doRagChat(@RequestParam(required = false) String chatId,
                                       @RequestParam String message) {
        if (chatId == null) {
            chatId = UUID.randomUUID().toString();
        }
        // 第一轮
        String answer ;
        answer =  loveApp.doChatWithRag(message, chatId);
        return ResultUtils.success(answer);
    }
}
