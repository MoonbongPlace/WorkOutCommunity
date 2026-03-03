// POST /api/v1/chat/messages  — plain (envelope 없음)
export interface ChatMessageResponse {
  answer: string
  createdAt: string
}

// GET /api/v1/chat/history  — plain (envelope 없음)
export type ChatRole = 'USER' | 'ASSISTANT' | 'SYSTEM'

export interface ChatHistoryMessage {
  id: number
  role: ChatRole
  message: string
  createdAt: string
}

export interface ChatHistoryResponse {
  chatId: number
  messages: ChatHistoryMessage[]
}
