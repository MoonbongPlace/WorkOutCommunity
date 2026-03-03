import { type ReactNode } from 'react'

interface Props {
  children: ReactNode
  className?: string
}

export default function Card({ children, className = '' }: Props) {
  return (
    <div
      className={`bg-white border border-[#E8E7D1] rounded-lg shadow-sm p-5 ${className}`}
    >
      {children}
    </div>
  )
}
