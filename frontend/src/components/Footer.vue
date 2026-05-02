<template>
  <footer class="footer">
    <div class="footer-inner">
      <div class="footer-content">
        <div class="footer-info">
          <h3 class="footer-title">AI Blog</h3>
          <p class="footer-description">
            一个结合人工智能技术的现代博客平台，致力于分享技术与生活的点滴。
          </p>
        </div>
        
        <div class="footer-links">
          <div class="footer-section">
            <h4 class="footer-heading">探索</h4>
            <ul class="footer-list">
              <li><router-link to="/">首页</router-link></li>
              <li><router-link to="/search">搜索</router-link></li>
              <li><a href="#about">关于</a></li>
            </ul>
          </div>
          
          <div class="footer-section">
            <h4 class="footer-heading">技术栈</h4>
            <ul class="footer-list">
              <li><a href="https://vuejs.org/" target="_blank" rel="noopener">Vue 3</a></li>
              <li><a href="https://spring.io/projects/spring-boot" target="_blank" rel="noopener">Spring Boot</a></li>
              <li><a href="https://nodejs.org/" target="_blank" rel="noopener">Node.js</a></li>
            </ul>
          </div>
          
          <div class="footer-section">
            <h4 class="footer-heading">联系</h4>
            <ul class="footer-list">
              <li><a href="mailto:contact@aiblog.com">邮箱</a></li>
              <li><a href="https://github.com" target="_blank" rel="noopener">GitHub</a></li>
              <li><a href="https://twitter.com" target="_blank" rel="noopener">Twitter</a></li>
            </ul>
          </div>
        </div>
      </div>
      
      <div class="footer-bottom">
        <div class="footer-bottom-content">
          <p class="copyright">
            © {{ new Date().getFullYear() }} AI Blog. 由 Vue3 + Spring Boot 驱动。
          </p>
          <div class="footer-actions">
            <button 
              class="theme-toggle-footer" 
              @click="themeStore.toggle"
              :aria-label="themeStore.isDark ? '切换到浅色模式' : '切换到深色模式'"
            >
              <span class="theme-icon">{{ themeStore.isDark ? '☀️' : '🌙' }}</span>
            </button>
          </div>
        </div>
      </div>
    </div>
  </footer>
</template>

<script setup>
import { useThemeStore } from '../stores/theme'

const themeStore = useThemeStore()
</script>

<style scoped>
.footer {
  background-color: var(--footer-bg);
  color: var(--footer-text);
  margin-top: auto;
  /* 确保背景正确显示 */
  background-image: none;
}

/* 深色模式下 Footer 背景增强 */
.dark-mode .footer {
  background-image: linear-gradient(180deg, rgba(88, 166, 255, 0.02) 0%, transparent 100%);
}

.footer-inner {
  max-width: var(--container-xl);
  margin: 0 auto;
  padding: var(--space-3xl) var(--space-lg) var(--space-xl);
}

.footer-content {
  display: grid;
  grid-template-columns: 1fr;
  gap: var(--space-2xl);
  margin-bottom: var(--space-2xl);
  border-bottom: 1px solid var(--border-color);
  padding-bottom: var(--space-2xl);
}

.footer-info {
  max-width: 500px;
}

.footer-title {
  font-size: 24px;
  font-weight: 700;
  margin-bottom: var(--space-md);
  color: var(--text-color);
}

.footer-description {
  color: var(--text-secondary);
  line-height: 1.6;
  font-size: 15px;
}

.footer-links {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: var(--space-xl);
}

.footer-section {
  display: flex;
  flex-direction: column;
  gap: var(--space-sm);
}

.footer-heading {
  font-size: 14px;
  font-weight: 600;
  color: var(--text-color);
  margin-bottom: var(--space-sm);
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.footer-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-xs);
  list-style: none;
  padding: 0;
  margin: 0;
}

.footer-list a {
  color: var(--text-secondary);
  text-decoration: none;
  font-size: 14px;
  transition: color var(--transition-fast);
  padding: var(--space-xs) 0;
}

.footer-list a:hover {
  color: var(--text-color);
  text-decoration: none;
}

.footer-bottom {
  border-top: 1px solid var(--border-color);
  padding-top: var(--space-xl);
}

.footer-bottom-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: var(--space-md);
}

.copyright {
  color: var(--text-secondary);
  font-size: 14px;
  margin: 0;
}

.footer-actions {
  display: flex;
  align-items: center;
  gap: var(--space-sm);
}

.theme-toggle-footer {
  background: none;
  border: 1px solid var(--border-color);
  border-radius: var(--radius-full);
  cursor: pointer;
  padding: var(--space-xs) var(--space-sm);
  transition: all var(--transition-fast);
  display: flex;
  align-items: center;
  justify-content: center;
}

.theme-toggle-footer:hover {
  background-color: var(--hover-bg);
  border-color: var(--accent-color);
}

.theme-toggle-footer:focus-visible {
  outline: 2px solid var(--accent-color);
  outline-offset: 2px;
}

.theme-icon {
  font-size: 16px;
  display: block;
  transition: transform var(--transition-base);
}

.theme-toggle-footer:hover .theme-icon {
  transform: rotate(15deg);
}

/* 响应式 - 平板端 */
@media (max-width: 1199px) and (min-width: 768px) {
  .footer-inner {
    padding: var(--space-2xl) var(--space-md) var(--space-lg);
  }
  
  .footer-content {
    gap: var(--space-xl);
    margin-bottom: var(--space-xl);
  }
  
  .footer-links {
    gap: var(--space-lg);
  }
}

/* 响应式 - 移动端 */
@media (max-width: 767px) {
  .footer-inner {
    padding: var(--space-xl) var(--space-md) var(--space-lg);
  }
  
  .footer-content {
    gap: var(--space-lg);
    margin-bottom: var(--space-lg);
  }
  
  .footer-links {
    grid-template-columns: repeat(2, 1fr);
    gap: var(--space-lg);
  }
  
  .footer-bottom-content {
    flex-direction: column;
    align-items: flex-start;
    gap: var(--space-md);
  }
}

/* 响应式 - 小移动端 */
@media (max-width: 479px) {
  .footer-content {
    grid-template-columns: 1fr;
  }
  
  .footer-links {
    grid-template-columns: 1fr;
  }
  
  .footer-title {
    font-size: 20px;
  }
  
  .footer-description {
    font-size: 14px;
  }
  
  .copyright {
    font-size: 13px;
  }
}

/* 打印样式 */
@media print {
  .footer {
    display: none;
  }
}
</style>
