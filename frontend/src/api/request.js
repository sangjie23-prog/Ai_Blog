import axios from 'axios'

// 创建axios实例
const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    // 如果有token，添加到请求头
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    const res = response.data
    // 如果状态码不是200，视为错误
    if (res.code !== 200) {
      console.error('API错误:', res.message)
      return Promise.reject(new Error(res.message))
    }
    return res
  },
  error => {
    console.error('请求错误:', error.message)
    return Promise.reject(error)
  }
)

export default request
