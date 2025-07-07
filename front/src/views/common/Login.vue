<template>
  <div class="login-root">
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
                      <input type="text" maxlength="50" id="userName" name="userName" class="form-control" placeholder="이름" v-model="user_name" required>
                    </div>
                    <div class="input-group input-group-outline my-3">
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
                    <div class="input-group input-group-outline my-3">
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

    <section class="login-body">
      <div class="login-container">
        <div class="site-title">
          <h1>ANIMAL HOSPITA</h1>
          <p class="intro-text ">신뢰할 수 있는 리뷰로 동물병원을 선택하세요.</p>
        </div>

        <div class="kakao-btn-container">
          <button class="kakao-btn" @click="kakaoLogin">
            <span>카카오로 시작하기</span>
          </button>
        </div>

        <div class="divider">
          <hr />
          <span class="divider-text">이메일 로그인</span>
        </div>

        <div class="container py-4">
          <form @submit.prevent="fnLogin">
            <div class="form-group">
              <label for="exampleInputEmail1" class="fw-bold mb-1">이메일</label>
              <input type="text" maxlength="50" class="form-control" id="exampleInputEmail1" placeholder="Enter Id" v-model="user_id">
            </div>

            <div class="form-group">
              <label for="exampleInputPassword1" class="fw-bold mb-1 mt-4">비밀번호</label>
              <input type="password" maxlength="50" class="form-control" id="exampleInputPassword1" placeholder="Password" autocomplete="on" v-model="user_pw">
            </div>

           <div class="">
            <div class="d-grid gap-2 mt-2">
               <button type="submit" class="btn btn-primary mt-3 fw-bold">로그인</button>
            </div>
            <div class="d-grid gap-2 mt-2">
              <router-link to="/join" type="button" class="btn btn-secondary mt-1 fw-bold">회원가입</router-link>
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
  </div>
</template>

<script>
import {mapActions, mapGetters} from 'vuex'   //vuex 추가

export default {

  data() {
    return {
      user_id: '',
      user_pw: '',
      user_name:'',
      user_email: '',
      userInfo: null,
    }

  }
  ,mounted() {

  },
  methods: {
    ...mapActions(['login']),
    async fnLogin() {
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
        let loginResult = await this.login({user_id: this.user_id, user_pw: this.user_pw })

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
    }
    ,async kakaoLogin() {
      try {
        const redirectUri = 'http://localhost:8080/oauth/kakao/callback';
        const clientId = '409b3fb04dd78999f86c8dbc4a19372a';
        const Auth_url = `https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=${clientId}&redirect_uri=${redirectUri}`;
        window.location.href = Auth_url;

      } catch (error) {
        console.error("카카오 로그인 URL을 가져오는 중 오류가 발생했습니다.", error);
      }
    }
    ,goToPages() {
      this.$router.replace({
        // path: './write',
        name: 'PageHome'
      })
    }
    ,checkEmail(){
      let apiUrl = '/user/find/pw?userEmail=' + this.user_email

      this.$axios.post(apiUrl, {
        params: {
          user_email: this.user_email
        }
      }).then((res)=> {
               console.log(res.data)
              if(res.data == '') {
                alert('일치하는 정보가 없습니다.')
              }else{
                alert("임시 비밀번호가 이메일로 전송되었습니다.\n로그인 후 반드시 비밀번호를 변경해주세요.")
              }

            }).catch((err) => {
          if (err.message.indexOf('Network Error') > -1) {
            alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
          }
        })
    }
    , fnFind(){
      let apiUrl = '/user/find'

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
.login-root {
  width: 100%;
}

.site-title {
  margin-top: 15px;
  margin-bottom: 30px; /* 제목과 로그인 폼 사이 간격 */
  margin-left: 27px;
}

.site-title h1 {
  margin-bottom: 0;
  font-size: 2rem; /* 제목 크기 */
  font-weight: bold;
  color: #333; /* 원하는 색상 */
}

.site-title p {
  margin-top: 0;
}

.intro-text {
  font-size: 1rem; /* 소개 텍스트 크기 */
  color: #666; /* 다소 연한 색상 */
  margin-top: 5px; /* 제목과 소개 텍스트 간격 */
  line-height: 1.5; /* 텍스트 간격 */
}

/* 구분선 스타일 */
.divider {
  position: relative;
  text-align: center;
  margin: 30px 0; /* 구분선의 위 아래 여백 */
}

.divider hr {
  border: 0;
  border-top: 1px solid #ccc; /* 구분선 색상 */
  width: 100%; /* 구분선의 너비 */
  margin: 0;
}

.divider-text {
  position: absolute;
  top: -0.7em; /* 구분선 위에 텍스트 위치 */
  left: 50%; /* 텍스트의 좌측을 가운데로 설정 */
  transform: translateX(-50%); /* 텍스트를 정확히 가운데로 정렬 */
  background-color: white;
  font-weight: bold;
  color: #ccc;
  font-size: 0.7rem;
}

.login-body {
  margin: 0;
  font-family: Arial, sans-serif;
  background-color: rgba(245, 245, 245, 0.8) !important; /* 부드러운 배경 색상 */
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh; /* 화면 전체 높이 */

}

.login-container {
  background: white;
  padding: 2rem 2rem 3rem;
  border-radius: 12px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1); /* 부드러운 그림자 */
  width: 100%;
  max-width: 600px; /* 최대 너비 */
  min-height: 600px; /* 최소 높이를 500px로 설정 */

}

.a{
  text-decoration-line:none;
}

.container.py-4{
  margin: 0 auto;
  width : 503px;
}

.kakao-btn-container {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
  padding-bottom: 30px;
  /*order-bottom: 2px solid #ddd; /* 회색 구분선 추가 */

}

.kakao-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #fee500; /* 카카오 노란색 */
  color: #000000; /* 검은색 텍스트 */
  font-size: 16px;
  font-weight: bold;
  border: none;
  border-radius: 8px;
  padding: 12px 16px;
  cursor: pointer;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  width: 490px
}

.kakao-btn:hover {
  transform: translateY(-2px); /* 약간 올라가는 효과 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 그림자 강화 */
}

.kakao-btn:active {
  transform: translateY(0); /* 원래 위치로 돌아오기 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
</style>