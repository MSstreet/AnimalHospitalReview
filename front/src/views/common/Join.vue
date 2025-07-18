<template>
  <!--  배경색-->

  <div class="join-body">
    <div class="container py-5">
      <div class="join-container">
        <h1 class="join-title">회원가입</h1>
        <p class="join-subtitle">반려동물 병원 서비스 이용을 위한 회원가입을 진행해주세요.</p>

        <form @submit.prevent="fnJoin" ref="form" class="join-form">
          <div>
            <div class="form-group" :class="{ 'error-margin': !check }">
              <label for="email" class="form-label">이메일</label>
              <div class="input-group">
                <input
                    type="text"
                    maxlength="50"
                    class="form-control"
                    id="email"
                    v-model="user_id"
                    @change="validEmailCheck"
                    placeholder="이메일을 입력해주세요"
                >
              </div>
            </div>
            <div id="checkEmail" class="validation-message" ></div>
          </div>

          <div class="form-group">
            <label class="form-label" for="password">비밀번호</label>
            <div class="input-group">
            <input type="password" maxlength="20" class="form-control" id="password" v-model="user_pw" @change="validPasswordCheck" placeholder="비밀번호를 입력해주세요">
            </div>
            <div id="checkPwd" class="validation-message"></div>
          </div>

          <div class="form-group">
            <label class="form-label" for="password-confirm">비밀번호 확인</label>
            <div class="input-group">
            <input type="password" maxlength="20" class="form-control" id="password-confirm" v-model="pwd_check" @change="validSamePasswordCheck" placeholder="비밀번호를 다시 입력해주세요">
            </div>
              <div id="doubleCheckPwd" class="validation-message"></div>
          </div>

          <div class="form-group">
            <label for="name" class="form-label">이름</label>
            <div class="input-group">
            <input type="text" maxlength="20" class="form-control" id="name" v-model="user_name" @change="validNameCheck" placeholder="이름을 입력해주세요">
            </div>
            <div id="nameCheck" class="validation-message"></div>
          </div>

          <div class="form-group">
            <label for="phone" class="form-label">전화번호</label>
            <div class="input-group">
            <input type="text" maxlength="20" class="form-control" id="phone" v-model="user_num" @change="validNumCheck" placeholder="전화번호를 입력해주세요">
            </div>
            <div id="numberCheck" class="validation-message"></div>
          </div>

          <div class="form-group d-flex align-items-start">
            <label for="zipInput" class="form-label">주소</label>
            <div class="flex-grow-1 w-100">
              <!-- 우편번호 + 버튼 -->
              <div class="input-group mb-2">
                <input type="text" maxlength="20" v-model="postcode" class="form-control" id="zipInput" placeholder="우편번호" readonly>
                <button type="button" class="btn btn-outline-primary" @click="execDaumPostcode()">
                  <i class="fas fa-search"></i> 우편번호 찾기
                </button>
              </div>
              <!-- 기본 주소 -->
              <div class="mb-2">
                <input type="text" maxlength="50" class="form-control" id="addressInput" v-model="address" placeholder="주소" readonly>
              </div>
              <!-- 상세 주소 -->
              <div>
                <input type="text" class="form-control" id="detailAddressInput" v-model="extra_address" placeholder="상세주소">
              </div>
            </div>
          </div>


          <div class="d-flex justify-content-between gap-3">
            <button class="btn btn-primary btn-lg join-btn" type="submit">회원가입</button>
            <button class="btn btn-outline-secondary btn-lg" type="button" @click="goToPages1">취소</button>
          </div>

        </form>
      </div>
    </div>
  </div>
</template>
<script>
import {mapActions, mapGetters} from 'vuex'   //vuex 추가

