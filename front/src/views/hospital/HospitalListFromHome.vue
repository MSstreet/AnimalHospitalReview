<template>
  <div class="hospital-list-container">
    <div class="container text-center mb-5">
      <div>
        <h1 class="mt-3 fs-1 fw-bold" style="text-align: center;"><i class="fa-solid fa-hippo"></i>Animal Hospital List<i class="fa-solid fa-otter"></i></h1>
      </div>

      <div class="card-header mt-5">
        <div class="input-group input-group-sm search-pos">
          <select v-model="search_key">
            <option value="author">병원명</option>
            <option value="title">지역명</option>
          </select>
          <input style="border-width: 1px;" type="text" maxlength="50" placeholder="검색어 입력" aria-label="search"
                 aria-describedby="button-addon2" class="ms-1" v-model="search_value" @keyup.enter="fnPage()">
          <button @click="fnPage()" class="btn btn-success ms-1" id="button-addon2">검색</button>
        </div>
      </div>

      <div class="container px-4 test-class" v-for="(row, idx) in list" :key="idx">
        <div class="row mt-5">
          <div class="col-12">
            <div class="card mb-3" style="max-width: 450px;">
              <div class="row g-0">
                <div class="col-md-12">
                  <div class="card-body">
                    <h5 class="card-title"><a v-on:click="fnView(`${row.hospital_id}`)">{{ row.hospital_name}}</a></h5>
                    <p class="card-text mb-1" v-if="row.hospital_num">{{ row.hospital_num }}</p>
                    <p class="card-text mb-1" v-if="row.hospital_sigun_name">{{ row.hospital_sigun_name }}</p>
                    <p class="card-text mb-1" v-if="row.hospital_addr">{{ row.hospital_addr }}</p>
                    <p class="card-text mb-1" v-if="row.hospital_score"><i class="fa-solid fa-star"></i> {{ row.hospital_score }}</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="test-position">
      <div>
        <nav aria-label="Page navigation example" v-if="paging.total_list_cnt > 0">
          <span class="center">
            <ul class="pagination">
              <li class="page-item"><a class="page-link" href="javascript:;" @click="fnPage(1)">처음</a></li>
              <a href="javascript:;" class="page-link" @click="fnPage(`${paging.start_page-1}`)">이전</a>

              <li v-for="(n,index) in paginavigation()" :key="index" class="page-item" :class="{ active: paging.page === n }">
                <a class="page-link" href="javascript:;" @click="fnPage(`${n}`)">{{ n }}</a>
              </li>

              <a href="javascript:;" class="page-link" v-if="paging.total_page_cnt > paging.end_page"
                 @click="fnPage(`${paging.end_page+1}`)">다음</a>
              <li class="page-item"><a class="page-link" href="javascript:;" @click="fnPage(`${paging.total_page_cnt}`)">마지막</a></li>
            </ul>
          </span>
        </nav>
      </div>
    </div>
  </div>
</template>

<script>

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

      search_key: this.$route.query.sk ? this.$route.query.sk : '',
      //search_key: '지역명',
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
  }
  ,methods: {
    fnGetList() {
      //console.log(this.search_key)
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

        console.log(res.data.data);
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
      }
      this.fnGetList()
    }
  }
}

</script>

<style>
ul {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  justify-content: center;
  gap: 0.5rem;
}

li {
  margin: 0;
  padding: 0;
  border: 0;
}

.test-class {
  display: inline-block;
  width: 35rem !important;
  margin: 0 auto;
}

.search-pos {
  max-width: 500px;
  margin: 0 auto;
}

.test-position {
  margin-top: 2rem;
  display: flex;
  justify-content: center;
}
</style>