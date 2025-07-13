import './assets/common.css'

// import Vue from 'vue' // Vue2 import 제거
import { createApp } from 'vue'
import App from './App.vue'
import VueSplide from '@splidejs/vue-splide'

import router from './router'
import axios from './utils/axios'
import store from './vuex/store'  //1. store 추가

import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap"

// const key = 4e9386c7cfa4631f95ba0a7f63036ba2;

const app = createApp(App)
app.config.globalProperties.$axios = axios;  //전역변수로 설정 컴포넌트에서 this.$axios 호출할 수 있음
app.config.globalProperties.$store = store
app
    .use(router)
    .use(store)   //2. store 등록
    .use(VueSplide)
    .mount('#app')

// Service Worker 등록
if ('serviceWorker' in navigator) {
  window.addEventListener('load', () => {
    navigator.serviceWorker.register('/sw.js')
      .then((registration) => {
        console.log('SW registered: ', registration);
      })
      .catch((registrationError) => {
        console.log('SW registration failed: ', registrationError);
      });
  });
}

// PWA 설치 프롬프트
let deferredPrompt;

window.addEventListener('beforeinstallprompt', (e) => {
  // Prevent Chrome 67 and earlier from automatically showing the prompt
  e.preventDefault();
  // Stash the event so it can be triggered later.
  deferredPrompt = e;
  // Update UI to notify the user they can add to home screen
  console.log('PWA install prompt available');
});

// PWA 설치 완료 이벤트
window.addEventListener('appinstalled', (evt) => {
  console.log('PWA installed successfully');
  deferredPrompt = null;
});