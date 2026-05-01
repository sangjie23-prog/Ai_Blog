<template>
  <div class="comment-manage">
    <h1 class="page-title">评论管理</h1>

    <!-- 筛选栏 -->
    <div class="filter-bar">
      <button :class="['filter-btn', { active: statusFilter === null }]" @click="changeStatusFilter(null)">全部</button>
      <button :class="['filter-btn', { active: statusFilter === 0 }]" @click="changeStatusFilter(0)">待审核</button>
      <button :class="['filter-btn', { active: statusFilter === 1 }]" @click="changeStatusFilter(1)">已通过</button>
      <button :class="['filter-btn', { active: statusFilter === 2 }]" @click="changeStatusFilter(2)">已拒绝</button>
    </div>

    <!-- 评论列表 -->
    <div class="comment-list">
      <div v-for="comment in comments" :key="comment.id" class="comment-card">
        <div class="comment-header">
          <div class="comment-info">
            <span class="nickname">{{ comment.nickname }}</span>
            <span class="email">{{ comment.email || '未填写' }}</span>
          </div>
          <span :class="['status-badge', `status-${comment.status}`]">
            {{ getStatusText(comment.status) }}
          </span>
        </div>
        <div class="comment-content">{{ comment.content }}</div>
        <div class="comment-footer">
          <span class="time">{{ formatDate(comment.createdAt) }}</span>
          <div class="actions">
            <button v-if="comment.status === 0" class="action-btn approve" @click="handleApprove(comment.id)">通过</button>
            <button v-if="comment.status === 0" class="action-btn reject" @click="handleReject(comment.id)">拒绝</button>
            <button class="action-btn delete" @click="handleDelete(comment.id)">删除</button>
          </div>
        </div>
      </div>
    </div>

    <!-- 空状态 -->
    <div v-if="comments.length === 0 && !loading" class="empty-state">暂无评论</div>

    <!-- 分页 -->
    <div v-if="totalPages > 1" class="pagination">
      <button :disabled="currentPage <= 1" @click="changePage(currentPage - 1)" class="page-btn">上一页</button>
      <span class="page-info">第 {{ currentPage }} / {{ totalPages }} 页</span>
      <button :disabled="currentPage >= totalPages" @click="changePage(currentPage + 1)" class="page-btn">下一页</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getCommentList, approveComment, rejectComment, deleteComment } from '../../api/admin/comment'

const comments = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = 10
const totalPages = ref(1)
const statusFilter = ref(null)

// 加载评论
async function loadComments() {
  loading.value = true
  try {
    const res = await getCommentList(currentPage.value, pageSize, statusFilter.value)
    comments.value = res.data.records || []
    totalPages.value = res.data.pages || 1
  } catch (error) {
    console.error('加载评论失败:', error)
  } finally {
    loading.value = false
  }
}

// 切换状态筛选
function changeStatusFilter(status) {
  statusFilter.value = status
  currentPage.value = 1
  loadComments()
}

// 切换页码
function changePage(page) {
  currentPage.value = page
  loadComments()
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

// 通过评论
async function handleApprove(id) {
  try {
    await approveComment(id)
    loadComments()
  } catch (error) {
    console.error('审核失败:', error)
  }
}

// 拒绝评论
async function handleReject(id) {
  try {
    await rejectComment(id)
    loadComments()
  } catch (error) {
    console.error('拒绝失败:', error)
  }
}

// 删除评论
async function handleDelete(id) {
  if (!confirm('确定要删除这条评论吗？')) return
  try {
    await deleteComment(id)
    loadComments()
  } catch (error) {
    console.error('删除失败:', error)
  }
}

// 获取状态文本
function getStatusText(status) {
  const map = { 0: '待审核', 1: '已通过', 2: '已拒绝' }
  return map[status] || '未知'
}

// 格式化日期
function formatDate(dateStr) {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN')
}

onMounted(() => {
  loadComments()
})
</script>

<style scoped>
.comment-manage {
  max-width: var(--container-xl);
  margin: 0 auto;
}

.page-title {
  font-size: 2rem;
  font-weight: 700;
  margin-bottom: var(--space-xl);
  color: var(--text-color);
  letter-spacing: -0.5px;
}

.filter-bar {
  display: flex;
  gap: var(--space-md);
  margin-bottom: var(--space-lg);
  flex-wrap: wrap;
}

.filter-btn {
  padding: var(--space-sm) var(--space-lg);
  border: 1px solid var(--border-color);
  background: none;
  border-radius: var(--radius-md);
  cursor: pointer;
  font-size: 0.9375rem;
  color: var(--text-secondary);
  transition: all var(--transition-fast);
  font-weight: 500;
}

.filter-btn:hover {
  background-color: var(--hover-bg);
}

.filter-btn.active {
  background-color: var(--accent-color);
  color: white;
  border-color: var(--accent-color);
}

.comment-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-md);
}

