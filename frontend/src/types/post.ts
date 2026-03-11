export type SearchType = 'TITLE' | 'CONTENT' | 'TITLE_CONTENT' | 'AUTHOR'

// GET /api/v1/posts
export interface PostListItem {
  id: number
  memberId: number
  memberName: string
  profileImage: string | null
  title: string
  content: string
  categoryId: number | null
  images: string[]
  visibility: 'VISIBLE' | 'HIDDEN'
  createdAt: string
  likeCount: number
  commentCount: number
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
  memberName: string
  profileImage: string | null
  title: string
  content: string
  categoryId: number | null
  views: number
  images: string[]
  createdAt: string
  likeCount: number
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
}

// PUT /api/v1/posts/{postId}
export interface UpdatePostRequest {
  title: string
  content: string
  categoryId?: number
  keepImages?: string[]
}
