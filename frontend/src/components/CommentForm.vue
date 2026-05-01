<template>
  <div class="comment-section">
    <h3 class="section-title">评论 ({{ comments.length }})</h3>

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
  }
}

// 提交评论
async function handleSubmit() {
  submitting.value = true
  try {
    await submitComment(form.value)
    alert('评论已提交，等待审核')
    form.value.content = ''
  } catch (error) {
    console.error('提交评论失败:', error)
    alert('提交失败，请重试')
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
  margin-top: 40px;
  padding-top: 30px;
  border-top: 1px solid var(--border-color);
}

.section-title {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 20px;
}

.comment-list {
  margin-bottom: 30px;
}

.comment-item {
  padding: 16px;
  border-bottom: 1px solid var(--border-color);
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.comment-nickname {
  font-weight: 600;
  color: var(--text-color);
}

.comment-time {
  font-size: 12px;
  color: var(--text-secondary);
}

.comment-content {
  color: var(--text-secondary);
  line-height: 1.6;
}

.empty-state {
  text-align: center;
  padding: 40px;
  color: var(--text-secondary);
}

.comment-form {
  background-color: var(--card-bg);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 20px;
}

.form-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 16px;
}

.form-group {
  margin-bottom: 12px;
}

.form-input,
.form-textarea {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid var(--border-color);
  border-radius: 6px;
  font-size: 14px;
  background-color: var(--bg-color);
  color: var(--text-color);
  outline: none;
}

.form-input:focus,
.form-textarea:focus {
  border-color: var(--accent-color);
}

.form-textarea {
  resize: vertical;
}

.submit-btn {
  padding: 10px 24px;
  border: none;
  border-radius: 6px;
  background-color: var(--accent-color);
  color: white;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
}

.submit-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
</style>
