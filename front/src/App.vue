<template>
  <PageHeader/> <!-- 헤더 컴포넌트 -->
  <router-view/>  <!-- 페이지 이동이 표시될 곳 -->
</template>

<script>
import PageHeader from '@/components/PageHeader'

export default {
  name: 'App',
  components: {
    PageHeader
  }
}
</script>

<style>
#app {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  min-height: 100vh;
  background-color: #f8f9fa;
}

h1 {
  font-family: "Dongle", serif;
  font-weight: 700;
  margin: 0;
  padding: 0;
}

/* 웹앱 최적화된 컴포넌트 스타일 */
.styled-select, .styled-button {
  width: 100%;
  max-width: 200px;
  padding: 12px 16px;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  font-size: 16px;
  background-color: #fff;
  color: #333;
  appearance: none;
  -webkit-appearance: none;
  -moz-appearance: none;
  cursor: pointer;
  transition: all 0.3s ease;
  min-height: 48px; /* 터치 최적화 */
  box-sizing: border-box;
}

.styled-input {
  width: 100%;
  max-width: 400px;
  padding: 12px 16px;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  font-size: 16px;
  background-color: #fff;
  color: #333;
  appearance: none;
  -webkit-appearance: none;
  -moz-appearance: none;
  cursor: pointer;
  transition: all 0.3s ease;
  box-sizing: border-box;
  min-height: 48px; /* 터치 최적화 */
}

.styled-select:focus, .styled-input:focus, .styled-button:focus {
  outline: none;
  border-color: #007bff;
  box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.1);
}

.styled-button {
  background-color: #007bff;
  color: #fff;
  border: none;
  cursor: pointer;
  border-radius: 8px;
  text-align: center;
  font-weight: 600;
  transition: all 0.3s ease;
  min-width: 120px;
}

.styled-button:hover {
  background-color: #0056b3;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(0, 123, 255, 0.3);
}

.styled-button:active {
  transform: translateY(0);
  box-shadow: 0 2px 6px rgba(0, 123, 255, 0.3);
}

/* 모바일 최적화 */
@media (max-width: 768px) {
  .styled-select, .styled-input, .styled-button {
    width: 100%;
    max-width: none;
    font-size: 16px; /* iOS에서 줌 방지 */
  }
  
  .styled-button {
    min-width: auto;
    width: 100%;
    margin-bottom: 0.5rem;
  }
}

/* 스크롤바 스타일링 */
::-webkit-scrollbar {
  width: 8px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* 포커스 표시 개선 */
*:focus {
  outline: 2px solid #007bff;
  outline-offset: 2px;
}

/* 터치 하이라이트 제거 */
* {
  -webkit-tap-highlight-color: transparent;
}

/* 텍스트 선택 개선 */
::selection {
  background-color: #007bff;
  color: white;
}

::-moz-selection {
  background-color: #007bff;
  color: white;
}
</style>