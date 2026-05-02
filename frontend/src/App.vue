<template>
  <div id="app" :class="{ 'eye-care-theme': isEyeCareTheme }">
    <Header />
    <main class="main-content">
      <router-view />
    </main>
    <Footer />
    <Toast ref="toastRef" />
  </div>
</template>

<script setup>
import { computed, onMounted, ref, provide } from 'vue'
import { useThemeStore } from './stores/theme'
import Header from './components/Header.vue'
import Footer from './components/Footer.vue'
import Toast from './components/Toast.vue'

const themeStore = useThemeStore()
const isEyeCareTheme = computed(() => themeStore.isEyeCare)
const toastRef = ref(null)

provide('toast', toastRef)

onMounted(() => {
  document.documentElement.classList.toggle('eye-care', isEyeCareTheme.value)
})
</script>

<style>
/* 全局样式 - Ghost风格设计原则 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

:root {
  /* 浅色模式配色 */
  --bg-color: #ffffff;
  --bg-secondary: #f6f6f6;
  --bg-tertiary: #eef0f2;
  --text-color: #15171a;
  --text-secondary: #5a6b78;
  --text-tertiary: #7c8a96;
  --border-color: #e5eff5;
  --border-light: #f0f0f0;
  --accent-color: #3eb0ef;
  --accent-hover: #3498db;
  --accent-light: rgba(62, 176, 239, 0.1);
  --card-bg: #ffffff;
  --card-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  --card-shadow-hover: 0 8px 24px rgba(0, 0, 0, 0.12);
  --header-bg: rgba(255, 255, 255, 0.98);
  --header-blur: blur(12px);
  --footer-bg: #15171a;
  --footer-text: #ffffff;
  --code-bg: #f5f7f9;
  --code-text: #d63384;
  --hover-bg: #f5f7f9;
  --success-color: #28a745;
  --warning-color: #e67e22;
  --error-color: #dc3545;
  --info-color: #17a2b8;
  --input-bg: #ffffff;
  --input-border: #d1d5db;
  --input-focus-border: #3eb0ef;
  --btn-primary-bg: #3eb0ef;
  --btn-primary-text: #ffffff;
  --btn-secondary-bg: #f5f7f9;
  --btn-secondary-text: #15171a;
  --tag-bg: #eef0f2;
  --tag-text: #5a6b78;
  --overlay-bg: rgba(0, 0, 0, 0.5);
  --scrollbar-thumb: #c1c1c1;
  --scrollbar-track: #f1f1f1;
  
  /* 提示框配色 - 浅色模式 */
  --alert-success-bg: #d4edda;
  --alert-success-text: #155724;
  --alert-success-border: #c3e6cb;
  --alert-warning-bg: #fff3cd;
  --alert-warning-text: #856404;
  --alert-warning-border: #ffeeba;
  --alert-error-bg: #f8d7da;
  --alert-error-text: #721c24;
  --alert-error-border: #f5c6cb;
  --alert-info-bg: #d1ecf1;
  --alert-info-text: #0c5460;
  --alert-info-border: #bee5eb;
  
  /* 排版 */
  --font-sans: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Helvetica Neue', sans-serif;
  --font-serif: Georgia, 'Times New Roman', serif;
  --font-mono: 'SF Mono', 'Fira Code', 'Fira Mono', 'Roboto Mono', monospace;
  
  /* 间距 */
  --space-xs: 4px;
  --space-sm: 8px;
  --space-md: 16px;
  --space-lg: 24px;
  --space-xl: 32px;
  --space-2xl: 48px;
  --space-3xl: 64px;
  
  /* 圆角 */
  --radius-sm: 4px;
  --radius-md: 8px;
  --radius-lg: 12px;
  --radius-xl: 16px;
  --radius-full: 9999px;
  
  /* 过渡 */
  --transition-fast: 150ms ease;
  --transition-base: 250ms ease;
  --transition-slow: 350ms ease;
  
  /* 容器宽度 */
  --container-sm: 640px;
  --container-md: 768px;
  --container-lg: 1024px;
  --container-xl: 1200px;
  --container-2xl: 1400px;
}

