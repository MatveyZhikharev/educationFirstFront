<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { apiClient, blockImageUrl, streamVideoUrl } from '@/services/apiClient'
import { resolveError } from '@/utils/error'
import type { BlockResponse } from '@/types/api'

type UserResponse = {
  id: string
  firstName?: string
  lastName?: string
  email?: string
  role?: string
  status?: string
  paymentDate?: string
}

type VideoResponse = {
  id: number
  title: string
  description?: string
  formattedDuration?: string
  formattedFileSize?: string
}

type VideoInfoResponse = {
  id: number
  title: string
  description?: string
  formattedDuration?: string
  formattedFileSize?: string
  mimeType?: string
  totalChunks?: number
  isReady?: boolean
}

const adminBlocks = ref<BlockResponse[]>([])
const blocksError = ref('')
const isLoadingBlocks = ref(false)
const newBlockTitle = ref('')
const updatePayload = reactive({ blockId: '', title: '' })
const swapPayload = reactive({ firstId: '', secondId: '' })
const uploadPayload = reactive<{ blockId: string; image: File | null; video: File | null }>({
  blockId: '',
  image: null,
  video: null,
})

const users = ref<UserResponse[]>([])
const page = ref(0)
const size = ref(5)
const userError = ref('')
const selectedUserId = ref('')
const userUpdate = reactive({
  firstName: '',
  lastName: '',
  email: '',
  status: '',
  role: '',
  paymentDate: '',
})

const videos = ref<VideoResponse[]>([])
const videoInfo = ref<VideoInfoResponse | null>(null)
const selectedVideoId = ref<number | null>(null)
const videoError = ref('')
const chunkIndex = ref(0)
const chunkMessage = ref('')
const contentType = ref('')
const newVideo = reactive({ title: '', description: '' })
const newVideoFile = ref<File | null>(null)

const statusMessage = ref('')
const PAYMENT_DATE_FIELD = 'paymentDate'

type ChunkResponse = {
  encryptedData?: Uint8Array | number[] | string
  byteLength?: number
  chunkIndex?: number
}

const isChunkResponse = (data: unknown): data is ChunkResponse =>
  typeof data === 'object' && data !== null

const formatPaymentDate = (value: string) => (value ? new Date(value).toISOString() : '')

const computeChunkSize = (data: unknown): number => {
  let size = 0
  if (isChunkResponse(data)) {
    if (Array.isArray(data.encryptedData)) size = data.encryptedData.length
    else if (typeof data.encryptedData === 'string') size = data.encryptedData.length
    else if (typeof data.byteLength === 'number') size = data.byteLength
  } else if (Array.isArray(data)) {
    size = data.length
  }
  return size
}

const streamUrl = computed(() => (selectedVideoId.value != null ? streamVideoUrl(selectedVideoId.value) : ''))

const loadBlocks = async () => {
  isLoadingBlocks.value = true
  blocksError.value = ''
  try {
    const { data } = await apiClient.getAdminBlocks()
    adminBlocks.value = data
  } catch (e: unknown) {
    blocksError.value = resolveError(e, 'Не удалось загрузить блоки')
  } finally {
    isLoadingBlocks.value = false
  }
}

const createBlock = async () => {
  if (!newBlockTitle.value) return
  try {
    await apiClient.addBlock({ title: newBlockTitle.value })
    newBlockTitle.value = ''
    statusMessage.value = 'Блок создан'
    await loadBlocks()
  } catch (e: unknown) {
    statusMessage.value = resolveError(e, 'Ошибка создания блока')
  }
}

const toggleBlockStatus = async (blockId: number) => {
  try {
    await apiClient.toggleBlockStatus(blockId)
    statusMessage.value = 'Статус блока обновлён'
    await loadBlocks()
  } catch (e: unknown) {
    statusMessage.value = resolveError(e, 'Не удалось изменить статус блока')
  }
}

const removeBlock = async (blockId: number) => {
  try {
    await apiClient.deleteBlock(blockId)
    statusMessage.value = 'Блок удалён'
    await loadBlocks()
  } catch (e: unknown) {
    statusMessage.value = resolveError(e, 'Не удалось удалить блок')
  }
}

const swapBlocksAction = async () => {
  if (!swapPayload.firstId || !swapPayload.secondId) return
  try {
    await apiClient.swapBlocks(Number(swapPayload.firstId), Number(swapPayload.secondId))
    statusMessage.value = 'Блоки поменяны местами'
    await loadBlocks()
  } catch (e: unknown) {
    statusMessage.value = resolveError(e, 'Не удалось поменять блоки местами')
  }
}

const updateBlockText = async () => {
  if (!updatePayload.blockId || !updatePayload.title) return
  try {
    await apiClient.updateBlockText({
      blockId: Number(updatePayload.blockId),
      title: updatePayload.title,
    })
    statusMessage.value = 'Блок обновлён'
    await loadBlocks()
  } catch (e: unknown) {
    statusMessage.value = resolveError(e, 'Не удалось обновить блок')
  }
}

