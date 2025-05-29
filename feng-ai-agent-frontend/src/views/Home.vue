<template>
  <div class="home-container">
    <!-- 主要内容区域 -->
    <div class="content-wrapper">
      <!-- 头部区域 -->
      <header class="header">
        <div class="hero-section">
          <div class="hero-icon">🤖</div>
          <h1 class="title">AI智能应用中心</h1>
          <p class="subtitle">体验最先进的人工智能助手服务</p>
          <div class="feature-tags">
            <span class="tag">💡 智能对话</span>
            <span class="tag">⚡ 实时响应</span>
            <span class="tag">🎯 专业建议</span>
          </div>
        </div>
      </header>

      <!-- 应用选择区域 -->
      <main class="main-content">
        <div class="section-title">
          <h2>选择您需要的AI助手</h2>
          <p>我们为您提供专业的AI服务，满足不同场景需求</p>
        </div>

        <div class="app-grid">
          <!-- AI恋爱大师应用卡片 -->
          <div class="app-card love-app" @click="navigateToLoveApp">
            <div class="card-header">
              <div class="app-icon">💕</div>
              <div class="app-badge">情感专家</div>
            </div>
            <div class="card-content">
              <h3 class="app-title">AI恋爱大师</h3>
              <p class="app-description">
                专业的恋爱咨询助手，提供个性化的情感建议，帮您解决感情困惑，提升恋爱技巧
              </p>
              <div class="app-features">
                <span class="feature">💬 恋爱咨询</span>
                <span class="feature">💡 表白技巧</span>
                <span class="feature">❤️ 情感分析</span>
              </div>
            </div>
            <div class="card-footer">
              <button class="app-button">开始聊天</button>
            </div>
          </div>

          <!-- AI超级智能体应用卡片 -->
          <div class="app-card manus-app" @click="navigateToManusApp">
            <div class="card-header">
              <div class="app-icon">🤖</div>
              <div class="app-badge">通用助手</div>
            </div>
            <div class="card-content">
              <h3 class="app-title">AI超级智能体</h3>
              <p class="app-description">
                强大的通用AI助手，提供学习、工作、生活等全方位智能支持，是您的贴心智能伙伴
              </p>
              <div class="app-features">
                <span class="feature">📚 学习辅导</span>
                <span class="feature">💼 工作助手</span>
                <span class="feature">🔍 知识问答</span>
              </div>
            </div>
            <div class="card-footer">
              <button class="app-button">开始聊天</button>
            </div>
          </div>
        </div>

        <!-- 使用统计 -->
        <div class="stats-section">
          <div class="stats-grid">
            <div class="stat-item">
              <div class="stat-number">1000+</div>
              <div class="stat-label">活跃用户</div>
            </div>
            <div class="stat-item">
              <div class="stat-number">50000+</div>
              <div class="stat-label">对话次数</div>
            </div>
            <div class="stat-item">
              <div class="stat-number">98%</div>
              <div class="stat-label">满意度</div>
            </div>
            <div class="stat-item">
              <div class="stat-number">24/7</div>
              <div class="stat-label">在线服务</div>
            </div>
          </div>
        </div>
      </main>
    </div>

    <!-- 底部版权信息 -->
    <footer class="footer">
      <div class="footer-content">
        <div class="footer-section">
          <h4>AI智能应用中心</h4>
          <p>基于最新AI技术，为您提供专业的智能助手服务</p>
        </div>
        <div class="footer-section">
          <h4>服务特色</h4>
          <ul>
            <li>实时对话响应</li>
            <li>专业知识支持</li>
            <li>个性化建议</li>
            <li>7x24小时服务</li>
          </ul>
        </div>
        <div class="footer-section">
          <h4>技术支持</h4>
          <ul>
            <li>Vue 3 + Vite</li>
            <li>SSE实时通信</li>
            <li>响应式设计</li>
            <li>现代化UI/UX</li>
          </ul>
        </div>
      </div>
      <div class="footer-bottom">
        <p>&copy; 2025 AI智能应用中心. 保留所有权利.</p>
        <p>Powered by Vue.js | 设计与开发：青春的疯子</p>
      </div>
    </footer>
  </div>
