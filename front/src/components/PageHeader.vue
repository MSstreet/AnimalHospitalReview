<!-- PageHeader.vue -->
<template>
  <header>
    <nav class="navbar">
        <div class="navbar-logo">
          <router-link to="/">
            <i class="fa-solid fa-shield-cat me-2"></i>
            <span class="logo-text">PetHospital</span>
          </router-link>
        </div>
        <div class="navbar-container">
            <!-- Navigation Links -->
            <ul class="navbar-links">
              <li class="navbar-item" v-for="item in menuItems" :key="item.text">
                <router-link :to="item.link">{{ item.text }}</router-link>
              </li>
              <li class="navbar-item" v-if="!$store.state.isLogin">
                <router-link to="/login" class="login-btn">로그인</router-link>
              </li>
              <li class="navbar-item" v-if="!$store.state.isLogin">
                <router-link to="/join" class="join-btn">회원가입</router-link>
              </li>
            </ul>

          <div class="navbar-user-info">
            <ul class="user-drop-button" v-if="this.$store.state.isLogin">
              <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                  <i class="fa-solid fa-user"></i>
                </a>
                <ul class="dropdown-menu dropdown-menu-end">
                  <li class="dropdown-item">
                    <router-link to="/mypage" class="dropdown-link" v-if="this.$store.state.isLogin">
                      <i class="fa-solid fa-user-circle me-2"></i>내 정보
                    </router-link>
                  </li>
                  <li class="dropdown-item">
                    <router-link v-if="this.$store.state.isLogin" to="/review/myreview" class="dropdown-link">
                      <i class="fa-solid fa-star me-2"></i>나의 리뷰
                    </router-link>
                  </li>
                  <li class="dropdown-item">
                    <router-link v-if="this.$store.state.isLogin" to="/wish/mywish" class="dropdown-link">
                      <i class="fa-solid fa-heart me-2"></i>찜한 병원
                    </router-link>
                  </li>
                  <li class="dropdown-item">
                    <a v-if="this.$store.state.isLogin" class="dropdown-link" @click="fnLogout">
                      <i class="fa-solid fa-sign-out-alt me-2"></i>로그아웃
                    </a>
                  </li>
                </ul>
              </li>
            </ul>
          </div>

          <!-- 모바일 메뉴 버튼 -->
          <div class="navbar-toggle" @click="toggleMenu">
            <span class="navbar-toggle-icon"></span>
            <span class="navbar-toggle-icon"></span>
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
.user-drop-button {
  margin-bottom: 0;
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
  background: linear-gradient(to right, #ffffff, #f8f9fa);
  color: #333;
  position: sticky;
  padding: 0 2rem;
  top: 0;
  z-index: 1000;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  max-width: 1200px;
  margin: 0 auto;
  border-radius: 0 0 10px 10px;
}

.navbar-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 70px;
  padding: 0 1rem;
}

.navbar-logo {
  font-size: 1.5rem;
  font-weight: bold;
}

.navbar-logo a {
  color: #2c3e50;
  display: flex;
  align-items: center;
  transition: transform 0.3s ease;
}

.navbar-logo a:hover {
  transform: scale(1.05);
}

.logo-text {
  font-family: 'Dongle', serif;
  font-size: 2rem;
  margin-left: 0.5rem;
  background: linear-gradient(45deg, #2c3e50, #3498db);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.navbar-links {
  list-style: none;
  display: flex;
  margin: 0;
  gap: 1.5rem;
}

.navbar-item {
  margin: 0;
}

.navbar-item a {
  color: #2c3e50;
  font-size: 1.2rem;
  padding: 0.5rem 1rem;
  border-radius: 8px;
  transition: all 0.3s ease;
  position: relative;
}

.navbar-item a::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  width: 0;
  height: 2px;
  background: #3498db;
  transition: all 0.3s ease;
  transform: translateX(-50%);
}

.navbar-item a:hover::after {
  width: 80%;
}

.login-btn, .join-btn {
  padding: 0.5rem 1.5rem !important;
  border-radius: 25px !important;
  font-weight: 500;
}

.login-btn {
  background-color: #f8f9fa;
  border: 1px solid #dee2e6;
}

.join-btn {
  background-color: #3498db;
  color: white !important;
}

.join-btn:hover {
  background-color: #2980b9 !important;
}

/* User Info Styles */
.navbar-user-info {
  display: flex;
  align-items: center;
}

.navbar-user-info a {
  color: #2c3e50 !important;
  font-size: 1.2rem;
  padding: 0.5rem;
  border-radius: 50%;
  transition: all 0.3s ease;
}

.navbar-user-info a:hover {
  background-color: #f8f9fa;
  transform: scale(1.1);
}

/* Dropdown Menu Styles */
.dropdown-menu {
  border: none;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  border-radius: 12px;
  padding: 0.5rem;
  margin-top: 0.5rem;
}

.dropdown-item {
  padding: 0.5rem 1rem;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.dropdown-item:hover {
  background-color: #f8f9fa;
}

.dropdown-link {
  color: #2c3e50 !important;
  display: flex;
  align-items: center;
  padding: 0.5rem;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.dropdown-link:hover {
  background-color: #f8f9fa;
  transform: translateX(5px);
}

/* Mobile Menu Styles */
.navbar-toggle {
  display: none;
  cursor: pointer;
  padding: 0.5rem;
}

.navbar-toggle-icon {
  display: block;
  width: 25px;
  height: 3px;
  background-color: #2c3e50;
  margin: 5px 0;
  border-radius: 3px;
  transition: all 0.3s ease;
}

@media (max-width: 768px) {
  .navbar {
    padding: 0 1rem;
  }

  .navbar-toggle {
    display: block;
  }

  .navbar-links {
    display: none;
  }

  .navbar-mobile-menu {
    position: fixed;
    top: 70px;
    left: 0;
    width: 100%;
    height: calc(100vh - 70px);
    background: white;
    padding: 2rem;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }

  .navbar-mobile-menu .navbar-links {
    display: flex;
    flex-direction: column;
    gap: 1rem;
  }

  .navbar-mobile-menu .navbar-item {
    margin: 0;
  }

  .navbar-mobile-menu a {
    font-size: 1.2rem;
    padding: 1rem;
    display: block;
  }
}
</style>