const uploadBlockImage = async () => {
  if (!uploadPayload.blockId || !uploadPayload.image) return
  try {
    await apiClient.updateBlockImage(Number(uploadPayload.blockId), uploadPayload.image)
    statusMessage.value = 'Изображение обновлено'
  } catch (e: unknown) {
    statusMessage.value = resolveError(e, 'Не удалось загрузить изображение')
  }
}

const uploadBlockVideo = async () => {
  if (!uploadPayload.blockId || !uploadPayload.video) return
  try {
    await apiClient.updateBlockVideo(Number(uploadPayload.blockId), uploadPayload.video)
    statusMessage.value = 'Видео обновлено'
  } catch (e: unknown) {
    statusMessage.value = resolveError(e, 'Не удалось загрузить видео')
  }
}

const onImageChange = (event: Event) => {
  const target = event.target as HTMLInputElement
  uploadPayload.image = target.files?.[0] || null
}

const onBlockVideoChange = (event: Event) => {
  const target = event.target as HTMLInputElement
  uploadPayload.video = target.files?.[0] || null
}

const loadUsers = async () => {
  userError.value = ''
  try {
    const { data } = await apiClient.getUsers(page.value, size.value)
    users.value = data?.content ?? data
  } catch (e: unknown) {
    userError.value = resolveError(e, 'Не удалось загрузить пользователей')
  }
}

const patchUser = async () => {
  if (!selectedUserId.value) return
  const payload: Record<string, unknown> = {}
  Object.entries(userUpdate).forEach(([key, value]) => {
    if (value !== '' && value !== null && value !== undefined) {
      payload[key] = key === PAYMENT_DATE_FIELD ? formatPaymentDate(value as string) : value
    }
  })

  if (!Object.keys(payload).length) {
    statusMessage.value = 'Укажите поля для обновления'
    return
  }

  try {
    await apiClient.updateUser(selectedUserId.value, payload)
    statusMessage.value = 'Пользователь обновлён'
    await loadUsers()
  } catch (e: unknown) {
    statusMessage.value = resolveError(e, 'Не удалось обновить пользователя')
  }
}

const loadVideos = async () => {
  videoError.value = ''
  try {
    const { data } = await apiClient.getVideos()
    videos.value = data
  } catch (e: unknown) {
    videoError.value = resolveError(e, 'Не удалось загрузить видео')
  }
}

const fetchVideoDetails = async (videoId?: number | null) => {
  const id = videoId ?? selectedVideoId.value
  if (id == null) return
  try {
    const { data } = await apiClient.getVideoInfo(id)
    videoInfo.value = data
    selectedVideoId.value = id
  } catch (e: unknown) {
    videoError.value = resolveError(e, 'Не удалось получить информацию о видео')
  }
}

const fetchContentType = async () => {
  if (selectedVideoId.value == null) return
  try {
    const { data } = await apiClient.getVideoContentType(selectedVideoId.value)
    contentType.value = data
  } catch (e: unknown) {
    videoError.value = resolveError(e, 'Не удалось получить content-type')
  }
}

const fetchChunk = async () => {
  if (selectedVideoId.value == null) return
  try {
    const { data } = await apiClient.getVideoChunk(selectedVideoId.value, chunkIndex.value)
    const size = computeChunkSize(data)
    chunkMessage.value = `Чанк ${data?.chunkIndex ?? chunkIndex.value}: получено ${size} байт`
  } catch (e: unknown) {
    videoError.value = resolveError(e, 'Не удалось получить чанк')
  }
}

const onVideoFileChange = (event: Event) => {
  const target = event.target as HTMLInputElement
  newVideoFile.value = target.files?.[0] || null
}

const uploadNewVideo = async () => {
  if (!newVideoFile.value || !newVideo.title) {
    statusMessage.value = 'Добавьте файл и название видео'
    return
  }
  try {
    await apiClient.uploadVideo(newVideoFile.value, newVideo.title, newVideo.description)
    statusMessage.value = 'Видео загружено'
    newVideoFile.value = null
    newVideo.title = ''
    newVideo.description = ''
    await loadVideos()
  } catch (e: unknown) {
    statusMessage.value = resolveError(e, 'Не удалось загрузить видео')
  }
}

onMounted(() => {
  loadBlocks()
  loadUsers()
  loadVideos()
})
</script>

