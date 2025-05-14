package com.qcdfz.fengaiagent.rag.store;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.BatchingStrategy;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @version v1.0.0
 * @belongsProject: feng-ai-agent
 * @belongsPackage: com.qcdfz.fengaiagent.rag
 * @author: fgh
 * @description:
 * @createTime: 2025-05-10 10:59
 */
@Configuration
public class EmbeddingConfig implements BatchingStrategy {

    @Override
    public List<List<Document>> batch(List<Document> documents) {
        List<List<Document>> batches = new ArrayList<>();
        int batchSize = 25; // DashScope 接口限制为 25 条/次
        for (int i = 0; i < documents.size(); i += batchSize) {
            int end = Math.min(i + batchSize, documents.size());
            batches.add(documents.subList(i, end));
        }
        return batches;
    }
}
