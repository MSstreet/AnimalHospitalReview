<!-- PageHeader.vue -->
<template>
  <header>
    <nav class="navbar">
        <div class="navbar-logo">
          <router-link to="/"><i class="fa-solid fa-shield-cat me-2"></i></router-link>
        </div>
        <div class="navbar-container">
            <!-- Navigation Links -->
            <ul class="navbar-links">
              <li class="navbar-item" v-for="item in menuItems" :key="item.text">
                <router-link :to="item.link">{{ item.text }}</router-link>
              </li>
              <li class="navbar-item" v-if="!$store.state.isLogin">
                <router-link to="/login">로그인</router-link>
              </li>
              <li class="navbar-item" v-if="!$store.state.isLogin">
                <router-link to="/join">회원가입</router-link>
              </li>
            </ul>

          <div class="navbar-user-info">
            <ul class="user-drop-button" v-if="this.$store.state.isLogin">
              <li class="nav-item dropdown" >
                <a class="nav-link text-light dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" >
                  <i class="fa-solid fa-user fa-2x"></i>
                </a>
                <ul class="dropdown-menu dropdown-menu-end">
                  <li class="dropdown-item">
                    <router-link to="/mypage" class=" text-black fw-bold" v-if="this.$store.state.isLogin" >내 정보</router-link>
                  </li>
                  <li class="dropdown-item">
                    <router-link v-if="this.$store.state.isLogin" to="/review/myreview" class="nav-link text-black fw-bold">나의 리뷰</router-link>
                  </li>
                  <li class="dropdown-item">
                    <router-link v-if="this.$store.state.isLogin" to="/wish/mywish" class="nav-link text-black fw-bold">찜한 병원</router-link>
                  </li>
                  <li class="dropdown-item">
                    <a v-if="this.$store.state.isLogin" class="nav-link text-black fw-bold" @click="fnLogout">로그아웃</a>
                  </li>
                </ul>
              </li>
            </ul>
          </div>

          <!-- 모바일 메뉴 버튼 -->
          <div class="navbar-toggle" @click="toggleMenu">
            <span class="navbar-toggle-icon"></span>
          </div>
        </div>
    </nav>
    <div class="navbar-mobile-menu" v-if="menuOpen">
      <ul class="navbar-links">
        <li class="navbar-item" v-for="item in menuItems" :key="item.text">
          <router-link :to="item.link">{{ item.text }}</router-link>
        </li>
      </ul>
    </div>
  </header>
</template>

<script>
import {IS_LOGIN} from '@/vuex/mutation_types'
import store from "@/vuex/store";


let setIsLogin = ({commit}, data) => {
  commit(IS_LOGIN, data)
}

export default {
  data() {
    return {
      menuOpen: false,
      menuItems: [
        {text: '동물병원', link: '/hospital/list'},
        {text: '소통창구', link: '/board/list'},
        {text: '공지사항', link: '/notice/list'},
      ]
    };
  },
  methods: {
    toggleMenu() {
      this.menuOpen = !this.menuOpen;
    },
    fnLogout() {
      if (!confirm("로그아웃 하시겠습니까?")) return;
      localStorage.removeItem("user_token");
      localStorage.removeItem("user_role");
      //location.reload()
      //setTimeout(()=>  {this.goToPages1(),1000})
      this.$store.state.isLogin = false;
      setIsLogin(store, false);
      this.goToPages1();
    },
    goToPages1() {
      this.$router.push({
        // path: './write',
        name: 'PageHome'
      })
    },
    goToMyPage(){

    }
  }


}


</script>

<style scoped>

.user-drop-button{
  margin-bottom : 0;
  padding-left: 0;
}

/* Global styles for body and font */
body {
  font-family: 'Arial', sans-serif;
  margin: 0;
  padding: 0;
}

a {
  text-decoration: none;
  color: inherit;
}

/* Navbar Styles */
.navbar {
  --bs-navbar-padding-x: unset;
  color: white;
  position: sticky;
  padding-left: 35px; /* 좌측 여백 */
  padding-right: 30px; /* 우측 여백 */
  top: 0;
  z-index: 1000;
  box-shadow: none; /* 그림자 제거 */
  max-width: 1200px;
  margin: 0 auto; /* 중앙 정렬 */
}

.navbar::after {
  content: '';
  position: absolute;
  bottom: 0;
  width: 100vw; /* 화면 전체 너비 */
  height: 1px; /* 구분선 두께 */
  background-color: rgba(0, 0, 0, 0.1); /* 구분선 색상 */
  transform: translateX(-50%);
  left: 50%;
}

.navbar-container {
  display: flex;
  justify-content: flex-end;; /* Left and right sections */
  align-items: center;
  height: 100%;
  padding: 15px 20px;

}

.navbar-logo {
  font-size: 24px;
  font-weight: bold;
  margin-left: 20px;
}

.navbar-logo a {
  color: black;
}

.navbar-links {
  list-style: none;
  display: flex;
  margin: 0;
}

.navbar-item {
  margin-left: 20px;
  font-family: "Dongle", serif;
}

.navbar-item a {
  color: black;
  font-size: 30px;
  padding: 6px;
  transition: background-color 0.3s ease;
}

.navbar-item a:hover {
  background-color: lightgrey;
  border-radius: 4px;
}

/* User Info on the right */
.navbar-user-info {
  display: flex;
  align-items: center;


}

.navbar-user-info a {
  color: black !important;
  font-size: 16px;
  padding: 8px;
  margin-left: 20px;
  transition: background-color 0.3s ease;
}


/* Mobile menu toggle button */
.navbar-toggle {
  display: none;
  cursor: pointer;
  flex-direction: column;
  align-items: flex-end;
}

.navbar-toggle-icon {
  width: 25px;
  height: 3px;
  background-color: white;
  margin: 4px 0;
}

/* Mobile Menu */
.navbar-mobile-menu {
  background-color: #333;
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  padding-top: 60px;
}

.navbar-mobile-menu ul {
  list-style: none;
  padding: 0;
}

.navbar-mobile-menu .navbar-item {
  margin: 20px 0;
}

.navbar-mobile-menu .navbar-item a {
  font-size: 18px;
  padding: 10px;
  color: white;
}

.navbar-mobile-menu .navbar-item a:hover {
   background-color: #444;
   border-radius: 4px;
 }

/* Responsive Styles for Mobile */
@media (max-width: 768px) {
  .navbar-links {
    display: none;
  }

  .navbar-toggle {
    display: flex;
  }

  .navbar-mobile-menu {
    display: flex;
  }

  .navbar-item a {
    font-size: 18px;
  }
}

</style>