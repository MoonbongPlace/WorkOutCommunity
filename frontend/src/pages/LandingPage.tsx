import { Link } from 'react-router-dom'
import Container from '../components/ui/Container'
import Hero from '../components/landing/Hero'
import ActivityFeedPreview from '../components/landing/ActivityFeedPreview'
import FeatureGrid from '../components/landing/FeatureGrid'
import AiCoachPreview from '../components/landing/AiCoachPreview'
import StatsStrip from '../components/landing/StatsStrip'
import FinalCta from '../components/landing/FinalCta'

export default function LandingPage() {
  return (
    /**
     * 최상위 wrapper: overflow 없음 → sticky header가 100vw 기준으로 동작
     * overflow-x-hidden은 hero 배경 orbs 클리핑용으로 main 안에만 적용
     */
    <div className="min-h-screen bg-white">

      {/* ── 헤더: overflow wrapper 완전 바깥 ──────────────────────────────
          block 요소이므로 width = 100vw. bg/backdrop-blur를 직접 적용하면
          별도 absolute 레이어 없이 전체 너비를 안정적으로 덮는다.          */}
      <header className="sticky top-0 z-50 w-full bg-white/80 backdrop-blur-md border-b border-[#E8E7D1]/80">
        <Container className="h-14 flex items-center justify-between">
          <Link to="/" className="flex items-center gap-2">
            <div className="w-7 h-7 rounded-lg bg-[#7A7F3A] flex items-center justify-center">
              <svg className="w-4 h-4 text-white" fill="none" stroke="currentColor" strokeWidth={2} viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" d="M3.75 3v11.25A2.25 2.25 0 006 16.5h2.25M3.75 3h-1.5m1.5 0h16.5m0 0h1.5m-1.5 0v11.25A2.25 2.25 0 0118 16.5h-2.25m-7.5 0h7.5m-7.5 0l-1 3m8.5-3l1 3" />
              </svg>
            </div>
            <span className="text-[#7A7F3A] font-bold text-base tracking-tight">FitCom</span>
          </Link>
          <nav className="hidden md:flex items-center gap-6 text-sm text-neutral-500">
            <a href="#demo" className="hover:text-neutral-800 transition-colors">커뮤니티</a>
            <a href="#features" className="hover:text-neutral-800 transition-colors">기능</a>
            <a href="#ai" className="hover:text-neutral-800 transition-colors">AI 코치</a>
          </nav>
          <div className="flex items-center gap-2">
            <Link
              to="/login"
              className="px-3 py-1.5 rounded-lg text-sm font-medium text-neutral-600 hover:bg-[#E8E7D1] hover:text-[#7A7F3A] transition-colors"
            >
              로그인
            </Link>
            <Link
              to="/signup"
              className="px-3 py-1.5 rounded-lg text-sm font-semibold bg-[#7A7F3A] text-white hover:bg-[#696e30] transition-colors"
            >
              회원가입
            </Link>
          </div>
        </Container>
      </header>

      {/* ── 메인 콘텐츠 ────────────────────────────────────────────────── */}
      <main>

        {/* Hero: 배경 레이어를 full-width section에 absolute로 배치하고
            실제 콘텐츠만 Container 안에 유지.
            overflow-hidden(x+y 모두)으로 orb 클리핑 — overflow-x만 쓰면
            CSS 스펙상 overflow-y가 auto로 강제돼 스크롤바가 생긴다. */}
        <section className="relative overflow-hidden">
          {/* 전체 너비 배경 레이어 */}
          <div className="absolute inset-0 bg-gradient-to-br from-[#E8E7D1]/50 via-white to-white pointer-events-none" />
          <div className="absolute -top-32 -right-32 w-[600px] h-[600px] bg-[#7A7F3A]/5 rounded-full blur-3xl pointer-events-none" />
          <div className="absolute -bottom-24 -left-24 w-[400px] h-[400px] bg-[#A6A66A]/8 rounded-full blur-3xl pointer-events-none" />
          {/* 콘텐츠는 Container 폭 유지 */}
          <Container className="relative">
            <Hero />
          </Container>
        </section>

        <StatsStrip />

        <Container>
          <ActivityFeedPreview />
          <FeatureGrid />
        </Container>

        <AiCoachPreview />

        <Container>
          <FinalCta />
        </Container>

      </main>
    </div>
  )
}
