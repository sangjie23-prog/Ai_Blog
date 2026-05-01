import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import ArticleDetail from '../views/ArticleDetail.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/article/:id',
    name: 'ArticleDetail',
    component: ArticleDetail
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  // 滚动到顶部
  scrollBehavior() {
    return { top: 0 }
  }
})

export default router
