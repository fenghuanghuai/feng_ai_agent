import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import LoveApp from '../views/LoveApp.vue'
import ManusApp from '../views/ManusApp.vue'

/**
 * 路由配置
 */
const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
    meta: { title: '应用首页' }
  },
  {
    path: '/love-app',
    name: 'LoveApp',
    component: LoveApp,
    meta: { title: 'AI恋爱大师' }
  },
  {
    path: '/manus-app',
    name: 'ManusApp',
    component: ManusApp,
    meta: { title: 'AI超级智能体' }
  }
]

/**
 * 创建路由实例
 */
const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router