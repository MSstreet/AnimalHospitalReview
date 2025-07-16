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
                  <button type="button" class="btn btn-outline-secondary" v-on:click="fnList">
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
                    <button
                        v-if="logged_idx == row.user_idx"
                        class="comment-action-btn edit"
                        @click="openEditModal(row.reply_idx, row.contents)"
                    >
                      <i class="fas fa-edit"></i>
                    </button>
                    <button
                        v-if="logged_idx == row.user_idx"
                        class="comment-action-btn delete"
                        v-on:click="fnComentDelete(row.reply_idx)">
                      <i class="fas fa-trash"></i>
                    </button>
                    <button
                        class="comment-action-btn reply"
                        @click="openReplyModal(row.reply_idx)"
                    >
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
                          <a
                              class="dropdown-item"
                              @click="openEditModal(row1.reply_idx, row1.contents)"
                          >
                            <i class="fas fa-edit me-2"></i>수정
                          </a>
                        </li>
                        <li>
                          <a class="dropdown-item text-danger" v-on:click="fnComentDelete(row1.reply_idx)">
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
                       @click="fnPage(paging.start_page-1)">
                      <i class="fas fa-angle-left"></i>
                    </a>
                  </li>
                  <li v-for="n in paginavigation()" :key="n" :class="['page-item', { active: paging.page == n }]">
                    <a class="page-link" href="javascript:;" @click="fnPage(n)">{{ n }}</a>
                  </li>
                  <li class="page-item">
                    <a class="page-link" href="javascript:;" v-if="paging.total_page_cnt > paging.end_page"
                       @click="fnPage(paging.end_page+1)">
                      <i class="fas fa-angle-right"></i>
                    </a>
                  </li>
                  <li class="page-item">
                    <a class="page-link" href="javascript:;" @click="fnPage(paging.total_page_cnt)">
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
    <CommentEditModal
        :modelValue="update_coments"
        :show="showEditModal"
        @update:modelValue="update_coments = $event"
        @update:show="showEditModal = $event"
        @confirm="fnComentUpdate"
    />

    <!-- 대댓글 작성 모달 -->
    <ReplyCreateModal
        v-model="coments"
        :show="showReplyModal"
        @update:show="showReplyModal = $event"
        @create="fnSave"
    />
  </div>
</template>

<script>
import CommentEditModal from '@/views/board/CommnetEditModal.vue';
import ReplyCreateModal from '@/views/board/ReplyCreateModal.vue';

