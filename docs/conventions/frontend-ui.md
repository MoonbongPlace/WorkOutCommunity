목적

Migration portfolio 프론트엔드 UI는 기능 전달과 구조 가독성을 최우선으로 한다.
디자인은 미니멀/클린을 기본으로 하며, 화이트 + 카키를 메인 팔레트로 사용하고 제한된 포인트 컬러로만 “킥”을 준다.

1. Styling 정책
1.1 기본 정책

Styling은 Tailwind CSS만 사용한다.

컴포넌트 라이브러리(MUI, AntD, Chakra 등) 사용 금지.

레이아웃은 card-based + 충분한 여백을 기본으로 한다.

전체 UI는 심플, 명확, 일관성이 우선이다. 과한 장식/그라데이션/애니메이션 금지.

1.2 타이포그래피

기본 폰트는 Tailwind 기본(font-sans) 사용.

헤더 계층은 명확히:

Page title: text-2xl font-semibold

Section title: text-lg font-semibold

Body: text-sm text-neutral-700

줄간격: 기본 leading-relaxed 권장.

2. 컬러 팔레트 및 사용 규칙

프로젝트 기본 톤: White + Khaki
포인트는 “한 화면에서 1~2개”만 사용한다.

2.1 Base Colors

Background (기본 배경): bg-white

App background (앱 전체): bg-neutral-50

Text primary: text-neutral-900

Text secondary: text-neutral-600

Border: border-neutral-200

2.2 Khaki Theme (메인 포인트)

카키는 “브랜드 톤”으로 사용한다.

Primary button / 강조 링크 / active tab 등에만 사용

과사용 금지

권장 톤(대표값, Tailwind에 직접 색상 토큰을 추가하기 전까지는 임시로 bg-[#...] 사용 가능):

Khaki 1 (main): #7A7F3A

Khaki 2 (soft): #A6A66A

Khaki 3 (pale): #E8E7D1

사용 규칙:

Primary CTA: bg-[#7A7F3A] text-white hover:opacity-90

Subtle badge: bg-[#E8E7D1] text-neutral-800

2.3 Accent Colors (보조 포인트)

화이트/카키와 조화되는 “보조 포인트 컬러”는 아래 3종만 사용한다.

Navy(신뢰/정보 강조)

#1F2A44

정보성 강조(링크 hover, info badge, section icon)에서 사용

Terracotta(경고/강조)

#C56B4A

경고/주의/삭제 버튼(secondary destructive)에서 제한적으로 사용

Teal(성공/완료)

#2F7D7A

성공 상태, 완료, 체크 상태 표시

2.4 Status Colors 규칙

Success: Teal 계열

Warning: Terracotta 계열

Error: Tailwind text-red-600 / bg-red-50 (기능적 표준 유지)

Info: Navy 계열

3. 레이아웃 규칙
3.1 페이지 레이아웃

전체 컨테이너: max-w-5xl mx-auto px-4 py-6

페이지 구분은 card로:

rounded-2xl bg-white border border-neutral-200 shadow-sm

3.2 컴포넌트 여백

card padding: p-5 (모바일은 p-4)

섹션 간격: space-y-4 또는 gap-4

입력 폼 간격: space-y-3

4. 컴포넌트 스타일 가이드
4.1 Button

Primary (카키):

bg-[#7A7F3A] text-white rounded-xl px-4 py-2 text-sm font-medium hover:opacity-90

Secondary:

bg-white border border-neutral-200 text-neutral-800 rounded-xl px-4 py-2 text-sm hover:bg-neutral-50

Destructive(삭제):

기본은 text-red-600 border-red-200 기반

과한 빨강 버튼은 지양, “텍스트/테두리형” 우선

4.2 Input

w-full rounded-xl border border-neutral-200 bg-white px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-[#A6A66A]

에러:

border-red-300 bg-red-50

4.3 Card

기본:

rounded-2xl border border-neutral-200 bg-white shadow-sm

Header는 항상 title + optional action(버튼) 구조로 정렬

4.4 Badge/Chip

카키 subtle:

bg-[#E8E7D1] text-neutral-800

상태 배지:

Success(Teal), Warning(Terracotta), Info(Navy)

5. UX 원칙
5.1 상태 처리

Loading: 스켈레톤 또는 단순 text-neutral-500 로딩 텍스트

Empty: “데이터가 없습니다” + 다음 행동 버튼(예: 생성)

Error: 사용자 친화 메시지 + 재시도 버튼

5.2 네비게이션

상단 Navbar 또는 좌측 Sidebar 중 하나로 통일

active 상태는 카키로 강조:

underline 또는 left border

5.3 폼 UX

Validation은 즉시 표시하되 과도한 경고는 금지

제출 버튼은 폼 하단 우측 정렬, primary 1개만 강조

6. Claude Code 작업 규칙 (UI 생성 시)

Claude Code로 프론트 UI를 생성/수정할 때:

반드시 docs/conventions/frontend-ui.md를 먼저 읽고 체크리스트를 출력한다.

Tailwind 외의 UI 라이브러리는 추가하지 않는다.

컬러는 본 문서 팔레트/규칙을 따른다. 한 화면에서 포인트 컬러는 1~2개로 제한한다.

UI는 기능/구조 중심으로, 과한 장식/애니메이션을 피한다.

7. 기본 체크리스트

 Tailwind만 사용했는가?

 White/Khaki 기반이며 포인트 컬러 과사용이 없는가?

 버튼/입력/카드 스타일이 일관적인가?

 로딩/빈상태/에러 상태 UI가 존재하는가?

 모바일에서도 깨지지 않는가? (기본 responsive 적용)