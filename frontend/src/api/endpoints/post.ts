import axiosInstance from '../axiosInstance'
import type { PostListResponse, PostDetailResponse, CreatePostRequest } from '../../types/post'

export const postApi = {
  list: (page = 0, size = 20) =>
    axiosInstance.get<PostListResponse>('/v1/posts', { params: { page, size, sort: 'createdAt,desc' } }),

  detail: (postId: number) =>
    axiosInstance.get<PostDetailResponse>(`/v1/posts/${postId}`),

  create: (body: CreatePostRequest) =>
    axiosInstance.post('/v1/posts', body),

  update: (postId: number, body: Partial<CreatePostRequest>) =>
    axiosInstance.put(`/v1/posts/${postId}`, body),

  remove: (postId: number) =>
    axiosInstance.delete(`/v1/posts/${postId}`),
}