</template>

<script>
import { useRouter } from 'vue-router'

export default {
  name: 'Home',
  setup() {
    const router = useRouter()

    /**
     * 导航到AI恋爱大师应用
     */
    const navigateToLoveApp = () => {
      router.push('/love-app')
    }

    /**
     * 导航到AI超级智能体应用
     */
    const navigateToManusApp = () => {
      router.push('/manus-app')
    }

    return {
      navigateToLoveApp,
      navigateToManusApp
    }
  }
}
</script>

<style scoped>
.home-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  flex-direction: column;
  position: relative;
  overflow-x: hidden;
}

.home-container::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: radial-gradient(circle at 20% 80%, rgba(255, 255, 255, 0.1) 0%, transparent 50%),
              radial-gradient(circle at 80% 20%, rgba(255, 255, 255, 0.1) 0%, transparent 50%);
  pointer-events: none;
}

.content-wrapper {
  flex: 1;
  position: relative;
  z-index: 1;
}

/* 头部样式 */
.header {
  padding: 60px 20px 40px;
  text-align: center;
  color: white;
}

.hero-section {
  max-width: 800px;
  margin: 0 auto;
}

.hero-icon {
  font-size: 4rem;
  margin-bottom: 20px;
  animation: float 3s ease-in-out infinite;
}

@keyframes float {
  0%, 100% { transform: translateY(0px); }
  50% { transform: translateY(-10px); }
}

.title {
  font-size: 3.5rem;
  font-weight: bold;
  margin-bottom: 15px;
  text-shadow: 2px 2px 8px rgba(0, 0, 0, 0.3);
  background: linear-gradient(45deg, #fff, #f0f0f0);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.subtitle {
  font-size: 1.3rem;
  opacity: 0.9;
  margin-bottom: 30px;
  line-height: 1.6;
}

.feature-tags {
  display: flex;
  justify-content: center;
  gap: 15px;
  flex-wrap: wrap;
}

.tag {
  background: rgba(255, 255, 255, 0.2);
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 0.9rem;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  transition: all 0.3s ease;
}

.tag:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: translateY(-2px);
}

/* 主要内容样式 */
.main-content {
  padding: 0 20px 40px;
  max-width: 1400px;
  margin: 0 auto;
  width: 100%;
}

.section-title {
  text-align: center;
  margin-bottom: 50px;
  color: white;
}

.section-title h2 {
  font-size: 2.2rem;
  margin-bottom: 10px;
  font-weight: bold;
}

.section-title p {
  font-size: 1.1rem;
  opacity: 0.9;
}

.app-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(450px, 1fr));
  gap: 40px;
  margin-bottom: 60px;
  justify-items: center;
}

.app-card {
  background: white;
  border-radius: 24px;
  overflow: hidden;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.2);
  transition: all 0.4s ease;
  cursor: pointer;
  position: relative;
  width: 100%;
  max-width: 480px;
  height: 420px;
  display: flex;
  flex-direction: column;
}

.app-card:hover {
  transform: translateY(-15px) scale(1.02);
  box-shadow: 0 30px 60px rgba(0, 0, 0, 0.3);
}

.card-header {
  padding: 30px 30px 20px;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  position: relative;
}

.app-icon {
  font-size: 3.5rem;
  margin-bottom: 10px;
}

.app-badge {
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  padding: 6px 12px;
  border-radius: 15px;
  font-size: 0.8rem;
  font-weight: bold;
  position: absolute;
  top: 20px;
  right: 20px;
}

