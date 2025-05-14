package com.qcdfz.fengaiagent.chatmemory;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @version v1.0.0
 * @belongsProject: feng-ai-agent
 * @belongsPackage: com.qcdfz.fengaiagent.chatmemory
 * @author: fgh
 * @description:
 * @createTime: 2025-04-29 18:02
 */

public class FileBasedChatMemory implements ChatMemory {

    private final String BASE_DIR;
    private static final Kryo kryo = new Kryo();
    /**
     * 配置Kryo序列化库的静态初始化块
     * 这里对Kryo实例进行配置，以优化其序列化和反序列化性能
     */
    static {
        // 设置是否需要注册类。关闭注册可以减少一些初始化开销，但可能会稍微降低性能
        kryo.setRegistrationRequired(false);
        // 设置Kryo的实例化策略为标准实例化策略，这影响了对象实例化的速度和内存使用
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
    }
    // 构造对象时，指定文件保存目录
    public FileBasedChatMemory(String dir) {
        this.BASE_DIR = dir;
        File file = new File(dir);
        if (!file.exists()){
            file.mkdirs();
        }
    }

    @Override
    public void add(String conversationId, Message message) {
       this.add(conversationId,List.of(message));
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        List<Message> conversationMessage = getOrCreateConversation(conversationId);
        conversationMessage.addAll(messages);
        saveConversation(conversationId,conversationMessage);
    }

    /**
     * 根据会话ID获取最近的N条消息
     *
     * @param conversationId 会话的唯一标识符
     * @param lastN 要获取的最近消息的数量
     * @return 包含最近N条消息的列表
     */
    @Override
    public List<Message> get(String conversationId, int lastN) {
        // 获取或创建会话的所有消息列表
        List<Message> allMessage = getOrCreateConversation(conversationId);

        // 计算并返回列表中最近的N条消息
        // 使用skip方法跳过前面的消息，只获取最后N条
        // 确保不会出现负数索引，使用Math.max保证最小值为0
        return allMessage.stream()
                .skip(Math.max(0,allMessage.size()-lastN))
                .toList();
    }

    @Override
    public void clear(String conversationId) {
        File file = getConversationFile(conversationId);
        if (file.exists()){
            file.delete();
        }
    }


    /**
     * 获取或创建对话
     * 该方法首先尝试从本地文件系统中加载与特定对话ID关联的对话记录如果文件存在，则读取并返回该对话中的消息记录
     * 如果文件不存在，将返回一个空的消息列表此方法主要用于支持消息的持久化存储和检索
     *
     * @param conversationId 对话的唯一标识符用于定位特定的对话文件
     * @return 返回一个消息列表，如果对话文件不存在，则返回空列表
     */
    private List<Message> getOrCreateConversation(String conversationId) {
        // 获取对话文件的路径和名称
        File file = getConversationFile(conversationId);
        // 初始化一个空的消息列表
        List<Message> messages = new ArrayList<>();
        // 检查对话文件是否存在
        if (file.exists()) {
            try (// 准备从文件中读取对话记录
                 Input input = new Input(new FileInputStream(file))) {
                // 使用Kryo反序列化文件中的对话记录
                messages = kryo.readObject(input, ArrayList.class);
            } catch (IOException e) {
                // 如果读取文件时发生错误，则打印错误信息
                e.printStackTrace();
            }
        }
        // 返回消息列表，如果文件不存在则返回空列表
        return messages;
    }

    /**
     * 保存对话到文件
     *
     * @param conversationId 对话ID，用于确定对话文件
     * @param messages 消息列表，包含对话中的所有消息
     */
    private void saveConversation(String conversationId,List<Message> messages){
        // 获取对话文件
        File file = getConversationFile(conversationId);
        try(
            // 创建输出流，用于将消息列表写入文件
            Output output = new Output(new FileOutputStream(file))
        ) {
            // 使用Kryo序列化工具将消息列表写入文件
            kryo.writeObject(output, messages);
        } catch (IOException e) {
            // 打印异常信息，以便于调试和日志记录
            e.printStackTrace();
        }
    }

    private File getConversationFile(String conversationId){
        return new File(BASE_DIR,conversationId+".kryo");
    }
}
