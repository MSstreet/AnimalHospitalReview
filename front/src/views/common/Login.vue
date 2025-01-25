
<template>

  <div id="findId" class="modal fade">
    <div class="modal-dialog modal-dialog-centered modal-login">
      <div class="modal-content">
        <div class="modal-body">
          <div class="container my-auto">
            <div class="row">
              <div class="card z-index-0 fadeIn3 fadeInBottom">
                <div class="card-header p-0 position-relative mt-n4 mx-3 z-index-2">
                  <div class="bg-gradient-primary shadow-primary border-radius-lg py-3 pe-1">
                    <h4 class="text-black font-weight-bolder text-center mt-2 mb-0">ID 찾기</h4>
                  </div>
                </div>
                <div class="card-body">
                    <p>가입 시 입력한 이름과 Email을 입력해주세요.</p>

                  <div class="input-group input-group-outline my-3">
<!--                    <label class="form-label">이름</label>-->
                    <input type="text" maxlength="50" id="userName" name="userName" class="form-control" placeholder="이름" v-model="user_name" required>
                  </div>

                  <div class="input-group input-group-outline my-3">
<!--                      <label class="form-label">Email</label>-->
                      <input type="email" maxlength="50" id="userEmail" name="memberEmail" class="form-control" placeholder="Email" v-model="user_email" required>
                    </div>
                    <div class="text-center">
                      <button type="button" class="btn btn-primary bg-gradient-primary w-100 my-4 mb-2"
                              id="checkEmail" @click="fnFind">확인</button>
                    </div>

                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <div id="findPw" class="modal fade">
    <div class="modal-dialog modal-dialog-centered modal-login">
      <div class="modal-content">
        <div class="modal-body">
          <div class="container my-auto">
            <div class="row">
              <div class="card z-index-0 fadeIn3 fadeInBottom">
                <div class="card-header p-0 position-relative mt-n4 mx-3 z-index-2">
                  <div class="bg-gradient-primary shadow-primary border-radius-lg py-3 pe-1">
                    <h4 class="text-black font-weight-bolder text-center mt-2 mb-0">비밀번호 찾기</h4>
                  </div>
                </div>
                <div class="card-body">
                  <p>입력한 이메일로 임시 비밀번호가 전송됩니다.</p>

                  <!--                  <div class="input-group input-group-outline my-3">-->
                  <!--&lt;!&ndash;                    <label class="form-label">이름</label>&ndash;&gt;-->
                  <!--                    <input type="text" id="userName" name="userName" class="form-control" placeholder="이름" v-model="user_name" required>-->
                  <!--                  </div>-->
                  <div class="input-group input-group-outline my-3">
                    <!--                      <label class="form-label">Email</label>-->
                    <input type="email" maxlength="50" id="userEmail" name="memberEmail" class="form-control" placeholder="Email" v-model="user_email" required>
                  </div>
                  <div class="text-center">
                    <button type="button" class="btn btn-primary bg-gradient-primary w-100 my-4 mb-2"
                            id="checkEmail" @click="checkEmail">비밀번호 발송</button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <section class="bg-light">
  <div class="social-btn-area">
    <button type="button" class="kakao-btn" @click="kakaoLogin">
      <img src="@/assets/kakao_login_medium.png" class="kakao-btn-img" alt="카카오 로그인 로고" />
    </button>
  </div>

  <div class="mt-10">
    <div class="container py-4">
      <form @submit.prevent="fnLogin">
        <div class="form-group">
          <label for="exampleInputEmail1" class="fw-bold mb-1">아이디</label>
          <input type="text" maxlength="50" class="form-control" id="exampleInputEmail1" placeholder="Enter Id" v-model="user_id">
        </div>

        <div class="form-group">
          <label for="exampleInputPassword1" class="fw-bold mb-1 mt-4">비밀번호</label>
          <input type="password" maxlength="50" class="form-control" id="exampleInputPassword1" placeholder="Password" autocomplete="on" v-model="user_pw">
        </div>

      <div class="row">
        <div class="d-grid gap-2 mt-2 col-6">
           <button type="submit" class="btn btn-primary mt-3 fw-bold">로그인</button>
        </div>
        <div class="d-grid gap-2 mt-2 col-6">
          <router-link to="/join" type="button" class="btn btn-success mt-3 fw-bold">회원가입</router-link>
        </div>
        <div class="mt-2 ">
          <span type="button" class="btn btn-link a" data-bs-toggle="modal"
                data-bs-target="#findId">ID를 잊으셨나요?</span>
         <span type="button" class="btn btn-link a" data-bs-toggle="modal"
               data-bs-target="#findPw">비밀번호를 잊으셨나요?</span>
        </div>
      </div>
      </form>
    </div>
  </div>
  </section>
