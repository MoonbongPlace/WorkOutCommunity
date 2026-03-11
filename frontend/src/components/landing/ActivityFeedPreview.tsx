import { useInView } from '../../hooks/useInView'

// PostListItem 형식에 맞는 샘플 데이터
// (profileImage: null → 이니셜 아바타, createdAt: ISO string, liked 상태)
const POSTS = [
  {
    id: 1,
    memberName: '김민준',
    profileInitial: '김',
    profileImage: null,
    title: '데드리프트 PR 달성! 🎉',
    content: '드디어 140kg 성공했습니다. 6개월 동안 꾸준히 훈련한 결과네요. 다들 포기하지 말고 화이팅!',
    category: '운동 인증',
    createdAt: '2026-03-11',
    likeCount: 47,
    commentCount: 12,
    liked: true,
  },
  {
    id: 2,
    memberName: '이지은',
    profileInitial: '이',
    profileImage: null,
    title: '오늘의 풀바디 루틴 공유 💪',
    content: '스쿼트 4×10, 벤치프레스 4×8, 풀업 3×Max. 총 1시간 15분 완료했어요. 루틴 참고하실 분들께 공유해요!',
    category: '루틴 공유',
    createdAt: '2026-03-11',
    likeCount: 31,
    commentCount: 8,
    liked: false,
  },
  {
    id: 3,
    memberName: '박준호',
    profileInitial: '박',
    profileImage: null,
    title: '러닝 5km 완주 후기 🏃',
    content: "오늘 5km 완주했어요. 평균 페이스 5'20\". 꾸준히 하다 보니 숨이 훨씬 덜 차네요. 다음 달 10km 도전!",
    category: '유산소',
    createdAt: '2026-03-11',
    likeCount: 19,
    commentCount: 5,
    liked: false,
  },
  {
    id: 4,
    memberName: '최민서',
    profileInitial: '최',
    profileImage: null,
    title: '숄더프레스 증량 성공 💪',
    content: '3개월 만에 12.5kg → 17.5kg으로 증량 성공! 어깨가 점점 넓어지는 게 느껴집니다. 고립 운동 병행이 도움이 됐어요.',
    category: '운동 인증',
    createdAt: '2026-03-10',
    likeCount: 38,
    commentCount: 14,
    liked: false,
  },
]

export default function ActivityFeedPreview() {
  const { ref, inView } = useInView()

  return (
    <section id="demo" className="py-20">
      <div ref={ref} className={`reveal ${inView ? 'visible' : ''} mb-12`}>
        <p className="text-xs font-bold text-[#7A7F3A] uppercase tracking-widest mb-3">커뮤니티 피드</p>
        <h2 className="text-2xl md:text-3xl font-bold text-neutral-900 mb-3">
          함께 성장하는 운동 커뮤니티
        </h2>
        <p className="text-neutral-500 max-w-xl">
          회원들의 실시간 운동 기록과 인증을 확인하고, 댓글과 좋아요로 서로를 응원하세요.
        </p>
      </div>

      <div className="grid md:grid-cols-2 gap-3">
        {POSTS.map((post, i) => (
          <div
            key={post.id}
            className={`reveal reveal-d${(i % 4) + 1} ${inView ? 'visible' : ''}`}
          >
            <FeedCard post={post} />
          </div>
        ))}
      </div>
    </section>
  )
}

function FeedCard({ post }: { post: (typeof POSTS)[0] }) {
  return (
    /* Card 컴포넌트와 동일한 스타일: bg-white border border-[#E8E7D1] rounded-lg shadow-sm p-5 */
    <div className="bg-white border border-[#E8E7D1] rounded-lg shadow-sm p-5 hover:border-[#A6A66A] transition-colors cursor-pointer">
      <div className="flex gap-3">
        <div className="flex-1 min-w-0">
          {/* 제목 - PostsPage 동일: text-[1.3rem] font-semibold text-gray-800 */}
          <h2 className="text-[1.3rem] font-semibold text-gray-800 mb-1">{post.title}</h2>

          {/* 작성자 행 - PostsPage 동일 구조 */}
          <div className="flex items-center gap-2 text-xs text-gray-500">
            {/* Avatar sm 대체: profileImage가 null이므로 이니셜 */}
            <div className="w-7 h-7 rounded-full bg-[#E8E7D1] flex items-center justify-center text-xs font-semibold text-[#7A7F3A] flex-shrink-0 border border-gray-200">
              {post.profileInitial}
            </div>
            <span>{post.memberName}</span>
            <span>·</span>
            <span>{post.createdAt}</span>
            {post.category && (
              /* PostsPage 카테고리 뱃지와 동일 */
              <span className="bg-[#F0F0E0] text-[#7A7F3A] px-2 py-0.5 rounded-full text-[11px] font-medium">
                {post.category}
              </span>
            )}
          </div>

          {/* 본문 - PostsPage 동일: text-lg text-gray-600 mt-2 line-clamp-2 */}
          {post.content && (
            <p className="text-lg text-gray-600 mt-2 line-clamp-2">{post.content}</p>
          )}
        </div>
      </div>

      {/* 좋아요 / 댓글 - PostsPage 동일 구조 */}
      <div className="mt-3 pt-3 border-t border-[#E8E7D1] flex items-center gap-1">
        <button
          className={`flex items-center gap-1 text-sm px-2 py-1 rounded-lg transition-colors ${
            post.liked
              ? 'text-red-500 hover:bg-red-50'
              : 'text-gray-400 hover:text-red-400 hover:bg-red-50'
          }`}
        >
          <span className="text-base leading-none">{post.liked ? '♥' : '♡'}</span>
          <span>{post.likeCount}</span>
        </button>
        <span className="text-sm px-2 py-1 text-gray-400">댓글 {post.commentCount}</span>
      </div>
    </div>
  )
}
