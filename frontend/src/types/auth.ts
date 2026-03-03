// POST /api/v1/auth/signin
export interface SigninResponse {
  code: string
  message: string
  timestamp: string
  memberSigninResult: {
    accessToken: string
    refreshToken: string  // 백엔드 body에 포함되나 Cookie로 처리 → 프론트 직접 사용 안 함
    email: string
    memberName: string
    role: string
  }
}

// POST /api/v1/auth/signup
export interface SignupResponse {
  code: string
  message: string
  timestamp: string
  memberSignupResult: unknown  // TODO: MemberSignupResult 구조 확인 후 정의
}

// POST /api/v1/auth/logout
export interface LogoutResponse {
  code: string
  message: string
  timestamp: string
}

// POST /api/v1/auth/reissue
// ⚠ envelope 없는 plain response — code/message/timestamp 없음
// 백엔드 ReissueResponse.java 직접 확인 완료
export interface ReissueResponse {
  grantType: string     // "Bearer"
  accessToken: string
  refreshToken: string  // 백엔드가 Cookie에 자동 세팅 — 프론트 직접 사용 안 함
  atExpIn: number       // AT 만료 시간(초)
  rtExpIn: number       // RT 만료 시간(초)
}
