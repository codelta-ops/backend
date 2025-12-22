<template>
  <div class="page">
    <div class="card">
      <h2 class="title">注册账户</h2>
      <div class="form-group" v-for="field in fields" :key="field.key">
        <label>{{ field.label }}</label>
        <input
          v-model="form[field.key]"
          :type="field.type || 'text'"
          :placeholder="field.placeholder"
        />
      </div>
      <button class="primary-btn" @click="handleRegister" :disabled="loading">
        {{ loading ? '注册中...' : '注册' }}
      </button>
      <div class="footer-text">
        已有账户？
        <span class="link" @click="goLogin">去登录</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { registerApi } from '../api/auth'

const router = useRouter()
const loading = ref(false)

const form = reactive({
  school: '',
  name: '',
  username: '',
  email: '',
  studentId: '',
  password: ''
})

const fields = [
  { key: 'school', label: '学校', placeholder: '请输入学校名称' },
  { key: 'name', label: '姓名', placeholder: '请输入真实姓名' },
  { key: 'username', label: '用户名', placeholder: '请输入用户名' },
  { key: 'email', label: '邮箱', placeholder: '请输入邮箱地址' },
  { key: 'studentId', label: '学号', placeholder: '请输入学号' },
  { key: 'password', label: '密码', type: 'password', placeholder: '请输入密码' }
]

async function handleRegister() {
  loading.value = true
  try {
    await registerApi(form)
    alert('注册成功，请登录')
    router.replace('/login')
  } catch (e) {
    alert(e.message || '注册失败')
  } finally {
    loading.value = false
  }
}

function goLogin() {
  router.push('/login')
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
  padding: 32px 28px 24px;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.08);
}
.title {
  text-align: center;
  margin-bottom: 16px;
}
.form-group {
  margin-bottom: 14px;
}
label {
  display: block;
  font-size: 14px;
  margin-bottom: 4px;
}
input {
  width: 100%;
  height: 40px;
  border-radius: 999px;
  border: 1px solid #e5e5e5;
  padding: 0 14px;
}
.primary-btn {
  width: 100%;
  height: 44px;
  border-radius: 999px;
  background: linear-gradient(135deg, #2d8cf0, #3268ff);
  border: none;
  color: #fff;
  font-weight: 600;
  margin-top: 8px;
}
.footer-text {
  margin-top: 12px;
  text-align: center;
  font-size: 13px;
}
.link {
  color: #2d8cf0;
  cursor: pointer;
}
</style>
