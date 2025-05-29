import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

/**
 * 创建并挂载Vue应用
 */
const app = createApp(App)
app.use(router)
app.mount('#app')
