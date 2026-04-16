-- =============================================================
-- 부하 테스트 시드 데이터: POST LIST 조회 API
-- 대상: GET /api/v1/posts?page=0&size=20
-- =============================================================
-- 규모 설계
--   member   : 500명  (400 VU 커버 + 여유분, 게시글 작성자 다양성)
--   posts    : 10,000건 (16개 카테고리 분산, 충분한 페이지 뎁스)
--   comments : ~30,000건 (게시글당 평균 3개 → N+1 댓글 카운트 부하 현실화)
-- =============================================================
-- 사전 조건
--   1. pgcrypto 확장 활성화 (비밀번호 해싱에 사용)
--      CREATE EXTENSION IF NOT EXISTS pgcrypto;
--   2. categories 시드가 먼저 적용되어 있어야 함 (exercise_seed.sql)
--   3. 트랜잭션 단위로 실행 권장
-- =============================================================
-- rollback;
BEGIN;

-- -------------------------------------------------------------
-- 0. pgcrypto 확장 (없으면 활성화)
-- -------------------------------------------------------------
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- -------------------------------------------------------------
-- 1. 테스트 멤버 500명 생성
--    - email    : seed_user_1@test.com ~ seed_user_500@test.com
--    - password : "Test1234!" (BCrypt cost=10 해시)
--    - role     : user
--    - status   : ACTIVE
-- -------------------------------------------------------------
INSERT INTO member (
    email,
    phone_number,
    member_name,
    password,
    name,
    age,
    sex,
    role,
    created_at,
    status
)
SELECT
    'seed_user_' || i || '@test.com',
    '010' || (10000000 + i)::text,
    'seed_user_' || i,
    crypt('Test1234!', gen_salt('bf', 10)),
    '테스트유저' || i,
    (20 + (i % 30))::int,
    CASE WHEN i % 2 = 0 THEN 'M' ELSE 'F' END,
    'user',
    NOW() - (random() * INTERVAL '365 days'),
    'ACTIVE'
FROM generate_series(1, 500) AS s(i)
ON CONFLICT DO NOTHING;

-- -------------------------------------------------------------
-- 2. 게시글 10,000건 생성
--    - category_id : 1~16 순환 (카테고리 균등 분산)
--    - member_id   : 위에서 삽입한 500명 중 랜덤
--    - visibility  : 95% VISIBLE / 5% HIDDEN (관리자 숨김 시나리오)
--    - created_at  : 최근 1년 내 랜덤
--    - views       : 0~1000 랜덤
--    - like_count  : 0~100 랜덤
-- -------------------------------------------------------------
INSERT INTO posts (
    title,
    content,
    category_id,
    views,
    member_id,
    image,
    created_at,
    updated_at,
    deleted_at,
    visibility,
    like_count
)
SELECT
    '부하테스트 게시글 #' || i,
    '이것은 부하 테스트용 게시글 내용입니다. 게시글 번호: ' || i
        || '. Lorem ipsum dolor sit amet, consectetur adipiscing elit. '
        || '헬스 커뮤니티 게시판에 오신 것을 환영합니다. '
        || repeat('테스트 내용 ', (i % 5) + 1),
    -- categories id 1~16 순환 (exercise_seed.sql 기준)
    (((i - 1) % 16) + 1)::bigint,
    (random() * 1000)::int,                  -- 조회수 0~1000
    -- 500명 작성자 중 랜덤 (member 테이블에서 시드 유저 id 참조)
    (SELECT id FROM member
     WHERE email = 'seed_user_' || ((i % 500) + 1) || '@test.com'
     LIMIT 1),
    '[]',                                    -- 이미지 없음
    NOW() - (random() * INTERVAL '365 days'),
    NULL,
    NULL,
    -- 95% VISIBLE, 5% HIDDEN
    CASE WHEN (i % 20) = 0 THEN 'HIDDEN' ELSE 'VISIBLE' END,
    (random() * 100)::int                    -- 좋아요 수 0~100
FROM generate_series(1, 10000) AS s(i);

-- -------------------------------------------------------------
-- 3. 댓글 ~30,000건 생성
--    - 게시글당 평균 3개 (0~9개 분산)
--    - N+1 countActiveByPostId 쿼리 부하를 현실화하기 위한 핵심 데이터
-- -------------------------------------------------------------
INSERT INTO comments (
    member_id,
    post_id,
    content,
    created_at,
    updated_at,
    deleted_at
)
SELECT
    -- 댓글 작성자: 500명 중 랜덤
    (SELECT id FROM member
     WHERE email = 'seed_user_' || ((s.comment_idx % 500) + 1) || '@test.com'
     LIMIT 1),
    p.id,
    '부하테스트 댓글 #' || s.comment_idx || ' on post ' || p.id,
    p.created_at + (random() * INTERVAL '30 days'),
    NULL,
    NULL
FROM posts p
-- 게시글당 댓글 수: post id 기반으로 0~9개 분산 (평균 ~4.5개)
CROSS JOIN generate_series(1, (p.id % 10)) AS s(comment_idx)
WHERE p.deleted_at IS NULL
  AND p.visibility = 'VISIBLE';

-- -------------------------------------------------------------
-- 4. 통계 확인
-- -------------------------------------------------------------
DO $$
DECLARE
    v_member_count  bigint;
    v_post_count    bigint;
    v_comment_count bigint;
BEGIN
    SELECT COUNT(*) INTO v_member_count  FROM member  WHERE email LIKE 'seed_user_%@test.com';
    SELECT COUNT(*) INTO v_post_count    FROM posts   WHERE title LIKE '부하테스트 게시글 #%';
    SELECT COUNT(*) INTO v_comment_count FROM comments WHERE content LIKE '부하테스트 댓글 #%';

    RAISE NOTICE '=== 시드 데이터 삽입 완료 ===';
    RAISE NOTICE 'member  : % 건', v_member_count;
    RAISE NOTICE 'posts   : % 건', v_post_count;
    RAISE NOTICE 'comments: % 건', v_comment_count;
END $$;

COMMIT;
