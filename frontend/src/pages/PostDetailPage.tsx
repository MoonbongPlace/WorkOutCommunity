import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import Card from '../components/ui/Card'
import StateBlock from '../components/ui/StateBlock'
import { postApi } from '../api/endpoints/post'
import { commentApi } from '../api/endpoints/comment'
import { useAuth } from '../context/AuthContext'
import type { PostDetailResult } from '../types/post'
import type { CommentItem } from '../types/comment'

function ImageGallery({ images }: { images: string[] }) {
  const [lightboxIdx, setLightboxIdx] = useState<number | null>(null)

  if (images.length === 0) return null

  function prev() {
    setLightboxIdx((i) => (i !== null ? (i - 1 + images.length) % images.length : null))
  }
  function next() {
    setLightboxIdx((i) => (i !== null ? (i + 1) % images.length : null))
  }

  return (
    <>
      <div className="mt-6 pt-5 border-t border-[#E8E7D1]">
        <p className="text-xs font-medium text-gray-500 mb-3">첨부 이미지 ({images.length})</p>
        <div className="flex flex-wrap gap-2">
          {images.map((url, i) => (
            <button
              key={i}
              type="button"
              onClick={() => setLightboxIdx(i)}
              className="w-32 h-32 rounded-lg overflow-hidden border border-gray-200 hover:opacity-90 transition-opacity focus:outline-none"
            >
              <img src={url} alt={`이미지 ${i + 1}`} className="w-full h-full object-cover" />
            </button>
          ))}
        </div>
      </div>

      {/* 라이트박스 */}
      {lightboxIdx !== null && (
        <div
          className="fixed inset-0 bg-black/80 flex items-center justify-center z-50"
          onClick={() => setLightboxIdx(null)}
        >
          {images.length > 1 && (
            <button
              className="absolute left-4 text-white text-3xl px-3 py-1 hover:bg-white/10 rounded-full transition-colors"
              onClick={(e) => { e.stopPropagation(); prev() }}
              aria-label="이전 이미지"
            >
              ‹
            </button>
          )}

          <img
            src={images[lightboxIdx]}
            alt={`이미지 ${lightboxIdx + 1}`}
            className="max-w-[90vw] max-h-[85vh] object-contain rounded-lg shadow-xl"
            onClick={(e) => e.stopPropagation()}
          />

          {images.length > 1 && (
            <button
              className="absolute right-4 text-white text-3xl px-3 py-1 hover:bg-white/10 rounded-full transition-colors"
              onClick={(e) => { e.stopPropagation(); next() }}
              aria-label="다음 이미지"
            >
              ›
            </button>
          )}

          <span className="absolute bottom-5 text-white/70 text-sm">
            {lightboxIdx + 1} / {images.length}
          </span>

          <button
            className="absolute top-4 right-4 text-white/70 hover:text-white text-2xl leading-none"
            onClick={() => setLightboxIdx(null)}
            aria-label="닫기"
          >
            ×
          </button>
        </div>
      )}
    </>
  )
}

function CommentSection({ postId }: { postId: number }) {
  const { user } = useAuth()
  const [comments, setComments] = useState<CommentItem[]>([])
  const [status, setStatus] = useState<'loading' | 'success' | 'error'>('loading')
  const [input, setInput] = useState('')
  const [submitting, setSubmitting] = useState(false)
  const [deletingId, setDeletingId] = useState<number | null>(null)

  async function loadComments() {
    setStatus('loading')
    try {
      const { data } = await commentApi.list(postId)
      setComments(data.getCommentsResult.comments)
      setStatus('success')
    } catch {
      setStatus('error')
    }
  }

  useEffect(() => { void loadComments() }, [postId])

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault()
    if (!input.trim() || submitting) return
    setSubmitting(true)
    try {
      await commentApi.create(postId, { content: input.trim() })
      setInput('')
      await loadComments()
    } finally {
      setSubmitting(false)
    }
  }

  async function handleDelete(commentId: number) {
    setDeletingId(commentId)
    try {
      await commentApi.remove(postId, commentId)
      setComments((prev) => prev.filter((c) => c.id !== commentId))
    } finally {
      setDeletingId(null)
    }
  }

  return (
    <div className="mt-4">
      <Card>
        <h2 className="text-base font-semibold text-gray-800 mb-4">
          댓글 {status === 'success' ? `(${comments.length})` : ''}
        </h2>

        {/* 댓글 목록 */}
        {status === 'loading' && (
          <p className="text-sm text-gray-400 py-4 text-center">불러오는 중...</p>
        )}
        {status === 'error' && (
          <div className="text-sm text-red-500 py-4 text-center">
            댓글을 불러오지 못했습니다.{' '}
            <button className="underline" onClick={() => void loadComments()}>재시도</button>
          </div>
        )}
        {status === 'success' && (
          <>
            {comments.length === 0 ? (
              <p className="text-sm text-gray-400 py-4 text-center">아직 댓글이 없습니다.</p>
            ) : (
              <ul className="space-y-3 mb-4">
                {comments.map((c) => (
                  <li key={c.id} className="flex items-start justify-between gap-3 py-3 border-b border-[#E8E7D1] last:border-0">
                    <div className="flex-1 min-w-0">
                      <div className="flex items-center gap-2 mb-1">
                        <span className="text-xs font-medium text-gray-700">{c.memberName}</span>
                        <span className="text-xs text-gray-400">{c.createdAt.slice(0, 10)}</span>
                      </div>
                      <p className="text-sm text-gray-700 leading-relaxed whitespace-pre-wrap">{c.content}</p>
                    </div>
                    {user && user.id === c.memberId && (
                      <button
                        className="flex-shrink-0 text-xs text-gray-400 hover:text-red-500 transition-colors disabled:opacity-50"
                        onClick={() => void handleDelete(c.id)}
                        disabled={deletingId === c.id}
                        aria-label="댓글 삭제"
                      >
                        삭제
                      </button>
                    )}
                  </li>
                ))}
              </ul>
            )}
          </>
        )}

        {/* 댓글 입력 */}
        {user ? (
          <form onSubmit={(e) => void handleSubmit(e)} className="flex gap-2 pt-2">
            <input
              className="flex-1 rounded-xl border border-neutral-200 bg-white px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-[#A6A66A]"
              placeholder="댓글을 입력하세요 (최대 255자)"
              maxLength={255}
              value={input}
              onChange={(e) => setInput(e.target.value)}
              disabled={submitting}
            />
            <button
              type="submit"
              className="bg-[#7A7F3A] text-white rounded-xl px-4 py-2 text-sm font-medium hover:opacity-90 disabled:opacity-50 transition-opacity"
              disabled={!input.trim() || submitting}
            >
              {submitting ? '등록 중' : '등록'}
            </button>
          </form>
        ) : (
          <p className="text-xs text-gray-400 pt-2">댓글을 작성하려면 로그인이 필요합니다.</p>
        )}
      </Card>
    </div>
  )
}

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
        <>
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
              <span>{post.memberName}</span>
              <span>조회 {post.views}</span>
              <span>{post.createdAt.slice(0, 10)}</span>
            </div>

            <p className="text-sm text-gray-700 leading-relaxed whitespace-pre-wrap">
              {post.content}
            </p>

            <ImageGallery images={post.images ?? []} />
          </Card>

          <CommentSection postId={Number(postId)} />
        </>
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
