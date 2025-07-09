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
                <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" @click.prevent="toggleDropdown">
                  <i class="fa-solid fa-user"></i>
                </a>
                <ul class="dropdown-menu dropdown-menu-end" :class="{ 'show': dropdownOpen }">
                  <li class="dropdown-item">
                    <router-link to="/mypage" class="dropdown-link" v-if="this.$store.state.isLogin" @click="closeDropdown">
                      <i class="fa-solid fa-user-circle me-2"></i>내 정보
                    </router-link>
                  </li>
                  <li class="dropdown-item">
                    <router-link v-if="this.$store.state.isLogin" to="/review/myreview" class="dropdown-link" @click="closeDropdown">
                      <i class="fa-solid fa-star me-2"></i>나의 리뷰
                    </router-link>
                  </li>
                  <li class="dropdown-item">
                    <router-link v-if="this.$store.state.isLogin" to="/wish/mywish" class="dropdown-link" @click="closeDropdown">
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
          <div class="navbar-toggle" @click="toggleMenu" :class="{ 'active': menuOpen }">
            <span class="navbar-toggle-icon"></span>
            <span class="navbar-toggle-icon"></span>
            <span class="navbar-toggle-icon"></span>
          </div>
        </div>
    </nav>
    <div class="navbar-mobile-menu" v-if="menuOpen" @click="closeMenu">
      <div class="mobile-menu-content" @click.stop>
        <ul class="navbar-links">
          <li class="navbar-item" v-for="item in menuItems" :key="item.text">
            <router-link :to="item.link" @click="closeMenu">{{ item.text }}</router-link>
          </li>
          <li class="navbar-item" v-if="!$store.state.isLogin">
            <router-link to="/login" class="login-btn" @click="closeMenu">로그인</router-link>
          </li>
          <li class="navbar-item" v-if="!$store.state.isLogin">
            <router-link to="/join" class="join-btn" @click="closeMenu">회원가입</router-link>
          </li>
        </ul>
      </div>
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
      dropdownOpen: false,
      menuItems: [
        {text: '동물병원', link: '/hospital/list'},
        {text: '소통창구', link: '/board/list'},
        {text: '공지사항', link: '/notice/list'},
      ]
    };
  },
  mounted() {
    // 외부 클릭 시 드롭다운 닫기
    document.addEventListener('click', this.handleClickOutside);
  },
  beforeUnmount() {
    document.removeEventListener('click', this.handleClickOutside);
  },
  methods: {
    toggleMenu() {
      this.menuOpen = !this.menuOpen;
      this.dropdownOpen = false; // 모바일 메뉴 열 때 드롭다운 닫기
    },
    closeMenu() {
      this.menuOpen = false;
    },
    toggleDropdown() {
      this.dropdownOpen = !this.dropdownOpen;
    },
    closeDropdown() {
      this.dropdownOpen = false;
    },
    handleClickOutside(event) {
      if (!event.target.closest('.dropdown')) {
        this.dropdownOpen = false;
      }
    },
    fnLogout() {
      if (!confirm("로그아웃 하시겠습니까?")) return;
      localStorage.removeItem("user_token");
      localStorage.removeItem("user_role");
      this.$store.state.isLogin = false;
      setIsLogin(store, false);
      this.closeDropdown();
      this.closeMenu();
      this.goToPages1();
    },
    goToPages1() {
      this.$router.push({
        name: 'PageHome'
      })
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
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
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
  padding: 0 1rem;
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
  padding: 0.5rem;
  border-radius: 8px;
}

.navbar-logo a:hover {
  transform: scale(1.05);
  background-color: rgba(52, 152, 219, 0.1);
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
  padding: 0.75rem 1rem;
  border-radius: 8px;
  transition: all 0.3s ease;
  position: relative;
  display: block;
  min-height: 44px;
  display: flex;
  align-items: center;
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

.navbar-item a:hover {
  background-color: rgba(52, 152, 219, 0.1);
}

.login-btn, .join-btn {
  padding: 0.75rem 1.5rem !important;
  border-radius: 25px !important;
  font-weight: 500;
  min-height: 44px;
  display: flex;
  align-items: center;
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
  transform: translateY(-1px);
}

/* User Info Styles */
.navbar-user-info {
  display: flex;
  align-items: center;
}

.navbar-user-info a {
  color: #2c3e50 !important;
  font-size: 1.2rem;
  padding: 0.75rem;
  border-radius: 50%;
  transition: all 0.3s ease;
  min-height: 44px;
  min-width: 44px;
  display: flex;
  align-items: center;
  justify-content: center;
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
  min-width: 200px;
}

.dropdown-menu.show {
  display: block;
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
  padding: 0.75rem;
  border-radius: 8px;
  transition: all 0.3s ease;
  min-height: 44px;
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
  flex-direction: column;
  justify-content: center;
  align-items: center;
  min-height: 44px;
  min-width: 44px;
}

.navbar-toggle-icon {
  display: block;
  width: 25px;
  height: 3px;
  background-color: #2c3e50;
  margin: 3px 0;
  border-radius: 3px;
  transition: all 0.3s ease;
}

.navbar-toggle.active .navbar-toggle-icon:nth-child(1) {
  transform: rotate(45deg) translate(5px, 5px);
}

.navbar-toggle.active .navbar-toggle-icon:nth-child(2) {
  opacity: 0;
}

.navbar-toggle.active .navbar-toggle-icon:nth-child(3) {
  transform: rotate(-45deg) translate(7px, -6px);
}

.navbar-mobile-menu {
  position: fixed;
  top: 70px;
  left: 0;
  width: 100%;
  height: calc(100vh - 70px);
  background: rgba(0, 0, 0, 0.5);
  z-index: 999;
  animation: fadeIn 0.3s ease;
}

.mobile-menu-content {
  background: white;
  height: 100%;
  padding: 2rem;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  animation: slideIn 0.3s ease;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

@keyframes slideIn {
  from { transform: translateX(-100%); }
  to { transform: translateX(0); }
}

@media (max-width: 768px) {
  .navbar {
    padding: 0 0.5rem;
  }

  .navbar-container {
    padding: 0 0.5rem;
  }

  .navbar-toggle {
    display: flex;
  }

  .navbar-links {
    display: none;
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
    border-radius: 8px;
    background-color: #f8f9fa;
    margin-bottom: 0.5rem;
  }

  .navbar-mobile-menu a:hover {
    background-color: #e9ecef;
  }

  .logo-text {
    font-size: 1.5rem;
  }

  .navbar-user-info {
    margin-right: 1rem;
  }
}

@media (max-width: 480px) {
  .navbar-container {
    height: 60px;
  }

  .navbar-mobile-menu {
    top: 60px;
    height: calc(100vh - 60px);
  }

  .logo-text {
    font-size: 1.3rem;
  }

  .navbar-logo {
    font-size: 1.2rem;
  }
}
</style>