</template>

<script>
import {mapActions, mapGetters} from 'vuex'   //vuex 추가
import axios from 'axios'

export default {

  data() {
    return {
      user_id: '',
      user_pw: '',
      user_name:'',
      user_email: '',
    }

  }
  ,mounted() {
  },
  methods: {

    ...mapActions(['login']),     //vuex/actions에 있는 login 함수

    async fnLogin() {       //async 함수로 변경
      if (this.user_id === '') {
        alert('ID를 입력하세요.')
        return
      }

      if (this.user_pw === '') {
        alert('비밀번호를 입력하세요.')
        return
      }

      //로그인 API 호출
      try {
        let loginResult = await this.login({user_id: this.user_id, user_pw: this.user_pw})

        if (loginResult) {
           this.goToPages()
           alert(this.user_id + "님 환영합니다.")
        }

      } catch (err) {
        if (err.message.indexOf('Network Error') > -1) {
          alert('서버에 접속할 수 없습니다. 상태를 확인해주세요.')
        } else {
          alert('로그인 정보를 확인할 수 없습니다.')
        }
      }
    },async kakaoLogin() {

      try {
        // 백엔드로 로그인 URL을 요청
        const response = await axios.get("http://localhost:8081/oauth/kakao/login-url");
        const authorizationUri = response.data;

        // 카카오 로그인 페이지로 리디렉션
        window.location.href = authorizationUri;
      } catch (error) {
        console.error("카카오 로그인 URL을 가져오는 중 오류가 발생했습니다.", error);
      }
    },async kakaoLogin1() {
      window.location.href = "http://localhost:8081/oauth2/authorization/kakao";
    }
    ,goToPages() {
      this.$router.replace({
        // path: './write',
        name: 'PageHome'
      })
    }
    ,checkEmail(){
      let apiUrl = this.$serverUrl + '/user/find/pw?userEmail=' + this.user_email

      this.$axios.post(apiUrl, {
        params: {
          user_email: this.user_email
        }
      }).then((res)=> {
               console.log(res.data)
              if(res.data == '') {
                alert('일치하는 정보가 없습니다.')
              }else{
                alert("회원님의 Email로 임시 비밀번호를 전송하였습니다.")
              }

            }).catch((err) => {
          if (err.message.indexOf('Network Error') > -1) {
            alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
          }
        })
    }
    , fnFind(){
      let apiUrl = this.$serverUrl + '/user/find'

      console.log(this.email)
      console.log(this.user_name)

      this.form = {
        "user_name": this.user_name,
        "email":this.user_email,

      }
      //UPDATE
      this.$axios.post(apiUrl, this.form)
          .then((res) => {
            console.log(res.data)
            if(res.data == '') {
              alert('일치하는 정보가 없습니다.')
              console.log('일치하는 정보가 없습니다.')
            }else{
              alert("회원님의 ID : " + res.data)
            }

          }).catch((err) => {
        if (err.message.indexOf('Network Error') > -1) {
          alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
        }
      })
    }
  },

  computed: {
    ...mapGetters({
      errorState: 'getErrorState',
    })
  }
}
</script>


<style>
#loginForm {
  width: 500px;
  margin: auto;

}
.a{
  text-decoration-line:none;
}
.bg-light{
  height: 1053px;
  padding-top:55px;
  padding-bottom:75px;
}

.container.py-4{
  margin: 0 auto;
  width : 503px;
}

.social-btn-area{
  display: flex;
  justify-content: center;
  border: none;
  border-radius: 5px;
  font-size: 18px;
  margin-top: 10px;
  cursor: pointer;
  background-color: transparent; /* 배경을 투명하게 설정 */
  padding: 0; /* 불필요한 여백 제거 */
}

.kakao-btn-img{
  padding : 0;
  height: auto; /* 비율을 유지 */
  max-width: 100%; /* 부모 요소보다 커지지 않도록 제한 */

}

.kakao-btn{
  background-color: transparent; /* 배경을 투명하게 설정 */
  border: none;
}
</style>