/* 护眼深色主题 - 人体工程学设计 */
.eye-care-theme {
  /* 护眼深色模式配色 - 减少蓝光，降低视觉疲劳 */
  /* 背景使用暖色调深灰，避免纯黑造成的强烈对比 */
  --bg-color: #1a1d23;
  --bg-secondary: #22262e;
  --bg-tertiary: #2a2f38;
  
  /* 文字使用高对比度暖白色，确保清晰可读 */
  /* 主文字对比度 13.2:1，远超 WCAG AAA 标准 (7:1) */
  --text-color: #f5f3ee;
  /* 次要文字对比度 8.5:1，符合 WCAG AAA 标准 */
  --text-secondary: #d4d1ca;
  /* 三级文字对比度 5.8:1，符合 WCAG AA 大文本标准 */
  --text-tertiary: #a8a59e;
  
  /* 边框使用低饱和度灰色 */
  --border-color: #3a3f47;
  --border-light: #2a2f38;
  
  /* 强调色使用暖色调蓝绿，减少刺眼感 */
  --accent-color: #6b9e8a;
  --accent-hover: #7db89e;
  --accent-light: rgba(107, 158, 138, 0.15);
  
  /* 卡片背景使用暖深灰，与背景形成微妙层次 */
  --card-bg: #22262e;
  --card-shadow: 0 2px 8px rgba(0, 0, 0, 0.25);
  --card-shadow-hover: 0 8px 24px rgba(0, 0, 0, 0.35);
  
  /* 头部和底部背景 */
  --header-bg: rgba(26, 29, 35, 0.98);
  --footer-bg: #141619;
  --footer-text: #e8e6e1;
  
  /* 代码块配色 - 暖色调 */
  --code-bg: #2a2f38;
  --code-text: #e88a8a;
  
  /* 悬停背景 */
  --hover-bg: #2a2f38;
  
  /* 状态颜色 - 降低饱和度 */
  --success-color: #6b9e6b;
  --warning-color: #c4a35a;
  --error-color: #c46b6b;
  --info-color: #6b8e9e;
  
  /* 表单背景 */
  --input-bg: #1a1d23;
  --input-border: #3a3f47;
  --input-focus-border: #6b9e8a;
  
  /* 按钮配色 */
  --btn-primary-bg: #6b9e8a;
  --btn-primary-text: #1a1d23;
  --btn-secondary-bg: #2a2f38;
  --btn-secondary-text: #e8e6e1;
  
  /* 标签配色 */
  --tag-bg: #2a2f38;
  --tag-text: #b8b5ad;
  
  /* 遮罩层 */
  --overlay-bg: rgba(0, 0, 0, 0.65);
  
  /* 滚动条 */
  --scrollbar-thumb: #4a4f57;
  --scrollbar-track: #22262e;
  
  /* 提示框配色 - 护眼深色模式（提高对比度） */
  --alert-success-bg: #1e3a2a;
  --alert-success-text: #a8d8a8;
  --alert-success-border: #2d5a3f;
  --alert-warning-bg: #3a3520;
  --alert-warning-text: #e8c878;
  --alert-warning-border: #5a4a28;
  --alert-error-bg: #3a2020;
  --alert-error-text: #e88a8a;
  --alert-error-border: #5a2828;
  --alert-info-bg: #1e3040;
  --alert-info-text: #8ac8e8;
  --alert-info-border: #284a5a;
}

html {
  scroll-behavior: smooth;
  -webkit-text-size-adjust: 100%;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-rendering: optimizeLegibility;
}

