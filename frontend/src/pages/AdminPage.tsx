import { useEffect, useState } from 'react'
import { adminApi } from '../api/endpoints/admin'
import Avatar from '../components/ui/Avatar'
import StateBlock from '../components/ui/StateBlock'
import type {
  AdminMemberListItem,
  AdminMemberDetail,
  AdminPostListItem,
  MemberStatus,
  PostVisibility,
} from '../types/admin'

// ── 상수 ─────────────────────────────────────────────────────────────
const PAGE_SIZE = 20

type PageStatus = 'loading' | 'success' | 'empty' | 'error'

const STATUS_TABS: { label: string; value: MemberStatus | undefined }[] = [
  { label: '전체',      value: undefined    },
  { label: 'ACTIVE',    value: 'ACTIVE'     },
  { label: 'SUSPENDED', value: 'SUSPENDED'  },
  { label: 'DELETED',   value: 'DELETED'    },
]

const STATUS_STYLE: Record<MemberStatus, string> = {
  ACTIVE:    'bg-green-100 text-green-700',
  SUSPENDED: 'bg-yellow-100 text-yellow-700',
  DELETED:   'bg-red-100 text-red-600',
}

const VISIBILITY_STYLE: Record<PostVisibility, string> = {
  VISIBLE: 'bg-green-100 text-green-700',
  HIDDEN:  'bg-gray-100 text-gray-500',
}

// ── 페이지 번호 컴포넌트 ────────────────────────────────────────────────
function Pagination({
  current,
  total,
  onChange,
}: {
  current: number
  total: number
  onChange: (page: number) => void
}) {
  if (total <= 1) return null
  return (
    <div className="mt-5 flex items-center justify-center gap-1">
      <button
        onClick={() => onChange(current - 1)}
        disabled={current === 0}
        className="w-8 h-8 flex items-center justify-center rounded-lg text-sm text-gray-500 hover:bg-[#E0DFC4] disabled:opacity-30 disabled:cursor-not-allowed transition-colors"
      >
        ‹
      </button>
      {Array.from({ length: total }, (_, i) => (
        <button
          key={i}
          onClick={() => onChange(i)}
          className={`w-8 h-8 flex items-center justify-center rounded-lg text-sm font-medium transition-colors ${
            i === current
              ? 'bg-[#7A7F3A] text-white'
              : 'text-gray-600 hover:bg-[#E0DFC4]'
          }`}
        >
          {i + 1}
        </button>
      ))}
      <button
        onClick={() => onChange(current + 1)}
        disabled={current === total - 1}
        className="w-8 h-8 flex items-center justify-center rounded-lg text-sm text-gray-500 hover:bg-[#E0DFC4] disabled:opacity-30 disabled:cursor-not-allowed transition-colors"
      >
        ›
      </button>
    </div>
  )
}

