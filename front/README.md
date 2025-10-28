# PetHospital Frontend - 웹앱 최적화 버전

동물병원 리뷰 및 정보 서비스를 위한 Vue.js 기반 프론트엔드 애플리케이션입니다.

## 주요 기능

### 웹앱 최적화
- **PWA (Progressive Web App)** 지원
- **모바일 터치 최적화**
- **반응형 디자인**
- **오프라인 캐싱**
- **성능 최적화**

### 핵심 기능
- 동물병원 검색 및 상세 정보
- 병원 리뷰 작성 및 조회
- 사용자 인증 (카카오 로그인)
- 찜한 병원 관리
- 게시판 및 공지사항

## 웹앱 최적화 특징

### 1. 터치 인터랙션 최적화
- 최소 44px 터치 영역 보장
- 터치 피드백 애니메이션
- 스와이프 제스처 지원
- 터치 하이라이트 제거

### 2. 반응형 디자인
- 모바일 우선 설계
- 태블릿 및 데스크톱 지원
- 유연한 그리드 시스템
- 적응형 타이포그래피

### 3. 성능 최적화
- 이미지 지연 로딩 (lazy loading)
- 코드 스플리팅
- 캐싱 전략
- 번들 크기 최적화

### 4. PWA 기능
- 홈 화면 설치 가능
- 오프라인 동작
- 푸시 알림 지원
- 앱과 같은 사용자 경험

## 기술 스택

- **Vue.js 3** - 프론트엔드 프레임워크
- **Vue Router 4** - 라우팅
- **Vuex 4** - 상태 관리
- **Axios** - HTTP 클라이언트
- **Bootstrap 5** - UI 프레임워크

## 설치 및 실행

### 필수 요구사항
- Node.js 16+ 
- npm 8+

### 설치
```bash
# 의존성 설치
npm install

# 개발 서버 실행
npm run serve

# 프로덕션 빌드
npm run build

# 린트 검사
npm run lint
```

### 개발 서버
- URL: http://localhost:8080
- 백엔드 프록시: http://localhost:8081

## 프로젝트 구조

```
src/
├── assets/          # 정적 자원
├── components/      # 재사용 컴포넌트
│   ├── LoadingSpinner.vue    # 로딩 스피너
│   ├── TouchFeedback.vue     # 터치 피드백
│   └── PageHeader.vue        # 헤더 컴포넌트
├── views/           # 페이지 컴포넌트
│   ├── PageHome.vue          # 홈페이지
│   ├── hospital/             # 병원 관련 페이지
│   ├── review/               # 리뷰 관련 페이지
│   └── common/               # 공통 페이지
├── router/          # 라우팅 설정
├── service/         # API 서비스
├── utils/           # 유틸리티
├── vuex/            # 상태 관리
└── main.js          # 앱 진입점
```

## 웹앱 설치 방법

### Chrome/Edge
1. 브라우저에서 사이트 접속
2. 주소창 옆 설치 아이콘 클릭
3. "설치" 버튼 클릭

### Safari (iOS)
1. Safari에서 사이트 접속
2. 공유 버튼 탭
3. "홈 화면에 추가" 선택

### Android Chrome
1. Chrome에서 사이트 접속
2. 메뉴 → "홈 화면에 추가"
3. "추가" 버튼 클릭

## 디자인 시스템

### 색상 팔레트
- **Primary**: #3498db (파란색)
- **Secondary**: #2c3e50 (다크 그레이)
- **Success**: #27ae60 (그린)
- **Warning**: #f39c12 (오렌지)
- **Background**: #f8f9fa (라이트 그레이)

### 타이포그래피
- **Primary Font**: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto
- **Logo Font**: 'Dongle', serif
- **Base Size**: 16px (모바일: 14px)

### 컴포넌트 크기
- **터치 영역**: 최소 44px
- **버튼 높이**: 48px
- **입력 필드**: 48px
- **카드 패딩**: 1.5rem (모바일: 1rem)

## 웹앱 최적화 설정

### PWA 설정
- `manifest.json`: 앱 메타데이터
- `sw.js`: Service Worker
- `vue.config.js`: PWA 플러그인 설정

### 성능 최적화
- 이미지 압축 및 최적화
- CSS/JS 번들 최적화
- 코드 스플리팅
- 캐싱 전략

### 접근성
- 키보드 네비게이션
- 스크린 리더 지원
- 고대비 모드 지원
- 포커스 표시 개선


---

**PetHospital** - 반려동물을 위한 최고의 병원 정보 서비스 🐾
