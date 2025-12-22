<template>
  <div class="page">
    <header class="top">
      <h2>个人中心</h2>
    </header>
    <main class="main">
      <div class="card user-card">
        <div class="avatar">
          <!-- 优先显示真实头像，否则显示首字母 -->
          <img
            v-if="user.avatar"
            :src="user.avatar"
            alt="头像"
            class="avatar-img"
            @error="handleAvatarError"
          />
          <div v-else class="circle">
            {{ user?.username?.[0]?.toUpperCase() || 'U' }}
          </div>
        </div>
        <div class="info">
          <div class="name">{{ user?.username || '未登录' }}</div>
          <div class="uid">UID: {{ user?.uid || 'xxxxxxxx' }}</div>
        </div>
      </div>

      <div class="card menu" @click="goArbitration">
        <span>申请仲裁</span>
        <span>›</span>
      </div>
      <div class="card menu" @click="goProfileEdit">
        <span>个人资料</span>
        <span>›</span>
      </div>
      <div class="card menu" @click="goSettings">
        <span>设置</span>
        <span>›</span>
      </div>
      <div class="card menu" @click="goAbout">
        <span>关于我们</span>
        <span>›</span>
      </div>
      <button class="logout" v-if="user.uid" @click="handleLogout">
        退出登录
      </button>
    </main>
  </div>
</template>

<script setup>
import { reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../store/auth'
import { logoutApi } from '../api/auth'
import { getUserInfoApi } from '../api/user'

const router = useRouter()
const auth = useAuthStore()

// 初始化用户数据
const user = reactive({
  uid: '',
  avatar: '',
  username: '',
  school: '',
  studentId: '',
  name: '',
  email: ''
})

// 获取用户信息
async function fetchUserInfo() {
  try {
    const data = await getUserInfoApi()
    Object.assign(user, data)
    console.log('✅ [7] 获取用户信息成功:', data)
  } catch (err) {
    console.error('获取用户信息失败:', err)
  }
}

// 头像加载失败时回退到首字母
function handleAvatarError() {
  user.avatar = '' // 清空 avatar，触发 fallback 显示
}

// 路由跳转
function goProfileEdit() {
  router.push('/profile/edit')
}
function goArbitration() {
  router.push('/arbitration')
}
function goAbout() {
  router.push('/about')
}
function goSettings() {
  router.push('/settings')
}

// 退出登录
async function handleLogout() {
  try {
    await logoutApi()
  } catch (err) {
    console.warn('登出 API 调用失败，但仍清除本地状态')
  }
  auth.logout()
  router.replace('/login')
}

onMounted(fetchUserInfo)
</script>

<style scoped>
.page {
  min-height: 100vh;
  background: #f0f4ff;
  padding-bottom: 20px;
}
.top {
  padding: 14px 16px;
}
.main {
  padding: 0 12px;
}
.card {
  background: #fff;
  border-radius: 16px;
  padding: 12px 14px;
  margin-bottom: 10px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
}
.user-card {
  display: flex;
  align-items: center;
}
.avatar {
  width: 52px;
  height: 52px;
  position: relative;
}
.avatar-img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  object-fit: cover;
  background: #eee;
}
.circle {
  width: 52px;
  height: 52px;
  border-radius: 50%;
  background: #d9d9d9;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 22px;
  color: #666;
  font-weight: 500;
}
.info {
  margin-left: 12px;
}
.name {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}
.uid {
  font-size: 12px;
  color: #999;
  margin-top: 2px;
}
.menu {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 15px;
  color: #444;
}
.logout {
  width: 100%;
  height: 42px;
  border-radius: 999px;
  border: none;
  background: #ff4d4f;
  color: #fff;
  margin-top: 14px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
}
</style>