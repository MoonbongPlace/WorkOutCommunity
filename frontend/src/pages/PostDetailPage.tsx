import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import Card from '../components/ui/Card'
import StateBlock from '../components/ui/StateBlock'
import { postApi } from '../api/endpoints/post'
import { useAuth } from '../context/AuthContext'
import type { PostDetailResult } from '../types/post'

export default function PostDetailPage() {
  const { postId } = useParams<{ postId: string }>()
  const navigate   = useNavigate()
  const { user }   = useAuth()
  const [status, setStatus] = useState<'loading' | 'success' | 'error'>('loading')
  const [post,   setPost]   = useState<PostDetailResult | null>(null)
  const [menuOpen,        setMenuOpen]        = useState(false)
  const [showDeleteModal, setShowDeleteModal] = useState(false)
  const [deleting,        setDeleting]        = useState(false)

  async function load() {
    if (!postId) return
    setStatus('loading')
    try {
      const { data } = await postApi.detail(Number(postId))
      setPost(data.post)
      setStatus('success')
    } catch {
      setStatus('error')
    }
  }

  useEffect(() => { void load() }, [postId])

  // 메뉴 외부 클릭 시 닫기
  useEffect(() => {
    if (!menuOpen) return
    function handleClick() { setMenuOpen(false) }
    document.addEventListener('click', handleClick)
    return () => document.removeEventListener('click', handleClick)
  }, [menuOpen])

  async function handleDelete() {
    if (!postId) return
    setDeleting(true)
    try {
      await postApi.remove(Number(postId))
      navigate('/posts')
    } catch {
      setDeleting(false)
      setShowDeleteModal(false)
    }
  }

  const isAuthor = post && user && post.member_id === user.id

  return (
    <div>
      <button
        className="flex items-center gap-1 text-sm text-gray-500 hover:text-[#7A7F3A] mb-4 transition-colors"
        onClick={() => navigate(-1)}
      >
        ← 목록으로
      </button>

      {status === 'loading' && <StateBlock type="loading" />}
      {status === 'error'   && <StateBlock type="error" onRetry={() => void load()} />}
      {status === 'success' && post && (
        <Card>
          <div className="flex items-start justify-between mb-3">
            <h1 className="text-xl font-bold text-gray-800">{post.title}</h1>

            {isAuthor && (
              <div className="relative ml-2 flex-shrink-0">
                <button
                  className="p-1.5 rounded hover:bg-gray-100 transition-colors text-gray-400 hover:text-gray-600 text-lg leading-none"
                  onClick={(e) => { e.stopPropagation(); setMenuOpen((prev) => !prev) }}
                  aria-label="게시글 관리"
                >
                  ⋮
                </button>

                {menuOpen && (
                  <div className="absolute right-0 top-9 w-28 bg-white border border-[#E8E7D1] rounded-lg shadow-md z-10 py-1">
                    <button
                      className="w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-[#E8E7D1] transition-colors"
                      onClick={() => { setMenuOpen(false); navigate(`/posts/${postId}/edit`) }}
                    >
                      수정
                    </button>
                    <button
                      className="w-full text-left px-4 py-2 text-sm text-red-500 hover:bg-red-50 transition-colors"
                      onClick={() => { setMenuOpen(false); setShowDeleteModal(true) }}
                    >
                      삭제
                    </button>
                  </div>
                )}
              </div>
            )}
          </div>

          <div className="flex gap-3 text-xs text-gray-400 mb-6 pb-4 border-b border-[#E8E7D1]">
            <span>조회 {post.views}</span>
            <span>{post.createdAt.slice(0, 10)}</span>
          </div>

          <p className="text-sm text-gray-700 leading-relaxed whitespace-pre-wrap">
            {post.content}
          </p>
        </Card>
      )}

      {/* 삭제 확인 모달 */}
      {showDeleteModal && (
        <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50">
          <div className="bg-white rounded-xl p-6 max-w-sm w-full mx-4 shadow-xl">
            <h3 className="text-lg font-semibold text-gray-800 mb-2">게시글 삭제</h3>
            <p className="text-sm text-gray-600 mb-6">
              이 게시글을 삭제하시겠습니까?<br />삭제 후 복구할 수 없습니다.
            </p>
            <div className="flex gap-3 justify-end">
              <button
                className="btn-secondary"
                onClick={() => setShowDeleteModal(false)}
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