<template>
  <div class="admin-container">
    <div class="header">
      <div>
        <h1>Админ-панель</h1>
        <p class="hint">
          Здесь собраны действия, которые используют почти все эндпоинты: блоки, пользователи и потоковое видео.
        </p>
      </div>
      <p v-if="statusMessage" class="status">{{ statusMessage }}</p>
    </div>

    <section class="admin-section">
      <div class="section-header">
        <div>
          <h2>Блоки</h2>
          <p class="hint">/api/blocks и /api/admin/blocks*</p>
        </div>
        <button class="primary" @click="loadBlocks">Обновить</button>
      </div>

      <div class="grid">
        <div class="card">
          <h3>Создать блок</h3>
          <input v-model="newBlockTitle" placeholder="Название блока">
          <button class="primary" @click="createBlock">Создать</button>
        </div>
        <div class="card">
          <h3>Обновить заголовок</h3>
          <input v-model="updatePayload.blockId" placeholder="ID блока">
          <input v-model="updatePayload.title" placeholder="Новое название">
          <button class="primary" @click="updateBlockText">Сохранить</button>
        </div>
        <div class="card">
          <h3>Поменять местами</h3>
          <input v-model="swapPayload.firstId" placeholder="Первый блок ID">
          <input v-model="swapPayload.secondId" placeholder="Второй блок ID">
          <button class="primary" @click="swapBlocksAction">Поменять</button>
        </div>
      </div>

      <div class="card">
        <h3>Загрузить медиа для блока</h3>
        <div class="form-grid">
          <label>
            ID блока
            <input v-model="uploadPayload.blockId" placeholder="ID блока">
          </label>
          <label>
            Фото (PUT /image)
            <input type="file" accept="image/*" @change="onImageChange">
          </label>
          <label>
            Видео (PUT /video)
            <input type="file" accept="video/*" @change="onBlockVideoChange">
          </label>
        </div>
        <div class="actions">
          <button class="secondary" @click="uploadBlockImage">Обновить фото</button>
          <button class="secondary" @click="uploadBlockVideo">Обновить видео</button>
        </div>
      </div>

      <div v-if="isLoadingBlocks" class="hint">Загружаем список блоков...</div>
      <div v-else-if="blocksError" class="error">{{ blocksError }}</div>
      <div class="block-list">
        <div v-for="block in adminBlocks" :key="block.id" class="card block-card">
          <div class="block-cover">
            <img :src="blockImageUrl(block.id)" alt="block cover">
          </div>
          <h4>{{ block.title }}</h4>
          <p>ID: {{ block.id }}</p>
          <p>Порядок: {{ block.sortOrder ?? '—' }}</p>
          <p>Доступен: {{ block.isAvailable ? 'да' : 'нет' }}</p>
          <p>Тест: {{ block.testId ?? '—' }}</p>
          <div class="actions">
            <button class="secondary" @click="toggleBlockStatus(block.id)">Переключить статус</button>
            <button class="danger" @click="removeBlock(block.id)">Удалить</button>
          </div>
        </div>
      </div>
    </section>

    <section class="admin-section">
      <div class="section-header">
        <div>
          <h2>Пользователи</h2>
          <p class="hint">/api/admin/users*</p>
        </div>
        <button class="primary" @click="loadUsers">Обновить</button>
      </div>

      <div class="card">
        <div class="form-grid">
          <label>
            Страница
            <input v-model.number="page" type="number" min="0">
          </label>
          <label>
            Размер
            <input v-model.number="size" type="number" min="1">
          </label>
        </div>
        <button class="secondary" @click="loadUsers">Применить</button>

        <p v-if="userError" class="error">{{ userError }}</p>
        <div class="table-wrapper" v-if="users.length">
          <table>
            <thead>
            <tr>
              <th>ID</th>
              <th>Имя</th>
              <th>Email</th>
              <th>Роль</th>
              <th>Статус</th>
              <th>Выбрать</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="user in users" :key="user.id" :class="{ selected: selectedUserId === user.id }">
              <td>{{ user.id }}</td>
              <td>{{ user.firstName }} {{ user.lastName }}</td>
              <td>{{ user.email }}</td>
              <td>{{ user.role }}</td>
              <td>{{ user.status }}</td>
              <td>
                <button class="secondary" @click="selectedUserId = user.id">Выбрать</button>
              </td>
            </tr>
            </tbody>
          </table>
        </div>

        <div class="form-grid">
          <label>
            ID пользователя
            <input v-model="selectedUserId" placeholder="Выберите из таблицы или введите">
          </label>
          <label>
            Имя
            <input v-model="userUpdate.firstName" placeholder="firstName">
          </label>
          <label>
            Фамилия
            <input v-model="userUpdate.lastName" placeholder="lastName">
          </label>
          <label>
            Email
            <input v-model="userUpdate.email" placeholder="email">
          </label>
          <label>
            Статус (enum)
            <input v-model="userUpdate.status" placeholder="ACTIVE/BANNED...">
          </label>
          <label>
            Роль (enum)
            <input v-model="userUpdate.role" placeholder="USER/ADMIN...">
          </label>
          <label>
            Дата оплаты
            <input v-model="userUpdate.paymentDate" type="datetime-local">
          </label>
        </div>
        <button class="primary" @click="patchUser">Обновить пользователя</button>
      </div>
    </section>

    <section class="admin-section">
      <div class="section-header">
        <div>
          <h2>Видео / поток</h2>
          <p class="hint">/api/v1/videos*</p>
        </div>
        <button class="primary" @click="loadVideos">Обновить</button>
      </div>

      <div class="grid">
        <div class="card">
          <h3>Список видео</h3>
          <p v-if="videoError" class="error">{{ videoError }}</p>
          <ul class="videos">
            <li v-for="video in videos" :key="video.id">
              <button class="link" @click="fetchVideoDetails(video.id)">
                {{ video.title }} (id: {{ video.id }})
              </button>
              <div class="hint">{{ video.formattedDuration }} • {{ video.formattedFileSize }}</div>
            </li>
          </ul>
        </div>

        <div class="card">
          <h3>Информация о видео</h3>
          <label>
            Video ID
            <input v-model.number="selectedVideoId" type="number" min="0">
          </label>
          <div class="actions">
            <button class="secondary" @click="fetchVideoDetails()">Получить информацию</button>
            <button class="secondary" @click="fetchContentType">Content-Type</button>
          </div>
          <p v-if="contentType">MIME: {{ contentType }}</p>
          <div v-if="videoInfo" class="video-info">
            <p><strong>{{ videoInfo.title }}</strong></p>
            <p>{{ videoInfo.description }}</p>
            <p>Готово к стриму: {{ videoInfo.isReady ? 'да' : 'нет' }}</p>
            <p>Чанков: {{ videoInfo.totalChunks }}</p>
          </div>
          <label>
            Номер чанка
            <input v-model.number="chunkIndex" type="number" min="0">
          </label>
          <button class="secondary" @click="fetchChunk">Запросить чанк</button>
          <p v-if="chunkMessage">{{ chunkMessage }}</p>

          <div v-if="streamUrl" class="video-player">
            <video :src="streamUrl" controls width="320" height="180"></video>
          </div>
        </div>

        <div class="card">
          <h3>Загрузить видео</h3>
          <label>
            Файл
            <input type="file" accept="video/*" @change="onVideoFileChange">
          </label>
          <label>
            Название
            <input v-model="newVideo.title" placeholder="Название видео">
          </label>
          <label>
            Описание
            <textarea v-model="newVideo.description" rows="3" placeholder="Описание видео"></textarea>
          </label>
          <button class="primary" @click="uploadNewVideo">Загрузить</button>
        </div>
      </div>
    </section>
  </div>
