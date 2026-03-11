import { useEffect, useRef, useState } from 'react'
import { chatApi } from '../../api/endpoints/chat'

interface ChatMessage {
  role: 'user' | 'assistant'
  content: string
}

export default function AiChatWidget() {
  const [open,    setOpen]    = useState(false)
  const [closing, setClosing] = useState(false)
  const [loaded,  setLoaded]  = useState(false)
  const [messages, setMessages] = useState<ChatMessage[]>([])
  const [input,   setInput]   = useState('')
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
        <div className={`${closing ? 'animate-chat-slide-down' : 'animate-chat-slide-up'} w-80 sm:w-96 h-[624px] max-h-[calc(100vh-8rem)] bg-white border border-[#E8E7D1] rounded-2xl shadow-2xl flex flex-col overflow-hidden`}>

          {/* 헤더 - 랜딩 페이지 스타일 */}
          <div className="bg-[#7A7F3A] text-white px-4 py-3 flex items-center justify-between shrink-0">
            <div className="flex items-center gap-2.5">
              <div className="w-8 h-8 rounded-lg bg-white/20 flex items-center justify-center flex-shrink-0">
                <svg className="w-4 h-4" fill="none" stroke="currentColor" strokeWidth={1.5} viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" d="M9.813 15.904L9 18.75l-.813-2.846a4.5 4.5 0 00-3.09-3.09L2.25 12l2.846-.813a4.5 4.5 0 003.09-3.09L9 5.25l.813 2.846a4.5 4.5 0 003.09 3.09L15.75 12l-2.846.813a4.5 4.5 0 00-3.09 3.09z" />
                </svg>
              </div>
              <div>
                <p className="font-semibold text-sm leading-tight">AI 운동 Q&A</p>
                <div className="flex items-center gap-1.5 mt-0.5">
                  <div className="w-1.5 h-1.5 bg-green-400 rounded-full" />
                  <p className="text-xs text-white/75">온라인</p>
                </div>
              </div>
            </div>
            <button
              onClick={handleClose}
              className="p-1 rounded hover:bg-white/20 transition-colors"
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
              <div key={idx} className={`flex gap-2 ${msg.role === 'user' ? 'justify-end' : 'justify-start'}`}>
                {/* AI 아바타 - 랜딩 페이지 스타일 */}
                {msg.role === 'assistant' && (
                  <div className="w-6 h-6 rounded-full bg-[#E8E7D1] flex-shrink-0 flex items-center justify-center text-[10px] font-bold text-[#7A7F3A] mt-0.5">
                    AI
                  </div>
                )}
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

            {/* 3-dot 타이핑 인디케이터 - 랜딩 페이지 스타일 */}
            {sending && (
              <div className="flex gap-2 justify-start">
                <div className="w-6 h-6 rounded-full bg-[#E8E7D1] flex-shrink-0 flex items-center justify-center text-[10px] font-bold text-[#7A7F3A] mt-0.5">
                  AI
                </div>
                <div className="bg-[#E8E7D1] px-3.5 py-3 rounded-2xl rounded-bl-sm flex items-center gap-1">
                  {[0, 1, 2].map((i) => (
                    <div
                      key={i}
                      className="w-1.5 h-1.5 bg-[#7A7F3A]/60 rounded-full animate-bounce"
                      style={{ animationDelay: `${i * 0.15}s` }}
                    />
                  ))}
                </div>
              </div>
            )}
            <div ref={bottomRef} />
          </div>

          {/* 입력 영역 */}
          <div className="border-t border-[#E8E7D1] px-4 py-3 flex items-center gap-2 shrink-0">
            <textarea
              className="flex-1 bg-neutral-50 border border-neutral-200 rounded-xl px-3 py-2 text-sm resize-none focus:outline-none focus:ring-2 focus:ring-[#A6A66A] focus:border-transparent transition placeholder-gray-400"
              rows={1}
              placeholder="질문을 입력하세요... (Shift+Enter: 줄바꿈)"
              value={input}
              onChange={(e) => setInput(e.target.value)}
              onKeyDown={handleKeyDown}
              disabled={sending}
            />
            <button
              className="w-9 h-9 rounded-xl bg-[#7A7F3A] hover:bg-[#696e30] flex items-center justify-center flex-shrink-0 disabled:opacity-50 transition-colors"
              onClick={() => void handleSend()}
              disabled={sending || !input.trim()}
              aria-label="전송"
            >
              <svg className="w-4 h-4 text-white" fill="none" stroke="currentColor" strokeWidth={2} viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" d="M6 12L3.269 3.126A59.768 59.768 0 0121.485 12 59.77 59.77 0 013.27 20.876L5.999 12zm0 0h7.5" />
              </svg>
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
