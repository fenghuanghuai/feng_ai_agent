package com.qcdfz.fengaiagent.rag.store;

import com.qcdfz.fengaiagent.rag.extract.MyKeywordEnricher;
import com.qcdfz.fengaiagent.rag.load.LoveAppDocumentLoader;
import com.qcdfz.fengaiagent.rag.load.LoveCandidateDocumentLoader;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @version v1.0.0
 * @belongsProject: feng-ai-agent
 * @belongsPackage: com.qcdfz.fengaiagent.rag
 * @author: fgh
 * @description: 初始化向量数据库
 * @createTime: 2025-05-04 21:04
 */
//@Configuration
@Slf4j
public class LoveAppVectorStoreConfig {
    @Resource
    private LoveAppDocumentLoader loveAppDocumentLoader;
    @Resource
    private LoveCandidateDocumentLoader loveCandidateDocumentLoader;
    @Resource
    private MyKeywordEnricher myKeywordEnricher;

    @Bean
    VectorStore loveAppVectorStore(EmbeddingModel dashscopeEmbeddingModel){
        log.info("开始创建恋爱知识库向量存储...");
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(dashscopeEmbeddingModel)
                .build();
        List<Document> documents = loveAppDocumentLoader.loadMarkdowns();
        // 自主切分
//        List<Document> splitDocuments = myTokenTextSplitter.splitCustomized(documents);
        // 自动补充关键词元信息
//        List<Document> enrichDocuments = myKeywordEnricher.enrichDocuments(documents);
        log.info("恋爱知识库加载了 {} 个文档", documents.size());
        simpleVectorStore.add(documents);
        return simpleVectorStore;
    }

    @Bean
    VectorStore loveAppCandidateVectorStore(EmbeddingModel dashscopeEmbeddingModel){
        log.info("开始创建恋爱对象向量存储...");
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(dashscopeEmbeddingModel)
                .build();
        try {
            List<Document> documents = loveCandidateDocumentLoader.loadCandidateDocuments();
            if (documents == null) {
                throw new IllegalStateException("恋爱对象文档列表为null");
            }
            if (documents.isEmpty()) {
                throw new IllegalStateException("恋爱对象文档列表为空，请检查文档加载配置");
            }
            simpleVectorStore.add(documents);
            log.info("恋爱对象向量存储创建成功，共添加了 {} 个文档", documents.size());
            return simpleVectorStore;
        } catch (Exception e) {
            log.error("创建恋爱对象向量存储失败", e);
            throw new IllegalStateException("创建恋爱对象向量存储失败: " + e.getMessage(), e);
        }
    }
}
