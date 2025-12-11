import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  withCredentials: true,
})

export const blockImageUrl = (blockId: number | string) => `/api/blocks/${blockId}/image`
export const streamVideoUrl = (videoId: number | string) => `/api/v1/videos/${videoId}/stream`

export const apiClient = {
  // Public blocks
  getBlocks: () => api.get('/blocks'),

  // Admin blocks
  addBlock: (request: { title: string }) => api.post('/admin/blocks', request),
  toggleBlockStatus: (blockId: number) => api.patch(`/admin/blocks/${blockId}/status`),
  swapBlocks: (firstBlockId: number, secondBlockId: number) =>
    api.put(`/admin/blocks/${firstBlockId}/swap/${secondBlockId}`),
  updateBlockImage: (blockId: number, image: File) => {
    const form = new FormData()
    form.append('image', image)
    return api.put(`/admin/blocks/${blockId}/image`, form)
  },
  updateBlockVideo: (blockId: number, video: File) => {
    const form = new FormData()
    form.append('video', video)
    return api.put(`/admin/blocks/${blockId}/video`, form)
  },
  updateBlockText: (payload: { blockId: number; title: string }) => api.put('/admin/blocks', payload),
  getAdminBlocks: () => api.get('/admin/blocks'),
  deleteBlock: (blockId: number) => api.delete(`/admin/blocks/${blockId}`),

  // Users
  getUsers: (page: number, size: number) => api.get('/admin/users', { params: { page, size } }),
  updateUser: (userId: string, payload: Record<string, unknown>) =>
    api.patch(`/admin/users/${userId}`, payload),

  // Auth
  getAuthUrl: (deviceId?: string) => api.get('/auth/vkUrl', { params: { device_id: deviceId } }),
  getAuthStatus: () => api.get('/auth/status'),
  sendVkCallback: (params: { code: string; state: string; deviceId: string }) =>
    api.get('/auth/vkCallback', {
      params: {
        code: params.code,
        state: params.state,
        device_id: params.deviceId,
      },
      // VK колбэк может отдавать редирект, поэтому отключаем авто-редиректы и считаем 3xx валидными
      maxRedirects: 0,
      validateStatus: (status) => status < 400,
    }),

  // Videos
  getVideos: () => api.get('/v1/videos'),
  getVideoInfo: (id: number) => api.get(`/v1/videos/${id}`),
  getVideoChunk: (id: number, chunkIndex: number) => api.get(`/v1/videos/${id}/stream/${chunkIndex}`),
  getVideoContentType: (id: number) => api.get(`/v1/videos/${id}/content-type`),
  uploadVideo: (file: File, title: string, description?: string) => {
    const form = new FormData()
    form.append('file', file)
    form.append('title', title)
    if (description) {
      form.append('description', description)
    }
    return api.post('/v1/videos', form)
  },
}
