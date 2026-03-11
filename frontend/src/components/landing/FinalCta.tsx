import { Link } from 'react-router-dom'
import { useInView } from '../../hooks/useInView'

export default function FinalCta() {
  const { ref, inView } = useInView()

  return (
    <section className="py-20">
      <div
        ref={ref}
        className={`reveal ${inView ? 'visible' : ''} relative rounded-3xl overflow-hidden bg-gradient-to-br from-[#7A7F3A] via-[#8A8F45] to-[#A6A66A] p-10 md:p-16 text-center`}
      >
        {/* 배경 장식 */}
        <div className="absolute top-0 right-0 w-64 h-64 bg-white/5 rounded-full -translate-y-1/2 translate-x-1/2 pointer-events-none" />
        <div className="absolute bottom-0 left-0 w-48 h-48 bg-white/5 rounded-full translate-y-1/2 -translate-x-1/2 pointer-events-none" />

        <div className="relative">
          <p className="text-white/70 text-sm font-semibold uppercase tracking-widest mb-4">
            지금 시작하세요
          </p>
          <h2 className="text-3xl md:text-4xl font-bold text-white mb-4 leading-tight">
            오늘의 운동을 기록하고<br />커뮤니티와 함께 성장하세요
          </h2>
          <p className="text-white/75 text-base mb-8 max-w-md mx-auto leading-relaxed">
            무료로 가입하고 운동일지, 커뮤니티, AI 코치를 모두 이용해 보세요.
          </p>
          <div className="flex flex-col sm:flex-row gap-3 justify-center">
            <Link
              to="/signup"
              className="inline-flex items-center justify-center gap-2 px-7 py-3.5 bg-white text-[#7A7F3A] text-sm font-bold rounded-xl hover:bg-neutral-50 transition-all duration-200 hover:shadow-lg hover:-translate-y-0.5"
            >
              시작하기
              <svg className="w-4 h-4" fill="none" stroke="currentColor" strokeWidth={2} viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" d="M13.5 4.5L21 12m0 0l-7.5 7.5M21 12H3" />
              </svg>
            </Link>
            <Link
              to="/login"
              className="inline-flex items-center justify-center px-7 py-3.5 bg-white/15 hover:bg-white/25 text-white text-sm font-semibold rounded-xl border border-white/30 transition-all duration-200"
            >
              이미 계정이 있어요
            </Link>
          </div>
        </div>
      </div>

      {/* 푸터 */}
      <div className="mt-12 pt-6 border-t border-neutral-100 flex flex-col md:flex-row items-center justify-between gap-3">
        <div className="flex items-center gap-2">
          <div className="w-5 h-5 rounded bg-[#7A7F3A] flex items-center justify-center">
            <svg className="w-3 h-3 text-white" fill="none" stroke="currentColor" strokeWidth={2} viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" d="M3.75 3v11.25A2.25 2.25 0 006 16.5h2.25M3.75 3h-1.5m1.5 0h16.5m0 0h1.5m-1.5 0v11.25A2.25 2.25 0 0118 16.5h-2.25m-7.5 0h7.5m-7.5 0l-1 3m8.5-3l1 3" />
            </svg>
          </div>
          <p className="text-xs text-neutral-400">FitCom · Portfolio Project</p>
        </div>
        <div className="flex items-center gap-5 text-xs text-neutral-400">
          <a href="https://github.com" className="hover:text-neutral-600 transition-colors">GitHub</a>
        </div>
      </div>
    </section>
  )
}
