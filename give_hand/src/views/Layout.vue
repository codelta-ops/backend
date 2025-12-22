<template>
  <div class="layout">
    <div class="content">
      <router-view />
    </div>
    <BottomTabBar />
  </div>
</template>

<script setup>
import BottomTabBar from '../components/BottonTabBar.vue'
</script>

<style scoped>
.layout {
  height: 100vh;
  display: flex;
  flex-direction: column;
}
.content {
  flex: 1;
  overflow-y: auto;
  padding-bottom: 56px; /* 预留给 tab-bar */
}
</style>
