# EveryHealth
운동 기록과 소셜 기능을 결합한 피트니스 커뮤니티 플랫폼입니다.

## 프로젝트 소개
EveryHealth는 개인의 운동 기록 관리와 친구들과의 운동 경험 공유를 통해 건강한 라이프스타일을 지원하는 웹 애플리케이션입니다.

### 주요 특징
- 운동 기록 관리 및 캘린더 뷰
- 🏃‍♂루틴 기반 운동 관리
- 친구 시스템 및 소셜 기능
- 운동 사진 공유
- 실시간 채팅 및 클럽 기능
- 운동 통계 및 진행도 추적

## ✨ 주요 기능

### 🏋️‍♂️ 운동 관리
- **개인 운동 기록**: 운동 종류별 세트, 횟수, 무게 기록
- **루틴 관리**: 자주 하는 운동들을 루틴으로 저장하고 재사용
- **운동 캘린더**: 월별/일별 운동 기록 시각화
- **운동 완료 체크**: 일일 운동 목표 달성 확인

### 👥 소셜 기능
- **친구 시스템**: 친구 추가, 요청 관리
- **운동 기록 공유**: 친구들의 운동 기록 조회
- **포스트 피드**: 운동 사진과 텍스트 공유
- **댓글 시스템**: 포스트에 댓글 작성 및 소통

### 🏘️ 클럽 기능
- **운동 클럽 생성**: 관심사별 운동 클럽 만들기
- **실시간 채팅**: WebSocket 기반 그룹 채팅
- **클럽 관리**: 클럽 가입/탈퇴

### 🔐 인증 시스템
- **OAuth2 로그인**: Google 소셜 로그인
- **JWT 토큰**: 액세스/리프레시 토큰 기반 인증
- **자동 토큰 갱신**: 만료 시 자동 리프레시

## 🛠 기술 스택

### Backend
- **프레임워크**: Spring Boot 3.3.5
- **언어**: Java 17
- **데이터베이스**: MySQL 8.0
- **캐싱**: Redis
- **인증**: Spring Security + OAuth2 + JWT
- **실시간 통신**: WebSocket + STOMP
- **문서화**: Swagger/OpenAPI 3
- **모니터링**: Spring Actuator + Prometheus + Grafana

### Frontend
- **프레임워크**: React 18
- **드래그앤드롭**: React Beautiful DnD
- **캘린더**: React Calendar

## 🔧 주요 기능별 상세

### 보안
- **JWT 토큰**: 액세스 토큰(30분) + 리프레시 토큰(7일)
- **CORS 설정**: 프론트엔드 도메인만 허용
- **Cookie 보안**: HttpOnly, Secure, SameSite 설정

### 실시간 기능
- **WebSocket**: STOMP 프로토콜 사용
- **채팅**: 그룹 채팅방별 메시지 브로드캐스팅
- **메시지 페이징**: 무한 스크롤 방식 메시지 로딩

## 🏗 아키텍처
![everyhealth_architecture](https://github.com/user-attachments/assets/f2c2017b-aa26-4ddf-8a72-6d6570434467)
![everyhealth_aws_architecture](https://github.com/user-attachments/assets/89b28646-eafb-4e51-bce5-97b2a790bb3b)
