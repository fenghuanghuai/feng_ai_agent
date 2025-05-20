package com.qcdfz.fengaiagent.app;

import com.qcdfz.fengaiagent.advisor.MyLoggerAdvisor;
import com.qcdfz.fengaiagent.chatmemory.MySQLChatMemory;
import com.qcdfz.fengaiagent.model.entity.User;
import com.qcdfz.fengaiagent.rag.transform.QueryRewriter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

/**
 * @version v1.0.0
 * @belongsProject: feng-ai-agent
 * @belongsPackage: com.qcdfz.fengaiagent.app
 * @author: fgh
 * @description: 恋爱助手
 * @createTime: 2025-04-27 21:07
 */
@Component
@Slf4j
public class LoveApp {

    private final ChatClient chatClient;

    private static final String SYSTEM_PROMPT = "扮演深耕恋爱心理领域的专家。开场向用户表明身份，告知用户可倾诉恋爱难题。" +
            "围绕单身、恋爱、已婚三种状态提问：单身状态询问社交圈拓展及追求心仪对象的困扰；" +
            "恋爱状态询问沟通、习惯差异引发的矛盾；已婚状态询问家庭责任与亲属关系处理的问题。" +
            "引导用户详述事情经过、对方反应及自身想法，以便给出专属解决方案。";

//    private static final String SYSTEM_PROMPT = "你是一名游戏搭子";

    public LoveApp(ChatModel dashscopeChatModel, MySQLChatMemory chatMemory) {
        // 初始化基于MySQL的对话记忆
//        String fileDir = System.getProperty("user.dir") + "/chat-memory";
//        ChatMemory chatMemory = new FileBasedChatMemory(fileDir);
//        InMemoryChatMemory chatMemory = new InMemoryChatMemory();
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory)
                        // 违禁词校验
//                        , new SensitiveWordAdvisor()
                        // 自定义日志 Advisor，可按需开启
                        , new MyLoggerAdvisor()
                        // 设置token用量
//                        , new SetTokensAdvisor()
                        // 自定义推理增强 Advisor，可按需开启
//                        , new ReReadingAdvisor()
                )
                .build();
    }

    /**
     * 执行聊天请求
     * <p>
     * 该方法用于向指定的聊天顾问发送消息，并获取回复该方法首先构建了一个聊天响应对象，
     * 其中包含了与特定聊天会话相关的信息和设置，然后调用聊天API，并处理返回的响应
     *
     * @param message 用户输入的消息，用于与聊天顾问进行交互
     * @param chatId  聊天会话的标识符，用于检索和维护聊天上下文
     * @return 返回聊天顾问的回复文本
     */
    public String doChat(String message, String chatId) {
        // 构建聊天响应对象，通过链式调用设置各项参数
        ChatResponse chatResponse = chatClient
                .prompt() // 开始一个新的聊天提示构建
                .user(message) // 设置用户输入的消息
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId) // 设置聊天会话ID参数
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10)) // 设置聊天记忆检索大小参数
                .call() // 调用聊天API并发送构建好的聊天提示
                .chatResponse(); // 获取聊天响应对象
        // 从聊天响应对象中提取结果中的文本输出
        String content = chatResponse.getResult().getOutput().getText();
        // 记录聊天顾问的回复文本
        log.info("content: {}", content);
        // 返回聊天顾问的回复文本
        return content;
    }

    record LoveReport(String title, List<String> suggestions) {
    }

    /**
     * 使用聊天机器人生成恋爱报告
     * <p>
     * 该方法通过与聊天机器人的对话来生成一份恋爱报告对话由给定的消息开始，
     * 并在对话结束后生成一份恋爱报告，报告内容包括建议列表
     *
     * @param message 用户的消息，用于开始与聊天机器人对话
     * @param chatId  对话的唯一标识符，用于检索对话上下文
     * @return LoveReport 对象，包含用户恋爱报告的标题和建议列表
     */
    public LoveReport doChatWithReport(String message, String chatId) {
        // 开始与聊天机器人的对话，并设置系统提示信息
        // 此处的系统提示包括了每次对话后都要生成恋爱结果的指令
        LoveReport entity = chatClient.prompt()
                .system(SYSTEM_PROMPT + "每次对话后都要生成恋爱结果，标题为{用户名}的恋爱报告，内容为建议列表")
                .user(message)
                // 设置对话顾问参数，包括对话记忆的对话ID和检索大小
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                // 发起对话并获取对话实体
                .call()
                .entity(LoveReport.class);
        // 记录生成的恋爱报告日志
        log.info("loveReport: {}", entity);
        // 返回恋爱报告实体
        return entity;
    }

    @Resource
    private VectorStore loveAppVectorStore;
    @Resource
    private Advisor loveAppRagCloudAdvisor;
    @Resource
    private VectorStore loveAppCandidateVectorStore;
    @Resource
    private VectorStore pgVectorVectorStore;
    @Resource
    private QueryRewriter queryRewriter;

    public String doChatWithRag(String message, String chatId) {
        String rewrite = queryRewriter.doQueryRewrite(message);
        ChatResponse chatResponse = chatClient.prompt()
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .advisors(new MyLoggerAdvisor())
                .user(rewrite)
                // 应用增强检索服务（云知识库服务）
//                .advisors(loveAppRagCloudAdvisor)
                // 应用增强检索服务（本地知识库服务）
//                .advisors(new QuestionAnswerAdvisor(loveAppVectorStore))
                // 对象匹配顾问
//                .advisors(new QuestionAnswerAdvisor(loveAppCandidateVectorStore))
                // pgsql向量数据库查询增强顾问
                .advisors(new QuestionAnswerAdvisor(pgVectorVectorStore))
                // 自定义 检索过滤 增强顾问
//                .advisors(
//                        LoveAppRagCustomAdvisorFactory.createLoveAppRagCustomAdvisor(
//                                pgVectorVectorStore, "已婚"))
                .call()
                .chatResponse();
        // 从聊天响应对象中提取结果中的文本输出
        String content = chatResponse.getResult().getOutput().getText();
        // 记录聊天顾问的回复文本
        log.info("content: {}", content);
        return content;
    }

    @Resource
    private ToolCallback[] allTools;

    public String doChatWithTools(String message, String chatId) {
        // 模拟用户信息
        User user = new User();
        user.setId(1L);
        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                // 开启日志，便于观察效果
                .advisors(new MyLoggerAdvisor())
                .tools(allTools)
                .toolContext(Map.of("userId", user.getId()))
                .call()
                .chatResponse();
        String content = response.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }
}
