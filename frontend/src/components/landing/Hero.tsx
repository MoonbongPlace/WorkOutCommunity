import { Link } from 'react-router-dom'

/**
 * 배경(그라디언트, orbs)은 LandingPage의 full-width <section>에서 처리.
 * Hero는 py padding + flex 콘텐츠만 담당한다.
 */
export default function Hero() {
  return (
    <div className="py-20 md:py-28">
      <div className="flex flex-col lg:flex-row items-center gap-12 lg:gap-16">

        {/* 텍스트 컬럼 */}
        <div className="flex-1 space-y-7">
          <div>
            <div className="inline-flex items-center gap-2 bg-[#E8E7D1] text-[#7A7F3A] text-xs font-semibold px-3 py-1.5 rounded-full mb-6">
              <span className="w-2 h-2 bg-[#7A7F3A] rounded-full animate-pulse" />
              실시간 운동 커뮤니티
            </div>
            <h1 className="text-4xl md:text-5xl lg:text-[3.5rem] font-bold text-neutral-900 leading-[1.15] tracking-tight">
              운동을 기록하고,<br />
              <span className="text-transparent bg-clip-text bg-gradient-to-r from-[#7A7F3A] to-[#A6A66A]">
                함께 성장하세요
              </span>
            </h1>
          </div>

          <p className="text-base md:text-lg text-neutral-600 leading-relaxed max-w-md">
            구조화된 운동일지로 내 루틴을 기록하고,<br className="hidden sm:block" />
            커뮤니티 피드에서 동기를 얻어보세요.<br className="hidden sm:block" />
            AI 코치가 운동 고민도 해결해 드립니다.
          </p>

          <div className="flex flex-col sm:flex-row gap-3">
            <Link
              to="/signup"
              className="inline-flex items-center justify-center gap-2 px-6 py-3.5 bg-[#7A7F3A] hover:bg-[#696e30] text-white text-sm font-semibold rounded-xl transition-all duration-200 hover:shadow-lg hover:shadow-[#7A7F3A]/30 hover:-translate-y-0.5"
            >
              시작하기
              <svg className="w-4 h-4" fill="none" stroke="currentColor" strokeWidth={2} viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" d="M13.5 4.5L21 12m0 0l-7.5 7.5M21 12H3" />
              </svg>
            </Link>
            <a
              href="#demo"
              className="inline-flex items-center justify-center gap-2 px-6 py-3.5 bg-white border border-neutral-200 hover:border-[#A6A66A] hover:bg-[#E8E7D1]/30 text-neutral-700 text-sm font-semibold rounded-xl transition-all duration-200"
            >
              커뮤니티 둘러보기
            </a>
          </div>

        </div>

        {/* 목업 컬럼 */}
        <div className="flex-1 w-full max-w-md lg:max-w-lg relative">
          {/* 게시글 카드 - PostsPage Card와 동일한 스타일 */}
          <div className="bg-white border border-[#E8E7D1] rounded-lg shadow-sm p-5 relative z-10">
            {/* 제목 */}
            <h2 className="text-[1.05rem] font-semibold text-gray-800 mb-1">데드리프트 PR 달성! 🎉</h2>
            {/* 작성자 행 */}
            <div className="flex items-center gap-2 text-xs text-gray-500 mb-2">
              <div className="w-7 h-7 rounded-full bg-[#E8E7D1] flex items-center justify-center text-xs font-semibold text-[#7A7F3A] border border-gray-200 flex-shrink-0">
                김
              </div>
              <span>김민준</span>
              <span>·</span>
              <span>2026-03-11</span>
              <span className="bg-[#F0F0E0] text-[#7A7F3A] px-2 py-0.5 rounded-full text-[11px] font-medium">
                운동인증
              </span>
            </div>
            {/* 본문 */}
            <p className="text-sm text-gray-600 line-clamp-2">
              드디어 140kg 성공했습니다. 6개월간 꾸준히 훈련한 결과네요. 다들 포기하지 말고 화이팅!
            </p>
            {/* 좋아요 / 댓글 */}
            <div className="mt-3 pt-3 border-t border-[#E8E7D1] flex items-center gap-1">
              <span className="flex items-center gap-1 text-sm px-2 py-1 text-red-500">
                <span className="text-base leading-none">♥</span>
                <span>47</span>
              </span>
              <span className="text-sm px-2 py-1 text-gray-400">댓글 12</span>
            </div>
          </div>

          {/* 운동일지 카드 */}
          <div className="rounded-2xl border border-neutral-200 bg-white shadow-2xl shadow-neutral-200/80 p-5 mt-3 ml-6 relative z-0">
            <div className="flex items-center justify-between mb-4">
              <div>
                <p className="text-xs font-bold text-[#7A7F3A] uppercase tracking-wider">운동일지</p>
                <p className="text-sm font-bold text-neutral-900 mt-0.5">2026.03.11 · 하체</p>
              </div>
              <div className="w-9 h-9 rounded-xl bg-[#E8E7D1] flex items-center justify-center">
                <svg className="w-5 h-5 text-[#7A7F3A]" fill="none" stroke="currentColor" strokeWidth={1.5} viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" d="M3.75 3v11.25A2.25 2.25 0 006 16.5h2.25M3.75 3h-1.5m1.5 0h16.5m0 0h1.5m-1.5 0v11.25A2.25 2.25 0 0118 16.5h-2.25m-7.5 0h7.5m-7.5 0l-1 3m8.5-3l1 3m0 0l.5 1.5m-.5-1.5h-9.5m0 0l-.5 1.5M9 11.25v1.5M12 9v3.75m3-6v6" />
                </svg>
              </div>
            </div>
            <div className="space-y-2">
              {[
                { name: '스쿼트', sets: 4, reps: '10', rpe: 8 },
                { name: '루마니안 데드리프트', sets: 3, reps: '8', rpe: 7 },
                { name: '레그 프레스', sets: 3, reps: '12', rpe: 6 },
              ].map((item) => (
                <div key={item.name} className="flex items-center justify-between bg-neutral-50 rounded-lg px-3 py-2">
                  <span className="text-xs font-medium text-neutral-700 truncate mr-2">{item.name}</span>
                  <div className="flex items-center gap-1.5 flex-shrink-0">
                    <span className="text-xs text-neutral-500">{item.sets}×{item.reps}</span>
                    <span className="text-xs bg-[#E8E7D1] text-[#7A7F3A] px-1.5 py-0.5 rounded font-bold">
                      RPE {item.rpe}
                    </span>
                  </div>
                </div>
              ))}
            </div>
          </div>

          {/* 플로팅 알림 뱃지 */}
          <div className="absolute -top-3 -right-2 lg:-right-6 z-20 bg-white border border-neutral-200 shadow-lg rounded-xl px-3 py-2 flex items-center gap-2 animate-float">
            <div className="w-2 h-2 bg-[#7A7F3A] rounded-full" />
            <p className="text-xs font-medium text-neutral-700 whitespace-nowrap">이지은님이 좋아요를 눌렀습니다</p>
          </div>
        </div>

      </div>
    </div>
  )
}
