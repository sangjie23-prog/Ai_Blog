<template>
  <div class="login-page">
    <div class="login-card">
      <h1 class="login-title">管理员登录</h1>
      
      <form @submit.prevent="handleLogin" class="login-form">
        <div class="form-group">
          <label for="username">用户名</label>
          <input 
            id="username"
            v-model="form.username" 
            type="text" 
            placeholder="请输入用户名"
            required
          />
        </div>
        
        <div class="form-group">
          <label for="password">密码</label>
          <input 
            id="password"
            v-model="form.password" 
            type="password" 
            placeholder="请输入密码"
            required
          />
        </div>
        
        <p v-if="errorMsg" class="error-msg">{{ errorMsg }}</p>
        
        <button type="submit" class="login-btn" :disabled="loading">
          {{ loading ? '登录中...' : '登录' }}
        </button>
      </form>
      
      <router-link to="/" class="back-link">返回首页</router-link>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../../stores/auth'
import request from '../../api/request'

const router = useRouter()
const authStore = useAuthStore()

const form = reactive({
  username: '',
  password: ''
})

const loading = ref(false)
const errorMsg = ref('')

// 处理登录
async function handleLogin() {
  loading.value = true
  errorMsg.value = ''
  
  try {
    const res = await request({
      url: '/auth/login',
      method: 'post',
      data: form
    })
    
    // 登录成功，保存状态
    authStore.login(res.data.token, res.data.username)
    
    // 跳转到后台首页
    router.push('/admin/dashboard')
  } catch (error) {
    errorMsg.value = error.message || '登录失败，请重试'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 80vh;
}

.login-card {
  background-color: var(--card-bg);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-xl);
  padding: var(--space-2xl);
  width: 100%;
  max-width: 420px;
  box-shadow: var(--card-shadow);
}

.login-title {
  text-align: center;
  font-size: 2rem;
  margin-bottom: var(--space-2xl);
  color: var(--text-color);
  font-weight: 700;
  letter-spacing: -0.5px;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: var(--space-xl);
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: var(--space-sm);
}

.form-group label {
  font-size: 0.9375rem;
  font-weight: 500;
  color: var(--text-secondary);
}

.form-group input {
  padding: var(--space-sm) var(--space-md);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  font-size: 1rem;
  background-color: var(--bg-color);
  color: var(--text-color);
  outline: none;
  transition: all var(--transition-fast);
}

.form-group input:focus {
  border-color: var(--accent-color);
  box-shadow: 0 0 0 3px var(--accent-light);
}

.error-msg {
  color: var(--error-color);
  font-size: 0.9375rem;
  text-align: center;
  padding: var(--space-sm);
  background-color: rgba(248, 81, 73, 0.1);
  border-radius: var(--radius-md);
}

.login-btn {
  padding: var(--space-md);
  background-color: var(--accent-color);
  color: white;
  border: none;
  border-radius: var(--radius-md);
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  transition: all var(--transition-fast);
}

.login-btn:hover:not(:disabled) {
  background-color: var(--accent-hover);
  transform: translateY(-1px);
}

.login-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.back-link {
  display: block;
  text-align: center;
  margin-top: var(--space-xl);
  font-size: 0.9375rem;
  color: var(--text-secondary);
  transition: color var(--transition-fast);
}

.back-link:hover {
  color: var(--accent-color);
}

@media (max-width: 767px) {
  .login-card {
    margin: var(--space-lg);
    padding: var(--space-xl);
  }

  .login-title {
    font-size: 1.75rem;
    margin-bottom: var(--space-xl);
  }
}

@media (max-width: 479px) {
  .login-card {
    margin: var(--space-md);
    padding: var(--space-lg);
  }

  .login-title {
    font-size: 1.5rem;
  }
}
</style>