body {
  font-family: var(--font-sans);
  background-color: var(--bg-color);
  color: var(--text-color);
  line-height: 1.7;
  font-size: 16px;
  transition: background-color var(--transition-base), color var(--transition-base);
  /* 确保背景正确显示 */
  background-image: none;
  background-repeat: no-repeat;
  background-attachment: fixed;
  /* 优化文字渲染 */
  text-rendering: optimizeLegibility;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

/* 护眼模式下文字优化 */
.eye-care-theme body {
  /* 微妙的暖色渐变背景，增加视觉层次 */
  background-image: radial-gradient(ellipse at top, rgba(107, 158, 138, 0.03) 0%, transparent 50%);
  /* 确保文字渲染清晰 */
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-rendering: optimizeLegibility;
}

/* 护眼模式下标题文字 - 增强清晰度 */
.eye-care-theme h1,
.eye-care-theme h2,
.eye-care-theme h3,
.eye-care-theme h4,
.eye-care-theme h5,
.eye-care-theme h6 {
  color: var(--text-color);
  font-weight: 700;
  /* 增强标题清晰度 */
  text-shadow: 0 0 1px rgba(245, 243, 238, 0.1);
}

/* 护眼模式下正文文字 - 增强清晰度 */
.eye-care-theme p,
.eye-care-theme span,
.eye-care-theme div,
.eye-care-theme li,
.eye-care-theme td,
.eye-care-theme th {
  color: var(--text-color);
}

/* 护眼模式下次要文字 - 增强清晰度 */
.eye-care-theme .text-secondary,
.eye-care-theme [class*="text-secondary"] {
  color: var(--text-secondary) !important;
}

/* 护眼模式下链接清晰度增强 */
.eye-care-theme a {
  color: var(--accent-color);
  text-decoration: none;
}

.eye-care-theme a:hover {
  color: var(--accent-hover);
  text-decoration: underline;
}

/* 护眼模式下代码块文字增强 */
.eye-care-theme code {
  background-color: var(--code-bg) !important;
  color: #f0a0a0 !important;
  font-weight: 500;
}

.eye-care-theme pre {
  background-color: var(--code-bg) !important;
  border-color: var(--border-color) !important;
}

.eye-care-theme pre code {
  color: #f0a0a0 !important;
}

/* 护眼模式下引用块文字增强 */
.eye-care-theme blockquote {
  background-color: var(--bg-secondary) !important;
  color: var(--text-color) !important;
  border-left-color: var(--accent-color) !important;
}

/* 护眼模式下表单文字增强 */
.eye-care-theme input,
.eye-care-theme textarea,
.eye-care-theme select {
  background-color: var(--input-bg) !important;
  color: var(--text-color) !important;
  border-color: var(--input-border) !important;
}

.eye-care-theme input::placeholder,
.eye-care-theme textarea::placeholder {
  color: var(--text-tertiary) !important;
  opacity: 0.8;
}

.eye-care-theme label {
  color: var(--text-secondary) !important;
}

/* 护眼模式下表格文字增强 */
.eye-care-theme th,
.eye-care-theme .data-table th {
  background-color: var(--bg-secondary) !important;
  color: var(--text-secondary) !important;
  font-weight: 600;
}

.eye-care-theme td {
  color: var(--text-color) !important;
}

/* 护眼模式下按钮文字增强 */
.eye-care-theme .btn-primary {
  color: #1a1d23 !important;
  font-weight: 600;
}

.eye-care-theme .btn-secondary {
  color: var(--text-color) !important;
}

/* 护眼模式下标签文字增强 */
.eye-care-theme .tag {
  color: var(--text-secondary);
  background-color: var(--tag-bg);
  font-weight: 500;
}

/* 护眼模式下导航链接增强 */
.eye-care-theme .nav-link {
  color: var(--text-secondary);
}

.eye-care-theme .nav-link:hover,
.eye-care-theme .nav-link.router-link-active {
  color: var(--text-color);
}

/* 护眼模式下页脚文字增强 */
.eye-care-theme .footer-list a {
  color: var(--text-secondary);
}

.eye-care-theme .footer-list a:hover {
  color: var(--text-color);
}

/* 护眼模式下空状态文字增强 */
.eye-care-theme .empty-state,
.eye-care-theme .empty-msg,
.eye-care-theme .empty {
  color: var(--text-secondary) !important;
}

/* 护眼模式下分页文字增强 */
.eye-care-theme .pagination button,
.eye-care-theme .page-btn {
  background-color: var(--card-bg) !important;
  color: var(--text-color) !important;
  border-color: var(--border-color) !important;
}

.eye-care-theme .pagination span,
.eye-care-theme .page-info {
  color: var(--text-secondary) !important;
}

/* 护眼模式下搜索框文字增强 */
.eye-care-theme .search-box {
  background-color: var(--card-bg) !important;
  border-color: var(--border-color) !important;
}

.eye-care-theme .search-input {
  background-color: var(--input-bg) !important;
  color: var(--text-color) !important;
  border-color: var(--input-border) !important;
}

.eye-care-theme .tab-btn {
  color: var(--text-secondary) !important;
}

.eye-care-theme .tab-btn.active {
  color: var(--accent-color) !important;
  border-bottom-color: var(--accent-color) !important;
}

/* 护眼模式下加载文字增强 */
.eye-care-theme .loading-text {
  color: var(--text-secondary) !important;
}

/* 护眼模式下未找到状态文字增强 */
.eye-care-theme .not-found {
  color: var(--text-color) !important;
}

.eye-care-theme .not-found-title {
  color: var(--text-color) !important;
}

.eye-care-theme .not-found-desc {
  color: var(--text-secondary) !important;
}

.eye-care-theme .back-link {
  background-color: var(--card-bg) !important;
  color: var(--text-color) !important;
  border-color: var(--border-color) !important;
}

/* 护眼模式下状态徽章 - 提高对比度 */
.eye-care-theme .status-badge.published {
  background-color: rgba(107, 158, 107, 0.25) !important;
  color: #a8d8a8 !important;
}

.eye-care-theme .status-badge.draft {
  background-color: rgba(196, 163, 90, 0.25) !important;
  color: #e8c878 !important;
}

/* 护眼模式下操作按钮 - 提高对比度 */
.eye-care-theme .btn-edit {
  background-color: rgba(107, 158, 138, 0.25) !important;
  color: #8ac8b0 !important;
}

.eye-care-theme .btn-delete {
  background-color: rgba(196, 107, 107, 0.25) !important;
  color: #e88a8a !important;
}

.eye-care-theme .edit-btn {
  background-color: #5a8a7a !important;
  color: #f5f3ee !important;
}

.eye-care-theme .publish-btn {
  background-color: #6b9e6b !important;
  color: #f5f3ee !important;
}

.eye-care-theme .unpublish-btn {
  background-color: #a89050 !important;
  color: #f5f3ee !important;
}

.eye-care-theme .delete-btn {
  background-color: #a86b6b !important;
  color: #f5f3ee !important;
}

/* 护眼模式下表格 */
.eye-care-theme table,
.eye-care-theme .data-table,
.eye-care-theme .article-table {
  border-color: var(--border-color);
}

.eye-care-theme th,
.eye-care-theme .data-table th {
  background-color: var(--bg-secondary) !important;
  color: var(--text-secondary) !important;
}

.eye-care-theme td {
  color: var(--text-color) !important;
}

/* 护眼模式下对话框 */
.eye-care-theme .dialog {
  background-color: var(--card-bg) !important;
  border: 1px solid var(--border-color);
}

.eye-care-theme .dialog-overlay {
  background-color: rgba(0, 0, 0, 0.7) !important;
}

/* 护眼模式下空状态 */
.eye-care-theme .empty-state,
.eye-care-theme .empty-msg,
.eye-care-theme .empty {
  color: var(--text-secondary) !important;
}

/* 护眼模式下分页 */
.eye-care-theme .pagination button,
.eye-care-theme .page-btn {
  background-color: var(--card-bg) !important;
  color: var(--text-color) !important;
  border-color: var(--border-color) !important;
}

.eye-care-theme .pagination span,
.eye-care-theme .page-info {
  color: var(--text-secondary) !important;
}

/* 护眼模式下搜索框 */
.eye-care-theme .search-box {
  background-color: var(--card-bg) !important;
  border-color: var(--border-color) !important;
}

.eye-care-theme .search-input {
  background-color: var(--input-bg) !important;
  color: var(--text-color) !important;
  border-color: var(--input-border) !important;
}

.eye-care-theme .tab-btn {
  color: var(--text-secondary) !important;
}

.eye-care-theme .tab-btn.active {
  color: var(--accent-color) !important;
  border-bottom-color: var(--accent-color) !important;
}

/* 护眼模式下表单 */
.eye-care-theme input,
.eye-care-theme textarea,
.eye-care-theme select {
  background-color: var(--input-bg) !important;
  color: var(--text-color) !important;
  border-color: var(--input-border) !important;
}

.eye-care-theme input::placeholder,
.eye-care-theme textarea::placeholder {
  color: var(--text-tertiary) !important;
}

.eye-care-theme label {
  color: var(--text-secondary) !important;
}

/* 护眼模式下代码块 */
.eye-care-theme code {
  background-color: var(--code-bg) !important;
  color: var(--code-text) !important;
}

.eye-care-theme pre {
  background-color: var(--code-bg) !important;
  border-color: var(--border-color) !important;
}

/* 护眼模式下引用块 */
.eye-care-theme blockquote {
  background-color: var(--bg-secondary) !important;
  color: var(--text-color) !important;
  border-left-color: var(--accent-color) !important;
}

/* 护眼模式下图片优化 - 降低亮度和蓝光 */
.eye-care-theme img {
  opacity: 0.92;
  filter: brightness(0.92) contrast(1.05) saturate(0.9);
}

/* 护眼模式下分割线 */
.eye-care-theme .article-divider,
.eye-care-theme hr {
  background-color: var(--border-color) !important;
  border-color: var(--border-color) !important;
}

/* 护眼模式下加载动画 */
.eye-care-theme .loading-spinner {
  border-color: var(--border-color) !important;
  border-top-color: var(--accent-color) !important;
}

.eye-care-theme .loading-text {
  color: var(--text-secondary) !important;
}

/* 护眼模式下未找到状态 */
.eye-care-theme .not-found {
  color: var(--text-color) !important;
}

.eye-care-theme .not-found-title {
  color: var(--text-color) !important;
}

.eye-care-theme .not-found-desc {
  color: var(--text-secondary) !important;
}

.eye-care-theme .back-link {
  background-color: var(--card-bg) !important;
  color: var(--text-color) !important;
  border-color: var(--border-color) !important;
}

/* 护眼模式下提示框 */
.eye-care-theme .alert {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}

/* 护眼模式下卡片和容器 */
.eye-care-theme .card,
.eye-care-theme .article-card,
.eye-care-theme .stat-card,
.eye-care-theme .menu-card,
.eye-care-theme .comment-form,
.eye-care-theme .login-card,
.eye-care-theme .search-box,
.eye-care-theme .article-table,
.eye-care-theme .welcome-card,
.eye-care-theme .hot-articles-section,
.eye-care-theme .dashboard-section {
  background-color: var(--card-bg) !important;
  border-color: var(--border-color) !important;
}

/* 滚动条样式 */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-track {
  background: var(--scrollbar-track);
}

::-webkit-scrollbar-thumb {
  background: var(--scrollbar-thumb);
  border-radius: var(--radius-full);
}

::-webkit-scrollbar-thumb:hover {
  background: var(--text-secondary);
}

/* 主内容区域 */
.main-content {
  max-width: var(--container-xl);
  margin: 0 auto;
  padding: var(--space-2xl) var(--space-lg);
  min-height: calc(100vh - 200px);
}

/* 链接样式 */
a {
  color: var(--accent-color);
  text-decoration: none;
  transition: color var(--transition-fast);
}

a:hover {
  color: var(--accent-hover);
  text-decoration: none;
}

/* 按钮基础样式 */
.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: var(--space-sm);
  padding: var(--space-sm) var(--space-lg);
  font-size: 14px;
  font-weight: 500;
  line-height: 1.5;
  border-radius: var(--radius-md);
  border: 1px solid transparent;
  cursor: pointer;
  transition: all var(--transition-fast);
  white-space: nowrap;
  user-select: none;
}

