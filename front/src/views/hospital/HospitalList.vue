<template>
<div class="hospital-list-container">
  <div class="search-section">
    <h1 class="page-title" @click="fnReload">동물병원 목록</h1>
    <div class="search-box">
      <select class="search-select" v-model="search_key">
        <option value="author">병원명</option>
        <option value="title">지역명</option>
      </select>
      <input type="text" placeholder="검색어를 입력하세요" maxlength="50" class="search-input" v-model="search_value" @keyup.enter="fnPage()">
      <button @click="fnPage()" class="search-button">
        <i class="fas fa-search"></i> 검색
      </button>
    </div>
  </div>

  <div class="no-results" v-if="list.length==0">
    <i class="fas fa-search-location"></i>
    <h3>조회하신 병원을 찾을 수 없습니다.</h3>
  </div>

  <div class="hospital-grid">
    <div class="hospital-card" v-for="(row, idx) in list" :key="idx" @click="fnView(`${row.hospital_id}`)" @touchstart="handleTouchStart" @touchend="handleTouchEnd">
      <div class="card-content">
        <h3 class="hospital-name">{{ row.hospital_name }}</h3>
        <div class="hospital-info">
          <p v-if="row.hospital_num"><i class="fas fa-phone"></i> {{ row.hospital_num }}</p>
          <p v-if="row.hospital_sigun_name"><i class="fas fa-map-marker-alt"></i> {{ row.hospital_sigun_name }}</p>
          <p v-if="row.hospital_addr"><i class="fas fa-location-dot"></i> {{ row.hospital_addr }}</p>
          <p class="rating" v-if="row.hospital_score"><i class="fas fa-star"></i> {{ row.hospital_score }}</p>
        </div>
      </div>
    </div>
  </div>

  <div class="pagination-container" v-if="paging.total_list_cnt > 0">
    <nav aria-label="Page navigation">
      <ul class="pagination">
        <li class="page-item">
          <a class="page-link" href="javascript:;" @click="fnPage(1)"><i class="fas fa-angle-double-left"></i></a>
        </li>
        <li class="page-item">
          <a class="page-link" href="javascript:;" @click="fnPage(`${paging.start_page-1}`)"><i class="fas fa-angle-left"></i></a>
        </li>

        <li v-for="(n,index) in paginavigation()" :key="index" class="page-item" :class="{ active: paging.page === n }">
          <a class="page-link" href="javascript:;" @click="fnPage(`${n}`)">{{ n }}</a>
        </li>

        <li class="page-item">
          <a class="page-link" href="javascript:;" v-if="paging.total_page_cnt > paging.end_page" @click="fnPage(`${paging.end_page+1}`)">
            <i class="fas fa-angle-right"></i>
          </a>
        </li>
        <li class="page-item">
          <a class="page-link" href="javascript:;" @click="fnPage(`${paging.total_page_cnt}`)">
            <i class="fas fa-angle-double-right"></i>
          </a>
        </li>
      </ul>
    </nav>
  </div>
</div>
</template>

<script>
import store from "@/vuex/store";

export default {
  data() { //변수생성
    return {
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
      size: this.$route.query.size ? this.$route.query.size : 12,

      search_key: 'author',
      search_value: this.$route.query.sv ? this.$route.query.sv : '',

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
    this.fnGetList()
    console.log(this.paging.page)
  },
  methods: {
    handleTouchStart(event) {
      // 터치 시작 시 피드백
      event.currentTarget.style.transform = 'scale(0.98)';
    },
    handleTouchEnd(event) {
      // 터치 종료 시 원래 크기로 복원
      event.currentTarget.style.transform = 'scale(1)';
    },
    fnGetList() {
      console.log(this.search_key)
      console.log("벨류확인" + this.search_value)
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

        console.log(res.data.pagination);
        console.log(res.data.data.length);

      }).catch((err) => {
        if (err.message.indexOf('Network Error') > -1) {
          alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
        }
      })
    },

    fnView(idx) {
      this.requestBody.idx = idx
      this.$router.push({
        path: './detail',
        query: this.requestBody
      })
    },

    fnPage(n) {
      if (this.page !== n) {
        this.page = n
        console.log(this.page)
      }
      this.fnGetList()
    },
    fnReload(){
      location.reload()
    }
  }
}

</script>

<style scoped>
.hospital-list-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
}

.search-section {
  text-align: center;
  margin-bottom: 3rem;
}

.page-title {
  font-size: 2.5rem;
  font-weight: 700;
  color: #2c3e50;
  margin-bottom: 2rem;
  cursor: pointer;
  transition: color 0.3s ease;
}