// ── 회원 상세 모달 ─────────────────────────────────────────────────────
function MemberDetailModal({
  memberId,
  onClose,
  onStatusChanged,
}: {
  memberId: number
  onClose: () => void
  onStatusChanged: () => void
}) {
  const [member,   setMember]   = useState<AdminMemberDetail | null>(null)
  const [loading,  setLoading]  = useState(true)
  const [updating, setUpdating] = useState(false)
  const [error,    setError]    = useState(false)

  useEffect(() => {
    void (async () => {
      try {
        const { data } = await adminApi.memberDetail(memberId)
        if (data.adminMemberDetailResult) {
          setMember(data.adminMemberDetailResult)
        } else {
          setError(true)
        }
      } catch (err) {
        console.error('[AdminPage] memberDetail error:', err)
        setError(true)
      } finally {
        setLoading(false)
      }
    })()
  }, [memberId])

  async function handleStatusChange(next: MemberStatus) {
    if (!member || updating) return
    setUpdating(true)
    try {
      await adminApi.updateMemberStatus(member.id, next)
      setMember((prev) => prev ? { ...prev, status: next } : prev)
      onStatusChanged()
    } catch {
      alert('상태 변경에 실패했습니다.')
    } finally {
      setUpdating(false)
    }
  }

  return (
    <div
      className="fixed inset-0 bg-black/40 flex items-center justify-center z-50"
      onClick={onClose}
    >
      <div
        className="bg-white rounded-xl shadow-xl w-full max-w-md mx-4"
        onClick={(e) => e.stopPropagation()}
      >
        {/* 헤더 */}
        <div className="flex items-center justify-between px-6 py-4 border-b border-[#E8E7D1]">
          <h2 className="text-base font-semibold text-gray-800">회원 상세 정보</h2>
          <button
            onClick={onClose}
            className="text-gray-400 hover:text-gray-600 text-xl leading-none"
            aria-label="닫기"
          >
            ✕
          </button>
        </div>

        {/* 바디 */}
        <div className="px-6 py-5">
          {loading && <StateBlock type="loading" />}
          {error   && <StateBlock type="error" />}
          {member  && (
            <>
              {/* 프로필 */}
              <div className="flex items-center gap-4 mb-5">
                <Avatar src={member.profileImage} name={member.memberName} size="md" />
                <div>
                  <p className="font-semibold text-gray-800">{member.memberName}</p>
                  <p className="text-sm text-gray-500">{member.email}</p>
                </div>
                <span
                  className={`ml-auto text-xs font-medium px-2 py-0.5 rounded-full ${member.status ? STATUS_STYLE[member.status] : 'bg-gray-100 text-gray-400'}`}
                >
                  {member.status ?? '-'}
                </span>
              </div>

              {/* 상세 정보 */}
              <dl className="grid grid-cols-2 gap-x-6 gap-y-3 text-sm">
                {(
                  [
                    ['ID',       String(member.id)],
                    ['이름',     member.name ?? '-'],
                    ['나이',     member.age != null ? String(member.age) : '-'],
                    ['성별',     member.sex ?? '-'],
                    ['역할',     member.role],
                    ['가입일',   member.createdAt?.slice(0, 10) ?? '-'],
                    ['탈퇴일',   member.deletedAt ? member.deletedAt.slice(0, 10) : '-'],
                  ] as [string, string][]
                ).map(([label, value]) => (
                  <div key={label}>
                    <dt className="text-gray-400 text-xs mb-0.5">{label}</dt>
                    <dd className="text-gray-800 font-medium">{value}</dd>
                  </div>
                ))}
              </dl>

              {/* 상태 변경 버튼 */}
              {member.status !== 'DELETED' && (
                <div className="mt-6 flex gap-3 justify-end">
                  {member.status === 'ACTIVE' ? (
                    <button
                      className="bg-yellow-500 hover:bg-yellow-600 text-white text-sm font-medium px-4 py-2 rounded transition-colors disabled:opacity-50"
                      onClick={() => void handleStatusChange('SUSPENDED')}
                      disabled={updating}
                    >
                      {updating ? '처리 중...' : '정지 (SUSPENDED)'}
                    </button>
                  ) : (
                    <button
                      className="bg-green-600 hover:bg-green-700 text-white text-sm font-medium px-4 py-2 rounded transition-colors disabled:opacity-50"
                      onClick={() => void handleStatusChange('ACTIVE')}
                      disabled={updating}
                    >
                      {updating ? '처리 중...' : '활성화 (ACTIVE)'}
                    </button>
                  )}
                </div>
              )}
            </>
          )}
        </div>
      </div>
    </div>
  )
}

