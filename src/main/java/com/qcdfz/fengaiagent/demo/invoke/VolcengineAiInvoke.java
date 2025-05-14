package com.qcdfz.fengaiagent.demo.invoke;


import cn.hutool.core.collection.CollUtil;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionChoice;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;

import java.util.ArrayList;
import java.util.List;

/**
 * VolcengineAiInvoke 类用于演示如何使用 Volcengine 的 AI 服务进行交互
 */
public class VolcengineAiInvoke {

    /**
     * 主函数执行标准请求和流式请求示例
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 使用测试 API Key 初始化
        String apiKey = TestApiKey.VOLCENGINE_KEY;

        // 构建 ArkService 实例
        ArkService arkService = ArkService
                .builder()
                .apiKey(apiKey)
                .build();

        // 打印标准请求的开始
        System.out.println("\n----- standard request -----");

        // 初始化消息列表
        final List<ChatMessage> messages = new ArrayList<>();

        // 构建系统消息
        ChatMessage systemMessage = ChatMessage.builder()
                .role(ChatMessageRole.SYSTEM)
                .content("你是豆包，是由字节跳动开发的 AI 人工智能助手")
                .build();

        // 构建用户消息
        ChatMessage userMessage = ChatMessage.builder()
                .role(ChatMessageRole.USER)
                .content("常见的十字花科植物有哪些？")
                .build();

        // 将消息添加到列表中
        messages.add(systemMessage);
        messages.add(userMessage);

        // 构建聊天完成请求
        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .model("deepseek-v3-241226")
                .messages(messages)
                .build();

        // 执行单轮请求
        extracted(arkService, completionRequest);

        // 执行流式请求
        streamChat(arkService, completionRequest);

        // 关闭执行器服务
        arkService.shutdownExecutor();
    }

    /**
     * 执行流式聊天请求
     * @param arkService AI 服务实例
     * @param completionRequest 聊天完成请求
     */
    private static void streamChat(ArkService arkService, ChatCompletionRequest completionRequest) {
        // 执行流式聊天请求并处理响应
        arkService.streamChatCompletion(completionRequest)
                .doOnError(Throwable::printStackTrace)
                .blockingForEach(
                        choice -> {
                            if (choice.getChoices().size() > 0) {
                                System.out.print(choice.getChoices().get(0).getMessage().getContent());
                            }
                        }
                );
    }

    /**
     * 执行单轮聊天请求
     * @param arkService AI 服务实例
     * @param completionRequest 聊天完成请求
     */
    private static void extracted(ArkService arkService, ChatCompletionRequest completionRequest) {
        // 获取聊天完成响应中的选择列表
        List<ChatCompletionChoice> choiceList = arkService.createChatCompletion(completionRequest).getChoices();

        // 如果响应为空，则抛出异常
        if (CollUtil.isEmpty(choiceList)) {
            throw new RuntimeException("AI 没有返回任何内容");
        }

        // 获取并打印 AI 返回的内容
        String content = (String) choiceList.get(0).getMessage().getContent();
        System.out.println("AI 返回内容：" + content);
    }
}

