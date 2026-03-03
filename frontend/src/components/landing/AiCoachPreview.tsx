export default function AiCoachPreview() {
  return (
    <section className="py-12">
      <div className="rounded-2xl border border-neutral-200 bg-white shadow-sm p-6 max-w-2xl">
        <div className="flex items-center gap-2 mb-4">
          <div className="w-7 h-7 rounded-lg bg-[#E8E7D1] flex items-center justify-center">
            <svg className="w-4 h-4 text-[#7A7F3A]" fill="none" stroke="currentColor" strokeWidth={1.5} viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" d="M9.813 15.904L9 18.75l-.813-2.846a4.5 4.5 0 00-3.09-3.09L2.25 12l2.846-.813a4.5 4.5 0 003.09-3.09L9 5.25l.813 2.846a4.5 4.5 0 003.09 3.09L15.75 12l-2.846.813a4.5 4.5 0 00-3.09 3.09z" />
            </svg>
          </div>
          <h2 className="text-sm font-semibold text-neutral-900">
            AI 코치 <span className="text-xs font-normal text-neutral-400">(보조 기능)</span>
          </h2>
        </div>

        {/* 질문 */}
        <div className="flex justify-end mb-3">
          <div className="bg-neutral-100 rounded-2xl rounded-tr-sm px-4 py-2 max-w-xs">
            <p className="text-xs text-neutral-700">스쿼트 시 무릎이 안쪽으로 쏠리는데 어떻게 해야 하나요?</p>
          </div>
        </div>

        {/* 답변 */}
        <div className="flex gap-2">
          <div className="w-6 h-6 rounded-full bg-[#E8E7D1] flex-shrink-0 flex items-center justify-center text-xs font-semibold text-[#7A7F3A] mt-0.5">
            AI
          </div>
          <div className="bg-white border border-neutral-200 rounded-2xl rounded-tl-sm px-4 py-2 max-w-sm">
            <p className="text-xs text-neutral-700 leading-relaxed">
              중둔근 등 외전 근육의 활성화가 부족할 수 있습니다. 클램쉘, 밴드 사이드워크 같은 외전 보강 운동을 병행해 보세요.
            </p>
          </div>
        </div>
      </div>
    </section>
  )
}
