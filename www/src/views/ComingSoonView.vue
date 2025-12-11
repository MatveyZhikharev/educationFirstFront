<script setup lang="ts">
defineProps<{
  title: string
}>()
</script>

<template>
  <div class="coming-soon">
    <h1>{{ title }}</h1>
    <p>Совсем скоро этот раздел будет доступен...</p>
  </div>
</template>

<style scoped>
.coming-soon {
  padding: 60px;
  width: 100%;
}

h1 {
  margin-bottom: 10px;
}
</style>
