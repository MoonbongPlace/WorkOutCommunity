import axiosInstance from '../axiosInstance'

export const postLikeApi = {
  check: (postId: number) =>
    axiosInstance.get<{ checkPostLike: boolean }>(`/v1/postLikes/${postId}`),

  create: (postId: number) =>
    axiosInstance.post(`/v1/postLikes/${postId}`),

  remove: (postId: number) =>
    axiosInstance.delete(`/v1/postLikes/${postId}`),
}
