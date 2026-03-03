import axiosInstance from '../axiosInstance'
import type { ChatMessageResponse, ChatHistoryResponse } from '../../types/chat'

export const chatApi = {
  sendMessage: (message: string) =>
    axiosInstance.post<ChatMessageResponse>('/v1/chat/messages', { message }),

  history: (limit = 30, order = 'asc') =>
    axiosInstance.get<ChatHistoryResponse>('/v1/chat/history', { params: { limit, order } }),
}
