import type { AxiosError } from 'axios'

type ErrorPayload = { message?: string; error?: string; detail?: string } | string

export const resolveError = (e: unknown, fallback: string) => {
  if (typeof e === 'object' && e !== null && 'response' in e) {
    const err = e as AxiosError<ErrorPayload>
    const data = err.response?.data
    if (typeof data === 'string') return data || fallback
    if (data?.message) return data.message
    if (data?.error) return data.error
    if (data?.detail) return data.detail
  }
  return fallback
}
