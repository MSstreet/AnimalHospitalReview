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

      <div class="board-list" v-if="list.length != 0">
        <div class="board-item" v-for="(row, idx) in list" :key="idx" @click="fnView(`${row.idx}`,`${row.user_idx}`,`${row.user_id}`)">
          <div class="board-item-header">
            <span class="board-number">#{{ row.idx }}</span>
            <span class="board-date">{{row.created_at1}}</span>
          </div>
          <h3 class="board-item-title">{{ row.title }}</h3>
          <div class="board-item-footer">
            <span class="board-author">
              <i class="fas fa-user"></i> {{ row.user_id }}
            </span>
            <span class="board-replies">
              <i class="fas fa-comment"></i> {{row.reply_count}}
            </span>
          </div>
        </div>
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
  max-width: 1200px;
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
  padding: 0.8rem;
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: white;
}

.search-input {
  flex: 1;
  padding: 0.8rem;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 1rem;
}

.search-btn {
  padding: 0.8rem 1.5rem;
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.search-btn:hover {
  background-color: #2980b9;
}

.board-list {
  display: grid;
  gap: 1rem;
}

.board-item {
  background-color: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  cursor: pointer;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.board-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0,0,0,0.15);
}

.board-item-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 0.5rem;
  color: #7f8c8d;
  font-size: 0.9rem;
}

.board-item-title {
  font-size: 1.2rem;
  font-weight: 600;
  color: #2c3e50;
  margin: 0.5rem 0;
}

.board-item-footer {
  display: flex;
  gap: 1rem;
  color: #7f8c8d;
  font-size: 0.9rem;
}

.board-author, .board-replies {
  display: flex;
  align-items: center;
  gap: 0.3rem;
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

  .board-item {
    padding: 1rem;
  }
}
</style>

