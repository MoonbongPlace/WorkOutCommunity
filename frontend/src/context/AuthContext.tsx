import { createContext, useContext, useState, useEffect, useRef, type ReactNode } from 'react'
import axiosInstance, { setAccessToken } from '../api/axiosInstance'
import { authApi } from '../api/endpoints/auth'
import type { ReissueResponse } from '../types/auth'
import type { MemberInfoResponse } from '../types/member'

export interface AuthUser {
  id: number
  email: string
  memberName: string
  role: string
  profileImage: string | null
}

interface AuthContextValue {
  user: AuthUser | null
  isAuthenticated: boolean
  initializing: boolean
  login: (token: string) => Promise<void>
  logout: () => Promise<void>
}

const AuthContext = createContext<AuthContextValue | null>(null)

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user,         setUser]         = useState<AuthUser | null>(null)
  const [initializing, setInitializing] = useState(true)
  // StrictMode는 동일 인스턴스에서 effect를 2회 실행한다.
  // useRef 값은 인스턴스 수명 동안 유지되므로, 두 번째 실행을 차단하는 가드로 활용한다.
  const hasTriedRestoreRef = useRef(false)

  useEffect(() => {
    if (hasTriedRestoreRef.current) return
    hasTriedRestoreRef.current = true

    async function tryRestore() {
      try {
        // RT(HttpOnly Cookie) 로 새 AT 발급
        const { data: reissue } = await axiosInstance.post<ReissueResponse>('/v1/auth/reissue')
        setAccessToken(reissue.accessToken)

        // 유저 정보 조회
        const { data: memberResp } = await axiosInstance.get<MemberInfoResponse>('/v1/members/me')
        const m = memberResp.detailMember
        setUser({ id: m.id, email: m.email, memberName: m.memberName, role: m.role, profileImage: m.profileImage })
      } catch {
        // RT 만료·없음 → 비로그인 상태 유지
      } finally {
        setInitializing(false)
      }
    }
    void tryRestore()
  }, [])

  async function login(token: string) {
    setAccessToken(token)
    const { data: memberResp } = await axiosInstance.get<MemberInfoResponse>('/v1/members/me')
    const m = memberResp.detailMember
    setUser({ id: m.id, email: m.email, memberName: m.memberName, role: m.role, profileImage: m.profileImage })
  }

  async function logout() {
    try { await authApi.logout() } catch { /* 무시 */ }
    setAccessToken(null)
    setUser(null)
  }

  return (
    <AuthContext.Provider value={{ user, isAuthenticated: user !== null, initializing, login, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth(): AuthContextValue {
  const ctx = useContext(AuthContext)
  if (!ctx) throw new Error('useAuth must be used inside AuthProvider')
  return ctx
}
