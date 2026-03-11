import { useInView } from '../../hooks/useInView'

const CHAT = [
  {
    role: 'user' as const,
    text: '스쿼트 시 무릎이 안쪽으로 쏠리는데 어떻게 해야 하나요?',
  },
  {
    role: 'ai' as const,
    text: '중둔근 등 외전 근육의 활성화 부족이 원인일 수 있어요. 클램쉘, 밴드 사이드워크 같은 외전 보강 운동을 병행해 보세요. 스쿼트 전 워밍업으로 활성화시키는 것도 효과적입니다.',
  },
  {
    role: 'user' as const,
    text: '데드리프트 후 허리가 뻐근한데 괜찮을까요?',
  },
  {
    role: 'ai' as const,
    text: '일반적인 근육 피로라면 정상이지만, 날카로운 통증이라면 충분한 휴식이 필요해요. 힙힌지 패턴을 재점검하고, 허리 아치가 과도하게 무너지지 않는지 확인해 보세요.',
  },
]

export default function AiCoachPreview() {
  const { ref, inView } = useInView()

  return (
    <section id="ai" className="py-20 bg-gradient-to-br from-[#F5F5EC] via-[#FAFAF6] to-white">
      <div className="max-w-5xl mx-auto px-4 sm:px-6">
        <div ref={ref} className="flex flex-col lg:flex-row items-center gap-12 lg:gap-16">
          {/* 텍스트 */}
          <div className={`flex-1 reveal ${inView ? 'visible' : ''}`}>
            <div className="inline-flex items-center gap-2 bg-[#E8E7D1] text-[#7A7F3A] text-xs font-semibold px-3 py-1.5 rounded-full mb-5">
              <svg className="w-3.5 h-3.5" fill="none" stroke="currentColor" strokeWidth={1.5} viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" d="M9.813 15.904L9 18.75l-.813-2.846a4.5 4.5 0 00-3.09-3.09L2.25 12l2.846-.813a4.5 4.5 0 003.09-3.09L9 5.25l.813 2.846a4.5 4.5 0 003.09 3.09L15.75 12l-2.846.813a4.5 4.5 0 00-3.09 3.09z" />
              </svg>
              AI 코치
            </div>
            <h2 className="text-2xl md:text-3xl font-bold text-neutral-900 mb-4 leading-tight">
              운동 고민,<br />AI 코치에게 물어보세요
            </h2>
            <p className="text-neutral-500 leading-relaxed mb-6">
              자세 교정, 루틴 설계, 부상 예방까지.<br />
              전문가 수준의 조언을 언제든지 받아보세요.
            </p>
            <ul className="space-y-3">
              {[
                '자세 및 기술 개선 조언',
                '개인 맞춤형 루틴 추천',
                '운동 관련 질문 즉시 답변',
              ].map((item) => (
                <li key={item} className="flex items-center gap-2.5 text-sm text-neutral-600">
                  <div className="w-5 h-5 rounded-full bg-[#E8E7D1] flex items-center justify-center flex-shrink-0">
                    <svg className="w-3 h-3 text-[#7A7F3A]" fill="none" stroke="currentColor" strokeWidth={2.5} viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" d="M4.5 12.75l6 6 9-13.5" />
                    </svg>
                  </div>
                  {item}
                </li>
              ))}
            </ul>
          </div>

          {/* 채팅 목업 */}
          <div className={`flex-1 w-full max-w-md reveal reveal-d2 ${inView ? 'visible' : ''}`}>
            <div className="rounded-2xl border border-neutral-200 bg-white shadow-xl overflow-hidden">
              {/* 채팅 헤더 */}
              <div className="flex items-center gap-3 px-4 py-3.5 bg-white border-b border-neutral-100">
                <div className="w-8 h-8 rounded-lg bg-gradient-to-br from-[#E8E7D1] to-[#C8C8A8] flex items-center justify-center">
                  <svg className="w-4 h-4 text-[#7A7F3A]" fill="none" stroke="currentColor" strokeWidth={1.5} viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" d="M9.813 15.904L9 18.75l-.813-2.846a4.5 4.5 0 00-3.09-3.09L2.25 12l2.846-.813a4.5 4.5 0 003.09-3.09L9 5.25l.813 2.846a4.5 4.5 0 003.09 3.09L15.75 12l-2.846.813a4.5 4.5 0 00-3.09 3.09z" />
                  </svg>
                </div>
                <div>
                  <p className="text-sm font-semibold text-neutral-900">AI 코치</p>
                  <div className="flex items-center gap-1.5">
                    <div className="w-1.5 h-1.5 bg-green-400 rounded-full" />
                    <p className="text-xs text-neutral-400">온라인</p>
                  </div>
                </div>
              </div>

              {/* 대화 목록 */}
              <div className="p-4 space-y-3 max-h-72 overflow-y-auto">
                {CHAT.map((msg, i) => (
                  <div
                    key={i}
                    className={`flex gap-2 ${msg.role === 'user' ? 'justify-end' : 'justify-start'}`}
                  >
                    {msg.role === 'ai' && (
                      <div className="w-6 h-6 rounded-full bg-[#E8E7D1] flex-shrink-0 flex items-center justify-center text-xs font-bold text-[#7A7F3A] mt-0.5">
                        AI
                      </div>
                    )}
                    <div
                      className={`rounded-2xl px-3.5 py-2.5 max-w-[75%] text-xs leading-relaxed ${
                        msg.role === 'user'
                          ? 'bg-[#7A7F3A] text-white rounded-tr-sm'
                          : 'bg-neutral-100 text-neutral-700 rounded-tl-sm'
                      }`}
                    >
                      {msg.text}
                    </div>
                  </div>
                ))}

                {/* 타이핑 인디케이터 */}
                <div className="flex gap-2 justify-start">
                  <div className="w-6 h-6 rounded-full bg-[#E8E7D1] flex-shrink-0 flex items-center justify-center text-xs font-bold text-[#7A7F3A]">
                    AI
                  </div>
                  <div className="rounded-2xl rounded-tl-sm bg-neutral-100 px-3.5 py-3 flex items-center gap-1">
                    {[0, 1, 2].map((i) => (
                      <div
                        key={i}
                        className="w-1.5 h-1.5 bg-neutral-400 rounded-full animate-bounce"
                        style={{ animationDelay: `${i * 0.15}s` }}
                      />
                    ))}
                  </div>
                </div>
              </div>

              {/* 입력 영역 */}
              <div className="px-4 py-3 border-t border-neutral-100 flex items-center gap-2">
                <div className="flex-1 bg-neutral-50 border border-neutral-200 rounded-xl px-3 py-2">
                  <p className="text-xs text-neutral-400">운동 고민을 입력하세요...</p>
                </div>
                <button className="w-8 h-8 rounded-xl bg-[#7A7F3A] flex items-center justify-center flex-shrink-0">
                  <svg className="w-4 h-4 text-white" fill="none" stroke="currentColor" strokeWidth={2} viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" d="M6 12L3.269 3.126A59.768 59.768 0 0121.485 12 59.77 59.77 0 013.27 20.876L5.999 12zm0 0h7.5" />
                  </svg>
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  )
}
