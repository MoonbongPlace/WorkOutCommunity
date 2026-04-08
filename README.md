# WorkOutCommunity

## 1. 개요 (Overview)

운동매니아들을 위한 커뮤니티. 운동 일지 기록 및 ChatBot을 통해 운동에 관한 정보 수집.
---

## 2. 목적 (Goals)

- RESTful 원칙에 맞는 API 엔드포인트 설계
- API First 전략 기반의 명세서 중심 개발
- UI(SSR) 라우트와 데이터 API의 명확한 분리
- 유지보수 및 확장이 가능한 백엔드 아키텍처 정립

---

## 3. 범위 (Scope)

이 리포지토리에서 다루는 주요 범위는 다음과 같습니다.

- 사용자(User) 관련 API
- 게시글(Post) / 댓글(Comment) API
- 알림(Notification) API
- 인증(Auth) 및 세션 관리
- 챗봇(Chatbot) API - OpenAI API
- 홈(Home) 화면을 위한 집계 API
- API 명세(OpenAPI) 및 설계 문서
- Claude Code CLI를 이용한 프론트 UI 구축.

---

## 4. API 설계 원칙

- **리소스 중심 URL 설계**
  - 동사가 아닌 명사 사용
  - 예: `/api/v1/posts/{postId}/comments`
- **HTTP Method의 의미 준수**
  - GET / POST / PATCH / DELETE 구분
- **URL 기반 버저닝**
  - Base Path: `/api/v1`
- **일관된 응답 포맷**
- **명세서(OpenAPI)를 기준으로 구현**

---

## 5. API 버전 정책

- 모든 API는 `/api/v1`을 기본 경로로 사용합니다.
- v1은 최초 공식 API 계약(contract)으로 유지됩니다.
- 구조적 변경이 필요한 경우 `/api/v2`로 신규 버전을 추가합니다.
- 단순 필드 추가 등 하위 호환 변경은 v1 내에서 처리합니다.

---

## 6. 디렉토리 구조
- Feature(도메인) 중심 구조로 디렉토리 구조 생성
```text
workout-community/
├── README.md
├── frontend/
│   └── src/
│       ├── api/
│       ├── components/
│       ├── contexts/
│       ├── layouts/
│       ├── pages/
│       ├── sytles/
│       ├── types/
├── docs/
│   └── api/
│       ├── README.md        # API 설계 및 규칙 문서
│       └── openapi.yaml     # OpenAPI(Swagger) 명세
├── src/
│   └── main/
│       ├── member/
│       ├── board/
│       ├── admin/
│           └── api/
│           └── application/
│           └── domain/
│           └── infra/
└── .gitignore
