import { useEffect, useRef, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import Card from '../components/ui/Card'
import StateBlock from '../components/ui/StateBlock'
import { postApi } from '../api/endpoints/post'
import { categoryApi } from '../api/endpoints/category'
import { useAuth } from '../context/AuthContext'
import type { PostListItem } from '../types/post'
import type { CategoryItem } from '../types/category'

type Status = 'loading' | 'success' | 'empty' | 'error'

export default function PostsPage() {
  const navigate = useNavigate()
  const { user } = useAuth()
  const [status,         setStatus]         = useState<Status>('loading')
  const [posts,          setPosts]          = useState<PostListItem[]>([])
  const [openMenuId,     setOpenMenuId]     = useState<number | null>(null)
  const [deleteTarget,   setDeleteTarget]   = useState<number | null>(null)
  const [deleting,       setDeleting]       = useState(false)
  const [categories,     setCategories]     = useState<CategoryItem[]>([])
  const [activeCategoryId, setActiveCategoryId] = useState<number | null>(null)
  const tabNavRef = useRef<HTMLElement>(null)

  function scrollTabs(direction: 'left' | 'right') {
    if (!tabNavRef.current) return
    tabNavRef.current.scrollBy({ left: direction === 'left' ? -160 : 160, behavior: 'smooth' })
  }

  // 마우스 휠로 탭 수평 스크롤 (passive: false 필수)
  useEffect(() => {
    const nav = tabNavRef.current
    if (!nav) return
    const handler = (e: WheelEvent) => {
      if (e.deltaY === 0) return
      e.preventDefault()
      nav.scrollBy({ left: e.deltaY, behavior: 'smooth' })
    }
    nav.addEventListener('wheel', handler, { passive: false })
    return () => nav.removeEventListener('wheel', handler)
  }, [])

  async function loadCategories() {
    try {
      const { data } = await categoryApi.list()
      setCategories(data.categoryListResult.categoryList)
    } catch {
      // 카테고리 로드 실패 시 탭 없이 전체 조회만 제공
    }
  }

  async function load(categoryId: number | null = activeCategoryId) {
    setStatus('loading')
    try {
      const { data } = await postApi.list(0, 20, categoryId ?? undefined)
      const items = data.postListResult.content
      setPosts(items)
      setStatus(items.length === 0 ? 'empty' : 'success')
    } catch {
      setStatus('error')
    }
  }

  useEffect(() => {
    void loadCategories()
    void load(null)
  }, [])

  function handleTabChange(categoryId: number | null) {
    setActiveCategoryId(categoryId)
    void load(categoryId)
  }

  // 메뉴 외부 클릭 시 닫기
  useEffect(() => {
    if (openMenuId === null) return
    function handleClick() { setOpenMenuId(null) }
    document.addEventListener('click', handleClick)
    return () => document.removeEventListener('click', handleClick)
  }, [openMenuId])

  async function handleDelete() {
    if (deleteTarget === null) return
    setDeleting(true)
    try {
      await postApi.remove(deleteTarget)
      setPosts((prev) => prev.filter((p) => p.id !== deleteTarget))
      setDeleteTarget(null)
    } catch {
      // 오류 발생 시 모달 유지
    } finally {
      setDeleting(false)
    }
  }

  return (
    <div>
      {/* 헤더 + 글쓰기 버튼 */}
      <div className="mb-4 flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-[#7A7F3A]">게시판</h1>
          <p className="mt-1 text-sm text-gray-500">커뮤니티 게시물</p>
        </div>
        <button className="btn-primary" onClick={() => navigate('/posts/new')}>
          글쓰기
        </button>
      </div>

      {/* 카테고리 탭 */}
      <div className="mb-5 rounded-lg bg-[#F3F3E6] border border-[#E0DFC4] px-2 py-1 flex items-center gap-1">
        {/* 왼쪽 스크롤 버튼 */}
        {categories.length > 0 && (
          <button
            onClick={() => scrollTabs('left')}
            className="shrink-0 w-7 h-7 flex items-center justify-center rounded-lg text-[#7A7F3A] hover:bg-[#E0DFC4] transition-colors"
            aria-label="이전 탭"
          >
            ‹
          </button>
        )}

        {/* 탭 네비게이션 */}
        <nav
          ref={tabNavRef}
          className="flex gap-1 flex-1 overflow-x-auto"
          style={{ scrollbarWidth: 'none', msOverflowStyle: 'none' } as React.CSSProperties}
        >
          <button
            onClick={() => handleTabChange(null)}
            className={`shrink-0 px-4 py-1.5 text-sm font-medium rounded-lg transition-colors whitespace-nowrap ${
              activeCategoryId === null
                ? 'bg-white text-[#7A7F3A] shadow-sm border border-[#C8C89A]'
                : 'text-gray-500 hover:text-[#7A7F3A] hover:bg-white/60'
            }`}
          >
            전체
          </button>
          {categories.map((cat) => (
            <button
              key={cat.id}
              onClick={() => handleTabChange(cat.id)}
              className={`shrink-0 px-4 py-1.5 text-sm font-medium rounded-lg transition-colors whitespace-nowrap ${
                activeCategoryId === cat.id
                  ? 'bg-white text-[#7A7F3A] shadow-sm border border-[#C8C89A]'
                  : 'text-gray-500 hover:text-[#7A7F3A] hover:bg-white/60'
              }`}
            >
              {cat.name}
            </button>
          ))}
        </nav>

        {/* 오른쪽 스크롤 버튼 */}
        {categories.length > 0 && (
          <button
            onClick={() => scrollTabs('right')}
            className="shrink-0 w-7 h-7 flex items-center justify-center rounded-lg text-[#7A7F3A] hover:bg-[#E0DFC4] transition-colors"
            aria-label="다음 탭"
          >
            ›
          </button>
        )}
      </div>

      {status === 'loading' && <StateBlock type="loading" />}
      {status === 'error'   && <StateBlock type="error" onRetry={() => void load(activeCategoryId)} />}
      {status === 'empty'   && <StateBlock type="empty" message="게시물이 없습니다." />}
      {status === 'success' && (
        <ul className="flex flex-col gap-3">
          {posts.map((post) => {
            const isAuthor   = user?.id === post.memberId
            const isMenuOpen = openMenuId === post.id
            return (
              <li key={post.id} className="relative">
                {/* 카드 클릭 → 상세 이동 */}
                <button
                  className="w-full text-left block"
                  onClick={() => navigate(`/posts/${post.id}`)}
                >
                  <Card className={`hover:border-[#A6A66A] transition-colors cursor-pointer${isAuthor ? ' pr-12' : ''}`}>
                    <div className="flex gap-3">
                      <div className="flex-1 min-w-0">
                        <h2 className="font-semibold text-gray-800 mb-1">{post.title}</h2>
                        <div className="flex gap-3 text-xs text-gray-500">
                          <span>{post.memberName}</span>
                          <span>{post.createdAt.slice(0, 10)}</span>
                          {post.categoryId && (() => {
                            const cat = categories.find((c) => c.id === post.categoryId)
                            return cat ? (
                              <span className="bg-[#F0F0E0] text-[#7A7F3A] px-2 py-0.5 rounded-full text-[11px] font-medium">
                                {cat.name}
                              </span>
                            ) : null
                          })()}
                        </div>
                        {post.content && (
                          <p className="text-sm text-gray-600 mt-2 line-clamp-2">{post.content}</p>
                        )}
                      </div>

                      {/* 첫 번째 이미지 썸네일 */}
                      {post.images && post.images.length > 0 && (
                        <div className="relative flex-shrink-0">
                          <img
                            src={post.images[0]}
                            alt="게시글 이미지"
                            className="w-20 h-20 object-cover rounded-lg border border-gray-200"
                          />
                          {post.images.length > 1 && (
                            <span className="absolute bottom-1 right-1 bg-black/50 text-white text-[10px] px-1 rounded">
                              +{post.images.length - 1}
                            </span>
                          )}
                        </div>
                      )}
                    </div>
                  </Card>
                </button>

                {/* 작성자 전용 햄버거 메뉴 */}
                {isAuthor && (
                  <div className="absolute top-3 right-3">
                    <button
                      className="p-1.5 rounded hover:bg-gray-100 transition-colors text-gray-400 hover:text-gray-600 text-lg leading-none"
                      onClick={(e) => { e.stopPropagation(); setOpenMenuId(isMenuOpen ? null : post.id) }}
                      aria-label="게시글 관리"
                    >
                      ⋮
                    </button>

                    {isMenuOpen && (
                      <div className="absolute right-0 top-9 w-28 bg-white border border-[#E8E7D1] rounded-lg shadow-md z-10 py-1">
                        <button
                          className="w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-[#E8E7D1] transition-colors"
                          onClick={(e) => { e.stopPropagation(); setOpenMenuId(null); navigate(`/posts/${post.id}/edit`) }}
                        >
                          수정
                        </button>
                        <button
                          className="w-full text-left px-4 py-2 text-sm text-red-500 hover:bg-red-50 transition-colors"
                          onClick={(e) => { e.stopPropagation(); setOpenMenuId(null); setDeleteTarget(post.id) }}
                        >
                          삭제
                        </button>
                      </div>
                    )}
                  </div>
                )}
              </li>
            )
          })}
        </ul>
      )}

      {/* 삭제 확인 모달 */}
      {deleteTarget !== null && (
        <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50">
          <div className="bg-white rounded-xl p-6 max-w-sm w-full mx-4 shadow-xl">
            <h3 className="text-lg font-semibold text-gray-800 mb-2">게시글 삭제</h3>
            <p className="text-sm text-gray-600 mb-6">
              이 게시글을 삭제하시겠습니까?<br />삭제 후 복구할 수 없습니다.
            </p>
            <div className="flex gap-3 justify-end">
              <button
                className="btn-secondary"
                onClick={() => setDeleteTarget(null)}
                disabled={deleting}
              >
                취소
              </button>
              <button
                className="bg-red-500 hover:bg-red-600 text-white font-medium px-4 py-2 rounded transition-colors disabled:opacity-50"
                onClick={() => void handleDelete()}
                disabled={deleting}
              >
                {deleting ? '삭제 중...' : '삭제'}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
