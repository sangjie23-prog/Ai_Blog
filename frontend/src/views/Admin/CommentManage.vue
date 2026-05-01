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
.page-title {
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 24px;
}

.filter-bar {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
}

.filter-btn {
  padding: 8px 16px;
  border: 1px solid var(--border-color);
  background: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  color: var(--text-secondary);
}

.filter-btn.active {
  background-color: var(--accent-color);
  color: white;
  border-color: var(--accent-color);
}

.comment-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.comment-card {
  background-color: var(--card-bg);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 16px;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.comment-info {
  display: flex;
  gap: 12px;
  align-items: center;
}

.nickname {
  font-weight: 600;
  color: var(--text-color);
}

.email {
  font-size: 13px;
  color: var(--text-secondary);
}

.status-badge {
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.status-0 {
  background-color: #fff3cd;
  color: #856404;
}

.status-1 {
  background-color: #d4edda;
  color: #155724;
}

.status-2 {
  background-color: #f8d7da;
  color: #721c24;
}

.comment-content {
  color: var(--text-color);
  line-height: 1.6;
  margin-bottom: 12px;
}

.comment-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.time {
  font-size: 12px;
  color: var(--text-secondary);
}

.actions {
  display: flex;
  gap: 8px;
}

.action-btn {
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 13px;
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
  padding: 60px 20px;
  color: var(--text-secondary);
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  margin-top: 30px;
}

.page-btn {
  padding: 8px 20px;
  border: 1px solid var(--border-color);
  background-color: var(--card-bg);
  color: var(--text-color);
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
}

.page-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-info {
  font-size: 14px;
  color: var(--text-secondary);
}
</style>
