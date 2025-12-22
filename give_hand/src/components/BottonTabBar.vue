<template>
  <nav class="tab-bar">
    <div
      v-for="item in tabs"
      :key="item.path"
      class="tab-item"
      :class="{ active: route.path.startsWith(item.path) }"
      @click="go(item.path)"
    >
      <span class="icon">{{ item.icon }}</span>
      <span class="label">{{ item.label }}</span>
    </div>
  </nav>
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const tabs = [
  { path: '/home', label: '首页', icon: '🏠' },
  { path: '/orders', label: '订单', icon: '📋' },
  { path: '/messages', label: '消息', icon: '💬' },
  { path: '/profile', label: '我的', icon: '👤' }
]

function go(path) {
  if (route.path !== path) {
    router.push(path)
  }
}
</script>

<style scoped>
.tab-bar {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  height: 56px;
  background: #ffffff;
  display: flex;
  border-top: 1px solid #e5e5e5;
}
.tab-item {
  flex: 1;
  text-align: center;
  font-size: 12px;
  padding-top: 4px;
  color: #666;
}
.icon {
  display: block;
  font-size: 18px;
}
.active {
  color: #2d8cf0;
}
</style>
