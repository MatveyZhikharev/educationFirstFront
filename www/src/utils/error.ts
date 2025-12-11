import type { AxiosError } from 'axios'

export const resolveError = (e: unknown, fallback: string) => {
  if (typeof e === 'object' && e !== null && 'response' in e) {
    const err = e as AxiosError<{ message?: string }>
    return err.response?.data?.message ?? fallback
  }
  return fallback
}
