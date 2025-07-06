<!-- PageHome.vue -->
<template>
  <div class="home-container">
    <!-- 메인 슬라이더 섹션 -->
    <section class="hero-section">
      <div class="container">
        <div class="slider-container">
          <splide :options="options" class="main-slider">
            <splide-slide>
              <img src="../assets/home.jpg" alt="동물병원 메인 이미지 1">
            </splide-slide>
            <splide-slide>
              <img src="../assets/home1.jpg" alt="동물병원 메인 이미지 2">
            </splide-slide>
            <splide-slide>
              <img src="../assets/home2.jpg" alt="동물병원 메인 이미지 3">
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
            arrows: false
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
  created(){
  },
  methods: {
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
        // this.fnView()
        console.log(res.data.data);
        // console.log(res.data.pagination);
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
    fnPage(location, type) {
      this.$router.push({
        path: '/board/list',
        query: { type: type, value: location }
      })
    },
    fnPage1(key, value) {
      this.$router.push({
        path: '/board/list',
        query: { type: key, value: value }
      })
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
}

.search-section {
  padding: 2rem 0;
  background-color: #ffffff;
}

.search-box {
  max-width: 800px;
  margin: 0 auto;
  padding: 0 1rem;
}

.search-input-group {
  display: flex;
  gap: 1rem;
  background: #ffffff;
  padding: 1rem;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.search-select {
  padding: 0.8rem 1.5rem;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  font-size: 1rem;
  color: #333;
  background-color: #ffffff;
  cursor: pointer;
}

.search-input {
  flex: 1;
  padding: 0.8rem 1.5rem;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  font-size: 1rem;
}

.search-button {
  padding: 0.8rem 1.5rem;
  background-color: #4a90e2;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.search-button:hover {
  background-color: #357abd;
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
  transition: transform 0.3s;
}

.location-card:hover {
  transform: translateY(-5px);
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
}

.location-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
  gap: 1rem;
}

.location-item {
  padding: 0.5rem 1rem;
  background-color: #f8f9fa;
  border-radius: 6px;
  cursor: pointer;
  text-align: center;
  transition: all 0.3s;
}

.location-item:hover {
  background-color: #4a90e2;
  color: white;
}

@media (max-width: 768px) {
  .main-slider img {
    height: 300px;
  }
  
  .search-input-group {
    flex-direction: column;
  }
  
  .location-grid {
    grid-template-columns: 1fr;
  }
}
</style>