<template>
  <div class="article-edit-page">
    <div class="page-header">
      <h1 class="page-title">{{ isEdit ? '编辑文章' : '新建文章' }}</h1>
      <button class="back-btn" @click="goBack">返回列表</button>
    </div>

    <form @submit.prevent="handleSubmit" class="edit-form">
      <!-- 标题 -->
      <div class="form-group">
        <label for="title">标题</label>
        <input id="title" v-model="form.title" type="text" placeholder="请输入文章标题" required />
      </div>

      <!-- 摘要 -->
      <div class="form-group">
        <label for="summary">摘要</label>
        <div class="summary-row">
          <textarea id="summary" v-model="form.summary" placeholder="请输入文章摘要（可选）" rows="3"></textarea>
          <button type="button" class="ai-btn" @click="handleGenerateSummary" :disabled="aiLoading || !form.content">
            {{ aiLoading ? '生成中...' : '✨ AI生成摘要' }}
          </button>
        </div>
      </div>

      <!-- 分类和标签 -->
      <div class="form-row">
        <div class="form-group">
          <label for="category">分类</label>
          <input id="category" v-model="form.category" type="text" placeholder="分类名称" />
        </div>
        <div class="form-group">
          <label for="tags">标签</label>
          <div class="tags-row">
            <input id="tags" v-model="form.tags" type="text" placeholder="标签，用逗号分隔" />
            <button type="button" class="ai-btn" @click="handleGenerateTags" :disabled="aiLoading || !form.content">
              {{ aiLoading ? '生成中...' : '✨ AI生成标签' }}
            </button>
          </div>
        </div>
      </div>

      <!-- 富文本编辑器 -->
      <div class="form-group">
        <label>内容</label>
        <MarkdownEditor
          v-model="form.content"
          :auto-save-key="autoSaveKey"
        />
      </div>

      <!-- 操作按钮 -->
      <div class="form-actions">
        <button type="submit" class="submit-btn" :disabled="loading">
          {{ loading ? '保存中...' : '保存草稿' }}
        </button>
        <button type="button" class="publish-btn" @click="handlePublish" :disabled="loading">
          {{ loading ? '发布中...' : '发布文章' }}
        </button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import MarkdownEditor from '../../components/MarkdownEditor.vue'
import { getAdminArticle, createAdminArticle, updateAdminArticle, publishArticle } from '../../api/admin/article'
import { generateSummary, generateTags } from '../../api/admin/ai'

const route = useRoute()
const router = useRouter()

const isEdit = computed(() => !!route.params.id)
const articleId = computed(() => route.params.id)
const autoSaveKey = computed(() => `article-editor-${articleId.value || 'new'}`)

const form = reactive({
  title: '',
  summary: '',
  content: '',
  category: '',
  tags: '',
  status: 0
})

const loading = ref(false)
const aiLoading = ref(false)

// 加载文章（编辑模式）
async function loadArticle() {
  if (!isEdit.value) return
  try {
    const res = await getAdminArticle(articleId.value)
    const article = res.data
    form.title = article.title || ''
    form.summary = article.summary || ''
    form.content = article.content || ''
    form.category = article.category || ''
    form.tags = article.tags || ''
  } catch (error) {
    alert('加载文章失败')
    router.push('/admin/articles')
  }
}

// 保存草稿
async function handleSubmit() {
  loading.value = true
  try {
    const data = {
      title: form.title,
      summary: form.summary,
      content: form.content,
      category: form.category,
      tags: form.tags,
      status: 0
    }

    if (isEdit.value) {
      await updateAdminArticle(articleId.value, data)
    } else {
      await createAdminArticle(data)
    }

    alert('保存成功')
    router.push('/admin/articles')
  } catch (error) {
    alert('保存失败')
  } finally {
    loading.value = false
  }
}

// 发布文章
async function handlePublish() {
  if (!form.title) {
    alert('请填写文章标题')
    return
  }

  loading.value = true
  try {
    const data = {
      title: form.title,
      summary: form.summary,
      content: form.content,
      category: form.category,
      tags: form.tags,
      status: 1
    }

    let id = articleId.value
    if (isEdit.value) {
      await updateAdminArticle(id, data)
    } else {
      const res = await createAdminArticle(data)
      id = res.data?.id
    }

    alert('发布成功')
    router.push('/admin/articles')
  } catch (error) {
    alert('发布失败')
  } finally {
    loading.value = false
  }
}

// AI生成摘要
async function handleGenerateSummary() {
  if (!form.content) {
    alert('请先输入文章内容')
    return
  }
  aiLoading.value = true
  try {
    const res = await generateSummary(form.content)
    form.summary = res.data
  } catch (error) {
    alert('生成摘要失败: ' + (error.message || '请检查DeepSeek API配置'))
  } finally {
    aiLoading.value = false
  }
}

// AI生成标签
async function handleGenerateTags() {
  if (!form.content) {
    alert('请先输入文章内容')
    return
  }
  aiLoading.value = true
  try {
    const res = await generateTags(form.content)
    form.tags = res.data.join(', ')
  } catch (error) {
    alert('生成标签失败: ' + (error.message || '请检查DeepSeek API配置'))
  } finally {
    aiLoading.value = false
  }
}

// 返回
function goBack() {
  router.push('/admin/articles')
}

onMounted(() => {
  loadArticle()
})
</script>

<style scoped>
.article-edit-page {
  max-width: 1000px;
  margin: 0 auto;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.page-title {
  font-size: 28px;
  font-weight: 700;
}

.back-btn {
  padding: 8px 16px;
  border: 1px solid var(--border-color);
  border-radius: 6px;
  background-color: var(--card-bg);
  color: var(--text-color);
  cursor: pointer;
  font-size: 14px;
}

.back-btn:hover {
  background-color: var(--hover-bg);
}

.edit-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.form-group label {
  font-size: 14px;
  font-weight: 500;
  color: var(--text-secondary);
}

.form-group input,
.form-group textarea {
  padding: 10px 14px;
  border: 1px solid var(--border-color);
  border-radius: 6px;
  font-size: 15px;
  background-color: var(--card-bg);
  color: var(--text-color);
  outline: none;
  transition: border-color 0.2s;
}

.form-group input:focus,
.form-group textarea:focus {
  border-color: var(--accent-color);
}

.summary-row,
.tags-row {
  display: flex;
  gap: 10px;
  align-items: flex-start;
}

.summary-row textarea {
  flex: 1;
}

.tags-row input {
  flex: 1;
}

.ai-btn {
  padding: 10px 16px;
  border: 1px solid var(--accent-color);
  border-radius: 6px;
  background-color: var(--accent-color);
  color: white;
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  white-space: nowrap;
  transition: opacity 0.2s;
}

.ai-btn:hover:not(:disabled) {
  opacity: 0.85;
}

.ai-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.form-actions {
  display: flex;
  gap: 12px;
}

.submit-btn,
.publish-btn {
  padding: 12px 24px;
  border: none;
  border-radius: 6px;
  font-size: 15px;
  font-weight: 500;
  cursor: pointer;
}

.submit-btn {
  background-color: var(--card-bg);
  color: var(--text-color);
  border: 1px solid var(--border-color);
}

.submit-btn:hover:not(:disabled) {
  background-color: var(--hover-bg);
}

.publish-btn {
  background-color: var(--accent-color);
  color: white;
}

.publish-btn:hover:not(:disabled) {
  opacity: 0.9;
}

.submit-btn:disabled,
.publish-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

@media (max-width: 768px) {
  .form-row {
    grid-template-columns: 1fr;
  }

  .form-actions {
    flex-direction: column;
  }
}
</style>
