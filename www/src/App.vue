<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
import AppMenu from "@/components/AppMenu.vue";
import AppHeader from "@/components/AppHeader.vue";
</script>

<template>

  <AppHeader></AppHeader>
  <div class="main-container">
    <RouterView />
    <AppMenu></AppMenu>
  </div>
</template>

<style scoped>
.main-container {
  display: flex;
}
</style>
