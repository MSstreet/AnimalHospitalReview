<!-- PageHome.vue -->
<template>
  <div class="home-container">
    <!-- 메인 슬라이더 섹션 -->
    <section class="hero-section">
      <div class="container">
        <div class="slider-container">
          <splide :options="options" class="main-slider">
            <splide-slide>
              <img src="../assets/home.jpg" alt="동물병원 메인 이미지 1" loading="lazy">
            </splide-slide>
            <splide-slide>
              <img src="../assets/home1.jpg" alt="동물병원 메인 이미지 2" loading="lazy">
            </splide-slide>
            <splide-slide>
              <img src="../assets/home2.jpg" alt="동물병원 메인 이미지 3" loading="lazy">
            </splide-slide>
          </splide>
        </div>
      </div>
    </section>

    <!-- 검색 섹션 -->
    <section class="search-section">
      <div class="container">
        <div class="search-box">
          <form @submit.prevent="fnPage1(search_key, search_value)">
            <div class="search-input-group">
              <select class="search-select" v-model="search_key">
                <option value="author">병원명</option>
                <option value="title">지역명</option>
              </select>
              <input 
                type="text" 
                class="search-input" 
                placeholder="검색어를 입력하세요" 
                v-model="search_value"
                maxlength="50"
                autocomplete="off"
              >
              <button type="submit" class="search-button">
                <i class="fas fa-search"></i>
              </button>
            </div>
          </form>
        </div>
      </div>
    </section>

    <!-- 지역 목록 섹션 -->
    <section class="location-section">
      <div class="container">
        <div class="location-grid">
          <div v-for="(locations, city) in groupedLocations" :key="city" class="location-card">
            <div class="location-header">
              <i class="fas fa-location-dot"></i>
              <h3>{{ city }}</h3>
            </div>
            <div class="location-list">
              <div 
                v-for="location in locations" 
                :key="location"
                class="location-item"
                @click="fnPage(location, 'title')"
                @touchstart="handleTouchStart"
                @touchend="handleTouchEnd"
              >
                {{ location }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
// @ is an alias to /src
import HelloWorld from '@/components/HelloWorld.vue'
import { Splide, SplideSlide } from '@splidejs/vue-splide'
import '@splidejs/vue-splide/css'

export default {
  name: 'PageHome',
  components: {
    HelloWorld,
    Splide,
    SplideSlide
  },
  data() {
    return {
      userInfo: {
        userId: null,
        nickname: null,
      },
      options: {
        type: 'loop',
        perPage: 1,
        autoplay: true,
        interval: 4000,
        pauseOnHover: true,
        arrows: true,
        pagination: true,
        breakpoints: {
          768: {
            arrows: false,
            pagination: true
          }
        }
      },
      map: null,
      markers: [],
      latitude: 0,
      longitude: 0,
      requestBody: {}, //리스트 페이지 데이터전송
      list: {}, //리스트 데이터
      no: '', //게시판 숫자처리
      paging: {
        block: 0,
        end_page: 0,
        next_block: 0,
        page: 0,
        page_size: 0,
        prev_block: 0,
        start_index: 0,
        start_page: 0,
        total_block_cnt: 0,
        total_list_cnt: 0,
        total_page_cnt: 0,
      }, //페이징 데이터
      page: this.$route.query.page ? this.$route.query.page : 1,
      size: this.$route.query.size ? this.$route.query.size : 10,
      search_key: 'author',
      search_value: '',
      groupedLocations: {
        '부천시': ['범안로', '중동로', '역곡로', '상일로', '소향로', '송내대로', '부일로', '상동로'],
        '수원시': ['권선구', '영통구', '장안구', '팔달구'],
        '성남시': ['수정구', '분당구', '중원구'],
        '용인시': ['수지구'],
        '시흥시': ['수인로', '승지로', '은행로', '함송로', '정왕대로', '호현로', '대은로'],
        '안산시': ['단원구', '상록구'],
        '고양시': ['덕양구', '일산동구', '일산서구'],
        '광명시': ['광덕산로', '하안로', '디지털로', '소하로', '금하로', '오리로'],
        '평택시': ['송탄로', '평택로', '탄현로', '청북읍', '중앙로', '서정역로', '안중읍']
      },
      paginavigation: function () { //페이징 처리 for문 커스텀
        let pageNumber = [] //;
        let start_page = this.paging.start_page;
        let end_page = this.paging.end_page;
        for (let i = start_page; i <= end_page; i++) pageNumber.push(i);
        return pageNumber;
      }
    }
  },
  mounted() {
    // 터치 이벤트 최적화
    this.optimizeTouchEvents();
  },
  methods: {
    optimizeTouchEvents() {
      // 터치 스크롤 최적화
      const sliders = document.querySelectorAll('.splide');
      sliders.forEach(slider => {
        slider.style.touchAction = 'pan-y';
      });
    },
    handleTouchStart(event) {
      // 터치 시작 시 피드백
      event.target.style.transform = 'scale(0.95)';
    },
    handleTouchEnd(event) {
      // 터치 종료 시 원래 크기로 복원
      event.target.style.transform = 'scale(1)';
    },
    fnGetList() {
      this.requestBody = { // 데이터 전송
        sk: this.search_key,
        sv: this.search_value,
        page: this.page,
        size: this.size
      }
      this.$axios.get("/hospital/list", {
        params: this.requestBody,
        headers: {}
      }).then((res) => {
        if (res.data.result_code === "OK") {
          this.list = res.data.data
          this.paging = res.data.pagination
          this.no = this.paging.total_list_cnt - ((this.paging.page - 1) * this.paging.page_size)
        }
        console.log(res.data.data);
      }).catch((err) => {
        if (err.message.indexOf('Network Error') > -1) {
          alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
        }
      })
    },
    fnGetUserInfo() {
      this.requestBody = { // 데이터 전송
        email: this.userInfo.userId,
        nickname: this.userInfo.nickname,
      }
      this.$axios.get("/oauth/kakao/user-info", {
        params: this.requestBody,
        headers: {}
      }).then((res) => {
        console.log(res.data);
      }).catch((err) => {
        if (err.message.indexOf('Network Error') > -1) {
          alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
        }
      })
    },
    // 지역 버튼 클릭 시 동물병원 리스트로 이동
    fnPage(location, type) {
      this.$router.push({
        path: '/hospital/list',
        query: { sk: 'title', sv: location }
      });
    },
    fnPage1(key, value) {
      // 병원명/지역명에 따라 병원 리스트로 이동
      if (key === 'author') {
        // 병원명 검색
        this.$router.push({
          path: '/hospital/list',
          query: { sk: 'author', sv: value }
        });
      } else if (key === 'title') {
        // 지역명 검색
        this.$router.push({
          path: '/hospital/list',
          query: { sk: 'title', sv: value }
        });
      }
    }
  }
}
</script>

<style scoped>
.home-container {
  min-height: 100vh;
  background-color: #f8f9fa;
}

.hero-section {
  padding: 2rem 0;
  background: linear-gradient(to bottom, #ffffff, #f8f9fa);
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 1rem;
}

.slider-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 1rem;
}

.main-slider {
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.main-slider img {
  width: 100%;
  height: 500px;
  object-fit: cover;
  border-radius: 20px;
  transition: transform 0.3s ease;
}

.main-slider img:hover {
  transform: scale(1.02);
}

.search-section {
  padding: 2rem 0;
  background-color: #ffffff;
}

.search-box {
  max-width: 600px;
  margin: 0 auto;
  padding: 0 1rem;
  display: flex;
  justify-content: center;
}

.search-input-group {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  background: #ffffff;
  padding: 0.7rem 1rem;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  width: 100%;
  max-width: 500px;
}

.search-select {
  padding: 0.6rem 1rem;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  font-size: 1rem;
  color: #333;
  background-color: #ffffff;
  cursor: pointer;
  min-height: 40px;
  height: 40px;
  width: 110px;
  flex: 0 0 110px;
  box-sizing: border-box;
  display: flex;
  align-items: center;
}

.search-input {
  flex: 1 1 220px;
  min-width: 120px;
  padding: 0.6rem 1rem;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  font-size: 1rem;
  min-height: 40px;
  height: 40px;
  margin-left: 0.2rem;
  margin-right: 0.2rem;
  box-sizing: border-box;
  display: flex;
  align-items: center;
}

.search-button {
  padding: 0.6rem 1.1rem;
  background-color: #4a90e2;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  min-height: 40px;
  min-width: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.search-button:hover {
  background-color: #357abd;
  transform: translateY(-1px);
}

.search-button:active {
  transform: translateY(0);
}

.location-section {
  padding: 3rem 0;
}

.location-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
  padding: 0 1rem;
}

.location-card {
  background: #ffffff;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.location-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.location-header {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-bottom: 1.5rem;
  padding-bottom: 0.5rem;
  border-bottom: 2px solid #4a90e2;
}

.location-header i {
  color: #4a90e2;
  font-size: 1.2rem;
}

.location-header h3 {
  margin: 0;
  color: #333;
  font-size: 1.2rem;
  font-weight: 600;
}

.location-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
  gap: 1rem;
}

.location-item {
  padding: 0.75rem 1rem;
  background-color: #f8f9fa;
  border-radius: 8px;
  cursor: pointer;
  text-align: center;
  transition: all 0.3s ease;
  min-height: 44px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 500;
  border: 1px solid transparent;
}

.location-item:hover {
  background-color: #4a90e2;
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(74, 144, 226, 0.3);
}

.location-item:active {
  transform: translateY(0);
}

/* 모바일 최적화 */
@media (max-width: 768px) {
  .hero-section {
    padding: 1rem 0;
  }
  
  .main-slider img {
    height: 300px;
  }
  
  .search-input-group {
    flex-direction: column;
    gap: 0.5rem;
  }
  
  .search-select,
  .search-input,
  .search-button {
    width: 100%;
    min-width: auto;
  }
  
  .location-grid {
    grid-template-columns: 1fr;
    gap: 1rem;
  }
  
  .location-list {
    grid-template-columns: repeat(auto-fill, minmax(80px, 1fr));
    gap: 0.5rem;
  }
  
  .location-item {
    padding: 0.5rem 0.75rem;
    font-size: 0.9rem;
    min-height: 40px;
  }
  
  .location-card {
    padding: 1rem;
  }
  
  .location-header h3 {
    font-size: 1.1rem;
  }
}

@media (max-width: 480px) {
  .container {
    padding: 0 0.5rem;
  }
  
  .slider-container {
    padding: 0 0.5rem;
  }
  
  .search-box {
    padding: 0 0.5rem;
  }
  
  .location-grid {
    padding: 0 0.5rem;
  }
  
  .main-slider img {
    height: 250px;
  }
  
  .location-list {
    grid-template-columns: repeat(auto-fill, minmax(70px, 1fr));
  }
  
  .location-item {
    font-size: 0.8rem;
    padding: 0.4rem 0.5rem;
  }
}

@media (max-width: 600px) {
  .search-box {
    max-width: 98vw;
    padding: 0 0.2rem;
  }
  .search-input-group {
    flex-direction: column;
    gap: 0.4rem;
    padding: 0.7rem 0.3rem;
    max-width: 98vw;
  }
  .search-select, .search-input, .search-button {
    width: 100%;
    min-width: 0;
    margin: 0;
  }
}

/* 터치 최적화 */
@media (hover: none) and (pointer: coarse) {
  .location-item:hover {
    background-color: #f8f9fa;
    color: inherit;
    transform: none;
    box-shadow: none;
  }
  
  .location-item:active {
    background-color: #4a90e2;
    color: white;
    transform: scale(0.95);
  }
  
  .search-button:hover {
    transform: none;
  }
  
  .search-button:active {
    transform: scale(0.95);
  }
}
</style>