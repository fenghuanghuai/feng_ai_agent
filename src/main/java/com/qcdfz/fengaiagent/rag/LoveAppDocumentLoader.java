package com.qcdfz.fengaiagent.rag;

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
 * @description: 文档加载器类
 * @createTime: 2025-05-04 11:17
 */
@Component
@Slf4j
public class LoveAppDocumentLoader {

    private final ResourcePatternResolver resourcePatternResolver;

    public LoveAppDocumentLoader (ResourcePatternResolver resourcePatternResolver){
        this.resourcePatternResolver = resourcePatternResolver;
    }
    public List<Document> loadMarkdowns(){
        List<Document> allDocument = new ArrayList<>();
             try {
                 Resource[] resources = resourcePatternResolver.getResources("classpath:document/*.md");
                 for (Resource resource : resources){
                     String fileName = resource.getFilename();
                     // 提取文档倒数第 3 和第 2 个字作为标签
                     String status = fileName.substring(fileName.length() - 6, fileName.length() - 4);
                     MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder()
                             .withHorizontalRuleCreateDocument(true)
                             .withIncludeCodeBlock(false)
                             .withIncludeBlockquote(false)
                             .withAdditionalMetadata("filename",fileName)
                             .withAdditionalMetadata("status",status)
                             .build();
                     MarkdownDocumentReader reader = new MarkdownDocumentReader(resource, config);
                     allDocument.addAll(reader.get());
                 }
             } catch (IOException e) {
            log.error("Markdown 文档加载失败",e);
        }
        return allDocument;
    }
}
