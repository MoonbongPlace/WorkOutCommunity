목적

이 문서는 Migration portfolio 프로젝트의 Spring Boot REST API에서 Controller 레이어의 코드 스타일/구조 컨벤션을 정의한다.
Controller는 Thin Controller를 원칙으로 하며, API 계약(요청/응답 DTO)과 인증 처리 방식의 일관성을 보장한다.

프로젝트 전제

Backend: Spring Boot + PostgreSQL

인증: JWT Access/Refresh + HttpOnly Cookie (Custom principal 사용)

v1 AI Chat: 운동 도메인 Q&A 코치만 제공 (루틴 추천 기능은 v2 이월)

1. Controller 설계 원칙
1.1 Thin Controller

Controller는 아래 4가지만 수행한다.

요청 데이터 수신(@RequestBody/@PathVariable/@RequestParam)

인증 principal에서 memberId 추출(인증 필요 시)

Service 호출

응답 DTO 변환 후 ResponseEntity 반환

금지

비즈니스 로직(정책 판단/계산/중복 체크/상태 변경 로직)

Repository 직접 접근

트랜잭션 처리(@Transactional)

엔티티 직접 반환(Entity -> Response 금지)

try/catch로 예외 처리(예외는 전역 처리로 통일)

2. 인증이 필요한 유즈케이스 규칙
2.1 Principal 주입

인증이 필요한 API는 컨트롤러 메서드 인자로 아래를 사용한다.

@AuthenticationPrincipal CustomUserPrincipal principal
2.2 memberId 추출 방식 고정

memberId는 반드시 아래 형태로 추출한다.

Long memberId = principal.memberId();

추가 규칙

principal null 처리 로직을 Controller에 두지 않는다. (인증/인가 실패는 Security 계층에서 차단)

memberId는 Service로 전달되는 “유일한 사용자 식별자”로 사용한다.

3. 반환 타입 규칙
3.1 ResponseEntity 제네릭 타입은 명시

Controller 메서드 반환 타입은 반드시 아래 형태로 작성한다.

public ResponseEntity<SomeResponse> ...

금지

ResponseEntity<?> 사용 금지
(타입 안정성/Swagger 문서화/프론트 계약 명확화를 위해 제네릭 명시)

4. 요청 DTO 규칙
4.1 RequestBody에는 기본으로 @Valid

@RequestBody를 받는 경우 기본적으로 @Valid를 사용한다.

public ResponseEntity<XxxResponse> create(
        @AuthenticationPrincipal CustomUserPrincipal principal,
        @Valid @RequestBody CreateXxxRequest request
)
4.2 PathVariable / RequestParam 분리

리소스 식별자는 @PathVariable

필터/페이징/정렬 등은 @RequestParam

5. 응답 DTO 규칙
5.1 UseCase 별 응답 DTO 사용

응답 DTO는 “도메인 + 유즈케이스” 기준으로 생성한다.

예시:

WorkOutLogListResponse

MemberInfoResponse

LogoutResponse

NotificationListResponse

5.2 공통 필드

응답 DTO는 공통적으로 다음 필드를 포함한다.

code

message

timestamp

data(또는 result) : 유즈케이스별 payload

payload 이름(data/result)은 프로젝트에서 이미 쓰는 형태로 통일한다.

5.3 응답 DTO 생성은 Static Factory로 통일

응답 변환은 정적 팩토리로 통일한다.

XxxResponse.from(result, "메시지")

또는 프로젝트 기존 관례가 get(...)이면 XxxResponse.get(result, "메시지")

6. HttpStatus 규칙

조회 성공: 200 OK

생성 성공: 201 CREATED

수정/삭제 성공: 기본은 200 OK
(추후 정책으로 204 No Content를 쓰더라도 v1에서는 OK로 통일 권장)

7. 예외 처리 규칙
7.1 Controller에서는 예외를 잡지 않는다

try/catch 금지

Controller에서 CommonException을 처리하지 않는다

7.2 Service에서 CommonException을 던진다

Service 계층에서:

throw new CommonException(ResponseCode.SOME_CODE);

전역 예외 처리는 @RestControllerAdvice에서 통일한다.

8. 네이밍 컨벤션
8.1 클래스/DTO 네이밍

Request: CreateXxxRequest, UpdateXxxRequest

Result(Service 반환용): XxxResult, XxxListResult, DetailXxxResult

Response(API 응답용): XxxResponse

8.2 메서드 네이밍(Controller)

조회: getXxx, getXxxList

생성: createXxx

수정: updateXxx

삭제: deleteXxx

9. 표준 Controller 메서드 템플릿
9.1 인증 필요한 조회
@GetMapping("/list")
public ResponseEntity<WorkOutLogListResponse> getWorkOutList(
        @AuthenticationPrincipal CustomUserPrincipal principal
) {
    Long memberId = principal.memberId();

    WorkOutLogListResult result = workOutLogService.getListWorkOutLog(memberId);

    return ResponseEntity
            .status(HttpStatus.OK)
            .body(WorkOutLogListResponse.from(result, "운동 일지 리스트 조회 성공"));
}
9.2 인증 필요한 생성(+ @Valid)
@PostMapping
public ResponseEntity<WorkOutLogCreateResponse> createWorkOutLog(
        @AuthenticationPrincipal CustomUserPrincipal principal,
        @Valid @RequestBody CreateWorkOutLogRequest request
) {
    Long memberId = principal.memberId();

    CreateWorkOutLogResult result = workOutLogService.createWorkOutLog(memberId, request);

    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(WorkOutLogCreateResponse.from(result, "운동 일지 생성 성공"));
}
10. Claude Code 사용 규칙(코드 생성 시)

Claude Code로 Controller 코드를 생성/수정할 때는 반드시:

이 문서(docs/conventions/controller.md)를 먼저 읽는다.

적용할 컨벤션 체크리스트를 출력한다.

코드 생성은 “Controller → Request/Response DTO → Result DTO” 순으로 일관되게 생성한다.

Controller에 비즈니스 로직을 넣지 않는다.