import { useEffect, useRef, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import Card from '../ui/Card'
import StateBlock from '../ui/StateBlock'
import Avatar from '../ui/Avatar'
import { postApi } from '../../api/endpoints/post'
import { postLikeApi } from '../../api/endpoints/postLike'
import { commentApi } from '../../api/endpoints/comment'
import { useAuth } from '../../context/AuthContext'
import type { PostDetailResult } from '../../types/post'
import type { CommentItem } from '../../types/comment'

/* ── 라이트박스 ── */
function Lightbox({ images, idx, onClose }: { images: string[]; idx: number | null; onClose: () => void }) {
  const [current, setCurrent] = useState(idx)
  useEffect(() => { setCurrent(idx) }, [idx])
  if (idx === null || current === null) return null

  function prev() { setCurrent(i => ((i ?? 0) - 1 + images.length) % images.length) }
  function next() { setCurrent(i => ((i ?? 0) + 1) % images.length) }

  return (
    <div className="fixed inset-0 bg-black/80 flex items-center justify-center z-[60]" onClick={onClose}>
      {images.length > 1 && (
        <button
          className="absolute left-4 text-white text-3xl px-3 py-1 hover:bg-white/10 rounded-full transition-colors"
          onClick={(e) => { e.stopPropagation(); prev() }}
        >‹</button>
      )}
      <img
        src={images[current]}
        alt={`이미지 ${current + 1}`}
        className="max-w-[90vw] max-h-[85vh] object-contain rounded-lg shadow-xl"
        onClick={(e) => e.stopPropagation()}
      />
      {images.length > 1 && (
        <button
          className="absolute right-4 text-white text-3xl px-3 py-1 hover:bg-white/10 rounded-full transition-colors"
          onClick={(e) => { e.stopPropagation(); next() }}
        >›</button>
      )}
      <span className="absolute bottom-5 text-white/70 text-sm">{current + 1} / {images.length}</span>
      <button
        className="absolute top-4 right-4 text-white/70 hover:text-white text-2xl leading-none"
        onClick={onClose}
      >×</button>
    </div>
  )
}

/* ── 인라인 컨텐츠 렌더러 ── */
function InlineContent({ content, images }: { content: string; images: string[] }) {
  const [lightboxIdx, setLightboxIdx] = useState<number | null>(null)
  const hasMarkers = /\[\[image:\d+\]\]/.test(content)

  if (!hasMarkers) {
    return (
      <>
        <p className="text-sm text-gray-700 leading-relaxed whitespace-pre-wrap">{content}</p>
        {images.length > 0 && (
          <div className="mt-4 space-y-3">
            {images.map((url, i) => (
              <img key={i} src={url} alt={`이미지 ${i + 1}`}
                className="w-full rounded-lg cursor-zoom-in"
                onClick={() => setLightboxIdx(i)} />
            ))}
          </div>
        )}
        <Lightbox images={images} idx={lightboxIdx} onClose={() => setLightboxIdx(null)} />
      </>
    )
  }

  const parts = content.split(/(\[\[image:\d+\]\])/)
  return (
    <>
      <div className="space-y-4">
        {parts.map((part, i) => {
          const match = part.match(/^\[\[image:(\d+)\]\]$/)
          if (match) {
            const idx = parseInt(match[1])
            if (idx >= images.length) return null
            return (
              <img key={i} src={images[idx]} alt={`이미지 ${idx + 1}`}
                className="w-full rounded-lg cursor-zoom-in"
                onClick={() => setLightboxIdx(idx)} />
            )
          }
          const text = part.replace(/^\n/, '').replace(/\n$/, '')
          if (!text) return null
          return <p key={i} className="text-sm text-gray-700 leading-relaxed whitespace-pre-wrap">{text}</p>
        })}
      </div>
      <Lightbox images={images} idx={lightboxIdx} onClose={() => setLightboxIdx(null)} />
    </>
  )
}

/* ── 댓글 섹션 ── */
function CommentSection({ postId }: { postId: number }) {
  const { user } = useAuth()
  const [comments,   setComments]   = useState<CommentItem[]>([])
  const [status,     setStatus]     = useState<'loading' | 'success' | 'error'>('loading')
  const [input,      setInput]      = useState('')
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
      setComments(prev => prev.filter(c => c.id !== commentId))
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

        {status === 'loading' && <p className="text-sm text-gray-400 py-4 text-center">불러오는 중...</p>}
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
                {comments.map(c => (
                  <li key={c.id} className="flex items-start gap-3 py-3 border-b border-[#E8E7D1] last:border-0">
                    <Avatar src={c.profileImage} name={c.memberName} size="sm" />
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

        {user ? (
          <form onSubmit={e => void handleSubmit(e)} className="flex gap-2 pt-2">
            <input
              className="flex-1 rounded-xl border border-neutral-200 bg-white px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-[#A6A66A]"
              placeholder="댓글을 입력하세요 (최대 255자)"
              maxLength={255}
              value={input}
              onChange={e => setInput(e.target.value)}
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

/* ── 게시글 상세 모달 ── */
interface PostDetailModalProps {
  postId: number
  onClose: () => void
  onDeleted?: () => void
}

export default function PostDetailModal({ postId, onClose, onDeleted }: PostDetailModalProps) {
  const navigate = useNavigate()
  const { user } = useAuth()
  const [status,          setStatus]          = useState<'loading' | 'success' | 'error'>('loading')
  const [post,            setPost]            = useState<PostDetailResult | null>(null)
  const [menuOpen,        setMenuOpen]        = useState(false)
  const [showDeleteModal, setShowDeleteModal] = useState(false)
  const [deleting,        setDeleting]        = useState(false)
  const [liked,           setLiked]           = useState(false)
  const [likeCount,       setLikeCount]       = useState(0)
  const [liking,          setLiking]          = useState(false)
  const scrollRef = useRef<HTMLDivElement>(null)

  async function load() {
    setStatus('loading')
    try {
      const { data } = await postApi.detail(postId)
      setPost(data.post)
      setLikeCount(data.post.likeCount)
      setStatus('success')
    } catch {
      setStatus('error')
    }
  }

  async function handleLike() {
    if (!user || liking) return
    setLiking(true)
    setLiked(prev => !prev)
    setLikeCount(prev => prev + (liked ? -1 : 1))
    try {
      if (liked) {
        await postLikeApi.remove(postId)
      } else {
        await postLikeApi.create(postId)
      }
    } catch {
      setLiked(prev => !prev)
      setLikeCount(prev => prev + (liked ? 1 : -1))
    } finally {
      setLiking(false)
    }
  }

  useEffect(() => { void load() }, [postId])

  useEffect(() => {
    if (!user) return
    postLikeApi.check(postId)
      .then(({ data }) => setLiked(data.checkPostLike))
      .catch(() => {})
  }, [user, postId])

  useEffect(() => {
    if (!menuOpen) return
    function handleClick() { setMenuOpen(false) }
    document.addEventListener('click', handleClick)
    return () => document.removeEventListener('click', handleClick)
  }, [menuOpen])

  // ESC 키로 닫기
  useEffect(() => {
    function handleKeyDown(e: KeyboardEvent) {
      if (e.key === 'Escape') onClose()
    }
    document.addEventListener('keydown', handleKeyDown)
    return () => document.removeEventListener('keydown', handleKeyDown)
  }, [onClose])

  async function handleDelete() {
    setDeleting(true)
    try {
      await postApi.remove(postId)
      onClose()
      onDeleted?.()
    } catch {
      setDeleting(false)
      setShowDeleteModal(false)
    }
  }

  const isAuthor = post && user && post.member_id === user.id

  return (
    <div
      className="fixed inset-0 bg-black/50 flex items-start justify-center z-50 p-4 pt-16 overflow-y-auto"
      onClick={onClose}
    >
      <div
        ref={scrollRef}
        className="bg-[#F7F7EE] rounded-2xl w-full max-w-2xl shadow-2xl mb-8"
        onClick={(e) => e.stopPropagation()}
      >
        {/* 모달 헤더 */}
        <div className="flex items-center justify-between px-6 py-4 border-b border-[#E8E7D1]">
          <span className="text-sm text-gray-500 font-medium">게시글 상세</span>
          <div className="flex items-center gap-2">
            <button
              className="text-sm text-[#7A7F3A] hover:underline"
              onClick={() => { onClose(); navigate(`/posts/${postId}`) }}
            >
              새 페이지로 보기
            </button>
            <button
              className="w-8 h-8 flex items-center justify-center rounded-lg text-gray-400 hover:text-gray-600 hover:bg-gray-100 transition-colors text-xl leading-none"
              onClick={onClose}
              aria-label="닫기"
            >
              ×
            </button>
          </div>
        </div>

        {/* 모달 본문 */}
        <div className="p-6">
          {status === 'loading' && <StateBlock type="loading" />}
          {status === 'error' && <StateBlock type="error" onRetry={() => void load()} />}
          {status === 'success' && post && (
            <>
              <Card>
                <div className="flex items-start justify-between mb-3">
                  <h1 className="text-xl font-bold text-gray-800">{post.title}</h1>
                  {isAuthor && (
                    <div className="relative ml-2 flex-shrink-0">
                      <button
                        className="p-1.5 rounded hover:bg-gray-100 transition-colors text-gray-400 hover:text-gray-600 text-lg leading-none"
                        onClick={(e) => { e.stopPropagation(); setMenuOpen(prev => !prev) }}
                        aria-label="게시글 관리"
                      >
                        ⋮
                      </button>
                      {menuOpen && (
                        <div className="absolute right-0 top-9 w-28 bg-white border border-[#E8E7D1] rounded-lg shadow-md z-10 py-1">
                          <button
                            className="w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-[#E8E7D1] transition-colors"
                            onClick={() => { setMenuOpen(false); onClose(); navigate(`/posts/${postId}/edit`) }}
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

                <div className="flex items-center gap-2 mb-6 pb-4 border-b border-[#E8E7D1]">
                  <Avatar src={post.profileImage} name={post.memberName} size="md" />
                  <div>
                    <p className="text-sm font-medium text-gray-700">{post.memberName}</p>
                    <div className="flex gap-2 text-xs text-gray-400">
                      <span>{post.createdAt.slice(0, 10)}</span>
                      <span>·</span>
                      <span>조회 {post.views}</span>
                    </div>
                  </div>
                </div>

                <InlineContent content={post.content} images={post.images ?? []} />

                {/* 좋아요 버튼 */}
                <div className="mt-6 pt-4 border-t border-[#E8E7D1] flex flex-col items-center">
                  <button
                    className={`flex items-center gap-2 px-5 py-2 rounded-full border text-sm font-medium transition-colors ${
                      liked
                        ? 'bg-red-50 border-red-300 text-red-500 hover:bg-red-100'
                        : user
                          ? 'bg-white border-gray-200 text-gray-500 hover:border-red-300 hover:text-red-400'
                          : 'bg-white border-gray-200 text-gray-400 cursor-default'
                    }`}
                    onClick={() => void handleLike()}
                    disabled={!user || liking}
                    aria-label={liked ? '좋아요 취소' : '좋아요'}
                  >
                    <span className="text-lg leading-none">{liked ? '♥' : '♡'}</span>
                    <span>{likeCount}</span>
                  </button>
                  {!user && (
                    <p className="text-xs text-gray-400 mt-2">좋아요를 누르려면 로그인이 필요합니다.</p>
                  )}
                </div>
              </Card>

              <CommentSection postId={postId} />
            </>
          )}
        </div>
      </div>

      {/* 삭제 확인 모달 */}
      {showDeleteModal && (
        <div
          className="fixed inset-0 bg-black/40 flex items-center justify-center z-[60]"
          onClick={(e) => e.stopPropagation()}
        >
          <div className="bg-white rounded-xl p-6 max-w-sm w-full mx-4 shadow-xl">
            <h3 className="text-lg font-semibold text-gray-800 mb-2">게시글 삭제</h3>
            <p className="text-sm text-gray-600 mb-6">
              이 게시글을 삭제하시겠습니까?<br />삭제 후 복구할 수 없습니다.
            </p>
            <div className="flex gap-3 justify-end">
              <button className="btn-secondary" onClick={() => setShowDeleteModal(false)} disabled={deleting}>
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
