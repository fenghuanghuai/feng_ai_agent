package com.qcdfz.fengaiagent.rag;

import com.qcdfz.fengaiagent.common.GlobalApiKeyProperties;
import com.qcdfz.fengaiagent.rag.load.GitHubDocumentLoader;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@SpringBootTest
class GitHubDocumentLoaderTest {
    @Resource
    private GlobalApiKeyProperties globalApiKeyProperties;

    @Test
    public void test() throws IOException {

        String token = globalApiKeyProperties.getGithub();

        // 推荐用GitHubBuilder
        GitHub github = new GitHubBuilder().withOAuthToken(token).build();
        GitHubDocumentLoader loader = GitHubDocumentLoader.builder()
                .gitHub(github)
                .owner("fenghuanghuai")
                .repo("hell-world")
                .branch("main")
                .build();

        // 读取文件
        Document doc = loader.loadDocument("/test/test.md");

        // 读取目录
        List<Document> docs = loader.loadDocuments("/");

        // 获取仓库信息
        Map<String, Object> repoInfo = loader.getRepositoryInfo();
        Assertions.assertNotNull(repoInfo);

    }

}
