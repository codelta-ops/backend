<template>
  <div class="page">
    <header class="top">
      <button class="back" @click="$router.back()">←</button>
      <div class="title">关于我们</div>
    </header>
    <main class="main">
      <div class="content">
        <h2>搭把手 - v1.0</h2>
        <p>这是一个任务管理平台应用，旨在帮助用户发布、接取、完成各种任务。</p>
        <p>
          开发者：<strong>惜哉剑气疏</strong><br />
          联系邮箱：<strong>230217821@qq.com</strong>
        </p>
        <p>感谢使用搭把手应用！</p>
      </div>
    </main>
  </div>
</template>

<script setup></script>

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
  padding: 12px 12px 70px;
}
.content {
  text-align: center;
  font-size: 14px;
}
h2 {
  font-size: 18px;
  margin-bottom: 12px;
}
</style>
