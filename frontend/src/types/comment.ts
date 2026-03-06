// GET /api/v1/posts/{postId}/comments
export interface CommentItem {
  id: number
  memberId: number
  memberName: string
  profileImage: string | null
  content: string
  createdAt: string
}

export interface GetCommentsResult {
  comments: CommentItem[]
}

export interface GetCommentsResponse {
  code: string
  message: string
  timestamp: string
  getCommentsResult: GetCommentsResult  // CommentListResponse 필드명
}

// POST /api/v1/posts/{postId}/comments
export interface CreateCommentRequest {
  content: string
}

export interface CreateCommentResult {
  id: number
  memberId: number
  postId: number
  content: string
  createdAt: string
}

export interface CreateCommentResponse {
  code: string
  message: string
  timestamp: string
  createCommentResult: CreateCommentResult
}
