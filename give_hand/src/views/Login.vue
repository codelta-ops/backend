<template>
  <div class="page">
    <div class="card">
      <h1 class="title">欢迎回来</h1>
      <p class="subtitle">请登录您的账户继续使用</p>

      <div class="form-group">
        <label>账号</label>
        <input
          v-model="form.account"
          placeholder="请输入用户名或邮箱"
          type="text"
        />
      </div>
      <div class="form-group">
        <label>密码</label>
        <input
          v-model="form.password"
          placeholder="请输入密码"
          type="password"
        />
      </div>

      <div class="actions">
        <a class="forget">忘记密码?</a>
      </div>

      <button class="primary-btn" @click="handleLogin" :disabled="loading">
        {{ loading ? '登录中...' : '登录' }}
      </button>

      <div class="footer-text">
        还没有账户？
        <span class="link" @click="goRegister">立即注册</span>
      </div>

      <div class="skip" @click="skip">跳过登录</div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { loginApi } from '../api/auth'
import { useAuthStore } from '../store/auth'

const router = useRouter()
const auth = useAuthStore()
const loading = ref(false)

const form = reactive({
  account: '',
  password: ''
})

async function handleLogin() {
  if (!form.account || !form.password) {
    alert('请输入账号和密码')
    return
  }
  loading.value = true
  try {
    const data = await loginApi(form)
    // data 包含 token + 用户信息
    const { token, ...user } = data
    auth.setAuth(token, user)
    router.replace('/home')
  } catch (e) {
    alert(e.message || '登录失败')
  } finally {
    loading.value = false
  }
}

function goRegister() {
  router.push('/register')
}

function skip() {
  router.replace('/home')
}
</script>

<style scoped>
.page {
  height: 100vh;
  background: #e5f1ff;
  display: flex;
  justify-content: center;
  align-items: center;
}
.card {
  width: 90%;
  max-width: 420px;
  background: #fff;
  border-radius: 24px;
  padding: 40px 32px 24px;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.08);
}
.title {
  text-align: center;
  color: #1d4f91;
  margin: 0 0 8px;
}
.subtitle {
  text-align: center;
  color: #666;
  margin-bottom: 24px;
  font-size: 14px;
}
.form-group {
  margin-bottom: 16px;
}
label {
  display: block;
  font-size: 14px;
  margin-bottom: 6px;
  color: #333;
}
input {
  width: 100%;
  height: 42px;
  border-radius: 999px;
  border: 1px solid #e5e5e5;
  padding: 0 14px;
  outline: none;
  font-size: 14px;
}
input:focus {
  border-color: #2d8cf0;
}
.actions {
  text-align: right;
  margin-bottom: 10px;
}
.forget {
  font-size: 12px;
  color: #2d8cf0;
  cursor: pointer;
}
.primary-btn {
  width: 100%;
  height: 44px;
  border-radius: 999px;
  border: none;
  background: linear-gradient(135deg, #2d8cf0, #3268ff);
  color: #fff;
  font-size: 16px;
  font-weight: 600;
}
.footer-text {
  margin-top: 16px;
  text-align: center;
  font-size: 13px;
  color: #666;
}
.link {
  color: #2d8cf0;
  cursor: pointer;
}
.skip {
  margin-top: 8px;
  text-align: center;
  font-size: 12px;
  color: #999;
  cursor: pointer;
}
</style>
