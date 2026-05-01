import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useAuthStore = defineStore('auth', () => {
  // 登录状态
  const isLoggedIn = ref(!!localStorage.getItem('token'))
  const username = ref(localStorage.getItem('username') || '')

  // 登录
  function login(token, user) {
    localStorage.setItem('token', token)
    localStorage.setItem('username', user)
    isLoggedIn.value = true
    username.value = user
  }

  // 登出
  function logout() {
    localStorage.removeItem('token')
    localStorage.removeItem('username')
    isLoggedIn.value = false
    username.value = ''
  }

  // 获取Token
  function getToken() {
    return localStorage.getItem('token')
  }

  return { isLoggedIn, username, login, logout, getToken }
})
