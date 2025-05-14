package com.qcdfz.fengaiagent.demo.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @version v1.0.0
 * @belongsProject: feng-ai-agent
 * @belongsPackage: com.qcdfz.fengaiagent.demo.rag
 * @author: fgh
 * @description: 多查询扩展
 * @createTime: 2025-05-11 21:51
 */
@Component
public class MultiQueryExpanderDome {

    private final ChatClient.Builder chatClientBuilder;

    public MultiQueryExpanderDome(ChatModel dashscopeChatModel){
         this.chatClientBuilder = ChatClient.builder(dashscopeChatModel);
    }

    public List<Query> expand(){
        MultiQueryExpander queryExpander = MultiQueryExpander.builder()
                .chatClientBuilder(chatClientBuilder)
                .numberOfQueries(3)
                .build();
        return queryExpander.expand(new Query("谁是程序员鱼皮啊？"));
    }
}
