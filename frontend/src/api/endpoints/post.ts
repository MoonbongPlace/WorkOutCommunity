import axiosInstance from '../axiosInstance'
import type { PostListResponse, PostDetailResponse, CreatePostRequest, UpdatePostRequest } from '../../types/post'

export const postApi = {
  me: (page = 0, size = 20) =>
    axiosInstance.get<PostListResponse>('/v1/posts/me', { params: { page, size, sort: 'createdAt,desc' } }),

  list: (page = 0, size = 20) =>
    axiosInstance.get<PostListResponse>('/v1/posts', { params: { page, size, sort: 'createdAt,desc' } }),

  detail: (postId: number) =>
    axiosInstance.get<PostDetailResponse>(`/v1/posts/${postId}`),

  // images: 첨부 이미지 파일 배열 (최대 6장)
  create: (data: CreatePostRequest, images?: File[]) => {
    const formData = new FormData()
    formData.append('data', JSON.stringify(data))
    images?.forEach((file) => formData.append('images', file))
    return axiosInstance.post('/v1/posts', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    })
  },

  // keepImages: 유지할 기존 이미지 URL 배열, images: 새로 추가할 이미지 파일 배열 (keepImages + images 합산 최대 6장)
  update: (postId: number, data: UpdatePostRequest, images?: File[]) => {
    const formData = new FormData()
    formData.append('data', JSON.stringify(data))
    images?.forEach((file) => formData.append('images', file))
    return axiosInstance.put(`/v1/posts/${postId}`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    })
  },

  remove: (postId: number) =>
    axiosInstance.delete(`/v1/posts/${postId}`),
}
