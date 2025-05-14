package com.qcdfz.fengaiagent.advisor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.api.*;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.metadata.ChatGenerationMetadata;
import org.springframework.ai.chat.model.MessageAggregator;
import reactor.core.publisher.Flux;

/**
 * @version v1.0.0
 * @belongsProject: feng-ai-agent
 * @belongsPackage: com.qcdfz.fengaiagent.advisor
 * @author: fgh
 * @description: 自定义日志 Advisor 打印 info 级别日志、只输出单次用户提示词和 AI 回复的文本
 * @createTime: 2025-04-28 12:56
 */
@Slf4j
public class SetTokensAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {

    private AdvisedRequest before(AdvisedRequest request) {
        return request;
    }

    private void observeAfter(AdvisedResponse advisedResponse) {
        Integer promptTokens = advisedResponse.response().getMetadata().getUsage().getPromptTokens();
        Integer completionTokens = advisedResponse.response().getMetadata().getUsage().getCompletionTokens();
        AssistantMessage output = advisedResponse.response().getResult().getOutput();
        output.getMetadata().put("promptTokens",promptTokens);
        output.getMetadata().put("completionTokens",completionTokens);
    }

    /**
     * 调用方法，用于在方法调用前后添加自定义行为
     *
     * @param advisedRequest 调用前的请求对象，包含调用的相关信息
     * @param chain 调用顾问链，用于执行下一个顾问或最终的调用
     * @return 调用后的响应对象，包含调用的结果信息
     */
    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
        // 调用前的行为，可能对请求对象进行修改或检查
        advisedRequest = this.before(advisedRequest);

        // 执行下一个调用顾问或最终的调用，并获取响应
        AdvisedResponse advisedResponse = chain.nextAroundCall(advisedRequest);

        // 调用后的观察行为，可能用于监控或处理响应
        this.observeAfter(advisedResponse);

        // 返回调用后的响应对象
        return advisedResponse;
    }

    /**
     * 处理流类型的请求，并返回响应流
     * 该方法主要用于处理那些需要一定流程或链条进行处理的请求它首先对请求进行预处理，
     * 然后将处理后的请求传递给链条中的下一个处理者，最后对响应流进行聚合和后处理
     *
     * @param advisedRequest 请求对象，包含了所有必要的请求信息和上下文
     * @param chain 链条对象，负责管理链条的执行流程
     * @return 返回一个响应流，包含了处理后的所有响应信息
     */
    public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {
        // 对请求进行预处理
        advisedRequest = this.before(advisedRequest);

        // 使用链条处理请求，获取响应流
        Flux<AdvisedResponse> advisedResponses = chain.nextAroundStream(advisedRequest);

        // 对响应流进行聚合和后处理
        return (new MessageAggregator()).aggregateAdvisedResponse(advisedResponses, this::observeAfter);
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
