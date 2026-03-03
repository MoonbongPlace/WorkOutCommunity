목적

이 문서는 Migration portfolio 프로젝트에서 Swagger/OpenAPI 문서 생성 및 운영 정책을 정의한다.
Swagger는 프론트 연동 및 테스트 효율을 위한 도구이며, API 계약의 단일 출처(Single Source of Truth) 로 사용한다.

프로젝트 전제

Backend: Spring Boot + PostgreSQL

인증: JWT 기반

AccessToken: Authorization: Bearer <token> 헤더

RefreshToken: HttpOnly Cookie 기반 (Swagger 입력/노출 대상 아님)

Controller 컨벤션: docs/conventions/controller.md 준수 필수

v1 AI Chat: 운동 도메인 Q&A 코치만 제공 (루틴 추천 기능은 v2 이월)

1. 라이브러리 및 문서 생성 방식
1.1 OpenAPI 구현 표준

Swagger 구현은 springdoc-openapi를 표준으로 사용한다.

OpenAPI 스펙은 Controller 기반 자동 생성을 기본으로 한다.

별도의 OpenAPI YAML 파일을 수동으로 작성하지 않는다. (v1 정책)

1.2 기본 엔드포인트

Swagger UI: /swagger-ui/index.html

OpenAPI JSON: /v3/api-docs

경로 변경이 필요한 경우에도 위 경로를 기본값으로 유지하는 것을 우선한다.

2. 보안(Security) 문서화 정책 (중요)
2.1 보안 스키마 기본 원칙

OpenAPI 보안 스키마에는 Bearer JWT(AccessToken)만 정의한다.

RefreshToken은 HttpOnly Cookie로 처리되며, Swagger 입력/출력 스키마에 포함하지 않는다.

이유: 브라우저/클라이언트에서 쿠키는 자동 전송되며, RefreshToken 노출은 보안 리스크가 높다.

2.2 Bearer 스키마 정의

스키마명은 bearerAuth로 통일한다.

형식은 HTTP + bearer + JWT를 따른다.

2.3 인증 적용 범위

인증이 필요한 API는 명시적으로 보안 요구사항을 적용한다.

권장: Controller 클래스 레벨 또는 메서드 레벨에서 @SecurityRequirement(name="bearerAuth")

인증이 필요 없는 API(예: 회원가입/로그인, 공개 조회 등)는 보안 요구사항을 적용하지 않는다.

2.4 Swagger에서의 인증 테스트 규칙

Swagger UI에서 Authorize를 통해 AccessToken을 입력하고 인증 API를 테스트한다.

RefreshToken 기반 재발급 흐름은 Swagger 문서화 범위에 포함하지 않는다. (v1 정책)

3. 문서 범위 및 노출 정책
3.1 v1 문서화 범위

v1 기준으로 존재하는 REST API만 문서화한다.

AI Chat은 운동 도메인 Q&A API만 문서화한다.

루틴 추천(ROUTINE) 관련 API는 v1에서 존재하지 않으며 문서화하지 않는다.

3.2 내부/관리자 API

내부 관리자 전용 API가 존재하는 경우, v1에서는 Swagger에서 제외할 수 있다.

제외 기준은 추후 group 또는 package 단위로 적용한다.

4. Controller 컨벤션 준수 (Swagger 추가 시 변경 금지 영역)

Swagger/OpenAPI를 추가하는 과정에서 다음을 변경하지 않는다:

기존 Controller 메서드 시그니처 및 구조

ResponseEntity<UseCaseResponseDTO> 제네릭 타입 명시 규칙

응답 DTO 구조(code/message/timestamp/data)

Service/Exception 흐름(Controller try/catch 금지)

Swagger 관련 변경은 다음 범위 내에서만 허용한다:

Gradle 의존성 추가

OpenAPI 설정용 Config 클래스 추가

필요 시 Swagger 전용 어노테이션 추가(@Operation, @Tag, @SecurityRequirement 등)

5. 환경별 활성화 정책
5.1 기본 원칙

Swagger UI는 개발 편의를 위해 dev 환경에서 활성화한다.

운영(prod) 환경에서는 비활성화 또는 제한 공개를 기본으로 고려한다.

5.2 설정 방식

가능하면 application.yml 수정 없이 Java Config 기반으로 최소 설정을 우선한다.

프로필 분리가 필요할 경우에만 application-*.yml을 사용한다.

단, application*.yml은 민감 정보가 포함될 수 있으므로 관리에 주의한다.

6. 문서 품질 가이드
6.1 기본 메타데이터

OpenAPI 문서에는 최소 아래 정보를 포함한다.

API Title: “Community API Migration”

Version: “v1”

Description: “Workout community backend migration API”

6.2 Operation 설명

주요 API에는 @Operation(summary=..., description=...)를 추가한다.

Request/Response DTO가 복잡한 경우 description을 통해 필드 의미를 보완한다.

6.3 표준 응답/에러 문서화

공통 에러 응답 포맷이 존재한다면, 대표적인 에러 케이스를 문서에 드러내는 것을 권장한다.

v1에서는 최소한 인증 실패/권한 없음/유효성 실패/리소스 없음 정도는 문서화 대상이다.

7. Claude Code 작업 규칙 (Swagger 작업 시)

Claude Code로 Swagger를 추가/수정할 때는 반드시:

docs/conventions/swagger.md와 docs/conventions/controller.md를 먼저 읽는다.

변경 계획(수정 파일 목록, 이유)을 먼저 제시한다.

Controller 비즈니스 로직/응답 구조를 변경하지 않는다.

의존성 추가 및 Config 추가 위주로 최소 변경을 유지한다.

빌드/실행 명령은 실행 전에 사용자에게 먼저 보여주고 진행한다.