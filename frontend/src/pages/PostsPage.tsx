import { useEffect, useRef, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import Card from '../components/ui/Card'
import StateBlock from '../components/ui/StateBlock'
import Avatar from '../components/ui/Avatar'
import { postApi } from '../api/endpoints/post'
import { postLikeApi } from '../api/endpoints/postLike'
import { categoryApi } from '../api/endpoints/category'
import { useAuth } from '../context/AuthContext'
import type { PostListItem, SearchType } from '../types/post'
import type { CategoryItem } from '../types/category'

type Status = 'loading' | 'success' | 'empty' | 'error'

const PAGE_SIZE = 10

const SEARCH_TYPE_LABELS: Record<SearchType, string> = {
  TITLE_CONTENT: '제목+내용',
  TITLE: '제목',
  CONTENT: '내용',
  AUTHOR: '작성자',
}

export default function PostsPage() {
  const navigate = useNavigate()
  const { user } = useAuth()
  const [status,           setStatus]           = useState<Status>('loading')
  const [posts,            setPosts]            = useState<PostListItem[]>([])
  const [openMenuId,       setOpenMenuId]       = useState<number | null>(null)
  const [deleteTarget,     setDeleteTarget]     = useState<number | null>(null)
  const [deleting,         setDeleting]         = useState(false)
  const [categories,       setCategories]       = useState<CategoryItem[]>([])
  const [activeCategoryId, setActiveCategoryId] = useState<number | null>(null)
  const [currentPage,      setCurrentPage]      = useState(0)
  const [totalPages,       setTotalPages]       = useState(0)
  const [likedPostIds,     setLikedPostIds]     = useState<Set<number>>(new Set())
  const [likeCounts,       setLikeCounts]       = useState<Map<number, number>>(new Map())
  const [likingIds,        setLikingIds]        = useState<Set<number>>(new Set())

  // 검색
  const [searchInput,   setSearchInput]   = useState('')
  const [searchType,    setSearchType]    = useState<SearchType>('TITLE_CONTENT')
  const [activeSearch,  setActiveSearch]  = useState<{ keyword: string; searchType: SearchType } | null>(null)

  const tabNavRef = useRef<HTMLElement>(null)

  function scrollTabs(direction: 'left' | 'right') {
    if (!tabNavRef.current) return
    tabNavRef.current.scrollBy({ left: direction === 'left' ? -160 : 160, behavior: 'smooth' })
  }

  useEffect(() => {
    const nav = tabNavRef.current
    if (!nav) return
    const handler = (e: WheelEvent) => {
      if (e.deltaY === 0) return
      e.preventDefault()
      nav.scrollLeft += e.deltaY
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

  async function checkLikes(loadedPosts: PostListItem[]) {
    if (!user || loadedPosts.length === 0) return
    const results = await Promise.allSettled(
      loadedPosts.map((p) => postLikeApi.check(p.id))
    )
    const liked = new Set<number>()
    results.forEach((result, i) => {
      if (result.status === 'fulfilled' && result.value.data.checkPostLike) {
        liked.add(loadedPosts[i].id)
      }
    })
    setLikedPostIds(liked)
  }

  async function load(
    page: number,
    categoryId: number | null = activeCategoryId,
    search: { keyword: string; searchType: SearchType } | null = activeSearch,
  ) {
    setStatus('loading')
    try {
      const { data } = search
        ? await postApi.search(page, PAGE_SIZE, search.keyword, search.searchType, categoryId ?? undefined)
        : await postApi.list(page, PAGE_SIZE, categoryId ?? undefined)

      const result = data.postListResult
      setPosts(result.content)
      setCurrentPage(result.page)
      setTotalPages(result.totalPages)
      setStatus(result.content.length === 0 ? 'empty' : 'success')
      const counts = new Map<number, number>()
      result.content.forEach((p) => counts.set(p.id, p.likeCount))
      setLikeCounts(counts)
      void checkLikes(result.content)
    } catch {
      setStatus('error')
    }
  }

  useEffect(() => {
    void loadCategories()
    void load(0, null, null)
  }, [])

  // user 세팅 후(auth 복원 완료) 좋아요 상태 재조회
  useEffect(() => {
    if (!user || posts.length === 0) return
    void checkLikes(posts)
  }, [user])

  function handleTabChange(categoryId: number | null) {
    setActiveCategoryId(categoryId)
    void load(0, categoryId, activeSearch)
  }

  function handlePageChange(page: number) {
    setCurrentPage(page)
    void load(page)
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }

  function handleSearch(e: React.FormEvent) {
    e.preventDefault()
    const keyword = searchInput.trim()
    if (!keyword) return
    const search = { keyword, searchType }
    setActiveSearch(search)
    setCurrentPage(0)
    void load(0, activeCategoryId, search)
  }

  function handleClearSearch() {
    setSearchInput('')
    setActiveSearch(null)
    setCurrentPage(0)
    void load(0, activeCategoryId, null)
  }

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
      setDeleteTarget(null)
      void load(currentPage)
    } catch {
      // 오류 발생 시 모달 유지
    } finally {
      setDeleting(false)
    }
  }

  async function handleLike(e: React.MouseEvent, postId: number) {
    e.stopPropagation()
    if (!user || likingIds.has(postId)) return

    setLikingIds(prev => new Set(prev).add(postId))
    const isLiked = likedPostIds.has(postId)

    // 낙관적 업데이트
    setLikedPostIds(prev => {
      const next = new Set(prev)
      isLiked ? next.delete(postId) : next.add(postId)
      return next
    })
    setLikeCounts(prev => {
      const next = new Map(prev)
      next.set(postId, (next.get(postId) ?? 0) + (isLiked ? -1 : 1))
      return next
    })

    try {
      if (isLiked) {
        await postLikeApi.remove(postId)
      } else {
        await postLikeApi.create(postId)
      }
    } catch {
      // 실패 시 롤백
      setLikedPostIds(prev => {
        const next = new Set(prev)
        isLiked ? next.add(postId) : next.delete(postId)
        return next
      })
      setLikeCounts(prev => {
        const next = new Map(prev)
        next.set(postId, (next.get(postId) ?? 0) + (isLiked ? 1 : -1))
        return next
      })
    } finally {
      setLikingIds(prev => {
        const next = new Set(prev)
        next.delete(postId)
        return next
      })
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

      {/* 검색 바 (로그인 사용자 전용) */}
      {user && (
        <form onSubmit={handleSearch} className="mb-4">
          <div className="flex items-center bg-white border border-gray-200 rounded-xl shadow-sm overflow-hidden focus-within:ring-2 focus-within:ring-[#A6A66A] focus-within:border-transparent transition-all">
            <select
              value={searchType}
              onChange={(e) => setSearchType(e.target.value as SearchType)}
              className="shrink-0 self-stretch border-r border-gray-200 bg-[#F7F7F0] text-[#7A7F3A] text-sm font-medium px-3 focus:outline-none cursor-pointer"
            >
              {(Object.keys(SEARCH_TYPE_LABELS) as SearchType[]).map((type) => (
                <option key={type} value={type}>{SEARCH_TYPE_LABELS[type]}</option>
              ))}
            </select>
            <input
              type="text"
              value={searchInput}
              onChange={(e) => setSearchInput(e.target.value)}
              placeholder="검색어를 입력하세요"
              className="flex-1 px-4 py-3 text-sm bg-transparent focus:outline-none placeholder-gray-400"
              maxLength={100}
            />
            {activeSearch && (
              <button
                type="button"
                onClick={handleClearSearch}
                className="shrink-0 px-2 text-gray-300 hover:text-gray-500 transition-colors text-lg leading-none"
                aria-label="검색 초기화"
              >
                ✕
              </button>
            )}
            <button
              type="submit"
              className="shrink-0 m-1.5 px-5 py-2 bg-[#7A7F3A] hover:bg-[#696e30] text-white text-sm font-medium rounded-lg transition-colors"
            >
              검색
            </button>
          </div>
        </form>
      )}

      {/* 검색 중 표시 */}
      {activeSearch && (
        <div className="mb-3 flex items-center gap-1.5 text-sm text-gray-500">
          <span className="bg-[#F0F0E0] text-[#7A7F3A] border border-[#D8D8B0] px-2 py-0.5 rounded text-xs font-medium">
            {SEARCH_TYPE_LABELS[activeSearch.searchType]}
          </span>
          <span>
            "<strong className="text-gray-700 font-semibold">{activeSearch.keyword}</strong>" 검색 결과
          </span>
        </div>
      )}

      {/* 카테고리 탭 */}
      <div className="mb-5 rounded-lg bg-[#F3F3E6] border border-[#E0DFC4] px-2 py-1 flex items-center gap-1">
        {categories.length > 0 && (
          <button
            onClick={() => scrollTabs('left')}
            className="shrink-0 w-7 h-7 flex items-center justify-center rounded-lg text-[#7A7F3A] hover:bg-[#E0DFC4] transition-colors"
            aria-label="이전 탭"
          >
            ‹
          </button>
        )}

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
      {status === 'error'   && <StateBlock type="error" onRetry={() => void load(currentPage)} />}
      {status === 'empty'   && (
        <StateBlock
          type="empty"
          message={activeSearch ? '검색 결과가 없습니다.' : '게시물이 없습니다.'}
        />
      )}
      {status === 'success' && (
        <ul className="flex flex-col gap-3">
          {posts.map((post) => {
            const isAuthor   = user?.id === post.memberId
            const isMenuOpen = openMenuId === post.id
            const isLiked    = likedPostIds.has(post.id)
            const likeCount  = likeCounts.get(post.id) ?? post.likeCount
            return (
              <li key={post.id} className="relative">
                <div
                  className="w-full text-left block cursor-pointer"
                  onClick={() => navigate(`/posts/${post.id}`)}
                >
                  <Card className={`hover:border-[#A6A66A] transition-colors py-6${isAuthor ? ' pr-12' : ''}`}>
                    <div className="flex gap-3">
                      <div className="flex-1 min-w-0">
                        <h2 className="text-[1.3rem] font-semibold text-gray-800 mb-1">{post.title}</h2>
                        <div className="flex items-center gap-2 text-xs text-gray-500">
                          <Avatar src={post.profileImage} name={post.memberName} size="sm" />
                          <span>{post.memberName}</span>
                          <span>·</span>
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
                          <p className="text-lg text-gray-600 mt-2 line-clamp-2">
                            {post.content.replace(/\[\[image:\d+\]\]/g, '').trim()}
                          </p>
                        )}
                      </div>

                      {post.images && post.images.length > 0 && (
                        <div className="relative flex-shrink-0">
                          <img
                            src={post.images[0]}
                            alt="게시글 이미지"
                            className="w-[135px] h-[135px] object-cover rounded-lg border border-gray-200"
                          />
                          {post.images.length > 1 && (
                            <span className="absolute bottom-1 right-1 bg-black/50 text-white text-[10px] px-1 rounded">
                              +{post.images.length - 1}
                            </span>
                          )}
                        </div>
                      )}
                    </div>

                    {/* 좋아요 영역 */}
                    <div
                      className="mt-3 pt-3 border-t border-[#E8E7D1] flex items-center gap-1"
                      onClick={(e) => e.stopPropagation()}
                    >
                      <button
                        className={`flex items-center gap-1 text-sm px-2 py-1 rounded-lg transition-colors ${
                          user
                            ? isLiked
                              ? 'text-red-500 hover:bg-red-50'
                              : 'text-gray-400 hover:text-red-400 hover:bg-red-50'
                            : 'text-gray-400 cursor-default'
                        }`}
                        onClick={(e) => void handleLike(e, post.id)}
                        disabled={!user || likingIds.has(post.id)}
                        aria-label={isLiked ? '좋아요 취소' : '좋아요'}
                      >
                        <span className="text-base leading-none">{isLiked ? '♥' : '♡'}</span>
                        <span>{likeCount}</span>
                      </button>
                      <span className="text-sm px-2 py-1 text-gray-400">댓글 {post.commentCount}</span>
                    </div>
                  </Card>
                </div>

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

      {/* 페이지 번호 */}
      {totalPages > 1 && (() => {
        const MAX = 8
        const start = Math.min(Math.max(0, currentPage - Math.floor(MAX / 2)), Math.max(0, totalPages - MAX))
        const end = Math.min(start + MAX, totalPages)
        const pages = Array.from({ length: end - start }, (_, i) => start + i)
        const btnCls = "w-8 h-8 flex items-center justify-center rounded-lg text-sm text-gray-500 hover:bg-[#E0DFC4] disabled:opacity-30 disabled:cursor-not-allowed transition-colors"
        return (
          <div className="mt-6 flex items-center justify-center gap-1">
            {/* 첫 페이지 */}
            <button onClick={() => handlePageChange(0)} disabled={currentPage === 0} className={btnCls} title="첫 페이지">
              «
            </button>
            <button onClick={() => handlePageChange(currentPage - 1)} disabled={currentPage === 0} className={btnCls}>
              ‹
            </button>
            {pages.map((i) => (
              <button
                key={i}
                onClick={() => handlePageChange(i)}
                className={`w-8 h-8 flex items-center justify-center rounded-lg text-sm font-medium transition-colors ${
                  i === currentPage
                    ? 'bg-[#7A7F3A] text-white'
                    : 'text-gray-600 hover:bg-[#E0DFC4]'
                }`}
              >
                {i + 1}
              </button>
            ))}
            <button onClick={() => handlePageChange(currentPage + 1)} disabled={currentPage === totalPages - 1} className={btnCls}>
              ›
            </button>
            {/* 마지막 페이지 */}
            <button onClick={() => handlePageChange(totalPages - 1)} disabled={currentPage === totalPages - 1} className={btnCls} title="마지막 페이지">
              »
            </button>
          </div>
        )
      })()}

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
