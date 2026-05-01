<template>
  <header class="header">
    <div class="header-inner">
      <router-link to="/" class="logo">AI Blog</router-link>
      <nav class="nav">
        <router-link to="/">首页</router-link>
        <button class="theme-toggle" @click="themeStore.toggle">
          {{ themeStore.isDark ? '☀️' : '🌙' }}
        </button>
      </nav>
    </div>
  </header>
</template>

<script setup>
import { useThemeStore } from '../stores/theme'

const themeStore = useThemeStore()
</script>

<style scoped>
.header {
  background-color: var(--header-bg);
  border-bottom: 1px solid var(--border-color);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-inner {
  max-width: 1200px;
  margin: 0 auto;
  padding: 16px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo {
  font-size: 24px;
  font-weight: 700;
  color: var(--text-color);
  text-decoration: none;
  letter-spacing: -0.5px;
}

.logo:hover {
  text-decoration: none;
}

.nav {
  display: flex;
  align-items: center;
  gap: 20px;
}

.nav a {
  color: var(--text-secondary);
  font-size: 14px;
  font-weight: 500;
}

.nav a:hover {
  color: var(--text-color);
  text-decoration: none;
}

.theme-toggle {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 18px;
  padding: 4px;
}

@media (max-width: 768px) {
  .header-inner {
    padding: 12px 15px;
  }
  
  .logo {
    font-size: 20px;
  }
}
</style>
