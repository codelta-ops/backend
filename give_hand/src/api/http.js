import axios from 'axios'
import { useAuthStore } from '../store/auth'
import { useRouter } from 'vue-router'

const instance = axios.create({
  baseURL: 'https://backend-8-ach8.onrender.com/api',
  timeout: 10000
})

// 请求拦截：加 token
instance.interceptors.request.use(
  (config) => {
    const auth = useAuthStore()
    if (auth.token) {
      config.headers.token = `${auth.token}` // 注意：建议用 Authorization，但按你后端要求
    }
    return config
  },
  (error) => Promise.reject(error)
)

// 响应拦截：统一处理 code
instance.interceptors.response.use(
  (res) => {
    const data = res.data

    // 假设后端约定：code = 401 表示 token 失效
    if (data.code === 401) {
      const auth = useAuthStore()
      const router = useRouter()

      // 清除 token
      auth.logout() // 建议在 store 中提供 logout 方法

      // 跳转到登录页
      router.push('/login')

      // 可选：提示用户
      // ElMessage.error('登录已过期，请重新登录')

      // 不再 reject，避免调用方 catch 到（或根据需求决定）
      return Promise.reject(new Error('Token expired'))
    }

    if (data.code !== 0) {
      console.error('API Error:', data.message)
      return Promise.reject(new Error(data.message || '请求失败'))
    }

    return data.data
  },
  (error) => {
    // 网络错误、超时等
    console.error('Network Error:', error)
    // 可选：ElMessage.error('网络异常')
    return Promise.reject(error)
  }
)

export default instance
