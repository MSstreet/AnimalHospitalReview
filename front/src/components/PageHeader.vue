<!-- PageHeader.vue -->
<template>


  <header>
<div id="topheader">

  <nav class="navbar">
    <div class="navbar-container">
      <!-- 왼쪽: Logo and Navigation Links -->
      <div class="navbar-left">
        <!-- Logo -->
        <div class="navbar-logo">
          <router-link to="/"><i class="fa-solid fa-shield-cat me-2"></i>리뷰</router-link>
        </div>

        <!-- Navigation Links -->
        <ul class="navbar-links">
          <li class="navbar-item" v-for="item in menuItems" :key="item.text">
            <router-link :to="item.link">{{ item.text }}</router-link>
          </li>
        </ul>
      </div>

      <!-- 오르쪽: 유저 정보 -->
      <div class="navbar-user-info">
        <router-link to="/login" v-if="!this.$store.state.isLogin">Login</router-link>
        <a v-if="this.$store.state.isLogin" @click="fnLogout">Logout</a>
        <router-link to="/join" v-if="!this.$store.state.isLogin">Join</router-link>
      </div>

      <!-- 모바일 메뉴 버튼 -->
      <div class="navbar-toggle" @click="toggleMenu">
        <span class="navbar-toggle-icon"></span>
      </div>
    </div>
  </nav>

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
        {text: '게시판', link: '/board/list'},
      ]
    };
  },
  methods: {
    toggleMenu() {
      this.menuOpen = !this.menuOpen;
    },
    fnLogout() {
      localStorage.removeItem("user_token")
      localStorage.removeItem("user_role")
      //location.reload()
      //setTimeout(()=>  {this.goToPages1(),1000})
      this.$store.state.isLogin = false
      setIsLogin(store, false)
      this.goToPages1()
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

/*#nav{
  text-align: center;
}

.nav-item {
  text-color: none;
}

.nav-item:hover  {
  text-decoration: underline;
  text-decoration-thickness: 2px;
  text-decoration-color: white;

}*/



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
  background-color: #333;
  color: white;
  position: sticky;
  top: 0;
  z-index: 1000;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.navbar-container {
  display: flex;
  justify-content: space-between; /* Left and right sections */
  align-items: center;
  padding: 15px 20px;
  height: 100%;
}

.navbar-left {
  display: flex;
  align-items: center;
}

.navbar-logo {
  font-size: 24px;
  font-weight: bold;
}

.navbar-links {
  list-style: none;
  display: flex;
  margin: 0;
}

.navbar-item {
  margin-left: 20px;
}

.navbar-item a {
  color: white;
  font-size: 16px;
  padding: 8px;
  transition: background-color 0.3s ease;
}

.navbar-item a:hover {
  background-color: #444;
  border-radius: 4px;
}

/* User Info on the right */
.navbar-user-info {
  display: flex;
  align-items: center;
}

.navbar-user-info a {
  color: white;
  font-size: 16px;
  padding: 8px;
  margin-left: 20px;
  transition: background-color 0.3s ease;
}

.navbar-user-info a:hover {
  background-color: #444;
  border-radius: 4px;
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