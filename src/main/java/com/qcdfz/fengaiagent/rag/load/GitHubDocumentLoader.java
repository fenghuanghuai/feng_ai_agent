package com.qcdfz.fengaiagent.rag.load;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.springframework.ai.document.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @version v1.0.0
 * @belongsProject: feng-ai-agent
 * @belongsPackage: com.qcdfz.fengaiagent.rag
 * @author: fgh
 * @description: GitHub文档加载器 用于加载GitHub仓库中的文档内容
 * @createTime: 2025-05-12 16:53
 */
@Slf4j
public class GitHubDocumentLoader {

    private final GitHub gitHub;
    private final String owner;
    private final String repo;
    private final String branch;

    /**
     * 构造函数
     *
     * @param gitHub GitHub客户端实例
     * @param owner 仓库所有者
     * @param repo 仓库名称
     * @param branch 分支名称
     */
    public GitHubDocumentLoader(GitHub gitHub, String owner, String repo, String branch) {
        Assert.notNull(gitHub, "GitHub实例不能为空");
        Assert.notNull(owner, "仓库所有者不能为空");
        Assert.notNull(repo, "仓库名称不能为空");
        this.gitHub = gitHub;
        this.owner = owner;
        this.repo = repo;
        this.branch = branch != null ? branch : "main";
    }

    /**
     * 加载单个文件内容
     *
     * @param path 文件路径
     * @return Document对象
     */
    public Document loadDocument(String path) {
        try {
            GHContent content = getRepository().getFileContent(path, branch);
            Assert.isTrue(content.isFile(), "路径必须指向文件");
            return createDocument(content);
        } catch (IOException e) {
            log.error("从GitHub加载文档失败: {}", path, e);
            throw new RuntimeException("从GitHub加载文档失败: " + path, e);
        }
    }

    /**
     * 加载目录下的所有文件
     *
     * @param path 目录路径
     * @return Document列表
     */
    public List<Document> loadDocuments(String path) {
        List<Document> documents = new ArrayList<>();
        try {
            List<GHContent> contents = getRepository().getDirectoryContent(path, branch);
            for (GHContent content : contents) {
                if (content.isFile()) {
                    documents.add(createDocument(content));
                } else if (content.isDirectory()) {
                    documents.addAll(loadDocuments(content.getPath()));
                }
            }
        } catch (IOException e) {
            log.error("从GitHub加载文档失败: {}", path, e);
            throw new RuntimeException("从GitHub加载文档失败: " + path, e);
        }
        return documents;
    }

    /**
     * 获取仓库信息
     *
     * @return 仓库信息Map
     */
    public Map<String, Object> getRepositoryInfo() {
        try {
            GHRepository repository = getRepository();
            return Map.of(
                    "name", Objects.toString(repository.getName(), ""),
                    "description", Objects.toString(repository.getDescription(), ""),
                    "stars", repository.getStargazersCount(),
                    "forks", repository.getForksCount(),
                    "language", Objects.toString(repository.getLanguage(), ""),
                    "defaultBranch", Objects.toString(repository.getDefaultBranch(), ""),
                    "htmlUrl", repository.getHtmlUrl() != null ? repository.getHtmlUrl().toString() : "",
                    "cloneUrl", Objects.toString(repository.getHttpTransportUrl(), "")
            );
        } catch (IOException e) {
            log.error("获取仓库信息失败: {}/{}", owner, repo, e);
            throw new RuntimeException("获取仓库信息失败: " + owner + "/" + repo, e);
        }
    }

    /**
     * 获取仓库对象
     *
     * @return GHRepository对象
     * @throws IOException 如果获取失败
     */
    private GHRepository getRepository() throws IOException {
        return gitHub.getRepository(owner + "/" + repo);
    }

    /**
     * 创建Document对象
     *
     * @param content GitHub内容对象
     * @return Document对象
     * @throws IOException 如果读取失败
     */
    private Document createDocument(GHContent content) throws IOException {
        String text = content.getContent();
        Map<String, Object> metadata = Map.of(
            "github_git_url", content.getGitUrl(),
            "github_download_url", content.getDownloadUrl(),
            "github_html_url", content.getHtmlUrl(),
            "github_url", content.getUrl(),
            "github_file_name", content.getName(),
            "github_file_path", content.getPath(),
            "github_file_sha", content.getSha(),
            "github_file_size", Long.toString(content.getSize()),
            "github_file_encoding", content.getEncoding()
        );
        return new Document(text, metadata);
    }


    /**
     * 创建Builder实例
     *
     * @return Builder实例
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 构建器类
     */
    public static class Builder {
        private GitHub gitHub;
        private String owner;
        private String repo;
        private String branch;

        /**
         * 设置GitHub客户端实例
         *
         * @param gitHub GitHub客户端实例
         * @return Builder实例
         */
        public Builder gitHub(GitHub gitHub) {
            this.gitHub = gitHub;
            return this;
        }

        /**
         * 设置仓库所有者
         *
         * @param owner 仓库所有者
         * @return Builder实例
         */
        public Builder owner(String owner) {
            this.owner = owner;
            return this;
        }

        /**
         * 设置仓库名称
         *
         * @param repo 仓库名称
         * @return Builder实例
         */
        public Builder repo(String repo) {
            this.repo = repo;
            return this;
        }

        /**
         * 设置分支名称
         *
         * @param branch 分支名称
         * @return Builder实例
         */
        public Builder branch(String branch) {
            this.branch = branch;
            return this;
        }

        /**
         * 构建GitHubDocumentLoader实例
         *
         * @return GitHubDocumentLoader实例
         */
        public GitHubDocumentLoader build() {
            return new GitHubDocumentLoader(gitHub, owner, repo, branch);
        }
    }
}
