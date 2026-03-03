import axios, { type AxiosResponse, type InternalAxiosRequestConfig } from 'axios'
import type { ReissueResponse } from '../types/auth'

const baseURL = import.meta.env.VITE_API_BASE_URL ?? '/api'

const axiosInstance = axios.create({
  baseURL,
  withCredentials: true,   // RefreshToken HttpOnly Cookie 자동 전송
  headers: { 'Content-Type': 'application/json' },
})

// ── AccessToken 명시적 주입 헬퍼 ──────────────────────────────────────
// request interceptor로 자동 주입하지 않음
// 로그인 성공 후: setAccessToken(token)
// 로그아웃 후:   setAccessToken(null)
export function setAccessToken(token: string | null): void {
  if (token) {
    axiosInstance.defaults.headers.common['Authorization'] = `Bearer ${token}`
  } else {
    delete axiosInstance.defaults.headers.common['Authorization']
  }
}

// ── 동시 401 제어용 큐 ────────────────────────────────────────────────
interface RetryConfig extends InternalAxiosRequestConfig {
  _retry?: boolean
}

interface QueueItem {
  resolve: (value: AxiosResponse) => void
  reject:  (reason?: unknown) => void
  config:  RetryConfig
}

let isRefreshing = false
let refreshQueue: QueueItem[] = []

// reissue 성공 → 큐 전체 새 토큰으로 재시도 후 resolve
function drainQueueSuccess(newToken: string): void {
  refreshQueue.forEach(({ resolve, reject, config }) => {
    config.headers = config.headers ?? {}   // headers 존재 보장
    config.headers['Authorization'] = `Bearer ${newToken}`

    axiosInstance(config)
      .then(resolve)
      .catch(reject)
  })
  refreshQueue = []
}

// reissue 실패 → 큐 전체 reject (영구 pending 방지)
function drainQueueError(error: unknown): void {
  refreshQueue.forEach(({ reject }) => reject(error))
  refreshQueue = []
}

// ── reissue 경로 판별 ─────────────────────────────────────────────────
// includes() → /reissue-token 오탐 위험
// endsWith()  → 쿼리스트링 (?foo=bar) 누락 위험
// 정규식으로 경로 끝 또는 '?' 직전에서만 매칭
const REISSUE_PATH_RE = /\/v1\/auth\/reissue(\?|$)/

// ── 401 Response Interceptor ──────────────────────────────────────────
axiosInstance.interceptors.response.use(
  (response) => response,

  async (error) => {
    const config         = error.config as RetryConfig | undefined
    const isUnauthorized = error.response?.status === 401
    const isReissueUrl   = REISSUE_PATH_RE.test(config?.url ?? '')

    // 즉시 reject 케이스
    // 1) 401이 아닌 오류
    // 2) config 없음 (네트워크 오류 등)
    // 3) reissue 요청 자체 401 → 무한 루프 방지
    // 4) _retry = true → 이미 1회 재시도 완료
    if (!isUnauthorized || !config || isReissueUrl || config._retry) {
      return Promise.reject(error)
    }

    config._retry = true  // 이후 재시도에서 루프 차단

    // 다른 요청이 이미 reissue 중 → 큐 등록 후 대기
    // AxiosResponse로 resolve / 실패 시 reject 전파
    if (isRefreshing) {
      return new Promise<AxiosResponse>((resolve, reject) => {
        refreshQueue.push({ resolve, reject, config })
      })
    }

    // 첫 번째 401 — reissue 직접 수행
    isRefreshing = true

    try {
      const { data } = await axiosInstance.post<ReissueResponse>('/v1/auth/reissue')
      const newToken = data.accessToken

      setAccessToken(newToken)          // defaults에 반영
      drainQueueSuccess(newToken)       // 대기 큐 일괄 재시도 + resolve

      config.headers = config.headers ?? {}           // headers 존재 보장
      config.headers['Authorization'] = `Bearer ${newToken}`

      return axiosInstance(config)      // 원 요청 1회 재시도

    } catch (reissueError) {
      setAccessToken(null)
      drainQueueError(reissueError)     // 대기 큐 전체 reject
      return Promise.reject(reissueError)

    } finally {
      isRefreshing = false
    }
  },
)

export default axiosInstance