</template>

<style scoped>
.admin-container {
  padding: 30px 50px;
  width: 100%;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 20px;
  margin-bottom: 10px;
}

.status {
  background: #f1f8ff;
  border: 1px solid #b6d7ff;
  border-radius: 8px;
  padding: 10px 14px;
}

.hint {
  color: #7a7a7a;
  margin: 4px 0;
}

.admin-section {
  border: 1px solid #e0e0e0;
  border-radius: 12px;
  padding: 20px;
  margin: 20px 0;
  background: #fff;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: 16px;
  margin: 16px 0;
}

.card {
  border: 1px solid #e0e0e0;
  border-radius: 12px;
  padding: 16px;
  background: #fafafa;
}

input,
textarea {
  width: 100%;
  padding: 10px;
  border: 1px solid #d0d0d0;
  border-radius: 8px;
  margin: 6px 0;
  font: inherit;
}

textarea {
  resize: vertical;
}

button {
  border: none;
  border-radius: 8px;
  padding: 10px 14px;
  cursor: pointer;
  font: inherit;
}

.primary {
  background: #007bff;
  color: white;
}

.secondary {
  background: #17a2b8;
  color: white;
}

.danger {
  background: #dc3545;
  color: white;
}

.actions {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
  margin-top: 8px;
}

.block-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: 12px;
}

.block-card {
  background: #fff;
}

.block-cover img {
  width: 100%;
  height: 140px;
  object-fit: cover;
  border-radius: 8px;
}

.error {
  color: #c0392b;
}

.table-wrapper {
  overflow-x: auto;
  margin: 12px 0;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th,
td {
  border: 1px solid #e0e0e0;
  padding: 8px;
  text-align: left;
}

tr.selected {
  background: #f1f8ff;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 12px;
  margin: 10px 0;
}

.videos {
  list-style: none;
  padding: 0;
  margin: 0;
}

.videos li {
  margin-bottom: 8px;
}

.link {
  background: transparent;
  color: #007bff;
  padding: 0;
}

.video-info {
  background: #f8f8f8;
  border-radius: 8px;
  padding: 10px;
  margin: 8px 0;
}

.video-player {
  margin-top: 12px;
}
</style>
