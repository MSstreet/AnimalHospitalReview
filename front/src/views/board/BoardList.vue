<template>

  <div class="container mt-5 mb-5">
    <h1  class="tt mt-1 mb-2 fs-1 fw-bold" @click="fnReload">유용한 정보를 공유해주세요</h1>
    <div class="input-area mb-5">
      <div class="" >
        <select class="styled-select" v-model="search_key">
          <option value="title">제목</option>
          <option value="contents">내용</option>
        </select>
        <input style="border-width: 1px;" type="text" maxlength="50"  placeholder="검색어 입력" aria-label="search"
               aria-describedby="button-addon2" class="styled-input ms-1" v-model="search_value" @keyup.enter="fnPage()">
        <button @click="fnPage()" class="styled-button ms-1" id="button-addon2">검색</button>
      </div>
    </div>

    <div class="card mb-4 text-center container">
      <div>
        <a class="btn btn-primary float-end me-2 mt-2" v-on:click="fnWrite"><i class="fas fa-edit"></i>  글 작성</a>
      </div>

      <div class="card-body">
        <div class="mb-3 test-position" v-if="list.length==0">
          <h3>조회하신 글을 찾을 수 없습니다.</h3>
        </div>

        <table v-if="list.length != 0" class="table table-hover table-striped">
          <thead>
          <tr>
            <th>글번호</th>
            <th>제목</th>
            <th>작성자</th>
            <th>작성일</th>

          </tr>
          </thead>
          <tbody>

          <tr class="t1" v-for="(row, idx) in list" :key="idx">

            <td width="10%">{{ row.idx }}</td>
            <td class="t"><a v-on:click="fnView(`${row.idx}`,`${row.user_idx}`,`${row.user_id}`)">{{ row.title }}&nbsp({{row.reply_count}})</a></td>

            <td>{{ row.user_id }}</td>
            <td width="20%">{{row.created_at1}}</td>

          </tr>
          </tbody>
        </table>

      </div>

      <div class="test-position">
        <nav aria-label="Page navigation example" v-if="paging.total_list_cnt> 0">
    <span class="center">
      <ul class="pagination">

        <li class="page-item"><a class="page-link" href="javascript:;" @click="fnPage(1)">&lt;&lt;</a></li>

         <a href="javascript:;" v-if="paging.start_page > 10" @click="fnPage(`${paging.start_page-1}`)">&lt;</a>

        <template v-for=" (n,index) in paginavigation()">
            <template v-if="paging.page==n">
               <div v-if="n == 2">
               </div>
              <li class="page-item active" :key="index"> <a class="page-link"> {{ n }}</a> </li>
            </template>

            <template v-else>
               <li class="page-item"> <a class="page-link" href="javascript:;" @click="fnPage(`${n}`)" :key="index"> {{ n }} </a> </li>
            </template>
        </template>

         <a href="javascript:;" v-if="paging.total_page_cnt > paging.end_page"

            @click="fnPage(`${paging.end_page+1}`)">&gt;</a>

        <li class="page-item"><a class="page-link" href="javascript:;" @click="fnPage(`${paging.total_page_cnt}`)">&gt;&gt;</a></li>
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

      search_key:"title",
      search_value: this.$route.query.sv ? this.$route.query.sv : '',

      paginavigation: function () { //페이징 처리 for문 커스텀
        let pageNumber = [] //;
        let start_page = this.paging.start_page;
        let end_page = this.paging.end_page;
        for (let i = start_page; i <= end_page; i++) pageNumber.push(i);
        return pageNumber;
      },
      isOpen: false,
    }
  }
  ,filters: {
    formatDate(value) {
      const date = new Date(value);
      const year = date.getFullYear();
      let month = date.getMonth() + 1;
      month = month > 9 ? month : `0${month}`;
      const day = date.getDate();
      let hours = date.getHours();
      hours = hours > 9 ? hours : `0${hours}`;
      let minutes = date.getMinutes();
      minutes = minutes > 9 ? minutes : `0${minutes}`;
      return `${year}-${month}-${day} ${hours}:${minutes}`;
    },
  }
  ,mounted() {
    this.fnGetList();

  },methods: {
    fnGetList() {
      this.requestBody = { // 데이터 전송
        sk: this.search_key,
        sv: this.search_value,
        page: this.page,
        size: this.size
      }

      this.$axios.get(this.$serverUrl + "/board/list", {
        params: this.requestBody,
        headers: {}
      }).then((res) => {

        if (res.data.result_code === "OK") {
          this.list = res.data.data
          this.paging = res.data.pagination
          this.no = this.paging.total_list_cnt - ((this.paging.page - 1) * this.paging.page_size)
        }

      }).catch((err) => {
        if (err.message.indexOf('Network Error') > -1) {
          alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
        }
      })
    }
    ,fnView(idx,user_idx,user_id) {
      this.requestBody.idx = idx
      this.requestBody.userIdx = user_idx
      this.requestBody.userId = user_id
      this.$router.push({
        path: './detail',
        query: this.requestBody
      })
    }
    ,fnReload(){
      location.reload();
    }
    ,fnWrite() {
      this.$router.push({
        path: './write'
      })
    },
    fnPage(n) {
      if (this.page !== n) {
        this.page = n
      }
      this.fnGetList()
    }
  }
}
</script>

<style>
.test-position{
  display: flex;
  justify-content: center;
  align-items: center;
}

.t{
  text-color: none;
}

.t:hover{
  text-decoration: underline;
  text-decoration-thickness: 1px;
  text-decoration-color: black;
}

.t1:hover{
  background-color: beige;
}

.tt:hover{
  cursor: pointer
}

.input-area{
  display: flex;
  justify-content: flex-start;
  margin-bottom: 10px;
}
</style>

