import { useEffect, useRef, useState } from 'react'
import PageHeader from '../components/ui/PageHeader'
import StateBlock from '../components/ui/StateBlock'
import { chatApi } from '../api/endpoints/chat'

interface ChatMessage {
  role: 'user' | 'assistant'
  content: string
}

type HistoryStatus = 'loading' | 'ready' | 'error'

export default function ChatPage() {
  const [historyStatus, setHistoryStatus] = useState<HistoryStatus>('loading')
  const [messages,      setMessages]      = useState<ChatMessage[]>([])
  const [input,         setInput]         = useState('')
  const [sending,       setSending]       = useState(false)
  const bottomRef = useRef<HTMLDivElement>(null)

  async function loadHistory() {
    setHistoryStatus('loading')
    try {
      const { data } = await chatApi.history()
      const mapped: ChatMessage[] = data.messages
        .filter((m) => m.role !== 'SYSTEM')
        .map((m) => ({
          role: m.role === 'USER' ? 'user' : 'assistant',
          content: m.message,
        }))
      setMessages(mapped)
      setHistoryStatus('ready')
    } catch {
      setHistoryStatus('error')
    }
  }

  useEffect(() => { void loadHistory() }, [])
  useEffect(() => { bottomRef.current?.scrollIntoView({ behavior: 'smooth' }) }, [messages])

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

  function handleKeyDown(e: React.KeyboardEvent<HTMLTextAreaElement>) {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault()
      void handleSend()
    }
  }

  if (historyStatus === 'loading') return <StateBlock type="loading" />
  if (historyStatus === 'error')   return <StateBlock type="error" onRetry={() => void loadHistory()} />

  return (
    <div className="flex flex-col h-[calc(100vh-10rem)]">
      <PageHeader title="AI 운동 Q&A" description="운동에 관한 질문을 입력하세요." />

      {/* Message list */}
      <div className="flex-1 overflow-y-auto flex flex-col gap-3 pr-1 pb-4">
        {messages.length === 0 && (
          <p className="text-sm text-gray-400 text-center mt-8">
            운동에 관한 질문을 해보세요.
          </p>
        )}
        {messages.map((msg, idx) => (
          <div
            key={idx}
            className={`flex ${msg.role === 'user' ? 'justify-end' : 'justify-start'}`}
          >
            <div
              className={`max-w-[72%] px-4 py-2.5 rounded-2xl text-sm leading-relaxed whitespace-pre-wrap ${
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
            <div className="bg-[#E8E7D1] px-4 py-2.5 rounded-2xl rounded-bl-sm text-sm text-gray-500 animate-pulse">
              답변 생성 중...
            </div>
          </div>
        )}
        <div ref={bottomRef} />
      </div>

      {/* Input area */}
      <div className="border-t border-[#E8E7D1] pt-4 flex gap-2 items-end">
        <textarea
          className="input-base resize-none flex-1"
          rows={2}
          placeholder="운동 관련 질문을 입력하세요... (Shift+Enter: 줄바꿈)"
          value={input}
          onChange={(e) => setInput(e.target.value)}
          onKeyDown={handleKeyDown}
          disabled={sending}
        />
        <button
          className="btn-primary shrink-0 h-10"
          onClick={() => void handleSend()}
          disabled={sending || !input.trim()}
        >
          전송
        </button>
      </div>
    </div>
  )
}
