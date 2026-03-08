import axiosInstance from '../axiosInstance'
import type {
  AdminMemberListResponse,
  AdminMemberDetailResponse,
  AdminMemberStatusUpdateResponse,
  AdminPostListResponse,
  AdminPostVisibilityUpdateResponse,
  AdminBroadcastRequest,
  AdminBroadcastResponse,
  MemberStatus,
  PostVisibility,
} from '../../types/admin'

export const adminApi = {
  // 회원 목록 (status 미전달 시 전체 조회)
  memberList: (page = 0, size = 20, status?: MemberStatus) =>
    axiosInstance.get<AdminMemberListResponse>('/v1/admin/members', {
      params: {
        page,
        size,
        sort: 'createdAt,desc',
        ...(status != null && { status }),
      },
    }),

  // 회원 상세
  memberDetail: (memberId: number) =>
    axiosInstance.get<AdminMemberDetailResponse>(`/v1/admin/members/${memberId}`),

  // 회원 상태 변경 (ACTIVE ↔ SUSPENDED)
  updateMemberStatus: (memberId: number, status: MemberStatus) =>
    axiosInstance.patch<AdminMemberStatusUpdateResponse>(`/v1/admin/${memberId}/status`, { status }),

  // 게시글 목록
  postList: (page = 0, size = 20) =>
    axiosInstance.get<AdminPostListResponse>('/v1/admin/posts', {
      params: { page, size, sort: 'createdAt,desc' },
    }),

  // 게시글 가시성 변경
  updatePostVisibility: (postId: number, postVisibility: PostVisibility) =>
    axiosInstance.patch<AdminPostVisibilityUpdateResponse>(
      `/v1/admin/posts/${postId}/visibility`,
      { postVisibility },
    ),

  // 알림 브로드캐스트
  broadcast: (body: AdminBroadcastRequest) =>
    axiosInstance.post<AdminBroadcastResponse>('/v1/admin/broadcast', body),
}
