import { useState } from 'react'

interface Props {
  src: string | null | undefined
  name: string
  size?: 'sm' | 'md'
}

/**
 * 백엔드가 반환하는 경로를 브라우저가 사용할 수 있는 URL로 정규화.
 * - 이미 절대 URL(http/https)이면 그대로 사용
 * - /로 시작하는 경로면 그대로 사용 (Vite 프록시가 처리)
 * - resources/static/ 또는 static/ 접두사가 붙은 파일시스템 경로면 제거 후 /로 시작하도록 변환
 * - 그 외 상대 경로면 /를 앞에 붙여 절대 경로로 변환
 */
const DEFAULT_PROFILE = '/assets/default-profile.jpg'

function resolveUrl(src: string | null | undefined): string {
  if (!src) return DEFAULT_PROFILE
  if (src.startsWith('http://') || src.startsWith('https://')) return src
  if (src.startsWith('/')) return src
  const cleaned = src.replace(/^resources\/static\//, '').replace(/^static\//, '')
  return `/${cleaned}`
}

export default function Avatar({ src, name, size = 'md' }: Props) {
  const [imgSrc, setImgSrc] = useState(resolveUrl(src))
  const dim = size === 'sm' ? 'w-7 h-7' : 'w-9 h-9'

  return (
    <img
      src={imgSrc}
      alt={name}
      className={`${dim} rounded-full object-cover flex-shrink-0 border border-gray-200`}
      onError={() => { if (imgSrc !== DEFAULT_PROFILE) setImgSrc(DEFAULT_PROFILE) }}
    />
  )
}
