# 🍣 sushiorder — 식당 QR 주문 시스템

주말에 스시집에서 직접 일하며 겪은 **주문 전달 과정의 비효율**(홀 직원이 주문을 받아 주방에 구두/수기로 전달하는 과정에서의 누락·지연)을 해결하기 위해, 기획부터 배포까지 직접 진행한 개인 프로젝트입니다.

손님이 테이블의 QR을 스캔해 주문하면, 주문이 주방 디스플레이(KDS)까지 **유실 없이** 전달되는 것을 목표로 설계했습니다.

## 핵심 기능

- **QR 주문**: 테이블 QR 스캔 → 세션 토큰 발급 → 테이블 단위로 격리된 주문 세션
- **주방 디스플레이(KDS)**: 접수된 주문을 실시간 표시, 조리 상태 관리
- **관리자/직원 화면**: 메뉴 관리, 테이블 관리, 주문 현황 조회

## 아키텍처

```
[고객 QR 주문 (React)]
        │ REST API
        ▼
[Spring Boot 3 API 서버]
        │
        ├─ 주문 저장 (MariaDB) ─── DB Outbox (큐 유실 대비 폴백)
        │
        ▼
[비동기 주문 전달 파이프라인]
  In-Memory Queue → KDS 전달
        │
        ├─ 전달 실패 시 → DelayQueue 기반 재시도
        └─ 재시도 초과 시 → Dead Letter Queue (운영자 확인용)
```

### 설계에서 신경 쓴 부분

**1. 주문 유실 방지 — "주문은 절대 사라지면 안 된다"**

주문 접수와 KDS 전달을 비동기 큐로 분리하되, 큐는 메모리 기반이라 서버 재시작 시 유실될 수 있습니다. 이를 막기 위해:

- 주문은 항상 **DB에 먼저 저장**(Outbox 역할)한 뒤 큐에 적재
- 서버 기동 시 미전달 주문을 DB에서 복구해 재적재
- KDS 전달 실패 시 **DelayQueue 기반 재시도**, 반복 실패 시 **Dead Letter Queue**로 이동해 운영자가 확인할 수 있도록 처리
- 서버 종료 시 처리 중인 주문이 유실되지 않도록 **Graceful Shutdown** 적용

**2. QR 주문 보안 — 테이블 단위 세션 격리**

QR URL만 알면 아무나 다른 테이블로 주문할 수 있는 문제를 막기 위해, QR 접근 시 **테이블 단위 세션 토큰**을 발급하고 주문 API는 토큰 검증을 거치도록 설계했습니다.

**3. 배포 — 컨테이너 기반 배포 사이클 직접 구성**

- API 서버·프론트엔드·DB를 각각 **Docker 컨테이너화**
- **Kubernetes**(Docker Desktop 내장 클러스터)에 배포, **nginx Ingress**로 라우팅 구성
  - API와 정적 리소스의 rewrite 충돌 문제를 Ingress 리소스 분리로 해결
- 초기 데이터(`data.sql`)가 재배포 시 중복 적재되지 않도록 멱등성 처리

## 기술 스택

| 구분 | 기술 |
|---|---|
| Backend | Java 17, Spring Boot 3, Spring Data JPA |
| Database | MariaDB |
| Frontend | React, Vite |
| Infra | Docker, Kubernetes, nginx Ingress |

## 실행 방법

```bash
# 1. 저장소 클론
git clone https://github.com/{TODO: 계정명}/sushiorder.git

# 2. Docker Compose로 로컬 실행
{TODO: 실제 실행 커맨드 확인 후 기재}

# 3. 접속
# 고객 주문:   http://localhost:{PORT}/order
# 주방(KDS):  http://localhost:{PORT}/kds
# 관리자:     http://localhost:{PORT}/admin
{TODO: 실제 경로 확인 후 수정}
```

## 화면

{TODO: 스크린샷 2~3장 추가 — 고객 주문 화면 / KDS 화면 권장}

| 고객 주문 | 주방 디스플레이(KDS) |
|---|---|
| (스크린샷) | (스크린샷) |

---

## 만들면서 배운 것

- **"동작하는 기능"과 "장애 상황에도 데이터가 어긋나지 않는 기능"의 차이.** 정상 흐름 구현보다 실패 시나리오(큐 유실, 전달 실패, 서버 재시작)를 설계하는 데 더 많은 시간을 썼습니다.
- 실무(SI)에서 다루지 못한 Spring Boot 3, JPA, Kubernetes를 학습에서 끝내지 않고 실제 동작하는 서비스로 검증했습니다.
