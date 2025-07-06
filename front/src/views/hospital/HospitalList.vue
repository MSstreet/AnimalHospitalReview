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
    <div class="hospital-card" v-for="(row, idx) in list" :key="idx" @click="fnView(`${row.hospital_id}`)">
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

        // test:this.$store.state.userIdx,

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

      //search_key: this.$route.query.sk ? this.$route.query.sk : '',
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
  }
  ,mounted() {
    this.fnGetList()
    console.log(this.paging.page)
  }
  ,methods: {
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
        // console.log(res.data.pagination);

      }).catch((err) => {
        if (err.message.indexOf('Network Error') > -1) {
          alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
        }
      })
    }

    ,fnView(idx) {
      this.requestBody.idx = idx
      this.$router.push({
        path: './detail',
        query: this.requestBody
      })
    }

    ,fnPage(n) {
      if (this.page !== n) {
        this.page = n
        console.log(this.page)
      }
      this.fnGetList()
    }
    ,fnReload(){
      location.reload()
    }
  }
}

</script>

<style>
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
}

.search-select, .search-input {
  padding: 0.8rem 1rem;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  font-size: 1rem;
  transition: all 0.3s ease;
}

.search-select {
  min-width: 120px;
}

.search-input {
  flex: 1;
}

.search-select:focus, .search-input:focus {
  border-color: #3498db;
  outline: none;
  box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.1);
}

.search-button {
  padding: 0.8rem 1.5rem;
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.search-button:hover {
  background-color: #2980b9;
}

.no-results {
  text-align: center;
  padding: 3rem;
  color: #7f8c8d;
}

.no-results i {
  font-size: 3rem;
  margin-bottom: 1rem;
}

.hospital-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 2rem;
  margin-bottom: 3rem;
}

.hospital-card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  cursor: pointer;
  overflow: hidden;
}

.hospital-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 15px rgba(0, 0, 0, 0.1);
}

.card-content {
  padding: 1.5rem;
}

.hospital-name {
  font-size: 1.5rem;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 1rem;
}

.hospital-info {
  color: #666;
}

.hospital-info p {
  margin: 0.5rem 0;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.hospital-info i {
  color: #3498db;
  width: 20px;
}

.rating {
  color: #f39c12;
  font-weight: 600;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 2rem;
}

.pagination {
  display: flex;
  gap: 0.5rem;
  list-style: none;
  padding: 0;
  margin: 0;
}

.page-item {
  margin: 0;
}

.page-link {
  padding: 0.5rem 1rem;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  color: #3498db;
  text-decoration: none;
  transition: all 0.3s ease;
}

.page-item.active .page-link {
  background-color: #3498db;
  color: white;
  border-color: #3498db;
}

.page-link:hover {
  background-color: #f8f9fa;
  border-color: #3498db;
}

@media (max-width: 768px) {
  .hospital-list-container {
    padding: 1rem;
  }

  .search-box {
    flex-direction: column;
  }

  .search-button {
    width: 100%;
    justify-content: center;
  }

  .hospital-grid {
    grid-template-columns: 1fr;
  }
}
</style>