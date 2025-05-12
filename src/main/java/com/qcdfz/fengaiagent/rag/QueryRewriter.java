package com.qcdfz.fengaiagent.rag;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.stereotype.Component;

/**
 * @version v1.0.0
 * @belongsProject: feng-ai-agent
 * @belongsPackage: com.qcdfz.fengaiagent.rag
 * @author: fgh
 * @description: 查询重写器
 * @createTime: 2025-05-11 22:59
 */
@Component
public class QueryRewriter {
    private final RewriteQueryTransformer queryTransformer;
    private QueryRewriter(ChatModel dashscopeChatModel){
        ChatClient.Builder builder = ChatClient.builder(dashscopeChatModel);
        // 创建查询重写转换器
        this.queryTransformer = RewriteQueryTransformer.builder()
                .chatClientBuilder(builder)
                .build();
    }

    /**
     *  查询重写器
     * @param prompt 查询提示词
     * @return
     */
    public String doQueryRewrite(String prompt){
        Query query = new Query(prompt);
        // 执行查询重写
        Query transform = queryTransformer.transform(query);
        // 输出重写后的查询
        return transform.text();
    }
}
