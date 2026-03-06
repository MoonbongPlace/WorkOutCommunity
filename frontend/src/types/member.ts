// GET /api/v1/members/me
export interface DetailMemberResult {
  id: number
  email: string
  memberName: string
  name: string | null
  age: number | null
  sex: string | null
  role: string
  createdAt: string
  status: 'ACTIVE' | 'SUSPENDED' | 'DELETED'
  profileImage: string | null
}

export interface MemberInfoResponse {
  code: string
  message: string
  timestamp: string
  detailMember: DetailMemberResult
}
