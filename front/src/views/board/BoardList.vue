<template>
  <div class="board-container">
    <div class="board-header">
      <h1 class="board-title" @click="fnReload">
        <i class="fas fa-comments"></i>
        유용한 정보를 공유해주세요
      </h1>
      <button class="write-btn" v-on:click="fnWrite">
        <i class="fas fa-edit"></i> 글 작성
      </button>
    </div>

    <div class="search-container">
      <div class="search-box">
        <select class="search-select" v-model="search_key">
          <option value="title">제목</option>
          <option value="contents">내용</option>
        </select>
        <input 
          type="text" 
          maxlength="50"  
          placeholder="검색어를 입력하세요" 
          class="search-input" 
          v-model="search_value" 
          @keyup.enter="fnPage()"
        >
        <button @click="fnPage()" class="search-btn">
          <i class="fas fa-search"></i>
        </button>
      </div>
    </div>

    <div class="board-content">
      <div class="no-results" v-if="list.length==0">
        <i class="fas fa-search"></i>
        <h3>조회하신 글을 찾을 수 없습니다.</h3>
      </div>

      <div v-if="list.length != 0">
        <table class="board-table">
          <colgroup>
            <col style="width:8%">
            <col style="width:40%">
            <col style="width:15%">
            <col style="width:20%">
            <col style="width:10%">
          </colgroup>
          <thead>
            <tr>
              <th>순번</th>
              <th>제목</th>
              <th>등록자명</th>
              <th>등록일</th>
              <th>조회수</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(row, idx) in list" :key="row.idx" @click="fnView(row.idx, row.user_idx, row.user_id)" class="board-row">
              <td>{{ row.idx }}</td>
              <td class="title-cell">
                {{ row.title }}
                <span v-if="row.reply_count > 0" class="reply-count-badge">({{ row.reply_count }})</span>
              </td>
              <td>{{ row.user_id }}</td>
              <td>{{ row.created_at1 }}</td>
              <td>{{ row.view_count }}</td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="pagination-container" v-if="paging.total_list_cnt > 0">
        <nav class="pagination">
          <a class="page-link" @click="fnPage(1)">
            <i class="fas fa-angle-double-left"></i>
          </a>
          <a class="page-link" v-if="paging.start_page > 10" @click="fnPage(`${paging.start_page-1}`)">
            <i class="fas fa-angle-left"></i>
          </a>
          
          <a 
            v-for="(n,index) in paginavigation()"
            :key="index"
            class="page-link" 
            :class="{ active: paging.page === n }"
            @click="fnPage(`${n}`)"
          >
            {{ n }}
          </a>

          <a 
            class="page-link" 
            v-if="paging.total_page_cnt > paging.end_page"
            @click="fnPage(`${paging.end_page+1}`)"
          >
            <i class="fas fa-angle-right"></i>
          </a>
          <a class="page-link" @click="fnPage(`${paging.total_page_cnt}`)">
            <i class="fas fa-angle-double-right"></i>
          </a>
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

      this.$axios.get("/board/list", {
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
.board-container {
  max-width: 1100px;
  margin: 2rem auto;
  padding: 0 1rem;
}

.board-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.board-title {
  font-size: 2rem;
  font-weight: 700;
  color: #2c3e50;
  cursor: pointer;
  transition: color 0.3s ease;
}

.board-title:hover {
  color: #3498db;
}

.write-btn {
  background-color: #3498db;
  color: white;
  border: none;
  padding: 0.8rem 1.5rem;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.write-btn:hover {
  background-color: #2980b9;
}

.search-container {
  margin-bottom: 2rem;
}

.search-box {
  display: flex;
  gap: 0.5rem;
  max-width: 600px;
}
.search-select {
  padding: 0.6rem 1rem;
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: white;
  font-size: 1rem;
  min-width: 90px;
  width: 110px;
  height: 40px;
  box-sizing: border-box;
  display: flex;
  align-items: center;
}
.search-input {
  flex: 1 1 220px;
  min-width: 180px;
  padding: 0.6rem 1rem;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 1rem;
  height: 40px;
  margin-left: 0.2rem;
  margin-right: 0.2rem;
  box-sizing: border-box;
  display: flex;
  align-items: center;
}
.search-btn {
  padding: 0.6rem 1.1rem;
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  min-width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
}

.search-btn:hover {
  background-color: #2980b9;
}

.board-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
  background: white;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
}
.board-table th, .board-table td {
  border: 1px solid #e0e0e0;
  padding: 0.9rem 0.5rem;
  text-align: center;
  font-size: 1rem;
}
.board-table th {
  background: #f8f9fa;
  font-weight: 600;
  color: #333;
}
.board-table .title-cell {
  text-align: left;
  color: #2c3e50;
  font-weight: 500;
  cursor: pointer;
  transition: color 0.2s;
}
.board-table .title-cell:hover {
  color: #3498db;
  text-decoration: underline;
}
.board-row:hover {
  background: #f3f7fa;
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

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 2rem;
}

.pagination {
  display: flex;
  gap: 0.5rem;
}

.page-link {
  padding: 0.5rem 1rem;
  border: 1px solid #ddd;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.page-link:hover {
  background-color: #f8f9fa;
}

.page-link.active {
  background-color: #3498db;
  color: white;
  border-color: #3498db;
}

.reply-count-badge {
  color: #666;
  font-size: 0.85rem;
  margin-left: 4px;
}

@media (max-width: 768px) {
  .board-container {
    margin: 1rem auto;
  }
  .board-title {
    font-size: 1.5rem;
  }
  .search-box {
    flex-direction: column;
  }
  .board-table th, .board-table td {
    padding: 0.5rem 0.2rem;
    font-size: 0.95rem;
  }
}
</style>

