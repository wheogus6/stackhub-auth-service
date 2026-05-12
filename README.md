# StackHub Auth Service

> 인증/인가 전담 서비스
> JWT 발급, 갱신, 로그아웃을 담당하며 stackhub-core-service와 분리된 독립 서비스입니다.

---

## 기술 스택

| 분류 | 기술 |
|------|------|
| Language | Java 21 |
| Framework | Spring Boot |
| DB | PostgreSQL |
| Cache | Redis |
| 인증 | JWT (jjwt) |
| 암호화 | BCrypt |
| Build | Gradle |

---

## 프로젝트 구조

```
auth/
  config/
    properties/   JwtProperties.java
    RedisConfig.java
    SecurityConfig.java
  controller/
    JoinController.java     회원가입
    LoginController.java    로그인
    LogoutController.java   로그아웃
    ReissueController.java  토큰 재발급
  dto/
    member/   MemberJoinReqDto, MemberLoginReqDto, MemberLoginResDto
    admin/    AdminLoginReqDto, AdminLoginResDto
  entity/     Member, Admin, CodePolicy
  jwt/        JwtProvider.java
  service/
    member/   MemberJoinService, MemberLoginService, MemberLogoutService, MemberReissueService
    admin/    AdminLoginService, AdminLogoutService, AdminReissueService
    common/   RedisService, CreateCodeService
  util/       CryptoUtil.java
```

---

## 핵심 구현 포인트

### 1. JWT 발급 구조

Access Token과 Refresh Token을 함께 발급합니다. 두 토큰 모두 동일한 `sessionToken`(UUID)을 클레임에 포함시켜 같은 세션에서 발급된 토큰임을 보장합니다.

```java
String sessionToken = UUID.randomUUID().toString();

String accessToken  = jwtProvider.generateAccessToken(memberId, memberCode, sessionToken);
String refreshToken = jwtProvider.generateRefreshToken(memberId, memberCode, sessionToken);
```

| 토큰 | 만료 | 용도 |
|------|------|------|
| Access Token | 1시간 | API 요청 인증 |
| Refresh Token | 7일 | Access Token 재발급 |

---

### 2. Refresh Token 저장 — Redis

Refresh Token을 Redis에 저장해 빠른 조회와 자동 만료를 처리합니다.

```java
// 키 구조: auth:{userType}:{userCode}:refresh
redisTemplate.opsForValue()
    .set(key, refreshToken, Duration.ofDays(7));
```

로그아웃 시 Redis에서 Refresh Token을 삭제해 재사용을 차단합니다.

> DB 대신 Redis 사용 이유: TTL 7일 자동 만료로 별도 정리 로직이 필요 없고, O(1) 조회로 재발급 응답 속도를 높였습니다.

---

### 3. 토큰 재발급 (Reissue)

```
1. Refresh Token 유효성 검증 (서명 + 만료)
2. Redis에 저장된 토큰과 일치 여부 확인
3. 새 sessionToken으로 Access + Refresh 모두 재발급
4. Redis에 새 Refresh Token 저장 (이전 토큰 교체)
```

Refresh Token 재사용을 방지하기 위해 재발급 시 Refresh Token도 함께 갱신합니다.

---

### 4. 회원/관리자 분리

회원(`member`)과 관리자(`admin`) 인증 로직을 서비스 레벨에서 분리했습니다. Redis 키도 `userType`으로 구분해 충돌을 방지합니다.

```
auth:member:{memberCode}:refresh
auth:admin:{adminCode}:refresh
```

---

## API 명세

| Method | URL | 설명 |
|--------|-----|------|
| POST | /join/member | 회원가입 |
| POST | /login/member | 로그인 (Access + Refresh Token 발급) |
| POST | /logout/member | 로그아웃 (Redis Refresh Token 삭제) |
| POST | /reissue/member | 토큰 재발급 |
| POST | /login/admin | 관리자 로그인 |
| POST | /logout/admin | 관리자 로그아웃 |
| POST | /reissue/admin | 관리자 토큰 재발급 |

---

## 실행 방법

**사전 요구사항**
- Java 21
- PostgreSQL
- Redis

**설정**

`src/main/resources/application.properties`에서 DB, Redis, JWT 설정을 합니다.

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=
spring.datasource.password=

spring.data.redis.host=localhost
spring.data.redis.port=6379

jwt.secret-key=
jwt.access-expiration=3600000
jwt.refresh-expiration=604800000
```

**빌드 및 실행**

```bash
./gradlew bootRun
```

---

## 관련 프로젝트

- [stackhub-core-service](https://github.com/wheogus6/stackhub-core-service) — 결제/정산 서비스

> auth 서비스에서 발급한 JWT를 core 서비스에서 검증하는 구조로 설계되었습니다.
