package com.qcdfz.fengaiagent.utils;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

public class SseEmitterUtils {
    private static final String DONE_MESSAGE = "[DONE]";

    /**
     * 完成SSE连接：发送结束标志并关闭连接
     *
     * @param emitter 要完成的SseEmitter对象
     */
    public static void completeWithDone(SseEmitter emitter) {
        try {
            emitter.send(DONE_MESSAGE);
        } catch (IOException e) {
            // 可选：记录日志或处理异常
        } finally {
            emitter.complete();
        }
    }

    /**
     * 完成SSE连接并携带错误信息
     *
     * @param emitter 要完成的SseEmitter对象
     * @param ex      发生的异常
     */
    public static void completeWithError(SseEmitter emitter, Throwable ex) {
        try {
            emitter.send("ERROR: " + ex.getMessage());
        } catch (IOException ignored) {
        } finally {
            emitter.completeWithError(ex);
        }
    }
}