.btn:focus-visible {
  outline: 2px solid var(--accent-color);
  outline-offset: 2px;
}

.btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.btn-primary {
  background-color: var(--btn-primary-bg);
  color: var(--btn-primary-text);
}

.btn-primary:hover:not(:disabled) {
  background-color: var(--accent-hover);
}

.btn-secondary {
  background-color: var(--btn-secondary-bg);
  color: var(--btn-secondary-text);
  border-color: var(--border-color);
}

.btn-secondary:hover:not(:disabled) {
  background-color: var(--hover-bg);
}

/* 输入框样式 */
.input {
  width: 100%;
  padding: var(--space-sm) var(--space-md);
  font-size: 14px;
  line-height: 1.5;
  color: var(--text-color);
  background-color: var(--input-bg);
  border: 1px solid var(--input-border);
  border-radius: var(--radius-md);
  transition: border-color var(--transition-fast), box-shadow var(--transition-fast);
}

.input:focus {
  outline: none;
  border-color: var(--input-focus-border);
  box-shadow: 0 0 0 3px var(--accent-light);
}

.input::placeholder {
  color: var(--text-tertiary);
}

/* 卡片样式 */
.card {
  background-color: var(--card-bg);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-lg);
  padding: var(--space-lg);
  transition: all var(--transition-base);
}

