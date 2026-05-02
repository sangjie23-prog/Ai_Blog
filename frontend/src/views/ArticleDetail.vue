<template>
  <div class="article-detail" v-if="article">
    <header class="article-header animate-fade-in">
      <div class="article-header-inner">
        <h1 class="article-title">{{ article.title }}</h1>
        <div class="article-meta">
          <time class="meta-item" :datetime="article.createdAt">
            <span class="meta-icon">📅</span>
            {{ formatDate(article.createdAt) }}
          </time>
          <span class="meta-item">
            <span class="meta-icon">👁️</span>
            {{ article.viewCount || 0 }} 阅读
          </span>
          <span class="meta-item">
            <span class="meta-icon">❤️</span>
            {{ article.likeCount || 0 }} 点赞
          </span>
        </div>
        <div class="article-tags" v-if="article.tags">
          <span 
            v-for="(tag, i) in article.tags.split(',')" 
            :key="i" 
            class="article-tag"
          >
            {{ tag.trim() }}
          </span>
        </div>
      </div>
    </header>

    <div class="article-divider"></div>

    <article 
      class="article-content animate-slide-up" 
      v-html="renderedContent"
      @click="handleImageClick"
    ></article>

    <ImageViewer 
      v-model="viewerVisible" 
      :image-url="currentImageUrl" 
      :image-alt="currentImageAlt"
      @close="viewerVisible = false"
    />

    <div class="article-footer animate-fade-in">
      <div class="article-actions">
        <button class="like-btn" @click="handleLike" aria-label="点赞">
          <span class="like-icon">❤️</span>
          <span class="like-text">点赞</span>
          <span class="like-count">{{ article.likeCount || 0 }}</span>
        </button>
      </div>
      
      <div class="article-share">
        <span class="share-label">分享文章</span>
        <div class="share-buttons">
          <button class="share-btn" @click="copyLink" aria-label="复制链接">
            🔗 复制链接
          </button>
        </div>
      </div>
    </div>

    <div class="article-divider"></div>

    <section class="article-comments">
      <h3 class="comments-title">评论</h3>
      <CommentForm :article-id="parseInt(route.params.id)" />
    </section>
  </div>

  <div v-else-if="loading" class="loading">
    <div class="loading-spinner"></div>
    <p class="loading-text">加载文章中...</p>
  </div>

  <div v-else class="not-found animate-fade-in">
    <div class="not-found-icon">📄</div>
    <h2 class="not-found-title">文章不存在</h2>
    <p class="not-found-desc">该文章可能已被删除或移动</p>
    <router-link to="/" class="back-link">← 返回首页</router-link>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, inject, nextTick, watch } from 'vue'
import { useRoute } from 'vue-router'
import { getArticleDetail, likeArticle } from '../api/article'
import MarkdownIt from 'markdown-it'
import hljs from 'highlight.js'
import CommentForm from '../components/CommentForm.vue'
import ImageViewer from '../components/ImageViewer.vue'

const route = useRoute()
const toast = inject('toast')
const article = ref(null)
const loading = ref(false)

const viewerVisible = ref(false)
const currentImageUrl = ref('')
const currentImageAlt = ref('')

const md = new MarkdownIt({
  html: true,
  linkify: true,
  typographer: true,
  breaks: true,
  highlight: function (str, lang) {
    const escapedCode = md.utils.escapeHtml(str)
    const language = lang && hljs.getLanguage(lang) ? lang : ''
    
    if (language) {
      try {
        const highlighted = hljs.highlight(str, { language, ignoreIllegals: true }).value
        return `<pre class="hljs" data-lang="${language}"><code>${highlighted}</code></pre>`
      } catch (__) {}
    }
    return `<pre class="hljs" data-lang="${language}"><code>${escapedCode}</code></pre>`
  }
})

