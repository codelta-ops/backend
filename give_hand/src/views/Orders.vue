<template>
  <div class="page">
    <header class="header">
      <h2>我的订单</h2>
      <div class="tabs">
        <span :class="{ active: type === 'published' }" @click="changeType('published')">
          我发布的
        </span>
        <!-- <span :class="{ active: type === 'accepted' }" @click="changeType('accepted')">
          我接取的
        </span> -->
      </div>
      <input
        v-model="keyword"
        class="search"
        placeholder="搜索订单标题"
        @keyup.enter="fetchOrders(true)"
      />
    </header>
    <main class="main">
      <div class="grid">
        <OrderCard
          v-for="item in orders"
          :key="item.id"
          :order="item"
          @click="goDetail(item.id)"
        />
      </div>
      <button class="create-btn" @click="goCreate">＋ 发布订单</button>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getOrderListApi } from '../api/orders'
import OrderCard from '../components/OrderCard.vue'

const router = useRouter()
const type = ref('published')
const keyword = ref('')
const orders = ref([])

async function fetchOrders() {
  const data = await getOrderListApi({
    type: type.value,
    keyword: keyword.value || undefined,
    page: 1,
    size: 20
  })
  orders.value = data.results || []
}

function changeType(t) {
  if (type.value === t) return
  type.value = t
  fetchOrders()
}

function goDetail(id) {
  router.push(`/orders/${id}`)
}

function goCreate() {
  router.push('/orders/create')
}

onMounted(fetchOrders)
</script>

<style scoped>
.page {
  min-height: 100%;
  background: #f0f4ff;
}
.header {
  padding: 14px 16px 8px;
}
h2 {
  margin: 0 0 6px;
}
.tabs span {
  margin-right: 12px;
  font-size: 14px;
  cursor: pointer;
  padding-bottom: 4px;
}
.tabs .active {
  color: #2d8cf0;
  border-bottom: 2px solid #2d8cf0;
}
.search {
  width: 100%;
  margin-top: 10px;
  height: 36px;
  border-radius: 999px;
  border: none;
  padding: 0 14px;
}
.main {
  padding: 8px 12px 80px;
}
.grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  grid-gap: 10px;
}
.create-btn {
  position: fixed;
  bottom: 70px;
  right: 16px;
  border-radius: 999px;
  background: #2d8cf0;
  border: none;
  color: #fff;
  padding: 10px 18px;
  font-size: 14px;
}
</style>
