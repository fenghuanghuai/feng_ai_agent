package com.qcdfz.fengaiagent.rag.extract;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.KeywordMetadataEnricher;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @version v1.0.0
 * @belongsProject: feng-ai-agent
 * @belongsPackage: com.qcdfz.fengaiagent.rag
 * @author: fgh
 * @description: 基于 AI 的文档元信息增强器（为文档补充元信息）
 * @createTime: 2025-05-11 21:16
 */
@Component
public class MyKeywordEnricher {
    @Resource
    private ChatModel dashscopeChatModel;

    public List<Document> enrichDocuments(List<Document> documents){
        KeywordMetadataEnricher keywordMetadataEnricher = new KeywordMetadataEnricher(dashscopeChatModel, 5);
        return keywordMetadataEnricher.apply(documents);
    }
}
