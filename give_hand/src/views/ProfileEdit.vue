<template>
  <div class="page">
    <header class="top">
      <button class="back" @click="$router.back()">←</button>
      <div class="title">编辑个人资料</div>
    </header>
    <main class="main">
      <div class="form-group">
        <label>头像</label>
        <input v-model="form.avatar" type="text" placeholder="请输入头像 URL" />
      </div>
      <div class="form-group">
        <label>用户名</label>
        <input v-model="form.username" type="text" placeholder="请输入用户名" />
      </div>
      <div class="form-group">
        <label>学校</label>
        <input v-model="form.school" type="text" placeholder="请输入学校名称" />
      </div>
      <div class="form-group">
        <label>学号</label>
        <input v-model="form.studentId" type="text" placeholder="请输入学号" />
      </div>
      <div class="form-group">
        <label>姓名</label>
        <input v-model="form.name" type="text" placeholder="请输入真实姓名" />
      </div>
      <div class="form-group">
        <label>邮箱</label>
        <input v-model="form.email" type="email" placeholder="请输入邮箱" />
      </div>

      <button class="primary-btn" @click="updateProfile" :disabled="loading">
        {{ loading ? '保存中...' : '保存' }}
      </button>
    </main>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getUserInfoApi, updateUserInfoApi } from '../api/user'
import { useAuthStore } from '../store/auth'

const router = useRouter()
const auth = useAuthStore()
const loading = ref(false)

const form = reactive({
  avatar: '',
  username: '',
  school: '',
  studentId: '',
  name: '',
  email: ''
})

async function fetchUserInfo() {
  const data = await getUserInfoApi()
  Object.assign(form, data)
}

async function updateProfile() {
  loading.value = true
  try {
    await updateUserInfoApi(form)
    auth.updateUser(form)
    alert('个人资料已更新')
    router.push('/profile')
  } catch (e) {
    alert(e.message || '更新失败')
  } finally {
    loading.value = false
  }
}

onMounted(fetchUserInfo)
</script>

<style scoped>
.page {
  min-height: 100%;
  background: #f0f4ff;
}
.top {
  display: flex;
  align-items: center;
  padding: 10px 12px;
  background: #5cb0ff;
  color: #fff;
}
.back {
  border: none;
  background: transparent;
  color: inherit;
  font-size: 18px;
  margin-right: 8px;
}
.main {
  padding: 10px 12px 70px;
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
  height: 42px;
  border-radius: 999px;
  border: none;
  background: #2d8cf0;
  color: #fff;
  font-size: 15px;
  margin-top: 10px;
}
</style>
