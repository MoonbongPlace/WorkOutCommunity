import axiosInstance from '../axiosInstance'
import type { GetCommentsResponse, CreateCommentRequest, CreateCommentResponse } from '../../types/comment'

export const commentApi = {
  list: (postId: number) =>
    axiosInstance.get<GetCommentsResponse>(`/v1/posts/${postId}/comments`),

  create: (postId: number, data: CreateCommentRequest) =>
    axiosInstance.post<CreateCommentResponse>(`/v1/posts/${postId}/comments`, data),

  remove: (postId: number, commentId: number) =>
    axiosInstance.delete(`/v1/posts/${postId}/comments/${commentId}`),
}
