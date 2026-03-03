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
    <div className="min-h-screen bg-white">
      {/* 랜딩 전용 헤더 */}
      <header className="sticky top-0 z-50 bg-white border-b border-[#E8E7D1]">
        <Container className="h-14 flex items-center justify-between">
          <Link to="/" className="text-[#7A7F3A] font-bold text-lg tracking-tight">
            Community
          </Link>
          <div className="flex items-center gap-2">
            <Link
              to="/login"
              className="px-3 py-1.5 rounded text-sm font-medium text-neutral-600 hover:bg-[#E8E7D1] hover:text-[#7A7F3A] transition-colors"
            >
              로그인
            </Link>
            <Link
              to="/signup"
              className="px-3 py-1.5 rounded-xl text-sm font-medium bg-[#7A7F3A] text-white hover:opacity-90 transition-opacity"
            >
              회원가입
            </Link>
          </div>
        </Container>
      </header>

      <Container>
        <Hero />
        <ActivityFeedPreview />
        <FeatureGrid />
        <AiCoachPreview />
        <StatsStrip />
        <FinalCta />
      </Container>
    </div>
  )
}
