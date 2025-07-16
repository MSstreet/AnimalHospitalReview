<template>
  <div class="board-write-container">
    <div class="board-write-header">
      <h1 class="board-write-title">게시글 작성</h1>

    </div>
    
    <div class="board-write-content">
      <div class="form-group">
        <label for="title">제목</label>
        <div class="input-wrapper">
          <input 
            type="text" 
            maxlength="200" 
            v-model="title" 
            class="form-control" 
            name="title" 
            id="title" 
            placeholder="제목을 입력해주세요(200자 이내)"
          >
          <span class="character-count">{{ title.length }}/200</span>
        </div>
      </div>

      <div class="form-group">
        <label for="content">내용</label>
        <div class="textarea-wrapper">
          <textarea 
            class="form-control" 
            maxlength="3000" 
            v-model="contents" 
            rows="10" 
            name="content" 
            id="content" 
            placeholder="내용을 입력해 주세요(3000자 이내)"
          ></textarea>
          <span class="character-count">{{ contents.length }}/3000</span>
        </div>
      </div>
    </div>

    <div class="button-group">
      <button type="button" class="btn btn-save" @click="fnSave">
        <i class="fas fa-save"></i> 저장하기
      </button>
      <button type="button" class="btn btn-list" @click="fnList">
        <i class="fas fa-list"></i> 목록으로
      </button>
    </div>
  </div>
</template>

<script>

export default {
  data() { //변수생성
    return {
      requestBody: this.$route.query,

      idx: this.$route.query.idx,

      user_idx:this.$store.state.userIdx,

      title: '',
      author:'',
      contents: '',
      created_at: '',

      check:false,
      form: {} // form 데이터 객체 추가
    }
  },
  mounted() {
    this.fnGetView()
  },
  methods: {
    validCheck(){
      if (this.title == '') {
        alert('제목을 입력하세요.')
        this.check = false
        return false
      }
      if (this.contents == '') {
        alert('내용를 입력하세요.')
        this.check = false
        return false
      }
      
      this.check = true
      return true
    }
    ,fnGetView() {
      if (this.idx !== undefined) {
                 this.$axios.get('/board/' + this.idx, {
          params: this.requestBody
        }).then((res) => {
          this.title = res.data.title
          this.author = res.data.author
          this.contents = res.data.contents
          this.created_at = res.data.created_at

          console.log("확인" + res.data)

        }).catch((err) => {
          console.log(err)
        })
      }
    }
    ,fnList() {
      delete this.requestBody.idx
      this.$router.push({
        path: './list',
        query: this.requestBody
      })
    }

    ,fnView(idx) {
      this.requestBody.idx = idx
      this.$router.push({
        path: './detail',
        query: this.requestBody
      })
    }

    ,fnSave() {
      this.validCheck()
      if(!(this.check)){
        return false
      }

      let apiUrl = '/board'
      this.form = {
        "idx": this.idx,
        "title": this.title,
        "contents": this.contents,
        "author": this.author,
        "user_idx" :this.user_idx
      }

      if (this.idx === undefined) {
        //INSERT
        this.$axios.post(apiUrl, this.form)
            .then((res) => {
              alert('글이 저장되었습니다.')
              this.fnView(res.data.idx)
            }).catch((err) => {
          if (err.message.indexOf('Network Error') > -1) {
            alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
          }
        })
      } else {
        //UPDATE
        this.$axios.patch(apiUrl, this.form)
            .then((res) => {
              alert('글이 저장되었습니다.')
              this.fnView(res.data.idx)
            }).catch((err) => {
          if (err.message.indexOf('Network Error') > -1) {
            alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
          }
        })
      }
    }

  }
}
</script>
<style scoped>
.board-write-container {
  max-width: 900px;
  margin: 2rem auto;
  padding: 2rem;
  background-color: #ffffff;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.board-write-header {
  text-align: center;
  margin-bottom: 2.5rem;
}

.board-write-title {
  color: #4c1192;
  font-size: 2rem;
  font-weight: 700;
  margin-bottom: 0.5rem;
}

.board-write-content {
  margin-bottom: 2rem;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.form-group {
  margin-bottom: 1.5rem;
  display: flex;
  align-items: flex-start;
  gap: 0.5rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.2rem;
  color: #333;
  font-weight: 500;
}

.input-wrapper, .textarea-wrapper {
  position: relative;
  max-width: 700px;
  margin: 0 0 0 24px;
}

.form-control {
  width: 100%;
  min-width: 400px;
  max-width: 700px;
  padding: 0.8rem;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  font-size: 1rem;
  transition: all 0.3s ease;
}

.form-control:focus {
  border-color: #4c1192;
  box-shadow: 0 0 0 3px rgba(76, 17, 146, 0.1);
  outline: none;
}

.character-count {
  position: absolute;
  right: 10px;
  bottom: 10px;
  color: #666;
  font-size: 0.875rem;
}

.button-group {
  display: flex;
  justify-content: center;
  gap: 1rem;
  margin-top: 2rem;
}

.btn {
  padding: 0.8rem 1.5rem;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.btn-save {
  background-color: #4c1192;
  color: white;
}

.btn-save:hover {
  background-color: #3a0d6f;
  transform: translateY(-1px);
}

.btn-list {
  background-color: #f5f5f5;
  color: #333;
}

.btn-list:hover {
  background-color: #e0e0e0;
  transform: translateY(-1px);
}

@media (max-width: 768px) {
  .board-write-container {
    margin: 1rem;
    padding: 1rem;
  }

  .board-write-title {
    font-size: 1.5rem;
  }

  .button-group {
    flex-direction: column;
  }

  .btn {
    width: 100%;
    justify-content: center;
  }
}
</style>