.love-app .app-badge {
  background: linear-gradient(135deg, #ff6b6b, #ff8e8e);
}

.manus-app .app-badge {
  background: linear-gradient(135deg, #4ecdc4, #45b7d1);
}

.card-content {
  padding: 0 30px 20px;
  flex: 1;
}

.app-title {
  font-size: 1.8rem;
  font-weight: bold;
  margin-bottom: 15px;
  color: #333;
}

.app-description {
  font-size: 1rem;
  color: #666;
  line-height: 1.6;
  margin-bottom: 20px;
}

.app-features {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.feature {
  background: #f8f9fa;
  color: #495057;
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 0.85rem;
  border: 1px solid #e9ecef;
}

.card-footer {
  padding: 20px 30px 30px;
}

.app-button {
  width: 100%;
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  border: none;
  padding: 15px 30px;
  border-radius: 20px;
  font-size: 1.1rem;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
}

.app-button:hover {
  transform: translateY(-3px);
  box-shadow: 0 12px 25px rgba(102, 126, 234, 0.6);
}

.love-app .app-button {
  background: linear-gradient(135deg, #ff6b6b, #ff8e8e);
  box-shadow: 0 8px 20px rgba(255, 107, 107, 0.4);
}

.love-app .app-button:hover {
  box-shadow: 0 12px 25px rgba(255, 107, 107, 0.6);
}

.manus-app .app-button {
  background: linear-gradient(135deg, #4ecdc4, #45b7d1);
  box-shadow: 0 8px 20px rgba(78, 205, 196, 0.4);
}

.manus-app .app-button:hover {
  box-shadow: 0 12px 25px rgba(78, 205, 196, 0.6);
}

/* 统计区域 */
.stats-section {
  margin-bottom: 40px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 30px;
  max-width: 1000px;
  margin: 0 auto;
}

.stat-item {
  text-align: center;
  color: white;
  background: rgba(255, 255, 255, 0.1);
  padding: 30px 20px;
  border-radius: 20px;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition: all 0.3s ease;
}

.stat-item:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateY(-5px);
}

.stat-number {
  font-size: 2.5rem;
  font-weight: bold;
  margin-bottom: 10px;
  background: linear-gradient(45deg, #fff, #f0f0f0);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.stat-label {
  font-size: 1rem;
  opacity: 0.9;
}

/* 底部样式 */
.footer {
  background: rgba(0, 0, 0, 0.3);
  backdrop-filter: blur(20px);
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  color: white;
  position: relative;
  z-index: 1;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 40px;
}

.footer-section h4 {
  font-size: 1.2rem;
  margin-bottom: 15px;
  color: #fff;
  font-weight: bold;
}

.footer-section p {
  line-height: 1.6;
  opacity: 0.8;
  margin-bottom: 15px;
}

.footer-section ul {
  list-style: none;
  padding: 0;
}

.footer-section li {
  padding: 5px 0;
  opacity: 0.8;
  position: relative;
  padding-left: 15px;
}

.footer-section li::before {
  content: '▶';
  position: absolute;
  left: 0;
  color: #667eea;
  font-size: 0.8rem;
}

.footer-bottom {
  background: rgba(0, 0, 0, 0.2);
  padding: 20px;
  text-align: center;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.footer-bottom p {
  margin: 5px 0;
  opacity: 0.8;
  font-size: 0.9rem;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .app-grid {
    grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
    gap: 30px;
  }
}

@media (max-width: 768px) {
  .title {
    font-size: 2.5rem;
  }

  .subtitle {
    font-size: 1.1rem;
  }

  .app-grid {
    grid-template-columns: 1fr;
    gap: 30px;
    padding: 0 10px;
  }

  .app-card {
    max-width: 100%;
    height: auto;
    min-height: 380px;
  }

  .card-header {
    padding: 25px 25px 15px;
  }

  .card-content {
    padding: 0 25px 15px;
  }

  .card-footer {
    padding: 15px 25px 25px;
  }

  .feature-tags {
    flex-direction: column;
    align-items: center;
    gap: 10px;
  }

  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 20px;
  }

  .stat-number {
    font-size: 2rem;
  }

  .footer-content {
    grid-template-columns: 1fr;
    gap: 30px;
    text-align: center;
  }
}

@media (max-width: 480px) {
  .header {
    padding: 40px 15px 30px;
  }

  .main-content {
    padding: 0 15px 30px;
  }

  .title {
    font-size: 2rem;
  }

  .app-card {
    min-height: 350px;
  }

  .stats-grid {
    grid-template-columns: 1fr;
  }
}
</style>