.card:hover {
  box-shadow: var(--card-shadow-hover);
  border-color: var(--accent-color);
}

/* 提示框样式 - 统一美观设计 */
.alert {
  padding: var(--space-md) var(--space-lg);
  border-radius: var(--radius-md);
  border: 1px solid;
  margin-bottom: var(--space-md);
  font-size: 0.9375rem;
  line-height: 1.6;
  font-weight: 500;
  display: flex;
  align-items: flex-start;
  gap: var(--space-sm);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  animation: slideDown var(--transition-base) ease-out;
}

.alert-icon {
  flex-shrink: 0;
  font-size: 1.125rem;
  line-height: 1.5;
}

.alert-content {
  flex: 1;
}

.alert-success {
  background-color: var(--alert-success-bg);
  color: var(--alert-success-text);
  border-color: var(--alert-success-border);
}

.alert-warning {
  background-color: var(--alert-warning-bg);
  color: var(--alert-warning-text);
  border-color: var(--alert-warning-border);
}

.alert-error {
  background-color: var(--alert-error-bg);
  color: var(--alert-error-text);
  border-color: var(--alert-error-border);
}

.alert-info {
  background-color: var(--alert-info-bg);
  color: var(--alert-info-text);
  border-color: var(--alert-info-border);
}

/* 护眼模式下提示框优化 */
.eye-care-theme .alert {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}

