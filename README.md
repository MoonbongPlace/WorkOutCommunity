# Community API Migration

## 1. 개요 (Overview)

이 리포지토리는 기존 Express + EJS 기반 커뮤니티 프로젝트를  
**RESTful API 중심 구조로 재설계하고, 향후 Spring MVC 기반으로 마이그레이션하기 위한 전용 저장소**입니다.

기존 프로젝트는 서버 사이드 렌더링(SSR) 위주의 구조로 개발되어  
API 엔드포인트, 인증 흐름, 응답 포맷 등이 혼재된 상태였습니다.  
이에 따라 확장성, 유지보수성, 프론트엔드/모바일 연동 측면에서 한계를 인식하게 되었고,  
본 리포지토리는 그 문제를 해결하기 위한 **이행(migration) 단계의 작업 공간**으로 생성되었습니다.

---

## 2. 목적 (Goals)

- RESTful 원칙에 맞는 API 엔드포인트 재설계
- API First 전략 기반의 명세서 중심 개발
- UI(SSR) 라우트와 데이터 API의 명확한 분리
- 향후 Spring MVC 구조로의 마이그레이션을 고려한 설계
- 유지보수 및 확장이 가능한 백엔드 아키텍처 정립

---

## 3. 범위 (Scope)

이 리포지토리에서 다루는 주요 범위는 다음과 같습니다.

- 사용자(User) 관련 API
- 게시글(Post) / 댓글(Comment) API
- 알림(Notification) API
- 인증(Auth) 및 세션 관리
- 홈(Home) 화면을 위한 집계 API
- API 명세(OpenAPI) 및 설계 문서

> 실제 서비스 UI(EJS, 프론트엔드 구현)는 이 리포지토리의 범위에 포함되지 않습니다.

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

```text
community-api-migration/
├── README.md
├── docs/
│   └── api/
│       ├── README.md        # API 설계 및 규칙 문서
│       └── openapi.yaml     # OpenAPI(Swagger) 명세
├── src/
│   ├── routes/
│   ├── controllers/
│   ├── services/
│   └── models/
└── .gitignore
