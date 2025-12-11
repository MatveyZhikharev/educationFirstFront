<script setup lang="ts">
import { reactive, ref } from 'vue'
import type { AxiosError } from 'axios'
import { apiClient } from '@/services/apiClient'

const deviceId = ref('')
const authUrl = ref('')
const authStatus = ref('')
const callbackResult = ref('')
const error = ref('')
const isLoading = ref(false)
const callbackForm = reactive({
  code: '',
  state: '',
  deviceId: '',
})

const resolveError = (e: unknown, fallback: string) => {
  if (typeof e === 'object' && e !== null && 'response' in e) {
    const err = e as AxiosError<{ message?: string }>
    return err.response?.data?.message ?? fallback
  }
  return fallback
}

const runSafe = async (fn: () => Promise<void>) => {
  isLoading.value = true
  error.value = ''
  try {
    await fn()
  } catch (e: unknown) {
    error.value = resolveError(e, 'Не удалось выполнить запрос')
  } finally {
    isLoading.value = false
  }
}

const fetchAuthUrl = () =>
  runSafe(async () => {
    const { data } = await apiClient.getAuthUrl(deviceId.value || undefined)
    authUrl.value = data.url
  })

const checkStatus = () =>
  runSafe(async () => {
    const { data } = await apiClient.getAuthStatus()
    authStatus.value = data.status ? 'Пользователь авторизован' : 'Пользователь не авторизован'
  })

const sendCallback = () =>
  runSafe(async () => {
    await apiClient.sendVkCallback({
      code: callbackForm.code,
      state: callbackForm.state,
      deviceId: callbackForm.deviceId,
    })
    callbackResult.value = 'Колбэк обработан, смотрите редирект в ответах'
  })
</script>

<template>
  <div class="about">
    <h1>Авторизация через VK</h1>
    <p>Здесь собраны действия, которые используют эндпоинты /api/auth.</p>

    <div class="card">
      <h3>Получить ссылку для входа</h3>
      <label>
        Device ID (опционально)
        <input v-model="deviceId" placeholder="device id">
      </label>
      <button @click="fetchAuthUrl" :disabled="isLoading">Сформировать ссылку</button>
      <p v-if="authUrl">
        Ссылка для входа: <a :href="authUrl" target="_blank" rel="noreferrer">{{ authUrl }}</a>
      </p>
    </div>

    <div class="card">
      <h3>Статус пользователя</h3>
      <button @click="checkStatus" :disabled="isLoading">Проверить статус</button>
      <p v-if="authStatus">{{ authStatus }}</p>
    </div>

    <div class="card">
      <h3>Эмуляция колбэка</h3>
      <div class="form-grid">
        <label>
          Code
          <input v-model="callbackForm.code" placeholder="code">
        </label>
        <label>
          State
          <input v-model="callbackForm.state" placeholder="state">
        </label>
        <label>
          Device ID
          <input v-model="callbackForm.deviceId" placeholder="device id">
        </label>
      </div>
      <button @click="sendCallback" :disabled="isLoading">Отправить</button>
      <p v-if="callbackResult">{{ callbackResult }}</p>
    </div>

    <p v-if="error" class="error">{{ error }}</p>
  </div>
</template>

<style scoped>
.about {
  padding: 40px 60px;
  width: 100%;
}

.card {
  border: 1px solid #e0e0e0;
  border-radius: 12px;
  padding: 20px;
  margin: 20px 0;
  max-width: 640px;
}

label {
  display: flex;
  flex-direction: column;
  gap: 6px;
  margin-bottom: 10px;
}

input {
  padding: 10px;
  border: 1px solid #d0d0d0;
  border-radius: 8px;
}

button {
  background: #007bff;
  color: white;
  border: none;
  padding: 10px 16px;
  border-radius: 8px;
  cursor: pointer;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 12px;
}

.error {
  color: #c0392b;
}
</style>
