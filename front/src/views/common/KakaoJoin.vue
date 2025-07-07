<template>
  <!--  배경색-->

  <div class="join-body">
    <div class="container py-4 ">
      <h1 class="fs-1 fw-bold text-center mb-1"><b>회원가입</b> </h1>
      <form @submit.prevent="fnUpdate" ref="form">
        <div class="form-group has-success">
          <label class="fw-bold form-label mt-4" for="inputValid">비밀번호</label>
          <input type="password" maxlength="20" class="form-control" id="inputValid" v-model="user_pw" @change="validPasswordCheck">
          <div id="checkPwd" class="mt-1"></div>
        </div>
        <div class="form-group has-danger" id="inputInvalid">
          <label class="fw-bold form-label mt-4" for="inputInvalid">비밀번호 재확인</label>
          <input type="password" maxlength="20" class="form-control"  v-model="pwd_check" @change="validSamePasswordCheck">
          <div id="doubleCheckPwd" class="mt-1"></div>
        </div>
        <div class="form-group">
          <label for="exampleInputNum" class="fw-bold form-label mt-4">전화번호</label>
          <input type="text" maxlength="20" class="form-control" id="exampleInputNum" v-model="user_num" @change="validNumCheck">
          <div id="numberCheck" class="mt-1"></div>
        </div>
        <div class="form-group">
          <label for="exampleInputZip" class="fw-bold form-label mt-4">우편번호</label>
          <input type="text" maxlength="20" v-model="postcode"  class="form-control mb-1" id="exampleInputZip">
        </div>
        <input type="button" class="btn btn-secondary" @click="execDaumPostcode()" value="우편번호 찾기"><br>
        <div class="form-group">
          <label for="exampleInputAddr" class="fw-bold form-label mt-4">주소</label>
          <input type="text" maxlength="50" class="form-control mb-4" id="exampleInputAddr" v-model="address">
        </div>
        <div class="form-group">
          <label for="exampleInputAddr" maxlength="50" class="fw-bold form-label mt-4">상세주소</label>
          <input type="text" class="form-control mb-4" id="exampleInputAddr" v-model="extra_address">
        </div>
        <div class="d-grid gap-2">
          <button class="btn btn-primary btn-lg" type="submit">가입하기</button>
        </div>
      </form>
    </div>
  </div>
</template>
<script>
import {mapGetters} from 'vuex'   //vuex 추가

export default {
  data() {
    return {
      check : false,
      user_pw: '',
      pwd_check: '',
      user_num: '',
      postcode:'',
      address: '',
      extra_address: '',
      user_idx:'',
      passwordError: '',
      doubleCheckPwdError: '',
      numberError: '',
      form: {
        idx: '',
        phone_num: '',
        zip_code: '',
        addr: '',
        detail_addr: '',
        email: ''
      }
    }
  },
  mounted() {
    console.log("User ID:", this.getUserId);  // 사용자 ID 가져오기
    this.user_idx = this.getIsUserIdx;
    this.form.idx = this.user_idx;
  },
  methods: {
    fnUpdate(){
      let apiUrl = '/user/'
      this.updateCheck()
      if(!(this.check)){
        return false
      }
      this.form.idx = this.user_idx;
      this.form.phone_num = this.user_num;
      this.form.zip_code = this.postcode;
      this.form.addr = this.address;
      this.form.detail_addr = this.extra_address;
      // email은 필요시 할당
      this.$axios.patch(apiUrl, this.form)
          .then((res) => {
            alert('정보가 수정되었습니다.')
            this.fnView(res.data.idx)
          }).catch((err) => {
        if (err.message.indexOf('Network Error') > -1) {
          alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
        }
      })
    },
    //비밀번호 유효성 체크
    validPasswordCheck(){
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
    },
    //비밀번호 확인 체크
    validSamePasswordCheck(){
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
    },
    //전화번호 유효성
    validNumCheck(){
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
    },
    //서브밋 체크
    updateCheck() {
      //const idCheck = new RegExp("^[A-Za-z0-9]{5,20}$")
      const pwCheck = new RegExp("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{10,}$");
      const numCheck = new RegExp("^(?:(010\\d{4})|(01[1|6|7|8|9]\\d{3,4}))(\\d{4})$")

      if (this.user_pw !== '' && !pwCheck.test(this.user_pw)) {
        alert('비밀번호 정규식에 맞지 않습니다.\n 10~20자, 하나 이상의 대문자, 하나의 소문자, 하나의 숫자 및 하나의 특수 문자가 포함되어야 합니다.')
        this.check = false
        return
      }else{
        this.check = true
      }
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
    },

    execDaumPostcode() {
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
    },
    goToLogin() {
      this.$router.push({
        name: 'Login'
      })
    }
  },
  computed: {
    ...mapGetters(['getUserId', 'loggedIn','getIsUserIdx'])
  }
}

</script>

<style scoped>
.join-body{
  background-color: rgba(245, 245, 245, 0.8) !important; /* 부드러운 배경 색상 */
}
.bg-light {
  height: 1053px;
  padding-top: 55px;
  padding-bottom: 75px;
}

.flex-fill.mx-xl-5.mb-2 {
  margin: 0 auto;
  width: 700px;
  padding-right: 7rem;
  padding-left: 7rem;
}

.container.py-4 {
  margin: 0 auto;
  width: 503px;
  padding-top: 0;
}

.d-grid.gap-2 {
  padding-top: 30px;
}

.bir_yy, .bir_mm, .bir_dd {
  width: 160px;
  display: table-cell;
}

.bir_mm + .bir_dd, .bir_yy + .bir_mm {
  padding-left: 10px;
}
</style>