import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import ArticleDetail from '../views/ArticleDetail.vue'
import Login from '../views/Admin/Login.vue'

const routes = [
  // 前台路由
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/article/:id',
    name: 'ArticleDetail',
    component: ArticleDetail
  },
  {
    path: '/search',
    name: 'Search',
    component: () => import('../views/Search.vue')
  },
  // 登录路由
  {
    path: '/admin/login',
    name: 'Login',
    component: Login
  },
  // 后台路由（需要登录）
  {
    path: '/admin/dashboard',
    name: 'Dashboard',
    component: () => import('../views/Admin/Dashboard.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/admin/articles',
    name: 'AdminArticles',
    component: () => import('../views/Admin/ArticleList.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/admin/articles/new',
    name: 'AdminArticleNew',
    component: () => import('../views/Admin/ArticleEdit.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/admin/articles/edit/:id',
    name: 'AdminArticleEdit',
    component: () => import('../views/Admin/ArticleEdit.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/admin/categories',
    name: 'AdminCategories',
    component: () => import('../views/Admin/CategoryManage.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/admin/tags',
    name: 'AdminTags',
    component: () => import('../views/Admin/TagManage.vue'),
    meta: { requiresAuth: true }
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

// 全局前置守卫
router.beforeEach((to, from, next) => {
  // 检查是否需要登录
  if (to.meta.requiresAuth) {
    const token = localStorage.getItem('token')
    if (!token) {
      // 未登录，跳转到登录页
      next({ name: 'Login', query: { redirect: to.fullPath } })
    } else {
      next()
    }
  } else {
    next()
  }
})

export default router
