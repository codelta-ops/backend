<template>
  <div class="card" @click="$emit('click')">
    <img v-if="order.url" :src="order.url" class="image" />
    <div v-else class="image placeholder"></div>
    <div class="body">
      <div class="title">{{ order.title }}</div>
      <div class="bottom">
        <div class="user">
          <div class="avatar-circle">{{ order.username?.[0] || '用' }}</div>
          <div class="name">{{ order.username }}</div>
        </div>
        <div class="price-status">
          <div class="price">¥{{ order.price }}</div>
          <div class="status" :class="statusClass">{{ statusText }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  order: { type: Object, required: true }
})

// 状态值 → 中文文本
const statusTextMap = {
  0: '有待接单',
  1: '进行中',
  2: '已完成',
  3: '正在仲裁'
}

// 状态值 → 样式类名
const statusClassMap = {
  0: 'pending',
  1: 'doing',
  2: 'done',
  3: 'arbitrating'
}

// 获取状态文本（默认“未知”）
const statusText = statusTextMap[props.order.status] || '未知'

// 获取状态样式类（默认 pending）
const statusClass = statusClassMap[props.order.status] || 'pending'
</script>

<style scoped>
.card {
  background: #fff;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.04);
}
.image {
  width: 100%;
  height: 120px;
  object-fit: cover;
  background: #f5f5f5;
}
.body {
  padding: 10px 12px;
}
.title {
  font-size: 14px;
  font-weight: 600;
  color: #333;
  min-height: 32px;
}
.bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 8px;
}
.user {
  display: flex;
  align-items: center;
}
.avatar-circle {
  width: 26px;
  height: 26px;
  border-radius: 50%;
  background: #8cc5ff;
  color: #fff;
  font-size: 13px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 4px;
}
.name {
  font-size: 12px;
  color: #666;
}
.price-status {
  text-align: right;
}
.price {
  color: #2d8cf0;
  font-weight: 700;
}
.status {
  margin-top: 4px;
  font-size: 11px;
  padding: 2px 6px;
  border-radius: 10px;
}
.status.pending {
  background: #fff7e6;
  color: #fa8c16;
}
.status.doing {
  background: #e6f7ff;
  color: #1890ff;
}
.status.done {
  background: #f6ffed;
  color: #52c41a;
}
.status.arbitrating {
  background: #fff1f0;
  color: #f5222d;
}
</style>