<template>
  <div class="comment-section">
    <h3 class="section-title">评论 ({{ comments.length }})</h3>

    <!-- 提示消息 -->
    <div v-if="successMsg" class="alert alert-success">
      <span class="alert-icon">✅</span>
      <span class="alert-content">{{ successMsg }}</span>
    </div>
    <div v-if="errorMsg" class="alert alert-error">
      <span class="alert-icon">⚠️</span>
      <span class="alert-content">{{ errorMsg }}</span>
    </div>

    <!-- 评论列表 -->
    <div class="comment-list">
      <div v-for="comment in comments" :key="comment.id" class="comment-item">
        <div class="comment-header">
          <span class="comment-nickname">{{ comment.nickname }}</span>
          <span class="comment-time">{{ formatDate(comment.createdAt) }}</span>
        </div>
        <div class="comment-content">{{ comment.content }}</div>
      </div>
      <div v-if="comments.length === 0" class="empty-state">暂无评论，快来抢沙发吧！</div>
    </div>

    <!-- 评论表单 -->
    <form @submit.prevent="handleSubmit" class="comment-form">
      <h4 class="form-title">发表评论</h4>
      <div class="form-group">
        <input v-model="form.nickname" placeholder="昵称 *" required class="form-input" />
      </div>
      <div class="form-group">
        <input v-model="form.email" type="email" placeholder="邮箱（选填）" class="form-input" />
      </div>
      <div class="form-group">
        <textarea v-model="form.content" placeholder="评论内容 *" required class="form-textarea" rows="4"></textarea>
      </div>
      <button type="submit" class="submit-btn" :disabled="submitting">
        {{ submitting ? '提交中...' : '提交评论' }}
      </button>
    </form>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getArticleComments, submitComment } from '../api/comment'

const props = defineProps({
  articleId: {
    type: Number,
    required: true
  }
})

const comments = ref([])
const successMsg = ref('')
const errorMsg = ref('')
const form = ref({
  articleId: props.articleId,
  nickname: '',
  email: '',
  content: ''
})
const submitting = ref(false)

// 加载评论
async function loadComments() {
  try {
    const res = await getArticleComments(props.articleId)
    comments.value = res.data || []
  } catch (error) {
    console.error('加载评论失败:', error)
    errorMsg.value = '加载评论失败，请刷新页面重试'
  }
}

// 提交评论
async function handleSubmit() {
  submitting.value = true
  successMsg.value = ''
  errorMsg.value = ''
  try {
    await submitComment(form.value)
    successMsg.value = '评论已提交，等待审核'
    form.value.content = ''
    loadComments()
  } catch (error) {
    console.error('提交评论失败:', error)
    errorMsg.value = '提交失败，请检查网络后重试'
  } finally {
    submitting.value = false
  }
}

// 格式化日期
function formatDate(dateStr) {
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

onMounted(() => {
  loadComments()
})
</script>

<style scoped>
.comment-section {
  margin-top: var(--space-2xl);
  padding-top: var(--space-xl);
  border-top: 1px solid var(--border-color);
}

.section-title {
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: var(--space-xl);
  color: var(--text-color);
}

.comment-list {
  margin-bottom: var(--space-2xl);
}

.comment-item {
  padding: var(--space-lg);
  border-bottom: 1px solid var(--border-color);
  transition: background-color var(--transition-fast);
}

.comment-item:hover {
  background-color: var(--bg-secondary);
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--space-sm);
}

.comment-nickname {
  font-weight: 600;
  color: var(--text-color);
  font-size: 1rem;
}

.comment-time {
  font-size: 0.8125rem;
  color: var(--text-secondary);
}

.comment-content {
  color: var(--text-secondary);
  line-height: 1.6;
  font-size: 0.9375rem;
}

.empty-state {
  text-align: center;
  padding: var(--space-3xl);
  color: var(--text-secondary);
  font-size: 1rem;
}

.comment-form {
  background-color: var(--card-bg);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-lg);
  padding: var(--space-xl);
  box-shadow: var(--card-shadow);
}

.form-title {
  font-size: 1.25rem;
  font-weight: 600;
  margin-bottom: var(--space-lg);
  color: var(--text-color);
}

.form-group {
  margin-bottom: var(--space-md);
}

.form-input,
.form-textarea {
  width: 100%;
  padding: var(--space-sm) var(--space-md);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  font-size: 0.9375rem;
  background-color: var(--bg-color);
  color: var(--text-color);
  outline: none;
  transition: all var(--transition-fast);
  font-family: var(--font-sans);
}

.form-input:focus,
.form-textarea:focus {
  border-color: var(--accent-color);
  box-shadow: 0 0 0 3px var(--accent-light);
}

.form-textarea {
  resize: vertical;
  min-height: 100px;
}

.submit-btn {
  padding: var(--space-sm) var(--space-xl);
  border: none;
  border-radius: var(--radius-md);
  background-color: var(--accent-color);
  color: white;
  font-size: 0.9375rem;
  font-weight: 500;
  cursor: pointer;
  transition: all var(--transition-fast);
}

.submit-btn:hover:not(:disabled) {
  background-color: var(--accent-hover);
  transform: translateY(-1px);
}

.submit-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

@media (max-width: 767px) {
  .comment-section {
    margin-top: var(--space-xl);
    padding-top: var(--space-lg);
  }

  .section-title {
    font-size: 1.25rem;
  }

  .comment-form {
    padding: var(--space-lg);
  }
}
</style>
