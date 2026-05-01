<template>
  <div class="dashboard">
    <h1 class="page-title">后台管理</h1>
    
    <div class="welcome-card">
      <p>欢迎回来，{{ authStore.username }}！</p>
      <button class="logout-btn" @click="handleLogout">退出登录</button>
    </div>

    <div class="menu-grid">
      <router-link to="/admin/articles" class="menu-card">
        <h3>📝 文章管理</h3>
        <p>创建、编辑、删除文章</p>
      </router-link>
      <router-link to="/admin/categories" class="menu-card">
        <h3>📂 分类管理</h3>
        <p>管理文章分类</p>
      </router-link>
      <router-link to="/admin/tags" class="menu-card">
        <h3>🏷️ 标签管理</h3>
        <p>管理文章标签</p>
      </router-link>
      <router-link to="/admin/comments" class="menu-card">
        <h3>💬 评论管理</h3>
        <p>审核和管理评论</p>
      </router-link>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useAuthStore } from '../../stores/auth'

const router = useRouter()
const authStore = useAuthStore()

// 处理登出
function handleLogout() {
  authStore.logout()
  router.push('/')
}
</script>

<style scoped>
.dashboard {
  max-width: 900px;
  margin: 0 auto;
}

.page-title {
  font-size: 32px;
  font-weight: 700;
  margin-bottom: 30px;
  text-align: center;
}

.welcome-card {
  background-color: var(--card-bg);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 30px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logout-btn {
  padding: 8px 20px;
  background-color: #ff4757;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
}

.logout-btn:hover {
  opacity: 0.9;
}

.menu-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
}

.menu-card {
  background-color: var(--card-bg);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 24px;
  text-decoration: none;
  color: var(--text-color);
  transition: box-shadow 0.2s, transform 0.2s;
}

.menu-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  transform: translateY(-2px);
  text-decoration: none;
}

.menu-card h3 {
  font-size: 18px;
  margin-bottom: 8px;
}

.menu-card p {
  font-size: 14px;
  color: var(--text-secondary);
}

@media (max-width: 768px) {
  .welcome-card {
    flex-direction: column;
    gap: 12px;
    text-align: center;
  }
}
</style>