const renderedContent = computed(() => {
  if (!article.value || !article.value.content) return ''
  let content = article.value.content
  // 兼容旧图片URL格式：将 /api/uploads/images/ 转换为 /api/uploads/
  content = content.replace(/\/api\/uploads\/images\//g, '/api/uploads/')
  return md.render(content)
})

const languageNames = {
  javascript: 'JavaScript',
  typescript: 'TypeScript',
  python: 'Python',
  java: 'Java',
  cpp: 'C++',
  c: 'C',
  csharp: 'C#',
  go: 'Go',
  rust: 'Rust',
  php: 'PHP',
  ruby: 'Ruby',
  swift: 'Swift',
  kotlin: 'Kotlin',
  sql: 'SQL',
  html: 'HTML',
  css: 'CSS',
  scss: 'SCSS',
  less: 'Less',
  json: 'JSON',
  xml: 'XML',
  yaml: 'YAML',
  markdown: 'Markdown',
  bash: 'Bash',
  shell: 'Shell',
  powershell: 'PowerShell',
  dockerfile: 'Dockerfile',
  nginx: 'Nginx',
  vue: 'Vue',
  react: 'React',
  tsx: 'TSX',
  jsx: 'JSX'
}

function enhanceCodeBlocks() {
  const codeBlocks = document.querySelectorAll('.article-content pre.hljs')
  
  codeBlocks.forEach((pre, index) => {
    if (pre.dataset.enhanced) return
    
    const lang = pre.dataset.lang || ''
    const code = pre.querySelector('code')
    const rawCode = code.textContent
    const lineCount = rawCode.split('\n').length
    const languageName = languageNames[lang.toLowerCase()] || lang || 'Text'
    
    // 创建代码块包装器
    const wrapper = document.createElement('div')
    wrapper.className = 'professional-code-block'
    wrapper.dataset.lang = lang
    
    // 创建头部
    const header = document.createElement('div')
    header.className = 'code-header'
    header.innerHTML = `
      <div class="code-header-left">
        <span class="code-language">${languageName}</span>
        <span class="code-lines-count">${lineCount} 行</span>
      </div>
      <div class="code-header-actions">
        <button class="code-action-btn code-wrap-btn" title="自动换行" aria-label="切换自动换行">
          <svg viewBox="0 0 24 24" width="16" height="16">
            <path fill="currentColor" d="M4 19h6v-2H4v2zM20 5H4v2h16V5zm-3 6H4v2h13.25c1.1 0 2 .9 2 2s-.9 2-2 2H15v-2l-3 3l3 3v-2h1c1.1 0 2-.9 2-2s-.9-2-2-2zM4 11h12v-2H4v2z"/>
          </svg>
        </button>
        <button class="code-action-btn code-collapse-btn" title="折叠代码" aria-label="切换折叠状态">
          <svg viewBox="0 0 24 24" width="16" height="16">
            <path fill="currentColor" d="M7 10l5 5 5-5z"/>
          </svg>
        </button>
        <button class="code-action-btn code-copy-btn" title="复制代码" aria-label="复制代码">
          <svg viewBox="0 0 24 24" width="16" height="16">
            <path fill="currentColor" d="M16 1H4c-1.1 0-2 .9-2 2v14h2V3h12V1zm3 4H8c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h11c1.1 0 2-.9 2-2V7c0-1.1-.9-2-2-2zm0 16H8V7h11v14z"/>
          </svg>
        </button>
      </div>
    `
    
    // 创建代码内容区域
    const content = document.createElement('div')
    content.className = 'code-content'
    
    const scrollWrapper = document.createElement('div')
    scrollWrapper.className = 'code-scroll-wrapper'
    
    // 创建带行号的表格
    const table = document.createElement('table')
    table.className = 'code-table'
    const tbody = document.createElement('tbody')
    
    const lines = rawCode.split('\n')
    lines.forEach((line, lineIndex) => {
      const tr = document.createElement('tr')
      
      const lineNumberTd = document.createElement('td')
      lineNumberTd.className = 'line-number'
      lineNumberTd.textContent = lineIndex + 1
      
      const lineContentTd = document.createElement('td')
      lineContentTd.className = 'line-content'
      
      // 如果有高亮，提取对应行的高亮HTML
      if (code.innerHTML !== rawCode) {
        // 从原始高亮HTML中提取该行
        const tempDiv = document.createElement('div')
        tempDiv.innerHTML = code.innerHTML
        const allText = tempDiv.innerHTML
        const allLines = allText.split('\n')
        if (allLines[lineIndex]) {
          lineContentTd.innerHTML = allLines[lineIndex]
        } else {
          lineContentTd.textContent = line
        }
      } else {
        lineContentTd.textContent = line
      }
      
      tr.appendChild(lineNumberTd)
      tr.appendChild(lineContentTd)
      tbody.appendChild(tr)
    })
    
    table.appendChild(tbody)
    scrollWrapper.appendChild(table)
    content.appendChild(scrollWrapper)
    
    // 组装
    wrapper.appendChild(header)
    wrapper.appendChild(content)
    
    // 替换原始pre元素
    pre.parentNode.replaceChild(wrapper, pre)
    pre.dataset.enhanced = 'true'
    
    // 绑定事件
    const copyBtn = wrapper.querySelector('.code-copy-btn')
    const collapseBtn = wrapper.querySelector('.code-collapse-btn')
    const wrapBtn = wrapper.querySelector('.codewrap-btn, .code-wrap-btn')
    
    copyBtn.addEventListener('click', async () => {
      try {
        await navigator.clipboard.writeText(rawCode)
        copyBtn.classList.add('copied')
        copyBtn.innerHTML = `
          <svg viewBox="0 0 24 24" width="16" height="16">
            <path fill="currentColor" d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z"/>
          </svg>
        `
        copyBtn.title = '已复制'
        toast.value?.success('代码已复制到剪贴板')
        setTimeout(() => {
          copyBtn.classList.remove('copied')
          copyBtn.innerHTML = `
            <svg viewBox="0 0 24 24" width="16" height="16">
              <path fill="currentColor" d="M16 1H4c-1.1 0-2 .9-2 2v14h2V3h12V1zm3 4H8c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h11c1.1 0 2-.9 2-2V7c0-1.1-.9-2-2-2zm0 16H8V7h11v14z"/>
            </svg>
          `
          copyBtn.title = '复制代码'
        }, 2000)
      } catch (err) {
        toast.value?.error('复制失败，请手动复制')
      }
    })
    
    collapseBtn.addEventListener('click', () => {
      wrapper.classList.toggle('is-collapsed')
      const isCollapsed = wrapper.classList.contains('is-collapsed')
      collapseBtn.title = isCollapsed ? '展开代码' : '折叠代码'
      collapseBtn.innerHTML = isCollapsed ? `
        <svg viewBox="0 0 24 24" width="16" height="16">
          <path fill="currentColor" d="M7 14l5-5 5 5z"/>
        </svg>
      ` : `
        <svg viewBox="0 0 24 24" width="16" height="16">
          <path fill="currentColor" d="M7 10l5 5 5-5z"/>
        </svg>
      `
    })
    
    wrapBtn.addEventListener('click', () => {
      content.classList.toggle('wrap-lines')
      const isWrapped = content.classList.contains('wrap-lines')
      wrapBtn.title = isWrapped ? '取消自动换行' : '自动换行'
      wrapBtn.innerHTML = isWrapped ? `
        <svg viewBox="0 0 24 24" width="16" height="16">
          <path fill="currentColor" d="M4 5h16v2H4V5zm0 4h16v2H4V9zm0 4h16v2H4v-2zm0 4h16v2H4v-2z"/>
        </svg>
      ` : `
        <svg viewBox="0 0 24 24" width="16" height="16">
          <path fill="currentColor" d="M4 19h6v-2H4v2zM20 5H4v2h16V5zm-3 6H4v2h13.25c1.1 0 2 .9 2 2s-.9 2-2 2H15v-2l-3 3l3 3v-2h1c1.1 0 2-.9 2-2s-.9-2-2-2zM4 11h12v-2H4v2z"/>
        </svg>
      `
    })
  })
}

watch(renderedContent, async () => {
  await nextTick()
  enhanceCodeBlocks()
})

async function loadArticle() {
  loading.value = true
  try {
    const res = await getArticleDetail(route.params.id)
    article.value = res.data
  } catch (error) {
    console.error('加载文章失败:', error)
    article.value = null
  } finally {
    loading.value = false
  }
}

async function handleLike() {
  try {
    await likeArticle(route.params.id)
    await loadArticle()
  } catch (error) {
    console.error('点赞失败:', error)
  }
}

function copyLink() {
  navigator.clipboard.writeText(window.location.href)
  toast.value?.success('链接已复制到剪贴板')
}

function formatDate(dateStr) {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN', { 
    year: 'numeric', 
    month: 'long', 
    day: 'numeric'
  })
}

