export type MemberStatus = 'ACTIVE' | 'SUSPENDED' | 'DELETED'
export type PostVisibility = 'VISIBLE' | 'HIDDEN'

// ── 회원 목록 ──────────────────────────────────────────────────────────
export interface AdminMemberListItem {
  id: number
  email: string
  memberName: string
  name: string | null
  age: number | null
  sex: string | null
  role: string
  status: MemberStatus
  createdAt: string
}

interface AdminMemberListResult {
  content: AdminMemberListItem[]
  page: number
  size: number
  totalElements: number
  totalPages: number
  last: boolean
}

export interface AdminMemberListResponse {
  code: string
  message: string
  timestamp: string
  adminMemberListResult: AdminMemberListResult
}

// ── 회원 상세 ──────────────────────────────────────────────────────────
export interface AdminMemberDetail {
  id: number
  email: string
  memberName: string
  name: string | null
  age: number | null
  sex: string | null
  role: string
  status: MemberStatus
  createdAt: string
  deletedAt: string | null
  profileImage: string | null
}

export interface AdminMemberDetailResponse {
  code: string
  message: string
  timestamp: string
  adminMemberDetailResult: AdminMemberDetail
}

// ── 회원 상태 변경 ─────────────────────────────────────────────────────
export interface AdminMemberStatusUpdateResponse {
  code: string
  message: string
  timestamp: string
  adminMemberStatusUpdateResult: {
    memberId: number
    status: MemberStatus
    updatedAt: string
  }
}

// ── 게시글 목록 ────────────────────────────────────────────────────────
export interface AdminPostListItem {
  id: number
  memberId: number
  title: string
  content: string
  categoryId: number | null
  images: string[]
  visibility: PostVisibility
  createdAt: string
}

interface AdminPostListResult {
  content: AdminPostListItem[]
  page: number
  size: number
  totalElements: number
  totalPages: number
  last: boolean
}

export interface AdminPostListResponse {
  code: string
  message: string
  timestamp: string
  adminPostListResult: AdminPostListResult
}

// ── 게시글 가시성 변경 ─────────────────────────────────────────────────
export interface AdminPostVisibilityUpdateResponse {
  code: string
  message: string
  timestamp: string
}

// ── 알림 브로드캐스트 ──────────────────────────────────────────────────
export interface AdminBroadcastRequest {
  message: string
  linkUrl?: string
}

export interface AdminBroadcastResponse {
  code: string
  message: string
  timestamp: string
  sent: number
}
