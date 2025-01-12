<template>
  <PageHeader/> <!-- 헤더 컴포넌트 -->
  <router-view/>  <!-- 페이지 이동이 표시될 곳 -->
  <PageFooter/> <!-- 푸터 컴포넌트 -->
</template>

<script>
import PageHeader from '@/components/PageHeader'
import PageFooter from '@/components/PageFooter'

export default {
  name: 'App',
  components: {
    PageFooter,
    PageHeader
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  /*text-align: center;*/
  /*color: #2c3e50;*/
}

/*#nav {
  padding: 10px;
}*/

/*#nav a {
  font-weight: bold;
  color: #2c3e50;
}

#nav a.router-link-exact-active {
  color: #42b983;
}*/

h1 {
  font-family: "Dongle", serif;
}

.styled-select, .styled-button   {
  width: 100px;
  padding: 10px;
  border: 2px solid #ccc;
  border-radius: 5px;
  font-size: 16px;
  background-color: #fff;
  color: #333;
  appearance: none; /* 기본 브라우저 스타일 제거 */
  -webkit-appearance: none; /* Safari에서의 기본 스타일 제거 */
  -moz-appearance: none; /* Firefox에서의 기본 스타일 제거 */
  cursor: pointer;
  transition: all 0.3s ease;
}

.styled-input{
  width: 300px;
  padding: 10px;
  border: 2px solid #ccc;
  border-radius: 5px;
  font-size: 16px;
  background-color: #fff;
  color: #333;
  appearance: none; /* 기본 브라우저 스타일 제거 */
  -webkit-appearance: none; /* Safari에서의 기본 스타일 제거 */
  -moz-appearance: none; /* Firefox에서의 기본 스타일 제거 */
  cursor: pointer;
  transition: all 0.3s ease;
  box-sizing: border-box; /* padding을 포함한 너비 설정 */
}

.styled-select:focus, .styled-input:focus, .styled-button:focus {
  outline: none;
  border-color: #007bff;
}

.styled-button {
  background-color: #007bff;
  color: #fff;
  border: none;
  cursor: pointer;
  border-radius: 5px;
  text-align: center;
  font-weight: bold;
  transition: all 0.3s ease;
}
</style>