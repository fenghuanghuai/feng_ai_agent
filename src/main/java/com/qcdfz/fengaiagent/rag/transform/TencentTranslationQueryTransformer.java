package com.qcdfz.fengaiagent.rag.transform;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.tmt.v20180321.TmtClient;
import com.tencentcloudapi.tmt.v20180321.models.TextTranslateRequest;
import com.tencentcloudapi.tmt.v20180321.models.TextTranslateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
/**
 * @version v1.0.0
 * @belongsProject: feng-ai-agent
 * @belongsPackage: com.qcdfz.fengaiagent.rag
 * @author: fgh
 * @description: 查询翻译转换器，自动将查询内容翻译为目标语言
 * @createTime: 2025-05-13 11:17
 */
@Slf4j
public class TencentTranslationQueryTransformer {

    private final String secretId;
    private final String secretKey;
    private final String region;
    private final String sourceLang;
    private final String targetLang;

    private TencentTranslationQueryTransformer(Builder builder) {
        Assert.notNull(builder.secretId,"SecretId 不能为空");
        Assert.notNull(builder.secretId,"secretKey 不能为空");
        Assert.notNull(builder.targetLang,"目标语言不能为空");
        this.secretId = builder.secretId;
        this.secretKey = builder.secretKey;
        this.region = builder.region;
        this.sourceLang = builder.sourceLang;
        this.targetLang = builder.targetLang;
    }

    public static Builder builder() {
        return new Builder();
    }
    /**
     * Builder模式
     */
    public static final class Builder {
        private String secretId;
        private String secretKey;
        private String region = "ap-guangzhou" ;
        private String sourceLang = "auto";
        private String targetLang;

        public Builder secretId(String secretId) {
            this.secretId = secretId;
            return this;
        }

        public Builder secretKey(String secretKey) {
            this.secretKey = secretKey;
            return this;
        }

        public Builder region(String region) {
            this.region = region;
            return this;
        }

        public Builder sourceLang(String sourceLang) {
            this.sourceLang = sourceLang;
            return this;
        }

        public Builder targetLang(String targetLang) {
            this.targetLang = targetLang;
            return this;
        }

        public TencentTranslationQueryTransformer build() {
            return new TencentTranslationQueryTransformer(this);
        }
    }

    /**
     * 翻译查询内容
     * @param query 原始查询
     * @return 翻译后的查询
     */
    public String transform(String query) {
        if (!StringUtils.hasText(query)) {
            return query;
        }
        try {
            Credential cred = new Credential(secretId, secretKey);
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("tmt.tencentcloudapi.com");
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            TmtClient client = new TmtClient(cred, region, clientProfile);

            TextTranslateRequest req = new TextTranslateRequest();
            req.setSourceText(query);
            req.setSource(sourceLang);
            req.setTarget(targetLang);
            req.setProjectId(0L);

            TextTranslateResponse resp = client.TextTranslate(req);
            return resp.getTargetText();
        } catch (TencentCloudSDKException e) {
            log.error("调用腾讯云翻译API失败: {}", e.getMessage(), e);
            // 失败时返回原文
            return query;
        }
    }
}
