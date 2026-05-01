<template>
  <header class="header">
    <div class="header-inner">
      <router-link to="/" class="logo" aria-label="返回首页">
        <span class="logo-icon">✦</span>
        <span class="logo-text">AI Blog</span>
      </router-link>
      
      <button 
        class="mobile-menu-btn" 
        @click="toggleMobileMenu"
        :aria-expanded="mobileMenuOpen"
        aria-label="切换菜单"
      >
        <span class="hamburger" :class="{ 'open': mobileMenuOpen }"></span>
      </button>
      
      <nav class="nav" :class="{ 'mobile-open': mobileMenuOpen }">
        <router-link to="/" class="nav-link" @click="closeMobileMenu">首页</router-link>
        <router-link to="/search" class="nav-link" @click="closeMobileMenu">搜索</router-link>
        <router-link v-if="authStore.isLoggedIn" to="/admin/dashboard" class="nav-link" @click="closeMobileMenu">后台</router-link>
        <router-link v-else to="/admin/login" class="nav-link" @click="closeMobileMenu">登录</router-link>
        <button 
          class="theme-toggle" 
          @click="themeStore.toggle"
          :aria-label="themeStore.isDark ? '切换到浅色模式' : '切换到深色模式'"
        >
          <span class="theme-icon">{{ themeStore.isDark ? '☀️' : '🌙' }}</span>
        </button>
      </nav>
    </div>
  </header>
</template>

<script setup>
import { ref } from 'vue'
import { useThemeStore } from '../stores/theme'
import { useAuthStore } from '../stores/auth'

const themeStore = useThemeStore()
const authStore = useAuthStore()
const mobileMenuOpen = ref(false)

function toggleMobileMenu() {
  mobileMenuOpen.value = !mobileMenuOpen.value
}

function closeMobileMenu() {
  mobileMenuOpen.value = false
}
</script>

<style scoped>
.header {
  background-color: var(--header-bg);
  backdrop-filter: var(--header-blur);
  -webkit-backdrop-filter: var(--header-blur);
  border-bottom: 1px solid var(--border-color);
  position: sticky;
  top: 0;
  z-index: 100;
  transition: all var(--transition-base);
}

.header-inner {
  max-width: var(--container-xl);
  margin: 0 auto;
  padding: var(--space-md) var(--space-lg);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo {
  display: flex;
  align-items: center;
  gap: var(--space-sm);
  font-size: 22px;
  font-weight: 700;
  color: var(--text-color);
  text-decoration: none;
  letter-spacing: -0.5px;
  transition: opacity var(--transition-fast);
}

.logo:hover {
  text-decoration: none;
  opacity: 0.8;
}

.logo-icon {
  font-size: 24px;
  color: var(--accent-color);
}

.nav {
  display: flex;
  align-items: center;
  gap: var(--space-lg);
}

.nav-link {
  color: var(--text-secondary);
  font-size: 14px;
  font-weight: 500;
  padding: var(--space-sm) 0;
  position: relative;
  transition: color var(--transition-fast);
}

.nav-link::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 0;
  height: 2px;
  background-color: var(--accent-color);
  transition: width var(--transition-base);
}

.nav-link:hover {
  color: var(--text-color);
  text-decoration: none;
}

.nav-link:hover::after,
.nav-link.router-link-active::after {
  width: 100%;
}

.nav-link.router-link-active {
  color: var(--text-color);
}

.theme-toggle {
  background: none;
  border: none;
  cursor: pointer;
  padding: var(--space-sm);
  border-radius: var(--radius-full);
  transition: background-color var(--transition-fast);
  display: flex;
  align-items: center;
  justify-content: center;
}

.theme-toggle:hover {
  background-color: var(--hover-bg);
}

.theme-toggle:focus-visible {
  outline: 2px solid var(--accent-color);
  outline-offset: 2px;
}

.theme-icon {
  font-size: 18px;
  display: block;
  transition: transform var(--transition-base);
}

.theme-toggle:hover .theme-icon {
  transform: rotate(15deg);
}

/* 移动端菜单按钮 */
.mobile-menu-btn {
  display: none;
  background: none;
  border: none;
  cursor: pointer;
  padding: var(--space-sm);
  z-index: 101;
}

.hamburger {
  display: block;
  width: 24px;
  height: 2px;
  background-color: var(--text-color);
  position: relative;
  transition: all var(--transition-base);
}

.hamburger::before,
.hamburger::after {
  content: '';
  position: absolute;
  width: 24px;
  height: 2px;
  background-color: var(--text-color);
  transition: all var(--transition-base);
}

.hamburger::before {
  top: -7px;
}

.hamburger::after {
  bottom: -7px;
}

.hamburger.open {
  background-color: transparent;
}

.hamburger.open::before {
  top: 0;
  transform: rotate(45deg);
}

.hamburger.open::after {
  bottom: 0;
  transform: rotate(-45deg);
}

/* 响应式 - 平板端 */
@media (max-width: 1199px) and (min-width: 768px) {
  .header-inner {
    padding: var(--space-md) var(--space-md);
  }
  
  .nav {
    gap: var(--space-md);
  }
}

/* 响应式 - 移动端 */
@media (max-width: 767px) {
  .header-inner {
    padding: var(--space-sm) var(--space-md);
  }
  
  .logo {
    font-size: 18px;
  }
  
  .logo-icon {
    font-size: 20px;
  }
  
  .mobile-menu-btn {
    display: block;
  }
  
  .nav {
    position: fixed;
    top: 0;
    right: -100%;
    width: 280px;
    height: 100vh;
    background-color: var(--card-bg);
    flex-direction: column;
    padding: 80px var(--space-lg) var(--space-lg);
    gap: 0;
    box-shadow: -4px 0 24px var(--overlay-bg);
    transition: right var(--transition-base);
    z-index: 100;
  }
  
  .nav.mobile-open {
    right: 0;
  }
  
  .nav-link {
    width: 100%;
    padding: var(--space-md) 0;
    font-size: 16px;
    border-bottom: 1px solid var(--border-light);
  }
  
  .nav-link::after {
    display: none;
  }
  
  .theme-toggle {
    margin-top: var(--space-md);
    align-self: flex-start;
  }
}

/* 响应式 - 小移动端 */
@media (max-width: 479px) {
  .header-inner {
    padding: var(--space-sm);
  }
  
  .logo {
    font-size: 16px;
  }
  
  .logo-text {
    display: none;
  }
  
  .nav {
    width: 100%;
  }
}
</style>
