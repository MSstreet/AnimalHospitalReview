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
                  <div class="comment-action-group">
                    <button v-if="logged_idx == row.user_idx" class="comment-action-btn edit" data-bs-toggle="modal" data-bs-target="#findPw" @click="testUpdate(`${row.reply_idx}`)">
                      <i class="fas fa-edit"></i>
                    </button>
                    <button v-if="logged_idx == row.user_idx" class="comment-action-btn delete" v-on:click="fnComentDelete(`${row.reply_idx}`)">
                      <i class="fas fa-trash"></i>
                    </button>
                    <button class="comment-action-btn reply" data-bs-toggle="modal" data-bs-target="#findPw1" @click="testUpdate(`${row.reply_idx}`)">
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
                          <a class="dropdown-item" data-bs-toggle="modal" data-bs-target="#findPw" @click="testUpdate(`${row1.reply_idx}`)">
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

          <!-- 댓글 작성 영역 -->
          <div class="card shadow-sm mb-4">
            <div class="card-body comment-write-area">
              <div class="textarea-row">
                <textarea v-model="coments" maxlength="500" class="form-control" rows="3" placeholder="내용을 입력하세요."></textarea>
                <button type="submit" class="btn btn-primary" @click="fnSave">
                  등록
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 댓글 수정 모달 -->
    <div id="findPw" class="modal fade" ref="myModal" tabindex="-1" role="dialog">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content modal-edit-comment">
          <div class="modal-header border-0 pb-0">
            <h5 class="modal-title modal-edit-title">댓글 수정</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body pt-2 pb-4 px-4">
            <textarea v-model="update_coments" class="modal-edit-textarea" rows="4" placeholder="댓글을 입력하세요."></textarea>
            <div class="modal-edit-btn-group">
              <button type="button" class="modal-edit-btn confirm" data-bs-dismiss="modal" @click="fnComentUpdate()">확인</button>
              <button type="button" class="modal-edit-btn cancel" data-bs-dismiss="modal">닫기</button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 대댓글 작성 모달 -->
    <div id="findPw1" class="modal fade" ref="myModal" tabindex="-1" role="dialog">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content modal-edit-comment">
          <div class="modal-header border-0 pb-0">
            <h5 class="modal-title modal-edit-title">대댓글 작성</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body pt-2 pb-4 px-4">
            <textarea v-model="coments" class="modal-edit-textarea" rows="4" placeholder="대댓글을 입력하세요."></textarea>
            <div class="d-flex justify-content-between align-items-center mt-2 mb-3">
              <small class="text-muted">
                <i class="fas fa-info-circle me-1"></i>최대 500자까지 입력 가능합니다
              </small>
              <small class="text-muted">{{coments.length}}/500</small>
            </div>
            <div class="modal-edit-btn-group">
              <button type="button" class="modal-edit-btn cancel" data-bs-dismiss="modal">취소</button>
              <button type="button" class="modal-edit-btn confirm" @click="fnSave()" data-bs-dismiss="modal">작성</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import CustomModal from '@/components/CustomModal.vue';
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
      user_pw:'',

      m_reply_idx:'',

      list: {}, //리스트 데이터
      list1: [],
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
      },
      showEditModal: false,
      showReplyModal: false,
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
              this.$axios.get('/board/' + this.idx, {
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

              this.$axios.delete('/board/' + this.idx, {})
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

      let apiUrl = '/reply/join'
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

              this.$axios.get('/reply/list/' + this.idx, {
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
              this.$axios.get('/reply/list1/' + this.idx, {
        params: this.requestBody,
        headers: {}
      }).then((res) => {
        if (res.data.result_code === "OK") {
          this.list1 = Array.isArray(res.data.data) ? res.data.data : []
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

      let apiUrl = '/reply'
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
              this.$axios.delete('/reply/' + idx, {})
          .then(() => {
            alert('삭제되었습니다.')
            this.fnGetComent()
            location.reload()
          }).catch((err) => {
        console.log(err);
      })
    }
    ,testUpdate(idx){
      this.m_reply_idx = idx
      this.showEditModal = true
    },
    openReplyModal(idx) {
      this.m_reply_idx = idx;
      this.showReplyModal = true;
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

<style>
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

.btn-group .btn {
  padding: 0.4rem 1rem;
  font-size: 0.98rem;
  min-width: 72px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  gap: 0.35em;
  font-weight: 500;
  box-shadow: 0 1px 4px rgba(0,0,0,0.04);
  transition: background 0.2s, color 0.2s, box-shadow 0.2s;
  flex-direction: row;
  white-space: nowrap;
}
.btn-group .btn i {
  font-size: 1.05em;
  margin-right: 0.3em;
}
.btn-group .btn-outline-primary {
  border: 1.5px solid #3498db;
  color: #3498db;
  background: #f8fbff;
}
.btn-group .btn-outline-primary:hover {
  background: #3498db;
  color: #fff;
}
.btn-group .btn-outline-danger {
  border: 1.5px solid #e74c3c;
  color: #e74c3c;
  background: #fff8f8;
}
.btn-group .btn-outline-danger:hover {
  background: #e74c3c;
  color: #fff;
}
.btn-group .btn-outline-secondary {
  border: 1.5px solid #b2bec3;
  color: #636e72;
  background: #f9f9f9;
}
.btn-group .btn-outline-secondary:hover {
  background: #636e72;
  color: #fff;
}

.card-title.mb-3 {
  font-size: 1.25rem;
  font-weight: 600;
  color: #222;
  margin-bottom: 1.2rem;
}
.form-group {
  margin-bottom: 1.2rem;
}
.form-group textarea.form-control {
  width: 100%;
  min-height: 100px;
  max-width: 600px;
  margin: 0 auto;
  border-radius: 10px;
  border: 1.5px solid #e0e0e0;
  font-size: 1.08rem;
  padding: 1.1rem 1rem;
  box-shadow: 0 1px 4px rgba(0,0,0,0.03);
  transition: border 0.2s, box-shadow 0.2s;
}
.form-group textarea.form-control:focus {
  border: 1.5px solid #3498db;
  box-shadow: 0 2px 8px rgba(52,152,219,0.08);
}
.text-end.mt-3 {
  display: flex;
  justify-content: center;
  margin-top: 1.5rem;
}
.btn.btn-primary {
  background: #2196f3;
  border: none;
  border-radius: 10px;
  font-size: 1.08rem;
  font-weight: 500;
  padding: 0.7rem 2rem;
  display: flex;
  align-items: center;
  gap: 0.5em;
  box-shadow: 0 2px 8px rgba(33,150,243,0.08);
  transition: background 0.2s, box-shadow 0.2s;
}
.btn.btn-primary:hover {
  background: #1769aa;
  box-shadow: 0 4px 16px rgba(33,150,243,0.13);
}

/* 심플한 댓글 작성 영역 스타일 */
.comment-write-area {
  display: flex;
  flex-direction: column;
  /* align-items: flex-end; 제거 */
  position: relative;
  background: #fff;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 1.2rem 1.5rem 4rem 1.5rem;
  box-shadow: none;
  gap: 1rem;
}
.comment-write-area .textarea-row {
  width: 100%;
  margin-bottom: 0.7rem;
  position: relative;
  min-height: 120px; /* textarea 높이(100px) + 버튼 높이(34px) */
}
.comment-write-area textarea.form-control {
  width: 100%;
  min-height: 100px;
  border-radius: 6px;
  border: 1px solid #d1d5db;
  font-size: 1.08rem;
  padding: 1.1rem 1rem;
  box-shadow: none;
  transition: border 0.2s;
  resize: none;
  background: #fff;
}
.comment-write-area textarea.form-control:focus {
  border: 1.5px solid #2196f3;
  outline: none;
}
.comment-write-area .btn.btn-primary {
  position: absolute;
  right: 1.5rem;
  top: 100%;
  margin-top: 5px;
  width: 80px;
  height: 34px;
  border-radius: 17px;
  font-size: 0.98rem;
  font-weight: 500;
  padding: 0;
  background: #6bb9df;
  color: #fff;
  border: none;
  box-shadow: none;
  transition: background 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.4em;
}
.comment-write-area .btn.btn-primary:hover {
  background: #3498db;
}

/* 댓글 옆 액션 버튼 이쁘게 */
.comment-action-group {
  display: flex;
  gap: 0.5rem;
}
.comment-action-btn {
  width: 38px;
  height: 38px;
  border-radius: 50%;
  background: #f4f7fd;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background 0.18s, box-shadow 0.18s;
  box-shadow: 0 1px 4px rgba(0,0,0,0.04);
  font-size: 1.15rem;
  cursor: pointer;
  padding: 0;
}
.comment-action-btn.edit { color: #3498db; }
.comment-action-btn.edit:hover { background: #eaf4fb; color: #217dbb; }
.comment-action-btn.delete { color: #e74c3c; }
.comment-action-btn.delete:hover { background: #fdeaea; color: #c0392b; }
.comment-action-btn.reply { color: #27ae60; }
.comment-action-btn.reply:hover { background: #eafaf1; color: #219150; }
.comment-action-btn i { font-size: 1.2em; }

@media (max-width: 600px) {
  .comment-write-area {
    padding: 1rem 0.5rem 0 0.5rem;
  }
  .comment-write-area .textarea-row {
    min-height: unset;
  }
  .comment-write-area .btn.btn-primary {
    position: static;
    width: 100%;
    min-width: 0;
    font-size: 0.96rem;
    height: 36px;
    border-radius: 18px;
    margin-top: 0.7rem;
  }
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

.modal-edit-comment {
  border-radius: 18px;
  box-shadow: 0 8px 32px rgba(44,62,80,0.12);
  border: none;
  background: #fff;
}
.modal-edit-title {
  font-size: 1.25rem;
  font-weight: 700;
  color: #222;
  margin: 0 auto 0.5rem auto;
  text-align: center;
  letter-spacing: -0.5px;
}
.modal-edit-textarea {
  width: 100%;
  min-height: 90px;
  border-radius: 12px;
  border: 1.5px solid #e0e0e0;
  font-size: 1.08rem;
  padding: 1.1rem 1rem;
  margin-bottom: 1.5rem;
  box-shadow: 0 1px 4px rgba(0,0,0,0.03);
  transition: border 0.2s, box-shadow 0.2s;
  resize: none;
  background: #fafbfc;
}
.modal-edit-textarea:focus {
  border: 1.5px solid #3498db;
  box-shadow: 0 2px 8px rgba(52,152,219,0.08);
  outline: none;
}
.modal-edit-btn-group {
  display: flex;
  gap: 1rem;
  justify-content: center;
}
.modal-edit-btn {
  min-width: 120px;
  padding: 0.7rem 0;
  border-radius: 22px;
  font-size: 1.08rem;
  font-weight: 600;
  border: none;
  transition: background 0.18s, color 0.18s;
  cursor: pointer;
  box-shadow: 0 1px 4px rgba(0,0,0,0.04);
}
.modal-edit-btn.confirm {
  background: #3498db;
  color: #fff;
}
.modal-edit-btn.confirm:hover {
  background: #217dbb;
}
.modal-edit-btn.cancel {
  background: #f4f7fd;
  color: #333;
}
.modal-edit-btn.cancel:hover {
  background: #e0e0e0;
}

.modal-backdrop {
  position: fixed !important;
  top: 0 !important;
  left: 0 !important;
  width: 100vw !important;
  height: 100vh !important;
  background: rgba(0, 0, 0, 0.4) !important;
  z-index: 1050 !important;
}
</style>