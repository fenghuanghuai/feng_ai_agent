<template>
  <div class="chat-container">
    <!-- 聊天头部 -->
    <header class="chat-header">
      <button class="back-button" @click="goBack">
        <span>←</span>
      </button>
      <div class="chat-info">
        <h2 class="chat-title">🤖 AI超级智能体</h2>
        <p class="chat-status">{{ connectionStatus }}</p>
      </div>
    </header>

    <!-- 聊天记录区域 -->
    <div class="chat-messages" ref="messagesContainer">
      <div v-for="message in messages" :key="message.id"
           :class="['message', message.type]">
        <div class="message-content">
          <div class="message-text">{{ message.text }}</div>
          <div class="message-time">{{ message.time }}</div>
        </div>
      </div>

      <!-- 加载状态 -->
      <div v-if="isLoading" class="message ai-message">
        <div class="message-content">
          <div class="typing-indicator">
            <span></span>
            <span></span>
            <span></span>
          </div>
        </div>
      </div>
    </div>

    <!-- 输入区域 -->
    <div class="chat-input-area">
      <div class="input-container">
        <textarea
          v-model="inputMessage"
          @keydown="handleKeyDown"
          :disabled="isLoading"
          placeholder="请输入您的问题..."
          class="message-input"
          rows="1"
        ></textarea>
        <button
          @click="sendMessage"
          :disabled="!inputMessage.trim() || isLoading"
          class="send-button"
        >
          发送
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { chatWithManusSSE } from '../services/api'

export default {
  name: 'ManusApp',
  setup() {
    const router = useRouter()
    const messages = ref([])
    const inputMessage = ref('')
    const isLoading = ref(false)
    const connectionStatus = ref('已连接')
    const messagesContainer = ref(null)
    const currentEventSource = ref(null)
    const currentAiMessage = ref(null)

    /**
     * 初始化聊天室
     */
    onMounted(() => {
      addWelcomeMessage()
    })

    /**
     * 组件卸载时清理资源
     */
    onUnmounted(() => {
      if (currentEventSource.value) {
        currentEventSource.value.close()
      }
    })

    /**
     * 添加欢迎消息
     */
    const addWelcomeMessage = () => {
      const welcomeMessage = {
        id: Date.now(),
        type: 'ai-message',
        text: '你好！我是AI超级智能体Manus，一个强大的通用AI助手。我可以帮助您解决各种问题，包括学习、工作、生活建议、技术问题等。请告诉我您需要什么帮助！🤖',
        time: formatTime(new Date())
      }
      messages.value.push(welcomeMessage)
      scrollToBottom()
    }

    /**
     * 发送消息
     */
    const sendMessage = async () => {
      if (!inputMessage.value.trim() || isLoading.value) return

      const userMessage = {
        id: Date.now(),
        type: 'user-message',
        text: inputMessage.value,
        time: formatTime(new Date())
      }

      messages.value.push(userMessage)
      const messageToSend = inputMessage.value
      inputMessage.value = ''
      isLoading.value = true
      connectionStatus.value = '正在发送...'

      // 添加AI消息占位符
      currentAiMessage.value = {
        id: Date.now() + 1,
        type: 'ai-message',
        text: '',
        time: formatTime(new Date())
      }
      messages.value.push(currentAiMessage.value)

      await nextTick()
      scrollToBottom()

      try {
        // 建立SSE连接
        currentEventSource.value = chatWithManusSSE(
          messageToSend,
          handleSSEMessage,
          handleSSEError,
          handleSSEClose
        )
      } catch (error) {
        handleSSEError(error)
      }
    }

    /**
     * 处理SSE消息
     * @param {string} data - 接收到的数据
     */
    const handleSSEMessage = (data) => {
      if (currentAiMessage.value) {
        currentAiMessage.value.text += data
        scrollToBottom()
      }
    }

    /**
     * 处理SSE错误
     * @param {Error} error - 错误对象
     */
    const handleSSEError = (error) => {
      console.warn('SSE连接异常:', error)

      // 只在真正的错误情况下更新状态
      if (isLoading.value) {
        isLoading.value = false
        connectionStatus.value = '连接异常，请重试'

        if (currentAiMessage.value && !currentAiMessage.value.text) {
          currentAiMessage.value.text = '抱歉，连接出现问题，请稍后重试。'
        }
      }
    }

    /**
     * 处理SSE连接关闭
     */
    const handleSSEClose = () => {
      console.log('SSE连接正常关闭')
      isLoading.value = false
      connectionStatus.value = '已连接'
      currentAiMessage.value = null
    }

    /**
     * 处理键盘事件
     * @param {KeyboardEvent} event - 键盘事件
     */
    const handleKeyDown = (event) => {
      if (event.key === 'Enter' && !event.shiftKey) {
        event.preventDefault()
        sendMessage()
      }
    }

    /**
     * 滚动到底部
     */
    const scrollToBottom = async () => {
      await nextTick()
      if (messagesContainer.value) {
        messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
      }
    }

    /**
     * 格式化时间
     * @param {Date} date - 日期对象
     * @returns {string} 格式化的时间字符串
     */
    const formatTime = (date) => {
      return date.toLocaleTimeString('zh-CN', {
        hour: '2-digit',
        minute: '2-digit'
      })
    }

    /**
     * 返回主页
     */
    const goBack = () => {
      if (currentEventSource.value) {
        currentEventSource.value.close()
      }
      router.push('/')
    }

    return {
      messages,
      inputMessage,
      isLoading,
      connectionStatus,
      messagesContainer,
      sendMessage,
      handleKeyDown,
      goBack
    }
  }
}
</script>

