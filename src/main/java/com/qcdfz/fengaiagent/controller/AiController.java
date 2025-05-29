package com.qcdfz.fengaiagent.controller;


import com.qcdfz.fengaiagent.agent.FengManus;
import com.qcdfz.fengaiagent.app.LoveApp;
import com.qcdfz.fengaiagent.utils.SseEmitterUtils;
import io.reactivex.Emitter;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.annotation.Resource;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;

/**
 * @version v1.0.0
 * @belongsProject: feng-ai-agent
 * @belongsPackage: com.qcdfz.fengaiagent.controller
 * @author: fgh
 * @description: 支持http接口调用智能体
 * @createTime: 2025-05-27 13:19
 */
@RestController
@RequestMapping("/ai")
public class AiController {

    @Resource
    private LoveApp loveApp;
    @Resource
    private ToolCallback[] allTools;
    @Resource
    private ChatModel dashscopeChatModel;
    @Resource
    private ToolCallbackProvider toolCallbackProvider;


    /**
     * 处理与Love应用的聊天同步请求
     *
     * @param message 用户发送的消息内容
     * @param chatId  聊天的唯一标识符
     * @return 返回处理后的聊天消息
     */
    @GetMapping("/love_app_chat/sync")
    public String doChatWithLoveAppSync(String message, String chatId) {
        return loveApp.doChat(message, chatId);
    }

    /**
     * 使用Server-Sent Events (SSE)处理与Love App的聊天功能
     * 此方法通过GET请求接收消息和聊天ID，并以文本事件流的形式返回聊天响应
     *
     * @param message 用户发送的消息
     * @param chatId  聊天的唯一标识符
     * @return 返回一个Flux响应式对象
     */
    @GetMapping(value = "/love_app_chat/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> doChatWithLoveAppSSE(String message, String chatId) {
        return loveApp.doChatWithStream(message, chatId);
    }

    /**
     * 使用Server-Sent Events (SSE)处理与Love App的聊天功能
     * 此方法通过GET请求提供一个持续更新的数据流，用于实现服务器向客户端推送消息
     *
     * @param message 用户发送的消息内容
     * @param chatId  聊天的唯一标识符，用于区分不同的聊天会话
     * @return 返回一个Flux流，包含String类型的数据，用于实时更新聊天内容
     */
    @GetMapping(value = "/love_app_chat/server_sent_event", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> doChatWithLoveAppServerSentEvent(String message, String chatId) {
        // 调用loveApp的流式聊天方法，处理用户消息并生成响应流
        return loveApp.doChatWithStream(message, chatId)
                .map(chunk -> ServerSentEvent.<String>builder()
                        .data(chunk)
                        .build());
    }

    @GetMapping(value = "/love_app_chat/sse_emitter", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter doChatWithLoveAppSSEEmitter(String message, String chatId) {
        // 创建一个超时时间较长的 SseEmitter
        SseEmitter emitter = new SseEmitter(180000L); // 3分钟超时
        // 获取 Flux 数据流并直接订阅
        loveApp.doChatWithStream(message, chatId)
                .subscribe(chunk -> {
                            try {
                                emitter.send(chunk);
                            } catch (IOException e) {
                                emitter.completeWithError(e);
                            }
                        },
                        // 处理错误
                        emitter::completeWithError,
                        // 处理完成
                        ()->SseEmitterUtils.completeWithDone(emitter));
        return emitter;
    }

    /**
     * 流式调用 Manus 超级智能体
     *
     * @param message
     * @return
     */
    @GetMapping("/manus/chat")
    public SseEmitter doChatWithManus(String message) {
        FengManus fengManus = new FengManus(allTools, toolCallbackProvider, dashscopeChatModel);
        return fengManus.runStream(message);
    }

}
