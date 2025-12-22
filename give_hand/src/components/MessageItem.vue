<template>
  <div class="message-item" @click="$emit('click')">
    <img :src="avatarUrl" class="avatar" alt="" />

    <div class="content">
      <div class="name">{{ message.username }}</div>
      <div class="latest">{{ message.latestMsg || '无消息' }}</div>
    </div>

    <div class="info">
      <div class="time">{{ displayTime }}</div>
      <div v-if="message.count && message.count > 0" class="badge">{{ formatCount }}</div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  message: {
    type: Object,
    required: true,
    default: () => ({})
  }
})

// 头像 fallback
const avatarUrl = computed(() => {
  return props.message.avatar || 'https://api.dicebear.com/7.x/initials/svg?seed=' + (props.message.username || 'U')
})

// 时间格式化
const displayTime = computed(() => {
  const time = props.message.time
  if (!time) return ''

  let date
  if (Array.isArray(time)) {
    const [y, M, d, h, m] = time
    date = new Date(y, M - 1, d, h, m)
  } else if (typeof time === 'string') {
    date = new Date(time)
  } else {
    date = time
  }

  if (isNaN(date.getTime())) return ''

  const now = new Date()
  const today = new Date(now.getFullYear(), now.getMonth(), now.getDate())
  const msgDate = new Date(date.getFullYear(), date.getMonth(), date.getDate())

  if (msgDate.getTime() === today.getTime()) {
    return `${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
  } else if (now.getTime() - date.getTime() < 7 * 24 * 60 * 60 * 1000) {
    const weekdays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']
    return weekdays[date.getDay()]
  } else {
    return `${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
  }
})

const formatCount = computed(() => {
  const count = props.message.count || 0
  return count > 99 ? '99+' : count
})
</script>

<style scoped>
.message-item {
  display: flex;
  align-items: center;
  padding: 12px;
  background: #fff;
  border-radius: 12px;
  margin-bottom: 10px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
  cursor: pointer;
  transition: background 0.2s;
}
.message-item:hover {
  background: #f9fbff;
}

.avatar {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  object-fit: cover;
  margin-right: 12px;
  background: #eee;
}

.content {
  flex: 1;
  min-width: 0;
}
.name {
  font-weight: 600;
  color: #333;
  font-size: 15px;
  margin-bottom: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.latest {
  font-size: 13px;
  color: #888;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.info {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  min-width: 40px;
}
.time {
  font-size: 11px;
  color: #aaa;
  margin-bottom: 4px;
}
.badge {
  background: #ff4d4f;
  color: white;
  font-size: 11px;
  font-weight: bold;
  padding: 2px 5px;
  border-radius: 10px;
  min-width: 18px;
  text-align: center;
}
</style>