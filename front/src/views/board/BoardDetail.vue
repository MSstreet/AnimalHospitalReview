<template>
  <div class="board-detail-wrapper">
    <div class="container py-5">
      <div class="row justify-content-center">
        <div class="col-lg-10">
          <!-- 게시글 제목 -->
          <div class="text-center mb-5">
            <h1 class="display-4 fw-bold text-primary">게시글 조회</h1>
          </div>

          <!-- 게시글 내용 -->
          <div class="card shadow-sm mb-4">
            <div class="card-body">
              <!-- 게시글 헤더 -->
              <div class="d-flex justify-content-between align-items-center mb-4">
                <h2 class="card-title h3 mb-0">{{ title }}</h2>
                <div class="btn-group">
                  <button type="button" class="btn btn-outline-primary me-2" v-if="logged_idx == writer_idx" v-on:click="fnUpdate">
                    <i class="fas fa-edit me-1"></i>수정
                  </button>
                  <button type="button" class="btn btn-outline-danger me-2" v-if="logged_idx == writer_idx" v-on:click="fnDelete">
                    <i class="fas fa-trash me-1"></i>삭제
                  </button>
                  <button type="button" class="btn btn-outline-secondary" data-bs-target="#findPw" v-on:click="fnList">
                    <i class="fas fa-list me-1"></i>목록
                  </button>
                </div>
              </div>

              <!-- 게시글 메타 정보 -->
              <div class="d-flex align-items-center text-muted mb-4">
                <div class="me-3">
                  <i class="fas fa-user me-1"></i>작성자: {{ writer_id }}
                </div>
                <div>
                  <i class="fas fa-calendar me-1"></i>작성일: {{ created_at }}
                </div>
              </div>

              <!-- 게시글 본문 -->
              <div class="content-area p-4 bg-light rounded">
                <div v-html="contents" class="content-text"></div>
              </div>
            </div>
          </div>

          <!-- 댓글 작성 영역 -->
          <div class="card shadow-sm mb-4">
            <div class="card-body">
              <h5 class="card-title mb-3">댓글 작성</h5>
              <div class="form-group">
                <textarea v-model="coments" maxlength="500" class="form-control" rows="3" 
                          placeholder="댓글을 입력해주세요..."></textarea>
              </div>
              <div class="text-end mt-3">
                <button type="submit" class="btn btn-primary" @click="fnSave">
                  <i class="fas fa-paper-plane me-1"></i>댓글 작성
                </button>
              </div>
            </div>
          </div>

          <!-- 댓글 목록 -->
          <div v-if="list.length != 0" class="card shadow-sm">
            <div class="card-body">
              <h5 class="card-title mb-4">
                댓글 목록 <span class="badge bg-primary">{{paging.total_list_cnt}}</span>
              </h5>
              
              <div v-for="(row, idx) in list" :key="idx" class="comment-item mb-4">
                <div class="d-flex justify-content-between align-items-start">
                  <div>
                    <div class="fw-bold">{{row.user_id}}</div>
                    <div class="text-muted small">{{row.created_at}}</div>
                  </div>
                  <div class="btn-group">
                    <button v-if="logged_idx == row.user_idx" class="btn btn-sm btn-link text-primary" 
                            data-bs-toggle="modal" data-bs-target="#findPw" @click="testUpdate(`${row.reply_idx}`)">
                      <i class="fas fa-edit"></i>
                    </button>
                    <button v-if="logged_idx == row.user_idx" class="btn btn-sm btn-link text-danger" 
                            v-on:click="fnComentDelete(`${row.reply_idx}`)">
                      <i class="fas fa-trash"></i>
                    </button>
                    <button class="btn btn-sm btn-link text-success" 
                            data-bs-toggle="modal" data-bs-target="#findPw1" @click="testUpdate(`${row.reply_idx}`)">
                      <i class="fas fa-reply"></i>
                    </button>
                  </div>
                </div>
                <div class="mt-2 comment-content" v-html="row.contents"></div>

                <!-- 대댓글 -->
                <div v-for="(row1, idx1) in filteredReplies(row.reply_idx)" :key="idx1" 
                     class="reply-item ms-4 mt-3 p-3 bg-light rounded">
                  <div class="d-flex justify-content-between align-items-start">
                    <div>
                      <div class="fw-bold">{{row1.user_id}}</div>
                      <div class="text-muted small">{{row1.created_at}}</div>
                    </div>
                    <div class="dropdown">
                      <button class="btn btn-sm btn-link" type="button" data-bs-toggle="dropdown">
                        <i class="fas fa-ellipsis-v"></i>
                      </button>
                      <ul class="dropdown-menu dropdown-menu-end">
                        <li>
                          <a class="dropdown-item" data-bs-toggle="modal" data-bs-target="#findPw" 
                             @click="testUpdate(`${row1.reply_idx}`)">
                            <i class="fas fa-edit me-2"></i>수정
                          </a>
                        </li>
                        <li>
                          <a class="dropdown-item text-danger" v-on:click="fnComentDelete(`${row1.reply_idx}`)">
                            <i class="fas fa-trash me-2"></i>삭제
                          </a>
                        </li>
                      </ul>
                    </div>
                  </div>
                  <div class="mt-2" v-html="row1.contents"></div>
                </div>
              </div>

              <!-- 페이지네이션 -->
              <nav v-if="paging.total_list_cnt > 0" class="mt-4">
                <ul class="pagination justify-content-center">
                  <li class="page-item">
                    <a class="page-link" href="javascript:;" @click="fnPage(1)">
                      <i class="fas fa-angle-double-left"></i>
                    </a>
                  </li>
                  <li class="page-item">
                    <a class="page-link" href="javascript:;" v-if="paging.start_page > 10" 
                       @click="fnPage(`${paging.start_page-1}`)">
                      <i class="fas fa-angle-left"></i>
                    </a>
                  </li>
                  <li v-for="n in paginavigation()" :key="n" :class="['page-item', { active: paging.page == n }]">
                    <a class="page-link" href="javascript:;" @click="fnPage(`${n}`)">{{ n }}</a>
                  </li>
                  <li class="page-item">
                    <a class="page-link" href="javascript:;" v-if="paging.total_page_cnt > paging.end_page"
                       @click="fnPage(`${paging.end_page+1}`)">
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
        </div>
      </div>
    </div>

    <!-- 댓글 수정 모달 -->
    <div id="findPw" class="modal fade" ref="myModal" tabindex="-1" role="dialog">
      <div class="modal-dialog modal-dialog-centered modal-login">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">댓글 수정</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body">
            <div class="container my-auto">
              <div class="row">
                <div class="card z-index-0 fadeIn3 fadeInBottom">
                  <div class="card-header p-0 position-relative mt-n4 mx-3 z-index-2">
                    <div class="bg-gradient-primary shadow-primary border-radius-lg py-3 pe-1">
                      <h4 class="text-black font-weight-bolder text-center mt-2 mb-0">댓글 수정</h4>
                    </div>
                  </div>

                  <div class="card-body text-center">
                    <div class="mb-2 form-group">
                      <label class="mb-1 fw-semibold" for="comment">Comment</label>
                      <textarea v-model="update_coments" class="form-control" id="comment" rows="3"></textarea>
                    </div>

                    <div>
                      <input type="hidden" v-model="m_reply_idx">
                    </div>

                    <div class="row text-center test-position">
                      <button type="button" class="me-1 text-center col-5 btn btn-secondary bg-gradient-primary my-4 mb-2" id="m_reply_idx"
                              data-bs-dismiss="modal" @click="fnComentUpdate()">확인</button>
                      <button type="button" class=" col-5 btn btn-secondary bg-gradient-primary my-4 mb-2" data-bs-dismiss="modal">닫기</button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 대댓글 작성 모달 -->
    <div id="findPw1" class="modal fade" ref="myModal" tabindex="-1" role="dialog">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow">
          <div class="modal-header border-0 pb-0">
            <h5 class="modal-title fw-bold text-primary">
              <i class="fas fa-reply me-2"></i>대댓글 작성
            </h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body">
            <div class="form-group">
              <div class="input-group">
                <span class="input-group-text bg-light border-end-0">
                  <i class="fas fa-comment text-primary"></i>
                </span>
                <textarea 
                  v-model="coments" 
                  class="form-control border-start-0 ps-0" 
                  rows="4"
                  placeholder="대댓글을 입력해주세요..."
                  style="resize: none;"
                ></textarea>
              </div>
              <div class="d-flex justify-content-between align-items-center mt-2">
                <small class="text-muted">
                  <i class="fas fa-info-circle me-1"></i>최대 500자까지 입력 가능합니다
                </small>
                <small class="text-muted">{{coments.length}}/500</small>
              </div>
            </div>

            <input type="hidden" v-model="m_reply_idx">

            <div class="d-flex gap-2 justify-content-end mt-4">
              <button type="button" 
                      class="btn btn-light px-4" 
                      data-bs-dismiss="modal">
                <i class="fas fa-times me-1"></i>취소
              </button>
              <button type="button" 
                      class="btn btn-primary px-4" 
                      @click="fnSave()">
                <i class="fas fa-paper-plane me-1"></i>작성
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() { //변수생성
    return {
      showReply:false,

      requestBody: this.$route.query,
      idx: this.$route.query.idx,
      logged_idx: this.$store.state.userIdx,
      writer_idx:this.$route.query.userIdx,
      writer_id:this.$route.query.userId,

      title: '',
      author: '',
      contents: '',
      created_at: '',
      /////replyData
      coments:'',
      coments1:'',
      c_created_at:'',
      user_name:'',
      update_coments:'',
      reply_idx:'',

      m_reply_idx:'',

      list: {}, //리스트 데이터
      list1: {},
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
      page: 1,
      size: this.$route.query.size ? this.$route.query.size : 10,
      search_key: this.$route.query.sk ? this.$route.query.sk : '',
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
    this.fnGetView()
    this.fnGetComent()
    this.fnGetComent1()
    console.log("글 번호>> : " + this.idx)
    console.log(this.logged_idx)
    console.log(this.writer_id)
  }
  ,methods: {
    fnGetView() {
      this.$axios.get(this.$serverUrl + '/board/' + this.idx, {
        params: this.requestBody
      }).then((res) => {
        this.title = res.data.title
        this.author = res.data.author
        this.contents = res.data.contents.replace(/\n/g, '<br/>')
        this.created_at = res.data.created_at
      }).catch((err) => {
        if (err.message.indexOf('Network Error') > -1) {
          alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
        }
      })
    }
    ,fnList() {
      delete this.requestBody.idx

      this.$router.push({
        path: './list',
        query: this.requestBody
      })
    }
    ,fnUpdate() {
      this.requestBody.idx = this.idx
      this.$router.push({
        path: './update',
        query: this.requestBody
      })
    }
    ,fnDelete() {
      if (!confirm("삭제하시겠습니까?")) return

      this.$axios.delete(this.$serverUrl + '/board/' + this.idx, {})
          .then(() => {
            alert('삭제되었습니다.')
            this.fnList();
          }).catch((err) => {
        console.log(err);
      })
    }
    ,fnSave(){
      const input = document.getElementById("m_reply_idx").value
      this.reply_idx = input

      if(this.coments == ''){
        alert("댓글을 입력해주세요")
        return false
      }

      let apiUrl = this.$serverUrl + '/reply/join'
      this.form = {
        "parent" : this.reply_idx,
        "board_idx": this.idx,
        "user_idx": this.logged_idx,
        "contents": this.coments.replace(/\n/g, '<br/>')
      }
        //INSERT
        this.$axios.post(apiUrl, this.form)
            .then((res) => {
              alert('댓글이 저장되었습니다.')
              location.reload()
            }).catch((err) => {
          if (err.message.indexOf('Network Error') > -1) {
            alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
          }
        })

    }
    ,fnGetComent(){
      this.requestBody = { // 데이터 전송
        page: this.page,
        size: this.size
      }

      this.$axios.get(this.$serverUrl + '/reply/list/' + this.idx, {
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
    ,fnGetComent1(){
      this.requestBody = { // 데이터 전송
        page: this.page,
        size: this.size
      }
      this.$axios.get(this.$serverUrl + '/reply/list1/' + this.idx, {
        params: this.requestBody,
        headers: {}
      }).then((res) => {
        if (res.data.result_code === "OK") {
          this.list1 = res.data.data
        }
        console.log(res.data.data)
      }).catch((err) => {
        if (err.message.indexOf('Network Error') > -1) {
          alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
        }
      })
    }
    ,fnPage(n) {
      if (this.page !== n) {
        this.page = n
      }
      this.fnGetComent()
    }
    ,fnComentUpdate(){
      const input = document.getElementById("m_reply_idx").value

      this.reply_idx = input

      let apiUrl = this.$serverUrl + '/reply'
      this.form = {
        "reply_idx":this.reply_idx,
        "board_idx": this.idx,
        "user_idx": this.logged_idx,
        "contents": this.update_coments
      }
      //INSERT
      this.$axios.patch(apiUrl, this.form)
          .then((res) => {
            alert('댓글이 저장되었습니다.')
            this.fnGetComent()
            this.fnGetComent1()
            this.update_coments = ''
          }).catch((err) => {
        if (err.message.indexOf('Network Error') > -1) {
          alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
        }
      })
    }
    ,fnComentDelete(idx){
      console.log(idx)
      if (!confirm("삭제하시겠습니까?")) return
      this.$axios.delete(this.$serverUrl + '/reply/' + idx, {})
          .then(() => {
            alert('삭제되었습니다.')
            this.fnGetComent()
            location.reload()
          }).catch((err) => {
        console.log(err);
      })
    }
    ,testUpdate(idx){
      document.getElementById("m_reply_idx").value = idx
    }
  },
  computed: {
    filteredReplies() {
      return (parentId) => {
        return this.list1.filter(reply => reply.parent === parentId);
      }
    }
  }
}
</script>

<style scoped>
.test-position{
  display: flex;
  justify-content: center;
  align-items: center;
}
li{
  list-style: none;
}
.container{
  display: flex;
  flex-direction: column;
}
.child:hover + test{
  display: block;
}

.content-area {
  min-height: 200px;
  border: 1px solid #e9ecef;
}

.comment-item {
  border-bottom: 1px solid #e9ecef;
  padding-bottom: 1rem;
}

.comment-item:last-child {
  border-bottom: none;
}

.reply-item {
  border-left: 3px solid #0d6efd;
}

.btn-link {
  text-decoration: none;
  padding: 0.25rem 0.5rem;
}

.btn-link:hover {
  opacity: 0.8;
}

.dropdown-item {
  cursor: pointer;
}

.dropdown-item:hover {
  background-color: #f8f9fa;
}

.pagination .page-link {
  color: #0d6efd;
}

.pagination .page-item.active .page-link {
  background-color: #0d6efd;
  border-color: #0d6efd;
}

@media (max-width: 768px) {
  .container {
    padding: 1rem;
  }
  
  .btn-group {
    flex-wrap: wrap;
  }
  
  .btn-group .btn {
    margin: 0.25rem;
  }
}
</style>