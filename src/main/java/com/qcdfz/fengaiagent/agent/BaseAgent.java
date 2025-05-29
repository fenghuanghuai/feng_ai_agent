package com.qcdfz.fengaiagent.agent;

import cn.hutool.core.util.StrUtil;
import com.qcdfz.fengaiagent.agent.model.AgentState;
import com.qcdfz.fengaiagent.common.ErrorCode;
import com.qcdfz.fengaiagent.exception.BusinessException;
import com.qcdfz.fengaiagent.utils.SseEmitterUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @version v1.0.0
 * @belongsProject: feng-ai-agent
 * @belongsPackage: com.qcdfz.fengaiagent.agent.model
 * @author: fgh
 * @description: 抽象基础代理类，用于管理代理状态和执行流程。
 *               提供状态转换、内存管理和基于步骤的执行循环的基础功能。
 *               子类必须实现step方法。
 * @createTime: 2025-05-25 20:15
 */
@Data
@Slf4j
public abstract class BaseAgent {
    // 核心属性
    private String name;

    // 提示词
    private String systemPrompt;
    private String nextStepPrompt;

    // 状态
    private AgentState state = AgentState.IDLE;

    // 执行控制
    private int maxSteps = 10;
    private int currentStep = 0;

    // LLM
    private ChatClient chatClient;

    // Memory（需要自主维护会话上下文）
    private List<Message> messageList = new ArrayList<>();

    /**
     * 运行代理
     *
     * @param userPrompt 用户提示词
     * @return 执行结果
     */
    public String run(String userPrompt) {
        if (this.state != AgentState.IDLE) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "Cannot run agent from state: " + this.state);
        }
        if (StrUtil.isBlank(userPrompt)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Cannot run agent with empty user prompt");
        }
        // 更改状态
        state = AgentState.RUNNING;
        // 记录消息上下文
        messageList.add(new UserMessage(userPrompt));
        // 保存结果列表
        List<String> results = new ArrayList<>();
        try {
            for (int i = 0; i < maxSteps && state != AgentState.FINISHED; i++) {
                int stepNumber = i + 1;
                currentStep = stepNumber;
                log.info("Executing step {}/{} ", stepNumber,maxSteps);
                // 单步 执行
                String stepResult = step();
                String result = "Step " + stepNumber + ": " + stepResult;
                results.add(result);
            }
            // 检查是否超出步骤限制
            if (currentStep >= maxSteps) {
                state = AgentState.FINISHED;
                results.add("Terminated: Reached max steps (" + maxSteps + ")");
            }
            return String.join("\n", results);
        } catch (Exception e) {
            state = AgentState.ERROR;
            log.error("Error executing agent", e);
            return "执行错误" + e.getMessage();
        } finally {
            // 清理资源
            this.cleanup();
        }
    }

    /**
     * 运行代理(SSE流式输出)
     *
     * @param userPrompt 用户提示词
     * @return 执行结果
     */
    public SseEmitter runStream(String userPrompt) {
        // 创建一个超时时间较长的 SseEmitter
        SseEmitter emitter = new SseEmitter(300000L); // 3分钟超时
        // 使用线程异步处理，避免阻塞主线程
        CompletableFuture.runAsync(()->{

            try {
                if (this.state != AgentState.IDLE) {
                    emitter.send("错误：无法从状态运行代理"+this.state);
    //                throw new BusinessException(ErrorCode.OPERATION_ERROR, "Cannot run agent from state: " + this.state);
                    SseEmitterUtils.completeWithDone(emitter);
                    return;
                }
                if (StrUtil.isBlank(userPrompt)) {
                    emitter.send("错误：不能使用空提示词运行代理");
    //                throw new BusinessException(ErrorCode.PARAMS_ERROR, "Cannot run agent with empty user prompt");
                    SseEmitterUtils.completeWithDone(emitter);
                    return;
                }
            } catch (Exception e) {
                emitter.completeWithError(e);
            }

            // 更改状态
            state = AgentState.RUNNING;
            // 记录消息上下文
            messageList.add(new UserMessage(userPrompt));
            // 保存结果列表
            List<String> results = new ArrayList<>();
            try {
                for (int i = 0; i < maxSteps && state != AgentState.FINISHED; i++) {
                    int stepNumber = i + 1;
                    currentStep = stepNumber;
                    log.info("Executing step {}/{} ", stepNumber,maxSteps);
                    // 单步 执行
                    String stepResult = step();
                    String result = "Step " + stepNumber + ": " + stepResult;
                    results.add(result);
                    // 发送每一步的结果
                    emitter.send(result);
                }
                // 检查是否超出步骤限制
                if (currentStep >= maxSteps) {
                    state = AgentState.FINISHED;
                    emitter.send("执行结束: 达到最大步骤  (" + maxSteps + ")");
//                    results.add("Terminated: Reached max steps (" + maxSteps + ")");
                }
                // 正常完成
                SseEmitterUtils.completeWithDone(emitter);
            } catch (Exception e) {
                state = AgentState.ERROR;
                log.error("执行智能体失败", e);
                try {
                    emitter.send("执行错误" + e.getMessage());
                    SseEmitterUtils.completeWithDone(emitter);
                } catch (Exception ex) {
                    emitter.completeWithError(ex);
                }
//                log.error("Error executing agent", e);
            } finally {
                // 清理资源
                this.cleanup();
            }
        });
        // 设置超时和完成回调
        emitter.onTimeout(() -> {
            this.state = AgentState.ERROR;
            this.cleanup();
            log.warn("SSE connection timed out");
        });

        emitter.onCompletion(() -> {
            if (this.state == AgentState.RUNNING) {
                this.state = AgentState.FINISHED;
            }
            this.cleanup();
            log.info("SSE connection completed");
        });
        return emitter;
    }

    /**
     * 执行单个步骤
     *
     * @return 步骤执行结果
     */
    public abstract String step();

    /**
     * 清理资源
     */
    protected void cleanup() {
        // 子类可以重写此方法来清理资源
    }
}
