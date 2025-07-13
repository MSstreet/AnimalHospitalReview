<template>

  <div class="container text-center mt-5 mb-5">
    <h3 class="fs-1 fw-bold text-center mb-5"> 나의 찜 목록</h3>
      <template v-if="list.length > 0">
        <div class="container px-4 test-class" v-for="(row, idx) in list" :key="idx">
          <div class="row mt-5">
            <div class="col-12">
              <div class="card mb-3" style="max-width: 450px;">
                <div class="row g-0">
                  <div class="col-md-12">
                    <div class="card-body">
                      <h5 class="card-title"><a v-on:click="fnView(`${row.pet_hospital_num}`)">{{ row.hospital_name}}</a></h5>
                      <p class="card-text mb-1">{{ row.hospital_num }}</p>
                      <p class="card-text mb-1">{{ row.hospital_sigun_name }}</p>
                      <p class="card-text mb-1">{{ row.hospital_addr }}</p>
                      <p class="card-text mb-1"><i class="fa-solid fa-star"></i>&nbsp;{{ row.hospital_score }}</p>
                      <button type="button" class="mt-0 mb-1 btn btn-success" @click="fnDeleteWish(`${row.wish_id}`)">찜삭제</button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </template>
    <div v-else>
      <div class="empty-wish-container">
        <h3 class="empty-wish-title">찜 목록이 아직 없습니다.</h3>
        <p class="empty-wish-desc">마음에 드는 동물병원을 <span style="color:#ff5a5f;">찜</span>해보세요!</p>
        <button class="btn btn-outline-primary mt-3" @click="$router.push('/hospital/list')">동물병원 보러가기</button>
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
export default {
  data() {
    return {
      requestBody: {},
      list: {},
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
      },
      page: this.$route.query.page ? this.$route.query.page : 1,
      size: this.$route.query.size ? this.$route.query.size : 10,
      search_key: this.$route.query.sk ? this.$route.query.sk : '',
      search_value: this.$route.query.sv ? this.$route.query.sv : '',
      paginavigation: function () {
        let pageNumber = [];
        let start_page = this.paging.start_page;
        let end_page = this.paging.end_page;
        for (let i = start_page; i <= end_page; i++) pageNumber.push(i);
        return pageNumber;
      }
    }
  },
  mounted() {
    this.fnGetList();
  },
  methods: {
    handleTouchStart(event) {
      event.currentTarget.style.transform = 'scale(0.98)';
    },
    handleTouchEnd(event) {
      event.currentTarget.style.transform = 'scale(1)';
    },
    fnDeleteWish(wish_idx) {
      if (!confirm("삭제하시겠습니까?")) return;
      this.$axios.get("/wish/change/" + wish_idx).then((res) => {
        this.wish_state = res.data.wish_state1;
        this.fnGetList();
      }).catch((err) => {
        if (err.message.indexOf('Network Error') > -1) {
          alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.');
        }
      });
    },
    fnGetList() {
      this.requestBody = {
        sk: this.search_key,
        sv: this.search_value,
        page: this.page,
        size: this.size
      };
      this.$axios.get("/wish/list/" + this.$store.state.userIdx, {
        params: this.requestBody,
        headers: {}
      }).then((res) => {
        if (res.data.result_code === "OK") {
          this.list = res.data.data;
          this.paging = res.data.pagination;
          this.no = this.paging.total_list_cnt - ((this.paging.page - 1) * this.paging.page_size);
        }
      }).catch((err) => {
        if (err.message.indexOf('Network Error') > -1) {
          alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.');
        }
      });
    },
    fnView(idx) {
      this.requestBody.idx = idx;
      this.$router.push({
        path: '/hospital/detail',
        query: this.requestBody
      });
    },
    fnPage(n) {
      if (this.page !== n) {
        this.page = n;
      }
      this.fnGetList();
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
.test-class {
  display: inline-block;
  width: 35rem !important;;
}
.empty-wish-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 4rem;
  margin-bottom: 4rem;
}
.empty-wish-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: #444;
  margin-bottom: 0.5rem;
}
.empty-wish-desc {
  font-size: 1.1rem;
  color: #888;
}
@media (max-width: 768px) {
  .hospital-list-container {
    padding: 1rem;
  }
  .hospital-grid {
    grid-template-columns: 1fr;
    gap: 1rem;
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
</style>