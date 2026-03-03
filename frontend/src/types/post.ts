// GET /api/v1/posts
export interface PostListItem {
  id: number
  memberId: number
  title: string
  content: string
  categoryId: number | null
  image: string | null
  visibility: 'VISIBLE' | 'HIDDEN'
  createdAt: string
}

export interface PostListResult {
  content: PostListItem[]
  page: number
  size: number
  totalElements: number
  totalPages: number
  last: boolean
}

export interface PostListResponse {
  code: string
  message: string
  timestamp: string
  postListResult: PostListResult
}

// GET /api/v1/posts/{postId}
export interface PostDetailResult {
  id: number
  member_id: number
  title: string
  content: string
  categoryId: number | null
  views: number
  image: string | null
  createdAt: string
}

export interface PostDetailResponse {
  code: string
  message: string
  timestamp: string
  post: PostDetailResult
}

// POST /api/v1/posts
export interface CreatePostRequest {
  title: string
  content: string
  categoryId?: number
  image?: string
}
