<template>
  <div class="page">
    <header class="top">
      <button class="back" @click="$router.back()">←</button>
      <div class="title">发布订单</div>
    </header>
    <main class="main">
      <div class="form-group">
        <label>标题</label>
        <input v-model="form.title" placeholder="请输入订单标题" />
      </div>
      <div class="form-group">
        <label>金额</label>
        <input v-model.number="form.price" type="number" placeholder="请输入金额" />
      </div>
      <div class="form-group">
        <label>详情</label>
        <textarea
          v-model="form.content"
          rows="5"
          placeholder="请详细描述任务内容、时间地点等"
        />
      </div>

      <!-- 图片上传区域 -->
      <div class="form-group">
        <label>上传图片（最多3张）</label>
        <div class="image-upload-area" @click="triggerFileInput">
          <input
            ref="fileInput"
            type="file"
            accept="image/*"
            multiple
            @change="handleImageChange"
            style="display: none"
          />
          <div v-if="previewImages.length === 0" class="upload-placeholder">
            ＋ 点击选择图片
          </div>
          <div v-else class="image-preview">
            <div
              v-for="(url, index) in previewImages"
              :key="index"
              class="preview-item"
            >
              <img :src="url" alt="预览" />
              <button class="remove-btn" @click.stop="removeImage(index)">×</button>
            </div>
          </div>
        </div>
        <div class="image-tip">建议上传清晰实拍图，提高接单率</div>
      </div>

      <button class="primary" @click="handleCreate" :disabled="loading">
        {{ loading ? '发布中...' : '发布订单' }}
      </button>
    </main>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { createOrderApi } from '../api/orders'
import { uploadImageApi } from '../api/upload'

const router = useRouter()
const loading = ref(false)
const fileInput = ref(null)
const previewImages = ref([]) // 用于预览的 base64 URL 数组

const form = reactive({
  title: '',
  price: '',
  content: '',
  images: [] // 存储 File 对象数组
})

// 触发文件选择
function triggerFileInput() {
  fileInput.value?.click()
}

// 处理图片选择
function handleImageChange(event) {
  const files = Array.from(event.target.files)
  if (files.length + form.images.length > 3) {
    alert('最多只能上传 3 张图片')
    return
  }

  // 限制总数量为 3
  const newFiles = files.slice(0, 3 - form.images.length)
  form.images.push(...newFiles)

  // 生成预览 URL
  newFiles.forEach(file => {
    const url = URL.createObjectURL(file)
    previewImages.value.push(url)
  })

  // 清空 input，确保同一张图能再次选择
  event.target.value = ''
}

// 移除某张图片
function removeImage(index) {
  // 释放内存
  URL.revokeObjectURL(previewImages.value[index])
  // 删除
  previewImages.value.splice(index, 1)
  form.images.splice(index, 1)
}

// 提交订单
async function handleCreate() {
  if (!form.title || !form.price || !form.content) {
    alert('请填写完整信息')
    return
  }

  loading.value = true
  try {
    // 上传图片并获取URL列表
    const imageUrls = []
    if (form.images.length > 0) {
      // 并行上传所有图片
      const uploadPromises = form.images.map(file => uploadImageApi(file))
      const results = await Promise.all(uploadPromises)
      
      // 提取返回的URL
      for (const result of results) {
        if (result.data && typeof result.data === 'string') {
          // 假设后端返回的是文件路径，需要拼接完整URL
          const fullUrl = 'http://' + result.data // 根据你的后端返回格式调整
          imageUrls.push(fullUrl)
        }
      }
    }

    // 构造 JSON payload（images 现在是URL字符串列表）
    const payload = {
      title: form.title,
      price: form.price,
      content: form.content,
      images: imageUrls // ← 现在是URL字符串列表！
    }

    // 发送 JSON 数据
    await createOrderApi(payload)

    alert('发布成功')
    router.replace('/orders')
  } catch (e) {
    console.error(e)
    alert(e.message || '发布失败')
  } finally {
    loading.value = false
  }
}
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
  margin-bottom: 12px;
}
label {
  display: block;
  font-size: 14px;
  margin-bottom: 4px;
}
input,
textarea {
  width: 100%;
  border-radius: 12px;
  border: none;
  padding: 8px 10px;
  resize: none;
}

/* 图片上传样式 */
.image-upload-area {
  border: 1px dashed #ccc;
  border-radius: 12px;
  padding: 12px;
  background: #fff;
  cursor: pointer;
  min-height: 60px;
  position: relative;
}

.upload-placeholder {
  color: #999;
  text-align: center;
  font-size: 14px;
}

.image-preview {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.preview-item {
  position: relative;
  width: 80px;
  height: 80px;
}

.preview-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 8px;
}

.remove-btn {
  position: absolute;
  top: -6px;
  right: -6px;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background: rgba(0, 0, 0, 0.6);
  color: white;
  border: none;
  font-size: 14px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}

.image-tip {
  font-size: 12px;
  color: #888;
  margin-top: 6px;
}

.primary {
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