function handleImageClick(event) {
  const target = event.target
  if (target.tagName === 'IMG') {
    event.preventDefault()
    event.stopPropagation()
    currentImageUrl.value = target.src
    currentImageAlt.value = target.alt || '图片查看'
    viewerVisible.value = true
  }
}

onMounted(async () => {
  await loadArticle()
  await nextTick()
  enhanceCodeBlocks()
})
</script>

<style scoped>
.article-detail {
  max-width: 800px;
  margin: 0 auto;
}

.article-header {
  margin-bottom: var(--space-xl);
}

.article-header-inner {
  text-align: center;
}

.article-title {
  font-size: 2.5rem;
  font-weight: 700;
  line-height: 1.3;
  margin-bottom: var(--space-md);
  color: var(--text-color);
  letter-spacing: -0.5px;
}

.article-meta {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: var(--space-lg);
  flex-wrap: wrap;
  margin-bottom: var(--space-md);
}

.meta-item {
  display: flex;
  align-items: center;
  gap: var(--space-xs);
  font-size: 0.875rem;
  color: var(--text-secondary);
}

.meta-icon {
  font-size: 14px;
  opacity: 0.7;
}

.article-tags {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: var(--space-sm);
  flex-wrap: wrap;
}

.article-tag {
  display: inline-block;
  padding: var(--space-xs) var(--space-sm);
  font-size: 0.75rem;
  font-weight: 500;
  color: var(--tag-text);
  background-color: var(--tag-bg);
  border-radius: var(--radius-sm);
  transition: all var(--transition-fast);
}

.article-tag:hover {
  color: var(--accent-color);
  background-color: var(--accent-light);
}

.article-divider {
  height: 1px;
  background-color: var(--border-color);
  margin: var(--space-xl) 0;
}

.article-content {
  font-size: 1.0625rem;
  line-height: 1.8;
  color: var(--text-color);
}

.article-content :deep(h1) {
  font-size: 1.75rem;
  margin: var(--space-2xl) 0 var(--space-lg);
  padding-bottom: var(--space-sm);
  border-bottom: 1px solid var(--border-color);
  font-weight: 600;
}

.article-content :deep(h2) {
  font-size: 1.5rem;
  margin: var(--space-xl) 0 var(--space-md);
  font-weight: 600;
}

