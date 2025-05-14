package com.qcdfz.fengaiagent.advisor;

import com.qcdfz.fengaiagent.common.ErrorCode;
import com.qcdfz.fengaiagent.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.api.*;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import reactor.core.publisher.Flux;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @version v1.0.0
 * @belongsProject: feng-ai-agent
 * @belongsPackage: com.qcdfz.fengaiagent.advisor
 * @author: fgh
 * @description: 违禁词校验Advisor（自包含实现，文件加载+Trie树）
 * @createTime: 2024-05-03 12:00
 */
@Slf4j
public class SensitiveWordAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {

    // Trie树实现
    private static class TrieNode {
        boolean isEnd = false;
        Map<Character, TrieNode> children = new HashMap<>();
    }

    private final TrieNode root = new TrieNode();

    // 启动时加载敏感词文件并构建Trie树
    public SensitiveWordAdvisor() {
        List<String> words = loadSensitiveWords("sensitive/sensitive-words.txt");
        buildTrie(words);
    }

    /**
     * 加载敏感词文件
     */
    private List<String> loadSensitiveWords(String path) {
        List<String> words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(path)),
                StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    words.add(line.trim());
                }
            }
        } catch (Exception e) {
            log.error("加载敏感词文件失败: {}", path, e);
            throw new RuntimeException("加载敏感词文件失败: " + path, e);
        }
        return words;
    }
    /**
     * 检查用户输入是否包含违禁词
     */
    private AdvisedRequest before(AdvisedRequest request) {
        String userText = request.userText();
        String hit = checkSensitive(userText);
        if (hit != null) {
            log.warn("检测到违禁词：{}，用户输入：{}", hit, userText);
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "提示词中包含违禁词：" + hit + "，请修改后重试");
        }
        return request;
    }

    @Override
    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
        try {
            advisedRequest = this.before(advisedRequest);
        } catch (BusinessException e) {
            return buildSensitiveWordResponse(e.getMessage(), advisedRequest);
        }
        return chain.nextAroundCall(advisedRequest);
    }

    @Override
    public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {
        try {
            advisedRequest = this.before(advisedRequest);
        } catch (BusinessException e) {
            return Flux.just(buildSensitiveWordResponse(e.getMessage(), advisedRequest));
        }
        return chain.nextAroundStream(advisedRequest);
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int getOrder() {
        return -256;
    }

    /**
     * 构建违禁词提示响应
     */
    private AdvisedResponse buildSensitiveWordResponse(String systemText, AdvisedRequest request) {
        AssistantMessage assistantMessage = new AssistantMessage(systemText);
        Generation generation = new Generation(assistantMessage);
        ChatResponse chatResponse = new ChatResponse(List.of(generation));
        Map<String, Object> context = new HashMap<>();
        context.put("sensitiveWordDetected", true);
        context.put("originalRequest", request);
        context.putAll(request.adviseContext());
        return AdvisedResponse.builder()
                .adviseContext(context)
                .response(chatResponse)
                .build();
    }
    /**
     * 检查文本是否包含敏感词
     */
    private String checkSensitive(String text) {
        for (int i = 0; i < text.length(); i++) {
            TrieNode node = root;
            int j = i;
            while (j < text.length() && node.children.containsKey(text.charAt(j))) {
                node = node.children.get(text.charAt(j));
                if (node.isEnd) {
                    return text.substring(i, j + 1);
                }
                j++;
            }
        }
        return null;
    }
    /**
     * 构建Trie树
     */
    private void buildTrie(List<String> words) {
        for (String word : words) {
            TrieNode node = root;
            for (char c : word.toCharArray()) {
                node = node.children.computeIfAbsent(c, k -> new TrieNode());
            }
            node.isEnd = true;
        }
    }
}
