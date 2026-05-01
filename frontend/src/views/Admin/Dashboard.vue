<template>
  <div class="dashboard">
    <h1 class="page-title">后台管理</h1>
    
    <div class="welcome-card">
      <p>欢迎回来，{{ authStore.username }}！</p>
      <button class="logout-btn" @click="handleLogout">退出登录</button>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-icon">📝</div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.totalArticles || 0 }}</div>
          <div class="stat-label">文章总数</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">👁️</div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.totalViews || 0 }}</div>
          <div class="stat-label">总阅读量</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">❤️</div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.totalLikes || 0 }}</div>
          <div class="stat-label">总点赞数</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">💬</div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.totalComments || 0 }}</div>
          <div class="stat-label">总评论数</div>
        </div>
      </div>
    </div>

    <!-- 热门文章 -->
    <div class="hot-articles-section">
      <h2 class="section-title">热门文章</h2>
      <div v-if="hotArticles.length > 0" class="hot-articles-list">
        <div v-for="(article, index) in hotArticles" :key="article.id" class="hot-article-item">
          <span class="rank-badge" :class="{ 'top-3': index < 3 }">{{ index + 1 }}</span>
          <div class="article-info">
            <div class="article-title">{{ article.title }}</div>
            <div class="article-meta">
              <span>👁️ {{ article.viewCount || 0 }}</span>
              <span>❤️ {{ article.likeCount || 0 }}</span>
            </div>
          </div>
        </div>
      </div>
      <div v-else class="empty-state">暂无热门文章</div>
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
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../../stores/auth'
import { getSystemStatistics, getHotArticles } from '../../api/admin/statistics'

const router = useRouter()
const authStore = useAuthStore()
const stats = ref({})
const hotArticles = ref([])

// 加载统计数据
async function loadStatistics() {
  try {
    const res = await getSystemStatistics()
    stats.value = res.data
  } catch (error) {
    console.error('加载统计数据失败:', error)
  }
}

// 加载热门文章
async function loadHotArticles() {
  try {
    const res = await getHotArticles(5)
    hotArticles.value = res.data
  } catch (error) {
    console.error('加载热门文章失败:', error)
  }
}

// 处理登出
function handleLogout() {
  authStore.logout()
  router.push('/')
}

onMounted(() => {
  loadStatistics()
  loadHotArticles()
})
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

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 16px;
  margin-bottom: 30px;
}

.stat-card {
  background-color: var(--card-bg);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-icon {
  font-size: 32px;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 24px;
  font-weight: 700;
  color: var(--text-color);
}

.stat-label {
  font-size: 13px;
  color: var(--text-secondary);
  margin-top: 4px;
}

.hot-articles-section {
  background-color: var(--card-bg);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 30px;
}

.section-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 16px;
}

.hot-articles-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.hot-article-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  border-bottom: 1px solid var(--border-color);
}

.hot-article-item:last-child {
  border-bottom: none;
}

.rank-badge {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background-color: var(--border-color);
  color: var(--text-secondary);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  font-weight: 600;
}

.rank-badge.top-3 {
  background-color: var(--accent-color);
  color: white;
}

.article-info {
  flex: 1;
}

.article-title {
  font-size: 14px;
  font-weight: 500;
  color: var(--text-color);
  margin-bottom: 4px;
}

.article-meta {
  display: flex;
  gap: 12px;
  font-size: 12px;
  color: var(--text-secondary);
}

.empty-state {
  text-align: center;
  padding: 20px;
  color: var(--text-secondary);
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

  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>
