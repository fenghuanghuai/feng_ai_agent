import axios from 'axios'

/**
 * API基础配置
 */
const BASE_URL = 'http://localhost:8123/api'

/**
 * SSE连接结束标志
 */
const DONE_MESSAGE = '[DONE]'

/**
 * Axios实例配置
 */
const apiClient = axios.create({
  baseURL: BASE_URL,
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json'
  }
})

/**
 * 生成随机聊天室ID
 * @returns {string} 聊天室ID
 */
export const generateChatId = () => {
  return 'chat_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9)
}

/**
 * AI恋爱大师SSE聊天接口
 * @param {string} message - 用户消息
 * @param {string} chatId - 聊天室ID
 * @param {function} onMessage - 消息回调函数
 * @param {function} onError - 错误回调函数
 * @param {function} onClose - 关闭回调函数
 * @returns {EventSource} SSE连接实例
 */
export const chatWithLoveAppSSE = (message, chatId, onMessage, onError, onClose) => {
  const url = `${BASE_URL}/ai/love_app_chat/sse_emitter?message=${encodeURIComponent(message)}&chatId=${encodeURIComponent(chatId)}`

  const eventSource = new EventSource(url)

  eventSource.onmessage = (event) => {
    try {
      const data = event.data

      // 检查是否收到结束标志
      if (data === DONE_MESSAGE) {
        console.log('收到SSE结束标志，正常关闭连接')
        eventSource.close()
        onClose()
        return
      }

      // 处理正常消息
      onMessage(data)
    } catch (error) {
      console.error('解析SSE消息失败:', error)
      onError(error)
    }
  }

  eventSource.onerror = (error) => {
    // 只有在连接状态不是CLOSED时才报告错误
    if (eventSource.readyState !== EventSource.CLOSED) {
      console.error('SSE连接错误:', error)
      onError(error)
    }
    eventSource.close()
  }

  // 监听连接打开事件
  eventSource.onopen = () => {
    console.log('SSE连接已建立')
  }

  return eventSource
}

/**
 * AI超级智能体Manus聊天接口
 * @param {string} message - 用户消息
 * @param {function} onMessage - 消息回调函数
 * @param {function} onError - 错误回调函数
 * @param {function} onClose - 关闭回调函数
 * @returns {EventSource} SSE连接实例
 */
export const chatWithManusSSE = (message, onMessage, onError, onClose) => {
  const url = `${BASE_URL}/ai/manus/chat?message=${encodeURIComponent(message)}`

  const eventSource = new EventSource(url)

  eventSource.onmessage = (event) => {
    try {
      const data = event.data

      // 检查是否收到结束标志
      if (data === DONE_MESSAGE) {
        console.log('收到SSE结束标志，正常关闭连接')
        eventSource.close()
        onClose()
        return
      }

      // 处理正常消息
      onMessage(data)
    } catch (error) {
      console.error('解析SSE消息失败:', error)
      onError(error)
    }
  }

  eventSource.onerror = (error) => {
    // 只有在连接状态不是CLOSED时才报告错误
    if (eventSource.readyState !== EventSource.CLOSED) {
      console.error('SSE连接错误:', error)
      onError(error)
    }
    eventSource.close()
  }

  // 监听连接打开事件
  eventSource.onopen = () => {
    console.log('SSE连接已建立')
  }

  return eventSource
}
