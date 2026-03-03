import { type ReactNode } from 'react'

interface Props { children: ReactNode; className?: string }

export default function Badge({ children, className = '' }: Props) {
  return (
    <span className={`inline-block bg-[#E8E7D1] text-neutral-800 text-xs font-medium px-2 py-0.5 rounded ${className}`}>
      {children}
    </span>
  )
}
