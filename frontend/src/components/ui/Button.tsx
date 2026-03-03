import { type ButtonHTMLAttributes } from 'react'

interface Props extends ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'primary' | 'secondary'
}

export default function Button({ variant = 'primary', className = '', children, ...props }: Props) {
  const base = 'rounded-xl px-4 py-2 text-sm font-medium transition-opacity disabled:opacity-50'
  const styles = {
    primary: 'bg-[#7A7F3A] text-white hover:opacity-90',
    secondary: 'bg-white border border-neutral-200 text-neutral-800 hover:bg-neutral-50',
  }
  return (
    <button className={`${base} ${styles[variant]} ${className}`} {...props}>
      {children}
    </button>
  )
}
