<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { apiClient, blockImageUrl } from '@/services/apiClient'
import { resolveError } from '@/utils/error'

type BlockResponse = {
  id: number
  title: string
  sortOrder: number
  isAvailable: boolean
  testId?: number | null
}

const isMaterialVisible = ref(false)
const blocks = ref<BlockResponse[]>([])
const isLoading = ref(false)
const error = ref('')

const toggleMaterials = () => {
  isMaterialVisible.value = !isMaterialVisible.value
}

const fetchBlocks = async () => {
  isLoading.value = true
  error.value = ''
  try {
    const { data } = await apiClient.getBlocks()
    blocks.value = data
    if (data?.length) {
      isMaterialVisible.value = true
    }
  } catch (e: unknown) {
    error.value = resolveError(e, 'Не удалось загрузить блоки')
  } finally {
    isLoading.value = false
  }
}

onMounted(fetchBlocks)
</script>

<template>
  <div class="course-container">
    <div class="course-header">
      <h1 class="course-title">Основы ремонта техники</h1>
      <p class="course-description">
        Научитесь ремонтировать технику, используя отвертку, руки и силу мыслей
      </p>
      <div class="course-meta">
        <div class="meta-tags">
          <div class="meta-tag">
            <span>24 часа</span>
          </div>
          <div class="meta-tag">
            <span>Начальный</span>
          </div>
          <div class="meta-tag">
            <span>1,245 студентов</span>
          </div>
        </div>
        <div class="instructor-info">
          <img
              src="https://sun1-85.userapi.com/s/v1/ig2/JquCXZGJElIMQC952QDJEeZfTBDgu5OdiROVOHKdguWo_-LFtxR09nGwdXhMecxOvSH2y6bTIsxvDyi8NR8PGDF1.jpg?quality=95&crop=392,386,328,328&as=32x32,48x48,72x72,108x108,160x160,240x240&ava=1&u=Kx6FRuRqcgp0kBMoK6rQAqva5ZjWDneWjG7NIeBuqns&cs=400x400"
              alt="Преподаватель" class="instructor-avatar">
          <div class="instructor-details">
            <h4 class="instructor-name">Вершинин Максим</h4>
            <p class="instructor-description">Старший разработчик, гений и миллиардер.</p>
          </div>
        </div>
      </div>
    </div>

    <div class="course-content">
      <section class="content-section">
        <h2 class="section-title">Описание</h2>
        <p>
          Этот курс предназначен для тех, кто только начинает свой путь в ремонте. Каждый модуль содержит практические
          задания
          для закрепления материала.
        </p>
      </section>

      <section class="content-section">
        <div class="section-header" @click="toggleMaterials">
          <h2 class="section-title">Опубликованные блоки</h2>
          <div class="toggle-icon">{{ isMaterialVisible ? '▼' : '▶' }}</div>
        </div>
        <div v-if="isLoading" class="status-text">Загружаем блоки...</div>
        <div v-else-if="error" class="status-text error">{{ error }}</div>
        <div v-else-if="isMaterialVisible" class="material-list">
          <div class="material-item" v-for="block in blocks" :key="block.id">
            <div class="item-preview">
              <img :src="blockImageUrl(block.id)" alt="cover" class="item-image" />
            </div>
            <h5 class="item-header">{{ block.title }}</h5>
            <p class="item-description">Сортировка: {{ block.sortOrder ?? '—' }}</p>
            <p class="item-description">Доступен: {{ block.isAvailable ? 'да' : 'нет' }}</p>
          </div>
          <p v-if="!blocks.length" class="status-text">Опубликованные блоки не найдены.</p>
        </div>
      </section>
    </div>
  </div>
</template>

<style scoped>
.course-container {
  padding: 0 100px;
  width: 100%;
}

.meta-tag {
  display: inline-block;
  border: #f1f1f1 solid 1px;
  border-radius: 15px;
  padding: 5px 20px;
  height: 20px;
}

.content-section {
  margin: 10px 0;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: pointer;
  padding: 10px 0;
  border-bottom: 0.5px solid #e0e0e0;
  user-select: none;
}

.toggle-icon {
  font-size: 18px;
  transition: transform 0.3s ease;
}

.material-list {
  display: grid;
  margin: 20px 0;
  grid-template-columns: repeat(auto-fill, 300px);
  gap: 20px;
  justify-content: center;
}

.material-item {
  height: 270px;
  border: 1px solid #e0e0e0;
  border-radius: 15px;
  overflow: hidden;
}

.item-preview {
  position: relative;
  height: 200px;
  width: 300px;
  background-size: cover;
  border-bottom: 1px solid #e0e0e0;
}

.item-header {
  padding: 0 10px;
  margin: 10px 0;
}

.item-description {
  padding: 0 10px;
  margin: 0;
}

.item-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.status-text {
  padding: 10px 0;
}

.status-text.error {
  color: #c0392b;
}

.course-meta {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.instructor-info {
  display: flex;
  align-items: center;
  gap: 25px;
}

.instructor-name {
  margin: 5px 0;
}

.instructor-description {
  margin: 0;
}

.instructor-avatar {
  width: 50px;
  height: 50px;
  border-radius: 25px;
}
</style>
