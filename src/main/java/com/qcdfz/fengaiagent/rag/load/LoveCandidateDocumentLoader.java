package com.qcdfz.fengaiagent.rag.load;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @version v1.0.0
 * @belongsProject: feng-ai-agent
 * @belongsPackage: com.qcdfz.fengaiagent.rag
 * @author: fgh
 * @description: 恋爱对象知识库文档加载器
 * @createTime: 2025-05-04 12:00
 */
@Component
@Slf4j
public class LoveCandidateDocumentLoader {

    private final ResourcePatternResolver resourcePatternResolver;

    public LoveCandidateDocumentLoader(ResourcePatternResolver resourcePatternResolver) {
        this.resourcePatternResolver = resourcePatternResolver;
    }

    /**
     * 加载resources/document/目录下所有.md文件为Document对象
     * @return 恋爱对象文档列表
     */
    public List<Document> loadCandidateDocuments() {
        List<Document> allDocuments = new ArrayList<>();
        try {
            Resource[] resources = resourcePatternResolver.getResources("classpath:document/love_candidates.md");
            if (resources.length == 0) {
                return allDocuments;
            }
            for (Resource resource : resources) {
                String fileName = resource.getFilename();
                MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder()
                        .withHorizontalRuleCreateDocument(true)
                        .withIncludeCodeBlock(false)
                        .withIncludeBlockquote(false)
                        .withAdditionalMetadata("filename", fileName)
                        .build();
                MarkdownDocumentReader reader = new MarkdownDocumentReader(resource, config);
                allDocuments.addAll(reader.get());
            }
        } catch (IOException e) {
            log.error("恋爱对象知识库文档加载失败", e);
        }
        return allDocuments;
    }
}