.comment-card {
  background-color: var(--card-bg);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-lg);
  padding: var(--space-lg);
  transition: all var(--transition-base);
  box-shadow: var(--card-shadow);
}

.comment-card:hover {
  box-shadow: var(--card-shadow-hover);
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--space-md);
}

.comment-info {
  display: flex;
  gap: var(--space-lg);
  align-items: center;
}

.nickname {
  font-weight: 600;
  color: var(--text-color);
  font-size: 1rem;
}

.email {
  font-size: 0.875rem;
  color: var(--text-secondary);
}

.status-badge {
  padding: var(--space-xs) var(--space-sm);
  border-radius: var(--radius-full);
  font-size: 0.75rem;
  font-weight: 500;
}

.status-0 {
  background-color: rgba(255, 193, 7, 0.15);
  color: #856404;
}

.status-1 {
  background-color: rgba(40, 167, 69, 0.15);
  color: #155724;
}

.status-2 {
  background-color: rgba(220, 53, 69, 0.15);
  color: #721c24;
}

.comment-content {
  color: var(--text-color);
  line-height: 1.6;
  margin-bottom: var(--space-md);
  font-size: 0.9375rem;
}

.comment-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.time {
  font-size: 0.8125rem;
  color: var(--text-secondary);
}

.actions {
  display: flex;
  gap: var(--space-sm);
}

.action-btn {
  padding: var(--space-xs) var(--space-md);
  border: none;
  border-radius: var(--radius-sm);
  cursor: pointer;
  font-size: 0.8125rem;
  transition: all var(--transition-fast);
  font-weight: 500;
}

.action-btn:hover {
  transform: translateY(-1px);
  opacity: 0.9;
}

.action-btn.approve {
  background-color: #28a745;
  color: white;
}

.action-btn.reject {
  background-color: #ffc107;
  color: #333;
}

.action-btn.delete {
  background-color: #dc3545;
  color: white;
}

.empty-state {
  text-align: center;
  padding: var(--space-3xl);
  color: var(--text-secondary);
  font-size: 1rem;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: var(--space-xl);
  margin-top: var(--space-2xl);
}

.page-btn {
  padding: var(--space-sm) var(--space-xl);
  border: 1px solid var(--border-color);
  background-color: var(--card-bg);
  color: var(--text-color);
  border-radius: var(--radius-md);
  cursor: pointer;
  font-size: 0.9375rem;
  transition: all var(--transition-fast);
  font-weight: 500;
}

.page-btn:hover:not(:disabled) {
  background-color: var(--accent-color);
  color: white;
  border-color: var(--accent-color);
  transform: translateY(-1px);
}

.page-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-info {
  font-size: 0.9375rem;
  color: var(--text-secondary);
}

@media (max-width: 767px) {
  .page-title {
    font-size: 1.5rem;
    margin-bottom: var(--space-lg);
  }

  .filter-bar {
    gap: var(--space-sm);
  }

  .filter-btn {
    padding: var(--space-xs) var(--space-md);
    font-size: 0.875rem;
  }

  .comment-header {
    flex-direction: column;
    align-items: flex-start;
    gap: var(--space-sm);
  }

  .comment-footer {
    flex-direction: column;
    align-items: flex-start;
    gap: var(--space-md);
  }

  .actions {
    width: 100%;
    justify-content: flex-start;
  }
}

@media (max-width: 479px) {
  .comment-card {
    padding: var(--space-md);
  }

  .pagination {
    gap: var(--space-md);
  }

  .page-btn {
    padding: var(--space-xs) var(--space-md);
    font-size: 0.875rem;
  }
}
</style>
