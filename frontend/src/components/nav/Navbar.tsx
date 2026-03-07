import { useState, useEffect, useRef } from 'react'
import { NavLink, useNavigate } from 'react-router-dom'
import { useAuth } from '../../context/AuthContext'
import Avatar from '../ui/Avatar'
import NotificationWidget from '../ui/NotificationWidget'

const links = [
  { to: '/posts',        label: '게시판' },
  { to: '/workout-logs', label: '운동일지' },
]

const linkClass = ({ isActive }: { isActive: boolean }) =>
  `px-3 py-1.5 rounded text-sm font-medium transition-colors ${
    isActive
      ? 'bg-[#E8E7D1] text-[#7A7F3A]'
      : 'text-gray-600 hover:bg-[#E8E7D1] hover:text-[#7A7F3A]'
  }`

export default function Navbar() {
  const [open, setOpen] = useState(false)
  const menuRef = useRef<HTMLDivElement>(null)
  const { user, logout } = useAuth()
  const navigate = useNavigate()

  async function handleLogout() {
    await logout()
    navigate('/login')
  }

  // 메뉴 외부 클릭 시 닫기
  useEffect(() => {
    function handleClickOutside(e: MouseEvent) {
      if (menuRef.current && !menuRef.current.contains(e.target as Node)) {
        setOpen(false)
      }
    }
    if (open) document.addEventListener('mousedown', handleClickOutside)
    return () => document.removeEventListener('mousedown', handleClickOutside)
  }, [open])

  return (
    <nav className="bg-white border-b border-[#E8E7D1] sticky top-0 z-50">
      <div className="max-w-5xl mx-auto px-4 h-14 flex items-center justify-between">
        {/* 로고 */}
        <NavLink to="/posts" className="text-[#7A7F3A] font-bold text-lg tracking-tight">
          Community
        </NavLink>

        {/* 데스크톱 링크 (md 이상) */}
        <ul className="hidden md:flex items-center gap-1">
          {links.map(({ to, label }) => (
            <li key={to}>
              <NavLink to={to} className={linkClass}>{label}</NavLink>
            </li>
          ))}
          {user && (
            <>
              <li>
                <NotificationWidget />
              </li>
              <li>
                <NavLink to="/me" className="flex items-center ml-1">
                  <Avatar src={user.profileImage} name={user.memberName} size="sm" />
                </NavLink>
              </li>
              <li>
                <button
                  onClick={handleLogout}
                  className="px-3 py-1.5 rounded text-sm font-medium text-gray-600 hover:bg-[#E8E7D1] hover:text-[#7A7F3A] transition-colors"
                >
                  로그아웃
                </button>
              </li>
            </>
          )}
        </ul>

        {/* 모바일 햄버거 버튼 (md 미만) */}
        <div className="relative md:hidden" ref={menuRef}>
          <button
            className="p-2 rounded hover:bg-[#E8E7D1] transition-colors"
            onClick={() => setOpen((v) => !v)}
            aria-label="메뉴 열기"
          >
            {open ? (
              /* X 아이콘 */
              <svg className="w-5 h-5 text-[#7A7F3A]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
              </svg>
            ) : (
              /* 햄버거 아이콘 */
              <svg className="w-5 h-5 text-[#7A7F3A]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
              </svg>
            )}
          </button>

          {/* 드롭다운 메뉴 */}
          {open && (
            <ul className="absolute right-0 top-full mt-1 w-36 bg-white border border-[#E8E7D1] rounded-lg shadow-md py-1 flex flex-col">
              {links.map(({ to, label }) => (
                <li key={to}>
                  <NavLink
                    to={to}
                    className={({ isActive }) =>
                      `block px-4 py-2 text-sm font-medium transition-colors ${
                        isActive
                          ? 'bg-[#E8E7D1] text-[#7A7F3A]'
                          : 'text-gray-600 hover:bg-[#E8E7D1] hover:text-[#7A7F3A]'
                      }`
                    }
                    onClick={() => setOpen(false)}
                  >
                    {label}
                  </NavLink>
                </li>
              ))}
              {user && (
                <>
                  <li>
                    <NavLink
                      to="/me"
                      className={({ isActive }) =>
                        `block px-4 py-2 text-sm font-medium transition-colors ${
                          isActive
                            ? 'bg-[#E8E7D1] text-[#7A7F3A]'
                            : 'text-gray-600 hover:bg-[#E8E7D1] hover:text-[#7A7F3A]'
                        }`
                      }
                      onClick={() => setOpen(false)}
                    >
                      내 정보
                    </NavLink>
                  </li>
                  <li>
                    <button
                      className="w-full text-left px-4 py-2 text-sm font-medium text-gray-600 hover:bg-[#E8E7D1] hover:text-[#7A7F3A] transition-colors"
                      onClick={() => { setOpen(false); handleLogout() }}
                    >
                      로그아웃
                    </button>
                  </li>
                </>
              )}
            </ul>
          )}
        </div>
      </div>
    </nav>
  )
}