export default {
  data() {
    return {

      check : false,

      duplicated_check: false,
      duplicated_id_check: false,
      user_id: '',
      user_pw: '',
      pwd_check: '',
      user_name: '',
      user_num: '',

      user_email:'',

      user_addr: '',

      postcode:'',
      address: '',
      extra_address: ''


    }
  },
  methods: {
    ...mapActions(['join']),     //vuex/actions에 있는 login 함수

    async fnJoin() {       //async 함수로 변경
      this.submitCheck()

      if(!(this.check)){
        return false
      }

      if(!(this.duplicated_check)){
        alert("이미 등록된 Email입니다.")
        return false
      }

      try {
        let joinResult = await this.join( {
          user_id: this.user_id,
          user_pw: this.user_pw,
          user_name: this.user_name,
          email:this.user_email,
          phone_num: this.user_num,
          zip_code: this.postcode,
          addr:this.address,
          detail_addr:this.extra_address
        })
        if (joinResult) {
          this.goToPages1()
          alert("가입이 완료되었습니다!")
        }
      }
      catch (err) {
        if (err.message.indexOf('Network Error') > -1) {
          alert('서버에 접속할 수 없습니다. 상태를 확인해주세요.')
        } else {
          alert('실패')
        }
      }
    }
    //Email 체크
    ,validEmailCheck(){
      const emailCheck = new RegExp("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
      if (this.user_id !== '' && !emailCheck.test(this.user_id)) {
        alert("올바른 이메일 형식이 아닙니다.")
        document.getElementById('checkEmail').style.color="red"
        document.getElementById('checkEmail').innerHTML = "올바른 이메일 형식이 아닙니다.";
        this.check = false
        return
      }else{
        document.getElementById('checkEmail').style.color="black"
        document.getElementById('checkEmail').innerHTML = "";
        this.check = true
        this.validDuplicationEmailCheck()
        return
      }
    }
    //Email 중복 체크
    ,validDuplicationEmailCheck(){
      if(this.user_id === '') {
        alert("이메일을 입력해주세요.")
        return
      }

      let apiUrl = '/user/check/mail?email=' + this.user_id

      this.$axios.get(apiUrl, {
        params:{
          //email:this.user_email
        }
      }).then((res) => {
        if(res.data === true){
          alert("이미 등록된 email입니다.")
          document.getElementById('checkEmail').style.color="red"
          document.getElementById('checkEmail').innerHTML = "이미 등록된 email입니다.";
          this.duplicated_check = false
          return
        }else{
          document.getElementById('checkEmail').style.color="black"
          document.getElementById('checkEmail').innerHTML = "";
          this.duplicated_check = true
          document.getElementById('checkEmail').style.color="green"
          document.getElementById('checkEmail').innerHTML = "사용 가능한 이메일입니다.";
        }
      }).catch((err) => {
        if (err.message.indexOf('Network Error') > -1){
          alert('네크워크가 원활하지 않습니다. \n잠시 후 다시 시도해주세요.')
        }
      })
    }
    //ID 중복 체크
    ,validIdDuplicationCheck(){
      let apiUrl = '/user/check?userId=' + this.user_id

      this.$axios.get(apiUrl, {
        params:{
          user_id:this.user_id
        }
      }).then((res) => {
            if(res.data === true){
              document.getElementById('checkId').style.color="red"
              document.getElementById('checkId').innerHTML = "중복된 ID 입니다.";
              alert("중복된 ID 입니다.")
              this.duplicated_id_check = false
              console.log("아이디 중복 확인 :" + this.check)
            }else{
              document.getElementById('checkId').style.color="black"
              document.getElementById('checkId').innerHTML = "";
              this.duplicated_id_check = true
            }
          }).catch((err) => {
          if (err.message.indexOf('Network Error') > -1){
            alert('네크워크가 원활하지 않습니다. \n잠시 후 다시 시도해주세요.')
          }
      })
    }
    //비밀번호 유효성 체크
    ,validPasswordCheck(){
      const pwCheck = new RegExp("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{10,20}$");
      if (this.user_pw !== '' && !pwCheck.test(this.user_pw)) {
        alert('비밀번호 정규식에 맞지 않습니다.\n 10 ~ 20자, 하나 이상의 대문자, 하나의 소문자, 하나의 숫자 및 하나의 특수 문자가 포함되어야 합니다.')
        document.getElementById('checkPwd').style.color="red"
        document.getElementById('checkPwd').innerHTML = " 10 ~ 20자, 하나 이상의 대문자, 하나의 소문자, 하나의 숫자 및 하나의 특수 문자가 포함되어야 합니다.";
        this.check = false
        return
      }else{
        document.getElementById('checkPwd').style.color="black"
        document.getElementById('checkPwd').innerHTML = "";
        this.check = true
        return
      }
    }
    //비밀번호 확인 체크
    ,validSamePasswordCheck(){
      if (this.user_pw !== '' && this.pwd_check !== '' && this.user_pw !== this.pwd_check) {
        alert('비밀번호와 비밀번호 확인이 서로 맞지 않습니다.')
        document.getElementById('doubleCheckPwd').style.color="red"
        document.getElementById('doubleCheckPwd').innerHTML = " 비밀번호와 비밀번호 확인이 서로 맞지 않습니다.";
        this.check = false
        return
      }else{
        document.getElementById('doubleCheckPwd').style.color="black"
        document.getElementById('doubleCheckPwd').innerHTML = "";
        this.check = true
        return
      }
    }
    //이름 유효성 검사
    ,validNameCheck(){
      const nameCheck = new RegExp("^[|가-힣]{2,20}");
      if (this.user_name !== '' && !nameCheck.test(this.user_name)) {
        alert('이름은 한글 입력로 최소 2자리 최대 20자리까지 입력 가능합니다.')
        document.getElementById('nameCheck').style.color="red"
        document.getElementById('nameCheck').innerHTML = " 이름은 한글 입력로 최소 2자리 최대 20자리까지 입력 가능합니다.";
        this.check = false
        return
      } else{
        document.getElementById('nameCheck').style.color="black"
        document.getElementById('nameCheck').innerHTML = "";
        this.check = true
        return
      }
    }

    //전화번호 유효성
    ,validNumCheck(){
      const numCheck = new RegExp("^(?:(010\\d{4})|(01[1|6|7|8|9]\\d{3,4}))(\\d{4})$")

      if (this.user_num !== '' && !numCheck.test(this.user_num)) {
        alert('올바른 전화번호 형식이 아닙니다. 예와 같이 입력해주세요.\u00a0\u00a0\u00a0\u00a0\u00a0 ex)\u00a000011112222')
        document.getElementById('numberCheck').style.color="red"
        document.getElementById('numberCheck').innerHTML = "올바른 전화번호 형식이 아닙니다. 예와 같이 입력해주세요.\u00a0\u00a0\u00a0\u00a0\u00a0 ex)\u00a000011112222 ";
        this.check = false
        return
      } else{
        document.getElementById('numberCheck').style.color="black"
        document.getElementById('numberCheck').innerHTML = "";
        this.check = true
        return
      }
    }
    //서브밋 체크
    ,submitCheck() {
      const pwCheck = new RegExp("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{10,}$");
      const emailCheck = new RegExp("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
      const numCheck = new RegExp("^(?:(010\\d{4})|(01[1|6|7|8|9]\\d{3,4}))(\\d{4})$")
      const nameCheck = new RegExp("^[|가-힣]{2,20}")

      if (this.user_pw !== '' && !pwCheck.test(this.user_pw)) {
        alert('비밀번호 정규식에 맞지 않습니다.\n 10~20자, 하나 이상의 대문자, 하나의 소문자, 하나의 숫자 및 하나의 특수 문자가 포함되어야 합니다.')
        this.check = false
        return
      }else{
        this.check = true
      }

      if (this.user_id !== '' && !emailCheck.test(this.user_id)) {
        document.getElementById('checkEmail').style.color="red"
        document.getElementById('checkEmail').innerHTML = " 올바른 이메일 형식이 아닙니다.";
        alert(" 올바른 이메일 형식이 아닙니다.")
        this.check = false
        return
      }else{
        document.getElementById('checkEmail').style.color="black"
        document.getElementById('checkEmail').innerHTML = "";
        this.check = true
       //return
      }
      this.validDuplicationEmailCheck()

      if (this.user_pw !== '' && this.pwd_check !== '' && this.user_pw !== this.pwd_check) {
        alert('비밀번호와 비밀번호 확인이 서로 맞지 않습니다.')
        document.getElementById('checkEmail').style.color="red"
        document.getElementById('checkEmail').innerHTML = " 비밀번호와 비밀번호 확인이 서로 맞지 않습니다.";
        this.check = false
        return
      }else{
        this.check = true
      }

      if (this.user_name !== '' && !nameCheck.test(this.user_name)) {
        document.getElementById('nameCheck').style.color="red"
        document.getElementById('nameCheck').innerHTML = " 이름은 한글 입력로 최소 2자리 최대 20자리까지 입력 가능합니다.";
        alert('이름은 한글 입력로 최소 2자리 최대 20자리까지 입력 가능합니다.')
        this.check = false
        return
      } else{
        document.getElementById('nameCheck').style.color="black"
        document.getElementById('nameCheck').innerHTML = "";
        this.check = true
        //return
      }

      if (this.user_num !== '' && !numCheck.test(this.user_num)) {
        //alert('전화번호를 입력하세요.')
        alert('올바른 전화번호 형식이 아닙니다. 예와 같이 입력해주세요.\u00a0\u00a0\u00a0\u00a0\u00a0 ex)\u00a000011112222')
        document.getElementById('numberCheck').style.color="red"
        document.getElementById('numberCheck').innerHTML = "올바른 전화번호 형식이 아닙니다. 예와 같이 입력해주세요.\u00a0\u00a0\u00a0\u00a0\u00a0 ex)\u00a000011112222 ";
        this.check = false
        return
      } else{
        document.getElementById('numberCheck').style.color="black"
        document.getElementById('numberCheck').innerHTML = "";
        this.check = true
        //return
      }

      if (this.user_id == '') {
        alert('ID를 입력하세요.')
        this.check = false
        return
      } else{
        this.check = true
      }

      if (this.user_pw == '') {
        alert('비밀번호를 입력하세요.')
        this.check = false
        return
      } else{
        this.check = true
      }

      if (this.pwd_check == '') {
        alert('비밀번호 확인을 입력하세요.')
        this.check = false
        return
      } else{
        this.check = true
      }

      if (this.user_name == '') {
        alert('이름을 입력하세요.')
        this.check = false
        return
      } else{
        this.check = true
      }

      if(this.user_num == ''){
        alert('전화번호를 입력하세요')
        this.check = false
        return
      }else{
        this.check = true
      }

      if(this.postcode == ''){
        alert('우편번호를 입력하세요')
        this.check = false
        return
      }else{
        this.check = true
      }

      if(this.address == ''){
        alert('주소를 입력하세요')
        this.check = false
        return
      }else{
        this.check = true
      }

    }

    ,execDaumPostcode() {
      new window.daum.Postcode({
        oncomplete: (data) => {
          if (this.extra_address !== "") {
            this.extra_address = "";
          }

          if (data.userSelectedType === "R") {
            // 사용자가 도로명 주소를 선택했을 경우
            this.address = data.roadAddress;
          } else {
            // 사용자가 지번 주소를 선택했을 경우(J)
            this.address = data.jibunAddress;
          }


          // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
          if (data.userSelectedType === "R") {
            // 법정동명이 있을 경우 추가한다. (법정리는 제외)
            // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
            if (data.bname !== "" && /[동|로|가]$/g.test(data.bname)) {
              this.extra_address += data.bname;
            }

            // 건물명이 있고, 공동주택일 경우 추가한다.
            if (data.buildingName !== "" && data.apartment === "Y") {
              this.extra_address +=
                  this.extra_address !== ""
                      ? `, ${data.buildingName}`
                      : data.buildingName;
            }

            // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
            if (this.extra_address !== "") {
              this.extra_address = `(${this.extra_address})`;
            }
          } else {
            this.extra_address = "";
          }

          // 우편번호를 입력한다.
          this.postcode = data.zonecode;
        },
      }).open();
    }
    ,goToPages1() {
      this.$router.push({
        // path: './write',
        name: 'Login'
      })
    }
  },

  computed: {
    ...mapGetters({
      errorState: 'getErrorState'
    })
  }
}

</script>

<style scoped>
.join-body {
  min-height: 100vh;
  background-color: #f8f9fa;
  padding: 1rem 0;
  display: flex;
  align-items: center;
  justify-content: center;
}

.join-container {
  width: 100%;
  max-width: 600px;
  margin: 0 auto;
  padding: 2rem;
  background: white;
  border-radius: 1rem;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
}

.join-title {
  font-size: clamp(1.5rem, 4vw, 2rem);
  font-weight: 700;
  color: #2c3e50;
  text-align: center;
  margin-bottom: 0.5rem;
  word-break: keep-all;
  line-height: 1.2;
}

.join-subtitle {
  color: #6c757d;
  text-align: center;
  margin-bottom: 2rem;
  font-size: clamp(0.875rem, 2.5vw, 1rem);
  word-break: keep-all;
  line-height: 1.4;
}

.join-form {
  padding: 0.5rem;
}

.form-group {
  margin-bottom: 1.5rem;
  transition: margin-bottom 0.3s ease;
}

.form-group.error-margin {
  margin-bottom: 0.5rem;
}

.form-label {
  font-weight: 600;
  color: #495057;
  margin-bottom: 0.5rem;
  font-size: clamp(0.875rem, 2.5vw, 1rem);
  display: block;
}

.form-control {
  border: 1px solid #ced4da;
  border-radius: 0.5rem;
  padding: 0.75rem 1rem;
  transition: all 0.3s ease;
  font-size: clamp(0.875rem, 2.5vw, 1rem);
  width: 100%;
  box-sizing: border-box;
}

.form-control:focus {
  border-color: #80bdff;
  box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
  outline: none;
}

.input-group {
  display: flex;
  gap: 0.5rem;
  flex-wrap: wrap;
}

.input-group .form-control {
  flex: 1;
  min-width: 0;
}

.btn {
  padding: 0.75rem 1rem;
  font-size: clamp(0.875rem, 2.5vw, 1rem);
  border-radius: 0.5rem;
  border: 1px solid transparent;
  cursor: pointer;
  transition: all 0.3s ease;
  white-space: nowrap;
  text-decoration: none;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-height: 44px; /* 터치 친화적 최소 높이 */
}

.btn:focus {
  outline: none;
  box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
}

.btn-outline-primary {
  background-color: transparent;
  border-color: #0d6efd;
  color: #0d6efd;
}

.btn-outline-primary:hover {
  background-color: #0d6efd;
  color: white;
}

.btn-outline-secondary {
  background-color: transparent;
  border-color: #6c757d;
  color: #6c757d;
}

.btn-outline-secondary:hover {
  background-color: #6c757d;
  color: white;
}

.btn-primary {
  background-color: #0d6efd;
  border-color: #0d6efd;
  color: white;
}

.btn-primary:hover {
  background-color: #0b5ed7;
  border-color: #0b5ed7;
  transform: translateY(-1px);
}

.btn-lg {
  padding: 0.875rem 1.5rem;
  font-size: clamp(1rem, 3vw, 1.125rem);
  font-weight: 600;
}

.validation-message {
  font-size: clamp(0.75rem, 2vw, 0.875rem);
  margin-top: 0.15rem;
  color: #dc3545;
  word-break: keep-all;
  line-height: 1.3;
}

.address-input-group {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.address-input-group .input-group {
  display: flex;
  gap: 0.5rem;
  flex-wrap: wrap;
}

.address-input-group .form-control {
  width: 100%;
  box-sizing: border-box;
}

.address-input-group .input-group .form-control {
  flex: 1;
  min-width: 0;
}

.address-area{
  display: flex;
}

.d-grid {
  display: grid;
}

/* 모바일 최적화 */
@media (max-width: 768px) {
  .join-body {
    padding: 0.5rem;
    min-height: 100vh;
    align-items: flex-start;
    padding-top: 1rem;
  }

  .join-container {
    padding: 1.5rem 1rem;
    margin: 0;
    border-radius: 0.75rem;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  }

  .join-form {
    padding: 0;
  }

  .input-group {
    flex-direction: column;
    gap: 0.5rem;
  }

  .input-group .form-control {
    flex: none;
  }

  .btn {
    width: 100%;
    justify-content: center;
  }

  .form-control {
    padding: 1.5rem 1.25rem !important;
    font-size: 1.125rem !important;
    min-height: 60px !important;
  }

  .address-input-group .form-control {
    padding: 1.5rem 1.25rem !important;
    font-size: 1.125rem !important;
    min-height: 60px !important;
    width: 100% !important;
    box-sizing: border-box !important;
  }

  /* 주소 입력 그룹의 input-group도 세로 배치 */
  .address-input-group .input-group {
    flex-direction: column;
    gap: 0.5rem;
  }

  .address-input-group .input-group .form-control {
    flex: none !important;
    width: 100% !important;
  }

  .btn-lg {
    padding: 1.5rem 2rem !important;
    font-size: 1.25rem !important;
    min-height: 64px !important;
  }

  .form-label {
    font-size: 1.125rem !important;
    margin-bottom: 1rem !important;
    font-weight: 700 !important;
  }

  .validation-message {
    font-size: 1rem !important;
    margin-top: 0.75rem !important;
  }
}

/* 작은 모바일 화면 */
@media (max-width: 480px) {
  .join-container {
    padding: 1rem 0.75rem;
    border-radius: 0.5rem;
  }

  .join-title {
    font-size: 1.5rem;
  }

  .join-subtitle {
    font-size: 0.875rem;
  }

  .form-control {
    padding: 1.5rem 1.25rem !important;
    font-size: 1.125rem !important;
    min-height: 60px !important;
  }

  /* 주소 입력 필드들 크기 통일 */
  .address-input-group .form-control {
    padding: 1.5rem 1.25rem !important;
    font-size: 1.125rem !important;
    min-height: 60px !important;
    width: 100% !important;
    box-sizing: border-box !important;
  }

  .btn {
    padding: 1.25rem 1.5rem !important;
    font-size: 1.125rem !important;
    min-height: 60px !important;
  }

  .btn-lg {
    padding: 1.5rem 2rem !important;
    font-size: 1.25rem !important;
    min-height: 64px !important;
  }

  .form-label {
    font-size: 1.125rem !important;
    margin-bottom: 1rem !important;
    font-weight: 700 !important;
  }
}

/* 태블릿 최적화 */
@media (min-width: 769px) and (max-width: 1024px) {
  .join-container {
    max-width: 500px;
    padding: 2rem 1.5rem;
  }
}

/* 큰 화면 최적화 */
@media (min-width: 1025px) {
  .join-container {
    max-width: 600px;
  }
}

/* 접근성 개선 */
@media (prefers-reduced-motion: reduce) {
  .form-control,
  .btn {
    transition: none;
  }
}


/* 터치 디바이스 최적화 */
@media (hover: none) and (pointer: coarse) {
  .btn {
    min-height: 48px;
    padding: 0.875rem 1rem;
  }

  .form-control {
    min-height: 48px;
    padding: 0.875rem 1rem;
  }
}
</style>