// ── 회원 관리 탭 ───────────────────────────────────────────────────────
function MemberTab() {
  const [statusFilter, setStatusFilter] = useState<MemberStatus | undefined>(undefined)
  const [pageStatus,   setPageStatus]   = useState<PageStatus>('loading')
  const [members,      setMembers]      = useState<AdminMemberListItem[]>([])
  const [currentPage,  setCurrentPage]  = useState(0)
  const [totalPages,   setTotalPages]   = useState(0)
  const [selectedId,   setSelectedId]   = useState<number | null>(null)

  async function load(page: number, filter: MemberStatus | undefined = statusFilter) {
    setPageStatus('loading')
    try {
      const { data } = await adminApi.memberList(page, PAGE_SIZE, filter)
      const result = data.adminMemberListResult
      setMembers(result.content)
      setCurrentPage(result.page)
      setTotalPages(result.totalPages)
      setPageStatus(result.content.length === 0 ? 'empty' : 'success')
    } catch {
      setPageStatus('error')
    }
  }

  useEffect(() => { void load(0, statusFilter) }, [statusFilter])

  function handleTabChange(filter: MemberStatus | undefined) {
    setStatusFilter(filter)
    setCurrentPage(0)
  }

  function handlePageChange(page: number) {
    setCurrentPage(page)
    void load(page)
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }

  return (
    <div>
      {/* 상태 탭 */}
      <div className="mb-4 flex gap-1 rounded-lg bg-[#F3F3E6] border border-[#E0DFC4] px-2 py-1">
        {STATUS_TABS.map(({ label, value }) => (
          <button
            key={label}
            onClick={() => handleTabChange(value)}
            className={`px-4 py-1.5 text-sm font-medium rounded-lg transition-colors whitespace-nowrap ${
              statusFilter === value
                ? 'bg-white text-[#7A7F3A] shadow-sm border border-[#C8C89A]'
                : 'text-gray-500 hover:text-[#7A7F3A] hover:bg-white/60'
            }`}
          >
            {label}
          </button>
        ))}
      </div>

      {pageStatus === 'loading' && <StateBlock type="loading" />}
      {pageStatus === 'error'   && <StateBlock type="error" onRetry={() => void load(currentPage)} />}
      {pageStatus === 'empty'   && <StateBlock type="empty" message="회원이 없습니다." />}

      {pageStatus === 'success' && (
        <>
          {/* 테이블 */}
          <div className="overflow-x-auto rounded-lg border border-[#E8E7D1]">
            <table className="w-full text-sm">
              <thead className="bg-[#F3F3E6] text-xs text-gray-500 uppercase tracking-wider">
                <tr>
                  {['ID', '이메일', '닉네임', '이름', '나이', '성별', '역할', '상태', '가입일'].map((h) => (
                    <th key={h} className="px-4 py-3 text-left font-medium whitespace-nowrap">{h}</th>
                  ))}
                </tr>
              </thead>
              <tbody className="divide-y divide-[#E8E7D1] bg-white">
                {members.map((m) => (
                  <tr
                    key={m.id}
                    onClick={() => setSelectedId(m.id)}
                    className="hover:bg-[#F9F9F0] cursor-pointer transition-colors"
                  >
                    <td className="px-4 py-3 text-gray-400">{m.id}</td>
                    <td className="px-4 py-3 text-gray-800">{m.email}</td>
                    <td className="px-4 py-3 font-medium text-gray-800">{m.memberName}</td>
                    <td className="px-4 py-3 text-gray-600">{m.name ?? '-'}</td>
                    <td className="px-4 py-3 text-gray-600">{m.age ?? '-'}</td>
                    <td className="px-4 py-3 text-gray-600">{m.sex ?? '-'}</td>
                    <td className="px-4 py-3 text-gray-600">{m.role}</td>
                    <td className="px-4 py-3">
                      <span className={`text-xs font-medium px-2 py-0.5 rounded-full ${m.status ? STATUS_STYLE[m.status] : 'bg-gray-100 text-gray-400'}`}>
                        {m.status ?? '-'}
                      </span>
                    </td>
                    <td className="px-4 py-3 text-gray-500 whitespace-nowrap">{m.createdAt?.slice(0, 10) ?? '-'}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          <Pagination current={currentPage} total={totalPages} onChange={handlePageChange} />
        </>
      )}

      {/* 회원 상세 모달 */}
      {selectedId !== null && (
        <MemberDetailModal
          memberId={selectedId}
          onClose={() => setSelectedId(null)}
          onStatusChanged={() => void load(currentPage)}
        />
      )}
    </div>
  )
}

// ── 게시글 관리 탭 ─────────────────────────────────────────────────────
function PostTab() {
  const [pageStatus,  setPageStatus]  = useState<PageStatus>('loading')
  const [posts,       setPosts]       = useState<AdminPostListItem[]>([])
  const [currentPage, setCurrentPage] = useState(0)
  const [totalPages,  setTotalPages]  = useState(0)
  const [togglingId,  setTogglingId]  = useState<number | null>(null)

  async function load(page: number) {
    setPageStatus('loading')
    try {
      const { data } = await adminApi.postList(page, PAGE_SIZE)
      const result = data.adminPostListResult
      setPosts(result.content)
      setCurrentPage(result.page)
      setTotalPages(result.totalPages)
      setPageStatus(result.content.length === 0 ? 'empty' : 'success')
    } catch {
      setPageStatus('error')
    }
  }

  useEffect(() => { void load(0) }, [])

  function handlePageChange(page: number) {
    setCurrentPage(page)
    void load(page)
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }

  async function handleVisibilityToggle(post: AdminPostListItem) {
    if (togglingId !== null) return
    const next: PostVisibility = post.visibility === 'VISIBLE' ? 'HIDDEN' : 'VISIBLE'
    setTogglingId(post.id)
    try {
      await adminApi.updatePostVisibility(post.id, next)
      setPosts((prev) =>
        prev.map((p) => (p.id === post.id ? { ...p, visibility: next } : p))
      )
    } catch {
      alert('게시글 상태 변경에 실패했습니다.')
    } finally {
      setTogglingId(null)
    }
  }

  return (
    <div>
      {pageStatus === 'loading' && <StateBlock type="loading" />}
      {pageStatus === 'error'   && <StateBlock type="error" onRetry={() => void load(currentPage)} />}
      {pageStatus === 'empty'   && <StateBlock type="empty" message="게시글이 없습니다." />}

      {pageStatus === 'success' && (
        <>
          <div className="overflow-x-auto rounded-lg border border-[#E8E7D1]">
            <table className="w-full text-sm">
              <thead className="bg-[#F3F3E6] text-xs text-gray-500 uppercase tracking-wider">
                <tr>
                  {['ID', '작성자 ID', '제목', '가시성', '작성일', '관리'].map((h) => (
                    <th key={h} className="px-4 py-3 text-left font-medium whitespace-nowrap">{h}</th>
                  ))}
                </tr>
              </thead>
              <tbody className="divide-y divide-[#E8E7D1] bg-white">
                {posts.map((post) => {
                  const isToggling = togglingId === post.id
                  return (
                    <tr key={post.id} className="hover:bg-[#F9F9F0] transition-colors">
                      <td className="px-4 py-3 text-gray-400">{post.id}</td>
                      <td className="px-4 py-3 text-gray-500">{post.memberId}</td>
                      <td className="px-4 py-3 max-w-xs">
                        <p className="truncate text-gray-800 font-medium">{post.title}</p>
                        {post.content && (
                          <p className="truncate text-gray-400 text-xs mt-0.5">
                            {post.content.replace(/\[\[image:\d+\]\]/g, '').trim()}
                          </p>
                        )}
                      </td>
                      <td className="px-4 py-3">
                        <span className={`text-xs font-medium px-2 py-0.5 rounded-full ${VISIBILITY_STYLE[post.visibility]}`}>
                          {post.visibility}
                        </span>
                      </td>
                      <td className="px-4 py-3 text-gray-500 whitespace-nowrap">{post.createdAt.slice(0, 10)}</td>
                      <td className="px-4 py-3">
                        <button
                          onClick={() => void handleVisibilityToggle(post)}
                          disabled={isToggling}
                          className={`text-xs font-medium px-3 py-1.5 rounded transition-colors disabled:opacity-50 ${
                            post.visibility === 'VISIBLE'
                              ? 'bg-gray-100 hover:bg-gray-200 text-gray-700'
                              : 'bg-green-50 hover:bg-green-100 text-green-700'
                          }`}
                        >
                          {isToggling
                            ? '처리 중...'
                            : post.visibility === 'VISIBLE'
                            ? '숨기기'
                            : '공개'}
                        </button>
                      </td>
                    </tr>
                  )
                })}
              </tbody>
            </table>
          </div>

          <Pagination current={currentPage} total={totalPages} onChange={handlePageChange} />
        </>
      )}
    </div>
  )
}

// ── 메인 관리자 페이지 ──────────────────────────────────────────────────
type AdminTab = 'members' | 'posts'

const ADMIN_TABS: { label: string; value: AdminTab }[] = [
  { label: '회원 관리', value: 'members' },
  { label: '게시글 관리', value: 'posts' },
]

export default function AdminPage() {
  const [activeTab, setActiveTab] = useState<AdminTab>('members')

  return (
    <div>
      {/* 헤더 */}
      <div className="mb-6">
        <h1 className="text-2xl font-bold text-[#7A7F3A]">관리자 페이지</h1>
        <p className="mt-1 text-sm text-gray-500">회원 및 게시글을 관리합니다.</p>
      </div>

      {/* 메인 탭 */}
      <div className="mb-6 flex border-b border-[#E8E7D1]">
        {ADMIN_TABS.map(({ label, value }) => (
          <button
            key={value}
            onClick={() => setActiveTab(value)}
            className={`px-6 py-3 text-sm font-medium transition-colors border-b-2 -mb-px ${
              activeTab === value
                ? 'border-[#7A7F3A] text-[#7A7F3A]'
                : 'border-transparent text-gray-500 hover:text-gray-700'
            }`}
          >
            {label}
          </button>
        ))}
      </div>

      {activeTab === 'members' && <MemberTab />}
      {activeTab === 'posts'   && <PostTab />}
    </div>
  )
}
