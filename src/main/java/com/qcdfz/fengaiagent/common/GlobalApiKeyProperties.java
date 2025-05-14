package com.qcdfz.fengaiagent.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JDos
 * 全局API密钥配置，支持自动注入和IDE提示
 */
@Data
@Component
@ConfigurationProperties(prefix = "api-key")
public class GlobalApiKeyProperties {
    private String dashscope;
    private String volcengine;
    private String github;
    private Tencent tencent = new Tencent();

    @Data
    public static class Tencent {
        private String secretId;
        private String secretKey;
    }
}