.page-title:hover {
  color: #3498db;
}

.search-box {
  display: flex;
  justify-content: center;
  gap: 1rem;
  max-width: 600px;
  margin: 0 auto;
  flex-wrap: wrap;
}

.search-select {
  padding: 0.75rem 1rem;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  font-size: 1rem;
  background-color: #fff;
  color: #333;
  cursor: pointer;
  min-height: 48px;
  min-width: 120px;
}

.search-input {
  flex: 1;
  min-width: 200px;
  padding: 0.75rem 1rem;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  font-size: 1rem;
  min-height: 48px;
}

.search-button {
  padding: 0.75rem 1.5rem;
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 1rem;
  font-weight: 500;
  transition: all 0.3s ease;
  min-height: 48px;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.search-button:hover {
  background-color: #2980b9;
  transform: translateY(-1px);
}

.search-button:active {
  transform: translateY(0);
}

.no-results {
  text-align: center;
  padding: 4rem 2rem;
  color: #666;
}

.no-results i {
  font-size: 4rem;
  color: #ddd;
  margin-bottom: 1rem;
}

.no-results h3 {
  font-size: 1.5rem;
  font-weight: 500;
  margin: 0;
}

.hospital-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 2rem;
  margin-bottom: 3rem;
}

.hospital-card {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: all 0.3s ease;
  overflow: hidden;
  border: 1px solid #f0f0f0;
}

.hospital-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.card-content {
  padding: 1.5rem;
}

.hospital-name {
  font-size: 1.3rem;
  font-weight: 600;
  color: #2c3e50;
  margin: 0 0 1rem 0;
  line-height: 1.4;
}

.hospital-info {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.hospital-info p {
  margin: 0;
  color: #666;
  font-size: 0.95rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  line-height: 1.4;
}

.hospital-info i {
  color: #3498db;
  width: 16px;
  text-align: center;
}

.rating {
  color: #f39c12 !important;
  font-weight: 500;
}

.rating i {
  color: #f39c12 !important;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 2rem;
}

.pagination {
  display: flex;
  list-style: none;
  padding: 0;
  margin: 0;
  gap: 0.25rem;
}

.page-item {
  margin: 0;
}

.page-link {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0.75rem 1rem;
  border: 1px solid #e0e0e0;
  background-color: #fff;
  color: #333;
  text-decoration: none;
  border-radius: 8px;
  transition: all 0.3s ease;
  min-height: 44px;
  min-width: 44px;
  font-weight: 500;
}

.page-link:hover {
  background-color: #3498db;
  color: white;
  border-color: #3498db;
  transform: translateY(-1px);
}

.page-item.active .page-link {
  background-color: #3498db;
  color: white;
  border-color: #3498db;
}

/* 모바일 최적화 */
@media (max-width: 768px) {
  .hospital-list-container {
    padding: 1rem;
  }
  
  .page-title {
    font-size: 2rem;
  }
  
  .search-box {
    flex-direction: column;
    gap: 0.5rem;
  }
  
  .search-select,
  .search-input,
  .search-button {
    width: 100%;
    min-width: auto;
  }
  
  .hospital-grid {
    grid-template-columns: 1fr;
    gap: 1rem;
  }
  
  .hospital-card {
    margin-bottom: 0;
  }
  
  .card-content {
    padding: 1rem;
  }
  
  .hospital-name {
    font-size: 1.2rem;
  }
  
  .pagination {
    flex-wrap: wrap;
    justify-content: center;
  }
  
  .page-link {
    padding: 0.5rem 0.75rem;
    font-size: 0.9rem;
    min-height: 40px;
    min-width: 40px;
  }
}

@media (max-width: 480px) {
  .hospital-list-container {
    padding: 0.5rem;
  }
  
  .page-title {
    font-size: 1.8rem;
  }
  
  .search-section {
    margin-bottom: 2rem;
  }
  
  .hospital-info p {
    font-size: 0.9rem;
  }
  
  .pagination {
    gap: 0.125rem;
  }
  
  .page-link {
    padding: 0.4rem 0.6rem;
    font-size: 0.8rem;
    min-height: 36px;
    min-width: 36px;
  }
}

/* 터치 최적화 */
@media (hover: none) and (pointer: coarse) {
  .hospital-card:hover {
    transform: none;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  }
  
  .hospital-card:active {
    transform: scale(0.98);
  }
  
  .search-button:hover {
    transform: none;
  }
  
  .search-button:active {
    transform: scale(0.95);
  }
  
  .page-link:hover {
    transform: none;
  }
  
  .page-link:active {
    transform: scale(0.95);
  }
}
</style>