export default {
  components: {
    CommentEditModal,
    ReplyCreateModal
  },
  data() {
    return {
      showReply: false,
      requestBody: this.$route.query,
      idx: this.$route.query.idx,
      logged_idx: this.$store.state.userIdx,
      writer_idx: this.$route.query.userIdx,
      writer_id: this.$route.query.userId,

      title: '',
      author: '',
      contents: '',
      created_at: '',

      // Reply data
      coments: '',
      coments1: '',
      c_created_at: '',
      user_name: '',
      update_coments: '',
      reply_idx: '',
      user_pw: '',
      m_reply_idx: '',

      list: {},
      list1: [],
      no: '',
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
      page: 1,
      size: this.$route.query.size ? this.$route.query.size : 10,
      search_key: this.$route.query.sk ? this.$route.query.sk : '',
      search_value: this.$route.query.sv ? this.$route.query.sv : '',

      paginavigation: function () {
        let pageNumber = [];
        let start_page = this.paging.start_page;
        let end_page = this.paging.end_page;
        for (let i = start_page; i <= end_page; i++) pageNumber.push(i);
        return pageNumber;
      },
      showEditModal: false,
      showReplyModal: false,
    }
  },
  mounted() {
    this.fnGetView();
    this.fnGetComent();
    this.fnGetComent1();
    console.log("글 번호>> : " + this.idx);
    console.log(this.logged_idx);
    console.log(this.writer_id);
  },
  methods: {
    fnGetView() {
      this.$axios.get('/board/' + this.idx, {
        params: this.requestBody
      }).then((res) => {
        this.title = res.data.title;
        this.author = res.data.author;
        this.contents = res.data.contents.replace(/\n/g, '<br/>');
        this.created_at = res.data.created_at;
      }).catch((err) => {
        if (err.message.indexOf('Network Error') > -1) {
          alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.');
        }
      });
    },
    fnList() {
      delete this.requestBody.idx;
      this.$router.push({
        path: './list',
        query: this.requestBody
      });
    },
    fnUpdate() {
      this.requestBody.idx = this.idx;
      this.$router.push({
        path: './update',
        query: this.requestBody
      });
    },
    fnDelete() {
      if (!confirm("삭제하시겠습니까?")) return;
      this.$axios.delete('/board/' + this.idx, {})
          .then(() => {
            alert('삭제되었습니다.');
            this.fnList();
          }).catch((err) => {
        console.log(err);
      });
    },
    fnSave() {
      if (this.coments == '') {
        alert("댓글을 입력해주세요");
        return false;
      }

      let apiUrl = '/reply/join';
      this.form = {
        "parent": this.reply_idx,
        "board_idx": this.idx,
        "user_idx": this.logged_idx,
        "contents": this.coments.replace(/\n/g, '<br/>')
      };

      this.$axios.post(apiUrl, this.form)
          .then((res) => {
            alert('댓글이 저장되었습니다.');
            this.coments = '';
            this.reply_idx = '';
            this.fnGetComent();
            this.fnGetComent1();
          }).catch((err) => {
        if (err.message.indexOf('Network Error') > -1) {
          alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.');
        }
      });
    },
    fnGetComent() {
      this.requestBody = {
        page: this.page,
        size: this.size
      };

      this.$axios.get('/reply/list/' + this.idx, {
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
    fnGetComent1() {
      this.requestBody = {
        page: this.page,
        size: this.size
      };
      this.$axios.get('/reply/list1/' + this.idx, {
        params: this.requestBody,
        headers: {}
      }).then((res) => {
        if (res.data.result_code === "OK") {
          this.list1 = Array.isArray(res.data.data) ? res.data.data : [];
        }
        console.log(res.data.data);
      }).catch((err) => {
        if (err.message.indexOf('Network Error') > -1) {
          alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.');
        }
      });
    },
    fnPage(n) {
      if (this.page !== n) {
        this.page = n;
      }
      this.fnGetComent();
    },
    fnComentUpdate() {
      let apiUrl = '/reply';
      this.form = {
        "reply_idx": this.reply_idx,
        "board_idx": this.idx,
        "user_idx": this.logged_idx,
        "contents": this.update_coments
      };

      this.$axios.patch(apiUrl, this.form)
          .then((res) => {
            alert('댓글이 저장되었습니다.');
            this.fnGetComent();
            this.fnGetComent1();
            this.update_coments = '';
          }).catch((err) => {
        if (err.message.indexOf('Network Error') > -1) {
          alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.');
        }
      });
    },
    fnComentDelete(idx) {
      console.log(idx);
      if (!confirm("삭제하시겠습니까?")) return;
      this.$axios.delete('/reply/' + idx, {})
          .then(() => {
            alert('삭제되었습니다.');
            this.fnGetComent();
            this.fnGetComent1();
          }).catch((err) => {
        console.log(err);
      });
    },
    openEditModal(replyIdx, contents) {
      this.reply_idx = replyIdx;
      this.update_coments = contents.replace(/<br\/>/g, '\n');
      this.showEditModal = true;
    },
    openReplyModal(replyIdx) {
      this.reply_idx = replyIdx;
      this.coments = '';
      this.showReplyModal = true;
    }
  },
  computed: {
    filteredReplies() {
      return (parentId) => {
        return this.list1.filter(reply => reply.parent === parentId);
      };
    }
  }
}
</script>

<style>
/* 기존 스타일은 그대로 유지 */
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

/* 댓글 작성 영역 */
.comment-write-area {
  display: flex;
  flex-direction: column;
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
  min-height: 120px;
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

/* 댓글 액션 버튼 */
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

/* 기본 Primary 버튼 */
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

/* 드롭다운 스타일 */
.dropdown-item {
  cursor: pointer;
}

.dropdown-item:hover {
  background-color: #f8f9fa;
}

/* 페이지네이션 스타일 */
.pagination .page-link {
  color: #0d6efd;
}

.pagination .page-item.active .page-link {
  background-color: #0d6efd;
  border-color: #0d6efd;
}



/* 반응형 디자인 */
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
</style>
