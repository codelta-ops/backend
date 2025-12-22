<template>
  <div class="arbitration-page">
    <header class="top">
      <button class="back" @click="$router.back()">←</button>
      <h2 class="title">申请仲裁</h2>
    </header>

    <main class="main">
      <div class="section">
        <h3>订单信息</h3>
        <div class="order-info">
          <p><strong>订单标题：</strong>{{ orderDetail?.title || '加载中...' }}</p>
          <p><strong>订单金额：</strong>¥{{ orderDetail?.price || 0 }}</p>
          <p><strong>订单状态：</strong>{{ statusText }}</p>
        </div>
      </div>

      <div class="section">
        <h3>仲裁理由</h3>
        <textarea
          v-model="formData.reason"
          class="reason-input"
          placeholder="请详细描述争议原因（如：对方未完成任务、服务态度差等）"
          rows="6"
        ></textarea>
      </div>

      <div class="section">
        <h3>证据材料（可选）</h3>
        <div class="evidence-section">
          <div class="evidence-list" v-if="formData.evidence.length > 0">
            <div class="evidence-item" v-for="(item, index) in formData.evidence" :key="index">
              <span class="evidence-text">{{ item }}</span>
              <button class="remove-btn" @click="removeEvidence(index)">×</button>
            </div>
          </div>
          
          <div class="add-evidence">
            <input
              v-model="evidenceInput"
              class="evidence-input"
              placeholder="输入证据链接或描述（如聊天记录截图、订单截图等）"
              @keyup.enter="addEvidence"
            />
            <button class="add-btn" @click="addEvidence">添加</button>
          </div>
        </div>
      </div>

      <div class="section">
        <h3>注意事项</h3>
        <ul class="tips">
          <li>请提供真实有效的争议信息</li>
          <li>虚假仲裁将影响您的信用记录</li>
          <li>平台将在1-3个工作日内处理</li>
        </ul>
      </div>
    </main>

    <footer class="footer">
      <button class="primary" @click="submitArbitration" :disabled="!canSubmit">
        提交仲裁申请
      </button>
    </footer>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '../store/auth'
import { getOrderDetailApi } from '../api/orders'
import { applyArbitrationApi } from '../api/arbitration'

const route = useRoute()
const router = useRouter()
const auth = useAuthStore()
const orderDetail = ref(null)
const evidenceInput = ref('')

// 表单数据
const formData = ref({
  orderId: null,
  reason: '',
  evidence: []
})

// 状态文本映射
const statusText = computed(() => {
  const map = { 0: '待接单', 1: '进行中', 2: '已完成', 3: '已取消' }
  return map[orderDetail.value?.status] || '未知'
})

// 是否可以提交
const canSubmit = computed(() => {
  return formData.value.reason.trim().length > 0 && formData.value.orderId
})

// 获取订单详情
async function fetchOrderDetail() {
  const orderId = route.query.orderId // 👈 获取 query 参数
  if (!orderId) {
    alert('订单ID缺失')
    router.back()
    return
  }

  try {
    orderDetail.value = await getOrderDetailApi(orderId)
    formData.value.orderId = Number(orderId) // 👉 转换为数字
  } catch (error) {
    console.error('获取订单详情失败:', error)
    alert('订单信息加载失败')
    router.back()
  }
}

// 添加证据
function addEvidence() {
  const text = evidenceInput.value.trim()
  if (text && !formData.value.evidence.includes(text)) {
    formData.value.evidence.push(text)
    evidenceInput.value = ''
  }
}

// 删除证据
function removeEvidence(index) {
  formData.value.evidence.splice(index, 1)
}

// 提交仲裁申请
async function submitArbitration() {
  if (!canSubmit.value) {
    alert('请填写仲裁理由')
    return
  }

  if (!confirm('确认提交仲裁申请吗？提交后无法撤销。')) {
    return
  }

  try {
    await applyArbitrationApi({
      orderId: formData.value.orderId,
      reason: formData.value.reason,
      evidence: formData.value.evidence
    })
    
    alert('仲裁申请提交成功，客服将尽快处理')
    router.back() // 返回订单详情页
  } catch (error) {
    console.error('提交仲裁失败:', error)
    alert('提交失败，请稍后重试')
  }
}

onMounted(fetchOrderDetail)
</script>

<style scoped>
.arbitration-page {
  min-height: 100vh;
  background: #f0f4ff;
  padding-bottom: 70px;
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
.title {
  font-size: 18px;
  font-weight: 600;
}

.main {
  padding: 10px 12px;
}

.section {
  background: #fff;
  border-radius: 16px;
  padding: 12px;
  margin-bottom: 10px;
}

.section h3 {
  margin: 0 0 10px 0;
  font-size: 16px;
  color: #333;
}

.order-info p {
  margin: 5px 0;
  font-size: 14px;
  color: #666;
}

.reason-input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 14px;
  resize: vertical;
  min-height: 100px;
  box-sizing: border-box;
}

.evidence-section {
  width: 100%;
}

.evidence-list {
  margin-bottom: 10px;
}

.evidence-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px;
  background: #f8f9fa;
  border-radius: 6px;
  margin-bottom: 5px;
  font-size: 14px;
}

.remove-btn {
  background: #ff4d4f;
  color: white;
  border: none;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  cursor: pointer;
  font-size: 16px;
}

.add-evidence {
  display: flex;
  gap: 8px;
}

.evidence-input {
  flex: 1;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
}

.add-btn {
  padding: 8px 12px;
  background: #2d8cf0;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

.tips {
  padding-left: 16px;
  font-size: 14px;
  color: #666;
  line-height: 1.6;
}

.tips li {
  margin: 4px 0;
}

.footer {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  padding: 8px 12px;
  background: #f0f4ff;
  z-index: 100;
  box-shadow: 0 -2px 8px rgba(0, 0, 0, 0.1);
}

.primary {
  width: 100%;
  height: 40px;
  border-radius: 999px;
  border: none;
  background: #2d8cf0;
  color: #fff;
  font-weight: 500;
  font-size: 16px;
}

.primary:disabled {
  background: #ccc;
  cursor: not-allowed;
}
</style>