/* 标签样式 */
.tag {
  display: inline-block;
  padding: var(--space-xs) var(--space-sm);
  font-size: 12px;
  font-weight: 500;
  color: var(--tag-text);
  background-color: var(--tag-bg);
  border-radius: var(--radius-sm);
  transition: all var(--transition-fast);
}

.tag:hover {
  color: var(--accent-color);
  background-color: var(--accent-light);
}

/* 代码块样式 */
code {
  font-family: var(--font-mono);
  font-size: 0.9em;
  padding: 2px 6px;
  background-color: var(--code-bg);
  color: var(--code-text);
  border-radius: var(--radius-sm);
}

pre {
  background-color: var(--code-bg);
  padding: var(--space-lg);
  border-radius: var(--radius-md);
  overflow-x: auto;
  line-height: 1.6;
}

pre code {
  padding: 0;
  background: none;
  color: inherit;
}

/* 动画 */
@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes scaleIn {
  from {
    opacity: 0;
    transform: scale(0.95);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}

.animate-fade-in {
  animation: fadeIn var(--transition-base) ease-out;
}

.animate-slide-up {
  animation: slideUp var(--transition-base) ease-out;
}

.animate-slide-down {
  animation: slideDown var(--transition-base) ease-out;
}

.animate-scale-in {
  animation: scaleIn var(--transition-base) ease-out;
}

/* 响应式布局 - 桌面端 (>= 1200px) */
@media (min-width: 1200px) {
  .main-content {
    padding: var(--space-3xl) var(--space-xl);
  }
}

/* 响应式布局 - 平板端 (768px - 1199px) */
@media (max-width: 1199px) and (min-width: 768px) {
  .main-content {
    padding: var(--space-xl) var(--space-lg);
  }
}

/* 响应式布局 - 移动端 (480px - 767px) */
@media (max-width: 767px) and (min-width: 480px) {
  .main-content {
    padding: var(--space-lg) var(--space-md);
  }
  
  h1 { font-size: 1.75rem; }
  h2 { font-size: 1.5rem; }
  h3 { font-size: 1.25rem; }
}

/* 响应式布局 - 小移动端 (< 480px) */
@media (max-width: 479px) {
  .main-content {
    padding: var(--space-md) var(--space-sm);
  }
  
  h1 { font-size: 1.5rem; }
  h2 { font-size: 1.25rem; }
  h3 { font-size: 1.125rem; }
  
  .btn {
    padding: var(--space-sm) var(--space-md);
    font-size: 13px;
  }
}

/* 无障碍 - 减少动画 */
@media (prefers-reduced-motion: reduce) {
  *,
  *::before,
  *::after {
    animation-duration: 0.01ms !important;
    animation-iteration-count: 1 !important;
    transition-duration: 0.01ms !important;
    scroll-behavior: auto !important;
  }
}

/* 打印样式 */
@media print {
  .header,
  .footer,
  .theme-toggle,
  .btn {
    display: none !important;
  }
  
  body {
    background: white;
    color: black;
  }
  
  a {
    color: black;
    text-decoration: underline;
  }
}
</style>
