<template>
  <div class="page" v-if="detail">
    <header class="top">
      <button class="back" @click="$router.back()">←</button>
      <div class="title">{{ detail.title }}</div>
    </header>
    <main class="main">
      <img v-if="detail.url" :src="detail.url" class="image" />
      <div class="section">
        <div class="price">¥{{ detail.price }}</div>
        <div class="status">状态：{{ statusText }}</div>
      </div>
      <div class="section">
        <div class="user">
          <div class="avatar-circle">{{ detail.username?.[0] || '用' }}</div>
          <div class="info">
            <div class="name">{{ detail.username }}</div>
          </div>
        </div>
      </div>
      <div class="section">
        <h3>订单详情</h3>
        <p class="content">{{ detail.content }}</p>
      </div>
    </main>

    <footer class="footer">
      <!-- 我是发布者 -->
      <template v-if="isOwner">
        <button class="primary" @click="cancelOrder" v-if="detail.status === 0">
          撤销订单
        </button>
        <button class="primary" @click="deleteOrder" v-else-if="[0, 2, 3].includes(detail.status)">
          删除订单
        </button>
      </template>

      <!-- 我是接单者 -->
      <button class="primary" @click="completeOrder" v-else-if="isAcceptor && detail.status === 1">
        完成订单
      </button>

      <!-- 其他人（可接单） -->
      <button class="primary" @click="acceptOrder" v-else-if="!isOwner && !detail.acceptorId && detail.status === 0">
        接取订单
      </button>

      <!-- 申请仲裁：发布者或接单者，在进行中/已完成时可申请 -->
      <button class="secondary" @click="applyArbitration" v-if="(isOwner || isAcceptor) && [1, 2].includes(detail.status)">
        申请仲裁
      </button>

      <!-- 联系TA（始终显示） -->
      <button class="secondary" @click="openChat">联系TA</button>
    </footer>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '../store/auth'
import {
  getOrderDetailApi,
  acceptOrderApi,
  completeOrderApi,
  cancelOrderApi,
  deleteOrderRecordApi
} from '../api/orders'

const route = useRoute()
const router = useRouter()
const auth = useAuthStore()
const detail = ref(null)

// 获取当前用户 ID
const currentUserId = computed(() => auth.user?.uid)

// 判断角色
const isOwner = computed(() => {
  return detail.value?.uid === currentUserId.value
})
const isAcceptor = computed(() => {
  return detail.value?.acceptorId === currentUserId.value
})

// 状态文本
const statusText = computed(() => {
  const map = { 0: '待接单', 1: '进行中', 2: '已完成', 3: '已取消' }
  return map[detail.value?.status] || '未知'
})

async function fetchDetail() {
  const id = route.params.id
  detail.value = await getOrderDetailApi(id)
  console.log('订单详情:', detail.value)
}

// 操作方法
async function acceptOrder() {
  await acceptOrderApi(route.params.id)
  alert('接单成功')
  fetchDetail()
}

async function completeOrder() {
  await completeOrderApi(route.params.id)
  alert('订单已完成')
  fetchDetail()
}

async function cancelOrder() {
  if (confirm('确定撤销此订单？撤销后无法恢复。')) {
    await cancelOrderApi(route.params.id)
    alert('订单已撤销')
    fetchDetail()
  }
}

async function deleteOrder() {
  if (confirm('确定删除此订单？')) {
    await deleteOrderRecordApi(route.params.id)
    alert('订单已删除')
    router.back()
  }
}

// 申请仲裁 - 跳转到仲裁表单页面
function applyArbitration() {
  router.push(`/arbitration/apply?orderId=${route.params.id}`)
}

function openChat() {
  const targetId = isOwner.value ? detail.value?.acceptorId : detail.value?.uid
  if (targetId) {
    router.push(`/chat/${targetId}`)
  } else {
    alert('对方信息不可用')
  }
}

onMounted(fetchDetail)
</script>

<style scoped>
/* 样式保持不变 */
.page {
  min-height: 100vh;
  background: #f0f4ff;
  padding-bottom: 70px;
  box-sizing: border-box;
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
  padding: 10px 12px;
}
.image {
  width: 100%;
  border-radius: 16px;
  margin-bottom: 10px;
}
.section {
  background: #fff;
  border-radius: 16px;
  padding: 10px 12px;
  margin-bottom: 10px;
}
.price {
  font-size: 20px;
  font-weight: 700;
  color: #2d8cf0;
}
.status {
  margin-top: 4px;
  color: #999;
}
.user {
  display: flex;
  align-items: center;
}
.avatar-circle {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: #8cc5ff;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  margin-right: 6px;
}
.footer {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  padding: 8px 12px;
  display: flex;
  gap: 8px;
  background: #f0f4ff;
  z-index: 100;
  box-shadow: 0 -2px 8px rgba(0, 0, 0, 0.1);
}
.primary,
.secondary {
  flex: 1;
  height: 40px;
  border-radius: 999px;
  border: none;
  font-weight: 500;
}
.primary {
  background: #2d8cf0;
  color: #fff;
}
.secondary {
  background: #fff;
  color: #2d8cf0;
  border: 1px solid #2d8cf0;
}
</style>