.article-content :deep(h3) {
  font-size: 1.25rem;
  margin: var(--space-lg) 0 var(--space-sm);
  font-weight: 600;
}

.article-content :deep(p) {
  margin-bottom: var(--space-md);
}

.article-content :deep(a) {
  color: var(--accent-color);
  text-decoration: underline;
  text-underline-offset: 2px;
}

.article-content :deep(a:hover) {
  color: var(--accent-hover);
}

.article-content :deep(code) {
  background-color: var(--code-bg);
  padding: 2px 6px;
  border-radius: var(--radius-sm);
  font-size: 0.9em;
  font-family: var(--font-mono);
  color: var(--code-text);
}

.article-content :deep(pre) {
  margin: 0;
  padding: 0;
  background: none;
  border: none;
  overflow: visible;
}

.article-content :deep(pre code) {
  background: none;
  padding: 0;
  color: inherit;
}

/* 专业代码块样式 - 跟随博客系统主题 */
.article-content :deep(.professional-code-block) {
  margin: 1.5rem 0;
  border-radius: 12px;
  overflow: hidden;
  background: var(--code-bg);
  border: 1px solid var(--border-color);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

.article-content :deep(.professional-code-block:hover) {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
}

.article-content :deep(.professional-code-block.is-collapsed) {
  max-height: 48px;
}

/* 代码块头部 */
.article-content :deep(.code-header) {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.75rem 1rem;
  background: var(--bg-secondary);
  border-bottom: 1px solid var(--border-color);
  user-select: none;
}

.article-content :deep(.code-header-left) {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.article-content :deep(.code-language) {
  font-size: 0.8125rem;
  font-weight: 600;
  color: var(--text-color);
  letter-spacing: 0.3px;
}

.article-content :deep(.code-lines-count) {
  font-size: 0.75rem;
  color: var(--text-tertiary);
}

.article-content :deep(.code-header-actions) {
  display: flex;
  align-items: center;
  gap: 0.25rem;
}

.article-content :deep(.code-action-btn) {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 28px;
  height: 28px;
  padding: 0;
  border: none;
  background: transparent;
  color: var(--text-tertiary);
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.article-content :deep(.code-action-btn:hover) {
  background: var(--hover-bg);
  color: var(--text-color);
}

.article-content :deep(.code-copy-btn.copied) {
  color: var(--success-color);
}

.article-content :deep(.code-copy-btn.copied:hover) {
  background: rgba(40, 167, 69, 0.1);
}

/* 代码内容区域 */
.article-content :deep(.code-content) {
  overflow: auto;
  max-height: 600px;
  transition: max-height 0.3s ease;
}

.article-content :deep(.professional-code-block.is-collapsed .code-content) {
  max-height: 0;
  overflow: hidden;
}

.article-content :deep(.code-scroll-wrapper) {
  overflow-x: auto;
  overflow-y: auto;
}

.article-content :deep(.code-content.wrap-lines .code-scroll-wrapper) {
  overflow-x: hidden;
}

.article-content :deep(.code-content.wrap-lines .line-content) {
  white-space: pre-wrap;
  word-break: break-all;
}

/* 代码表格布局 - 无横线设计 */
.article-content :deep(.code-table) {
  width: 100%;
  border-collapse: collapse;
  font-family: 'JetBrains Mono', 'Fira Code', 'SF Mono', 'Cascadia Code', 'Consolas', monospace;
  font-size: 0.875rem;
  line-height: 1.7;
  tab-size: 2;
  -moz-tab-size: 2;
}

.article-content :deep(.code-table tbody) {
  counter-reset: line;
}

.article-content :deep(.line-number) {
  width: 3rem;
  min-width: 3rem;
  padding: 0 0.75rem 0 1rem;
  text-align: right;
  color: var(--text-tertiary);
  background: var(--code-bg);
  user-select: none;
  font-size: 0.8125rem;
  vertical-align: top;
  opacity: 0.6;
}

.article-content :deep(.line-content) {
  padding: 0 1rem 0 0.75rem;
  color: var(--text-color);
  vertical-align: top;
  white-space: pre;
}

/* 代码行悬停效果 */
.article-content :deep(.code-table tr:hover .line-number) {
  color: var(--text-secondary);
  opacity: 1;
}

.article-content :deep(.code-table tr:hover .line-content) {
  background: var(--hover-bg);
}

/* 语法高亮样式 - Gemini 风格 */
.article-content :deep(.code-table .hljs-keyword) { color: #c678dd; }
.article-content :deep(.code-table .hljs-built_in) { color: #e5c07b; }
.article-content :deep(.code-table .hljs-type) { color: #e5c07b; }
.article-content :deep(.code-table .hljs-literal) { color: #c678dd; }
.article-content :deep(.code-table .hljs-number) { color: #d19a66; }
.article-content :deep(.code-table .hljs-regexp) { color: #e06c75; }
.article-content :deep(.code-table .hljs-string) { color: #98c379; }
.article-content :deep(.code-table .hljs-title) { color: #61afef; }
.article-content :deep(.code-table .hljs-function) { color: #61afef; }
.article-content :deep(.code-table .hljs-params) { color: #abb2bf; }
.article-content :deep(.code-table .hljs-comment) { color: #7f848e; font-style: italic; }
.article-content :deep(.code-table .hljs-meta) { color: #7f848e; }
.article-content :deep(.code-table .hljs-tag) { color: #e06c75; }
.article-content :deep(.code-table .hljs-attribute) { color: #d19a66; }
.article-content :deep(.code-table .hljs-variable) { color: #e06c75; }
.article-content :deep(.code-table .hljs-selector-tag) { color: #e06c75; }
.article-content :deep(.code-table .hljs-selector-class) { color: #61afef; }
.article-content :deep(.code-table .hljs-selector-id) { color: #61afef; }
.article-content :deep(.code-table .hljs-selector-pseudo) { color: #61afef; }
.article-content :deep(.code-table .hljs-section) { color: #e5c07b; }
.article-content :deep(.code-table .hljs-name) { color: #e06c75; }
.article-content :deep(.code-table .hljs-attr) { color: #d19a66; }
.article-content :deep(.code-table .hljs-symbol) { color: #56b6c2; }
.article-content :deep(.code-table .hljs-bullet) { color: #56b6c2; }
.article-content :deep(.code-table .hljs-link) { color: #61afef; text-decoration: underline; }
.article-content :deep(.code-table .hljs-emphasis) { font-style: italic; }
.article-content :deep(.code-table .hljs-strong) { font-weight: bold; }
.article-content :deep(.code-table .hljs-formula) { color: #abb2bf; }
.article-content :deep(.code-table .hljs-quote) { color: #7f848e; font-style: italic; }

/* 滚动条样式 */
.article-content :deep(.code-scroll-wrapper::-webkit-scrollbar) {
  width: 6px;
  height: 6px;
}

.article-content :deep(.code-scroll-wrapper::-webkit-scrollbar-track) {
  background: transparent;
}

.article-content :deep(.code-scroll-wrapper::-webkit-scrollbar-thumb) {
  background: var(--border-color);
  border-radius: 3px;
}

.article-content :deep(.code-scroll-wrapper::-webkit-scrollbar-thumb:hover) {
  background: var(--text-tertiary);
}

.article-content :deep(blockquote) {
  border-left: 4px solid var(--accent-color);
  padding: var(--space-md) var(--space-lg);
  margin: var(--space-md) 0;
  color: var(--text-color);
  background-color: var(--bg-secondary);
  border-radius: 0 var(--radius-md) var(--radius-md) 0;
  font-style: italic;
}

.article-content :deep(img) {
  max-width: 100%;
  border-radius: var(--radius-md);
  margin: var(--space-md) 0;
  box-shadow: var(--card-shadow);
  cursor: zoom-in;
  transition: transform 0.2s ease;
}

.article-content :deep(img:hover) {
  transform: scale(1.02);
}

.article-content :deep(ul), .article-content :deep(ol) {
  padding-left: var(--space-xl);
  margin-bottom: var(--space-md);
}

.article-content :deep(li) {
  margin-bottom: var(--space-sm);
}

.article-content :deep(li input[type="checkbox"]) {
  margin-right: 8px;
}

.article-content :deep(hr) {
  border: none;
  height: 1px;
  background-color: var(--border-color);
  margin: var(--space-xl) 0;
}

.article-content :deep(table) {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: var(--space-md);
}

.article-content :deep(th), .article-content :deep(td) {
  border: 1px solid var(--border-color);
  padding: var(--space-sm) var(--space-md);
  text-align: left;
}

.article-content :deep(th) {
  background-color: var(--bg-secondary);
  font-weight: 600;
}

.article-footer {
  margin-top: var(--space-2xl);
}

.article-actions {
  display: flex;
  justify-content: center;
  margin-bottom: var(--space-xl);
}

.like-btn {
  display: inline-flex;
  align-items: center;
  gap: var(--space-sm);
  padding: var(--space-sm) var(--space-xl);
  background-color: var(--card-bg);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-full);
  cursor: pointer;
  font-size: 0.9375rem;
  color: var(--text-color);
  transition: all var(--transition-fast);
}

.like-btn:hover {
  background-color: #ff4757;
  color: white;
  border-color: #ff4757;
  transform: scale(1.05);
}

.like-icon {
  font-size: 16px;
}

.like-count {
  font-weight: 600;
  min-width: 20px;
}

.article-share {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--space-sm);
  padding: var(--space-lg);
  background-color: var(--bg-secondary);
  border-radius: var(--radius-lg);
}

.share-label {
  font-size: 0.875rem;
  font-weight: 600;
  color: var(--text-secondary);
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.share-buttons {
  display: flex;
  gap: var(--space-sm);
}

.share-btn {
  padding: var(--space-xs) var(--space-md);
  background-color: var(--card-bg);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  cursor: pointer;
  font-size: 0.8125rem;
  color: var(--text-color);
  transition: all var(--transition-fast);
}

.share-btn:hover {
  background-color: var(--accent-color);
  color: white;
  border-color: var(--accent-color);
}

.article-comments {
  margin-top: var(--space-2xl);
}

.comments-title {
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: var(--space-lg);
  color: var(--text-color);
}

.loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--space-md);
  padding: var(--space-3xl) 0;
}

.loading-spinner {
  width: 32px;
  height: 32px;
  border: 3px solid var(--border-color);
  border-top-color: var(--accent-color);
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.loading-text {
  color: var(--text-secondary);
  font-size: 14px;
  margin: 0;
}

.not-found {
  text-align: center;
  padding: var(--space-3xl) var(--space-xl);
}

.not-found-icon {
  font-size: 48px;
  margin-bottom: var(--space-lg);
  opacity: 0.8;
}

.not-found-title {
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: var(--space-sm);
  color: var(--text-color);
}

.not-found-desc {
  color: var(--text-secondary);
  margin-bottom: var(--space-lg);
}

.back-link {
  display: inline-flex;
  align-items: center;
  gap: var(--space-xs);
  padding: var(--space-sm) var(--space-lg);
  background-color: var(--card-bg);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  color: var(--text-color);
  text-decoration: none;
  font-weight: 500;
  transition: all var(--transition-fast);
}

.back-link:hover {
  background-color: var(--accent-color);
  color: white;
  border-color: var(--accent-color);
  text-decoration: none;
}

/* 护眼模式 - Gemini 深色主题优化 */
.eye-care-theme .article-detail {
  /* 整体背景优化 */
}

.eye-care-theme .article-header {
  /* 头部样式优化 */
}

.eye-care-theme .article-title {
  color: var(--text-color);
  font-weight: 700;
  letter-spacing: -0.5px;
}

.eye-care-theme .article-meta {
  color: var(--text-secondary);
}

.eye-care-theme .meta-item {
  color: var(--text-secondary);
}

.eye-care-theme .meta-icon {
  opacity: 0.8;
}

.eye-care-theme .article-tag {
  color: var(--text-secondary);
  background-color: var(--tag-bg);
}

.eye-care-theme .article-tag:hover {
  color: var(--accent-color);
  background-color: var(--accent-light);
}

.eye-care-theme .article-divider {
  background-color: var(--border-color);
}

.eye-care-theme .article-content {
  color: var(--text-color);
}

.eye-care-theme .article-content :deep(h1),
.eye-care-theme .article-content :deep(h2),
.eye-care-theme .article-content :deep(h3),
.eye-care-theme .article-content :deep(h4),
.eye-care-theme .article-content :deep(h5),
.eye-care-theme .article-content :deep(h6) {
  color: var(--text-color);
  font-weight: 700;
}

.eye-care-theme .article-content :deep(h1) {
  border-bottom-color: var(--border-color);
}

.eye-care-theme .article-content :deep(p) {
  color: var(--text-color);
}

.eye-care-theme .article-content :deep(a) {
  color: var(--accent-color);
}

.eye-care-theme .article-content :deep(a:hover) {
  color: var(--accent-hover);
}

.eye-care-theme .article-content :deep(code) {
  background-color: var(--code-bg);
  color: #d4a0a0;
}

.eye-care-theme .article-content :deep(pre code) {
  color: inherit;
}

/* 护眼模式下专业代码块优化 */
.eye-care-theme .professional-code-block {
  background: var(--code-bg);
  border-color: var(--border-color);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.25);
}

.eye-care-theme .professional-code-block:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.35);
}

.eye-care-theme .code-header {
  background: var(--bg-secondary);
  border-color: var(--border-color);
}

.eye-care-theme .code-language {
  color: var(--text-color);
}

.eye-care-theme .code-lines-count {
  color: var(--text-tertiary);
}

.eye-care-theme .code-action-btn {
  color: var(--text-tertiary);
}

.eye-care-theme .code-action-btn:hover {
  background: var(--hover-bg);
  color: var(--text-color);
}

.eye-care-theme .code-copy-btn.copied {
  color: var(--success-color);
}

.eye-care-theme .code-copy-btn.copied:hover {
  background: rgba(126, 184, 126, 0.1);
}

.eye-care-theme .line-number {
  background: var(--code-bg);
  color: var(--text-tertiary);
}

.eye-care-theme .line-content {
  color: var(--text-color);
}

.eye-care-theme .code-table tr:hover .line-number {
  color: var(--text-secondary);
}

.eye-care-theme .code-table tr:hover .line-content {
  background: var(--hover-bg);
}

.eye-care-theme .code-scroll-wrapper::-webkit-scrollbar-track {
  background: transparent;
}

.eye-care-theme .code-scroll-wrapper::-webkit-scrollbar-thumb {
  background: var(--border-color);
}

.eye-care-theme .code-scroll-wrapper::-webkit-scrollbar-thumb:hover {
  background: var(--text-tertiary);
}

/* 护眼模式下语法高亮 - Gemini 深色风格 */
.eye-care-theme .code-table .hljs-keyword { color: #c678dd; }
.eye-care-theme .code-table .hljs-built_in { color: #e5c07b; }
.eye-care-theme .code-table .hljs-type { color: #e5c07b; }
.eye-care-theme .code-table .hljs-literal { color: #c678dd; }
.eye-care-theme .code-table .hljs-number { color: #d19a66; }
.eye-care-theme .code-table .hljs-regexp { color: #e06c75; }
.eye-care-theme .code-table .hljs-string { color: #98c379; }
.eye-care-theme .code-table .hljs-title { color: #61afef; }
.eye-care-theme .code-table .hljs-function { color: #61afef; }
.eye-care-theme .code-table .hljs-params { color: #abb2bf; }
.eye-care-theme .code-table .hljs-comment { color: #7f848e; font-style: italic; }
.eye-care-theme .code-table .hljs-meta { color: #7f848e; }
.eye-care-theme .code-table .hljs-tag { color: #e06c75; }
.eye-care-theme .code-table .hljs-attribute { color: #d19a66; }
.eye-care-theme .code-table .hljs-variable { color: #e06c75; }
.eye-care-theme .code-table .hljs-selector-tag { color: #e06c75; }
.eye-care-theme .code-table .hljs-selector-class { color: #61afef; }
.eye-care-theme .code-table .hljs-selector-id { color: #61afef; }
.eye-care-theme .code-table .hljs-selector-pseudo { color: #61afef; }
.eye-care-theme .code-table .hljs-section { color: #e5c07b; }
.eye-care-theme .code-table .hljs-name { color: #e06c75; }
.eye-care-theme .code-table .hljs-attr { color: #d19a66; }
.eye-care-theme .code-table .hljs-symbol { color: #56b6c2; }
.eye-care-theme .code-table .hljs-bullet { color: #56b6c2; }
.eye-care-theme .code-table .hljs-link { color: #61afef; text-decoration: underline; }
.eye-care-theme .code-table .hljs-emphasis { font-style: italic; }
.eye-care-theme .code-table .hljs-strong { font-weight: bold; }
.eye-care-theme .code-table .hljs-formula { color: #abb2bf; }
.eye-care-theme .code-table .hljs-quote { color: #7f848e; font-style: italic; }

.eye-care-theme .article-content :deep(blockquote) {
  background-color: var(--bg-secondary);
  color: var(--text-color);
  border-left-color: var(--accent-color);
}

.eye-care-theme .article-content :deep(img) {
  box-shadow: var(--card-shadow);
}

.eye-care-theme .article-content :deep(hr) {
  background-color: var(--border-color);
}

.eye-care-theme .article-content :deep(table) {
  border-color: var(--border-color);
}

.eye-care-theme .article-content :deep(th),
.eye-care-theme .article-content :deep(td) {
  border-color: var(--border-color);
}

.eye-care-theme .article-content :deep(th) {
  background-color: var(--bg-secondary);
}

.eye-care-theme .article-actions {
  /* 操作按钮区域 */
}

.eye-care-theme .like-btn {
  background-color: var(--card-bg);
  border-color: var(--border-color);
  color: var(--text-color);
}

.eye-care-theme .like-btn:hover {
  background-color: #ff4757;
  color: white;
  border-color: #ff4757;
}

.eye-care-theme .article-share {
  background-color: var(--bg-secondary);
}

.eye-care-theme .share-label {
  color: var(--text-secondary);
}

.eye-care-theme .share-btn {
  background-color: var(--card-bg);
  border-color: var(--border-color);
  color: var(--text-color);
}

.eye-care-theme .share-btn:hover {
  background-color: var(--accent-color);
  color: white;
  border-color: var(--accent-color);
}

.eye-care-theme .comments-title {
  color: var(--text-color);
}

.eye-care-theme .loading-text {
  color: var(--text-secondary);
}

.eye-care-theme .not-found {
  color: var(--text-color);
}

.eye-care-theme .not-found-title {
  color: var(--text-color);
}

.eye-care-theme .not-found-desc {
  color: var(--text-secondary);
}

.eye-care-theme .back-link {
  background-color: var(--card-bg);
  border-color: var(--border-color);
  color: var(--text-color);
}

.eye-care-theme .back-link:hover {
  background-color: var(--accent-color);
  color: white;
  border-color: var(--accent-color);
}

/* 响应式 - 平板端 */
@media (max-width: 1199px) and (min-width: 768px) {
  .article-title {
    font-size: 2.25rem;
  }
}

/* 响应式 - 移动端 */
@media (max-width: 767px) {
  .article-title {
    font-size: 1.75rem;
  }
  
  .article-meta {
    flex-direction: column;
    gap: var(--space-sm);
  }
  
  .article-content {
    font-size: 1rem;
  }
  
  .article-content :deep(h1) {
    font-size: 1.5rem;
  }
  
  .article-content :deep(h2) {
    font-size: 1.25rem;
  }
  
  /* 移动端代码块优化 */
  .article-content :deep(.professional-code-block) {
    margin: 1rem 0;
    border-radius: 8px;
  }
  
  .article-content :deep(.code-header) {
    padding: 0.625rem 0.75rem;
  }
  
  .article-content :deep(.code-language) {
    font-size: 0.75rem;
  }
  
  .article-content :deep(.code-lines-count) {
    font-size: 0.6875rem;
  }
  
  .article-content :deep(.code-action-btn) {
    width: 24px;
    height: 24px;
  }
  
  .article-content :deep(.code-action-btn svg) {
    width: 14px;
    height: 14px;
  }
  
  .article-content :deep(.code-table) {
    font-size: 0.75rem;
    line-height: 1.6;
  }
  
  .article-content :deep(.line-number) {
    width: 2.5rem;
    min-width: 2.5rem;
    padding: 0 0.5rem 0 0.75rem;
    font-size: 0.6875rem;
  }
  
  .article-content :deep(.line-content) {
    padding: 0 0.75rem 0 0.5rem;
  }
  
  .article-content :deep(.code-content) {
    max-height: 400px;
  }
  
  .article-share {
    flex-direction: row;
    justify-content: center;
    gap: var(--space-md);
  }
}

/* 响应式 - 小移动端 */
@media (max-width: 479px) {
  .article-title {
    font-size: 1.5rem;
  }
  
  .article-content {
    font-size: 0.9375rem;
  }
  
  /* 小屏移动端代码块优化 */
  .article-content :deep(.professional-code-block) {
    margin: 0.75rem 0;
    border-radius: 6px;
  }
  
  .article-content :deep(.code-header) {
    padding: 0.5rem 0.625rem;
  }
  
  .article-content :deep(.code-header-left) {
    gap: 0.5rem;
  }
  
  .article-content :deep(.code-language) {
    font-size: 0.6875rem;
  }
  
  .article-content :deep(.code-lines-count) {
    display: none;
  }
  
  .article-content :deep(.code-table) {
    font-size: 0.6875rem;
    line-height: 1.5;
  }
  
  .article-content :deep(.line-number) {
    width: 2rem;
    min-width: 2rem;
    padding: 0 0.375rem 0 0.5rem;
    font-size: 0.625rem;
  }
  
  .article-content :deep(.line-content) {
    padding: 0 0.5rem 0 0.375rem;
  }
  
  .like-btn {
    padding: var(--space-sm) var(--space-lg);
  }
  
  .share-buttons {
    flex-direction: column;
    width: 100%;
  }
  
  .share-btn {
    width: 100%;
    text-align: center;
  }
}

/* 护眼模式 - 移动端响应式优化 */
@media (max-width: 767px) {
  .eye-care-theme .article-content :deep(.professional-code-block) {
    margin: 1rem 0;
    border-radius: 8px;
  }
  
  .eye-care-theme .article-content :deep(.code-header) {
    padding: 0.625rem 0.75rem;
  }
  
  .eye-care-theme .article-content :deep(.code-language) {
    font-size: 0.75rem;
  }
  
  .eye-care-theme .article-content :deep(.code-lines-count) {
    font-size: 0.6875rem;
  }
  
  .eye-care-theme .article-content :deep(.code-action-btn) {
    width: 24px;
    height: 24px;
  }
  
  .eye-care-theme .article-content :deep(.code-table) {
    font-size: 0.75rem;
    line-height: 1.6;
  }
  
  .eye-care-theme .article-content :deep(.line-number) {
    width: 2.5rem;
    min-width: 2.5rem;
    padding: 0 0.5rem 0 0.75rem;
    font-size: 0.6875rem;
  }
  
  .eye-care-theme .article-content :deep(.line-content) {
    padding: 0 0.75rem 0 0.5rem;
  }
  
  .eye-care-theme .article-content :deep(.code-content) {
    max-height: 400px;
  }
}

@media (max-width: 479px) {
  .eye-care-theme .article-content :deep(.professional-code-block) {
    margin: 0.75rem 0;
    border-radius: 6px;
  }
  
  .eye-care-theme .article-content :deep(.code-header) {
    padding: 0.5rem 0.625rem;
  }
  
  .eye-care-theme .article-content :deep(.code-header-left) {
    gap: 0.5rem;
  }
  
  .eye-care-theme .article-content :deep(.code-language) {
    font-size: 0.6875rem;
  }
  
  .eye-care-theme .article-content :deep(.code-lines-count) {
    display: none;
  }
  
  .eye-care-theme .article-content :deep(.code-table) {
    font-size: 0.6875rem;
    line-height: 1.5;
  }
  
  .eye-care-theme .article-content :deep(.line-number) {
    width: 2rem;
    min-width: 2rem;
    padding: 0 0.375rem 0 0.5rem;
    font-size: 0.625rem;
  }
  
  .eye-care-theme .article-content :deep(.line-content) {
    padding: 0 0.5rem 0 0.375rem;
  }
}

/* 打印样式 */
@media print {
  .article-actions,
  .article-share,
  .article-comments {
    display: none;
  }
  
  .article-divider {
    display: none;
  }
}
</style>