<style scoped>
.chat-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.chat-header {
  background: rgba(255, 255, 255, 0.95);
  padding: 15px 20px;
  display: flex;
  align-items: center;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
}

.back-button {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  margin-right: 15px;
  padding: 5px;
  border-radius: 50%;
  transition: background 0.3s ease;
}

.back-button:hover {
  background: rgba(78, 205, 196, 0.1);
}

.chat-info {
  flex: 1;
}

.chat-title {
  margin: 0 0 5px 0;
  font-size: 1.2rem;
  font-weight: bold;
  color: #333;
}

.chat-status {
  margin: 0;
  font-size: 0.9rem;
  color: #666;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.message {
  display: flex;
  max-width: 70%;
  word-wrap: break-word;
}

.user-message {
  align-self: flex-end;
}

.user-message .message-content {
  background: linear-gradient(135deg, #4ecdc4, #45b7d1);
  color: white;
  border-radius: 20px 20px 5px 20px;
  padding: 12px 16px;
  box-shadow: 0 3px 10px rgba(78, 205, 196, 0.3);
}

.ai-message {
  align-self: flex-start;
}

.ai-message .message-content {
  background: white;
  color: #333;
  border-radius: 20px 20px 20px 5px;
  padding: 12px 16px;
  box-shadow: 0 3px 10px rgba(0, 0, 0, 0.1);
}

.message-text {
  line-height: 1.4;
  margin-bottom: 5px;
}

.message-time {
  font-size: 0.8rem;
  opacity: 0.7;
  text-align: right;
}

.ai-message .message-time {
  text-align: left;
}

.typing-indicator {
  display: flex;
  gap: 4px;
  align-items: center;
}

.typing-indicator span {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #4ecdc4;
  animation: typing 1.4s infinite ease-in-out;
}

.typing-indicator span:nth-child(1) {
  animation-delay: -0.32s;
}

.typing-indicator span:nth-child(2) {
  animation-delay: -0.16s;
}

@keyframes typing {
  0%, 80%, 100% {
    transform: scale(0.8);
    opacity: 0.5;
  }
  40% {
    transform: scale(1);
    opacity: 1;
  }
}

.chat-input-area {
  background: rgba(255, 255, 255, 0.95);
  padding: 20px;
  backdrop-filter: blur(10px);
}

.input-container {
  display: flex;
  gap: 10px;
  align-items: flex-end;
}

.message-input {
  flex: 1;
  border: 2px solid #e0f7ff;
  border-radius: 20px;
  padding: 12px 16px;
  font-size: 1rem;
  resize: none;
  outline: none;
  font-family: inherit;
  transition: border-color 0.3s ease;
  background: white;
}

.message-input:focus {
  border-color: #4ecdc4;
}

.message-input:disabled {
  background: #f5f5f5;
  cursor: not-allowed;
}

.send-button {
  background: linear-gradient(135deg, #4ecdc4, #45b7d1);
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 20px;
  font-size: 1rem;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 3px 10px rgba(78, 205, 196, 0.3);
}

.send-button:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(78, 205, 196, 0.4);
}

.send-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
  transform: none;
}

/* 滚动条样式 */
.chat-messages::-webkit-scrollbar {
  width: 6px;
}

.chat-messages::-webkit-scrollbar-track {
  background: rgba(255, 255, 255, 0.3);
  border-radius: 3px;
}

.chat-messages::-webkit-scrollbar-thumb {
  background: rgba(78, 205, 196, 0.5);
  border-radius: 3px;
}

.chat-messages::-webkit-scrollbar-thumb:hover {
  background: rgba(78, 205, 196, 0.7);
}

@media (max-width: 768px) {
  .chat-messages {
    padding: 15px;
  }

  .message {
    max-width: 85%;
  }

  .chat-input-area {
    padding: 15px;
  }
}
</style>