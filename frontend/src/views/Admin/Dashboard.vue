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
  max-width: var(--container-lg);
  margin: 0 auto;
}

.page-title {
  font-size: 2.5rem;
  font-weight: 700;
  margin-bottom: var(--space-2xl);
  text-align: center;
  color: var(--text-color);
  letter-spacing: -0.5px;
}

.welcome-card {
  background-color: var(--card-bg);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-lg);
  padding: var(--space-xl);
  margin-bottom: var(--space-2xl);
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: var(--card-shadow);
}

.logout-btn {
  padding: var(--space-sm) var(--space-xl);
  background-color: var(--error-color);
  color: white;
  border: none;
  border-radius: var(--radius-md);
  cursor: pointer;
  font-size: 0.9375rem;
  font-weight: 500;
  transition: all var(--transition-fast);
}

.logout-btn:hover {
  opacity: 0.9;
  transform: translateY(-1px);
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: var(--space-lg);
  margin-bottom: var(--space-2xl);
}

.stat-card {
  background-color: var(--card-bg);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-lg);
  padding: var(--space-xl);
  display: flex;
  align-items: center;
  gap: var(--space-lg);
  transition: all var(--transition-base);
  box-shadow: var(--card-shadow);
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--card-shadow-hover);
}

.stat-icon {
  font-size: 2.5rem;
  opacity: 0.9;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 2rem;
  font-weight: 700;
  color: var(--text-color);
  line-height: 1.2;
}

.stat-label {
  font-size: 0.875rem;
  color: var(--text-secondary);
  margin-top: var(--space-xs);
}

.hot-articles-section {
  background-color: var(--card-bg);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-lg);
  padding: var(--space-xl);
  margin-bottom: var(--space-2xl);
  box-shadow: var(--card-shadow);
}

.section-title {
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: var(--space-lg);
  color: var(--text-color);
}

.hot-articles-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-sm);
}

.hot-article-item {
  display: flex;
  align-items: center;
  gap: var(--space-md);
  padding: var(--space-md);
  border-radius: var(--radius-md);
  transition: background-color var(--transition-fast);
}

.hot-article-item:hover {
  background-color: var(--hover-bg);
}

.hot-article-item:last-child {
  border-bottom: none;
}

.rank-badge {
  width: 32px;
  height: 32px;
  border-radius: var(--radius-full);
  background-color: var(--bg-tertiary);
  color: var(--text-secondary);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.875rem;
  font-weight: 600;
  flex-shrink: 0;
}

.rank-badge.top-3 {
  background-color: var(--accent-color);
  color: white;
}

.article-info {
  flex: 1;
  min-width: 0;
}

.article-title {
  font-size: 1rem;
  font-weight: 500;
  color: var(--text-color);
  margin-bottom: var(--space-xs);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.article-meta {
  display: flex;
  gap: var(--space-lg);
  font-size: 0.8125rem;
  color: var(--text-secondary);
}

.empty-state {
  text-align: center;
  padding: var(--space-2xl);
  color: var(--text-secondary);
}

.menu-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: var(--space-lg);
}

.menu-card {
  background-color: var(--card-bg);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-lg);
  padding: var(--space-2xl);
  text-decoration: none;
  color: var(--text-color);
  transition: all var(--transition-base);
  box-shadow: var(--card-shadow);
}

.menu-card:hover {
  box-shadow: var(--card-shadow-hover);
  transform: translateY(-4px);
  text-decoration: none;
  border-color: var(--accent-color);
}

.menu-card h3 {
  font-size: 1.25rem;
  margin-bottom: var(--space-sm);
  font-weight: 600;
}

.menu-card p {
  font-size: 0.9375rem;
  color: var(--text-secondary);
  line-height: 1.5;
}

@media (max-width: 767px) {
  .page-title {
    font-size: 2rem;
    margin-bottom: var(--space-xl);
  }

  .welcome-card {
    flex-direction: column;
    gap: var(--space-md);
    text-align: center;
  }

  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: var(--space-md);
  }

  .stat-card {
    padding: var(--space-lg);
  }

  .stat-icon {
    font-size: 2rem;
  }

  .stat-value {
    font-size: 1.5rem;
  }

  .menu-grid {
    grid-template-columns: 1fr;
    gap: var(--space-md);
  }
}

@media (max-width: 479px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }
}
</style>
