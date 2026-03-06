import { useEffect, useRef, useState } from 'react'
import { chatApi } from '../../api/endpoints/chat'

interface ChatMessage {
  role: 'user' | 'assistant'
  content: string
}

export default function AiChatWidget() {
  const [open, setOpen] = useState(false)
  const [closing, setClosing] = useState(false)
  const [loaded, setLoaded] = useState(false)
  const [messages, setMessages] = useState<ChatMessage[]>([])
  const [input, setInput] = useState('')
  const [sending, setSending] = useState(false)
  const bottomRef = useRef<HTMLDivElement>(null)

  useEffect(() => {
    if (open && !loaded) {
      chatApi.history().then(({ data }) => {
        const mapped: ChatMessage[] = data.messages
          .filter((m) => m.role !== 'SYSTEM')
          .map((m) => ({
            role: m.role === 'USER' ? 'user' : 'assistant',
            content: m.message,
          }))
        setMessages(mapped)
        setLoaded(true)
      }).catch(() => setLoaded(true))
    }
  }, [open, loaded])

  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: 'smooth' })
  }, [messages])

  async function handleSend() {
    const text = input.trim()
    if (!text || sending) return

    setInput('')
    setMessages((prev) => [...prev, { role: 'user', content: text }])
    setSending(true)

    try {
      const { data } = await chatApi.sendMessage(text)
      setMessages((prev) => [...prev, { role: 'assistant', content: data.answer }])
    } catch {
      setMessages((prev) => [...prev, { role: 'assistant', content: '답변을 가져오지 못했습니다. 다시 시도해주세요.' }])
    } finally {
      setSending(false)
    }
  }

  function handleClose() {
    setClosing(true)
    setTimeout(() => {
      setOpen(false)
      setClosing(false)
    }, 220)
  }

  function handleToggle() {
    if (open) {
      handleClose()
    } else {
      setOpen(true)
    }
  }

  function handleKeyDown(e: React.KeyboardEvent<HTMLTextAreaElement>) {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault()
      void handleSend()
    }
  }

  return (
    <div className="fixed bottom-6 right-6 z-50 flex flex-col items-end gap-3">
      {/* 채팅 창 */}
      {(open || closing) && (
        <div className={`${closing ? 'animate-chat-slide-down' : 'animate-chat-slide-up'} w-80 sm:w-96 h-[624px] bg-white border border-[#E8E7D1] rounded-2xl shadow-2xl flex flex-col overflow-hidden`}>
          {/* 헤더 */}
          <div className="bg-[#7A7F3A] text-white px-4 py-3 flex items-center justify-between shrink-0">
            <div>
              <p className="font-semibold text-sm">AI 운동 Q&A</p>
              <p className="text-xs text-[#E8E7D1] mt-0.5">운동에 관한 질문을 입력하세요.</p>
            </div>
            <button
              onClick={handleClose}
              className="p-1 rounded hover:bg-[#5e6430] transition-colors"
              aria-label="닫기"
            >
              <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>

          {/* 메시지 목록 */}
          <div className="flex-1 overflow-y-auto flex flex-col gap-3 p-3">
            {!loaded && (
              <p className="text-sm text-gray-400 text-center mt-8">불러오는 중...</p>
            )}
            {loaded && messages.length === 0 && (
              <p className="text-sm text-gray-400 text-center mt-8">운동에 관한 질문을 해보세요.</p>
            )}
            {messages.map((msg, idx) => (
              <div key={idx} className={`flex ${msg.role === 'user' ? 'justify-end' : 'justify-start'}`}>
                <div
                  className={`max-w-[80%] px-3 py-2 rounded-2xl text-sm leading-relaxed whitespace-pre-wrap ${
                    msg.role === 'user'
                      ? 'bg-[#7A7F3A] text-white rounded-br-sm'
                      : 'bg-[#E8E7D1] text-gray-800 rounded-bl-sm'
                  }`}
                >
                  {msg.content}
                </div>
              </div>
            ))}
            {sending && (
              <div className="flex justify-start">
                <div className="bg-[#E8E7D1] px-3 py-2 rounded-2xl rounded-bl-sm text-sm text-gray-500 animate-pulse">
                  답변 생성 중...
                </div>
              </div>
            )}
            <div ref={bottomRef} />
          </div>

          {/* 입력 영역 */}
          <div className="border-t border-[#E8E7D1] p-3 flex gap-2 items-end shrink-0">
            <textarea
              className="input-base resize-none flex-1 text-sm border-[#BCC0C5]"
              rows={2}
              placeholder="질문을 입력하세요... (Shift+Enter: 줄바꿈)"
              value={input}
              onChange={(e) => setInput(e.target.value)}
              onKeyDown={handleKeyDown}
              disabled={sending}
            />
            <button
              className="btn-primary shrink-0 h-10 text-sm"
              onClick={() => void handleSend()}
              disabled={sending || !input.trim()}
            >
              전송
            </button>
          </div>
        </div>
      )}

      {/* 토글 버튼 */}
      <button
        onClick={handleToggle}
        className="w-[68px] h-[68px] rounded-full bg-[#7A7F3A] text-white shadow-lg hover:bg-[#5e6430] transition-colors flex items-center justify-center"
        aria-label="AI 채팅 열기"
      >
        {open ? (
          <svg className="w-7 h-7" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
          </svg>
        ) : (
          <svg className="w-7 h-7" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
              d="M8 10h.01M12 10h.01M16 10h.01M9 16H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-5l-5 5v-5z" />
          </svg>
        )}
      </button>
    </div>
  )
}
