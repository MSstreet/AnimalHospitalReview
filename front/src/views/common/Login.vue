<template>
  <div class="login-root responsive-root">
    <!-- ID 찾기 모달 -->
    <div id="findId" class="modal fade" tabindex="-1" aria-labelledby="findIdLabel" aria-modal="true" role="dialog">
      <div class="modal-dialog modal-dialog-centered modal-login">
        <div class="modal-content">
          <div class="modal-body">
            <div class="container my-auto">
              <div class="row">
                <div class="card z-index-0 fadeIn3 fadeInBottom">
                  <div class="card-header p-0 position-relative mt-n4 mx-3 z-index-2">
                    <div class="bg-gradient-primary shadow-primary border-radius-lg py-3 pe-1">
                      <h4 class="text-black font-weight-bolder text-center mt-2 mb-0" id="findIdLabel">ID 찾기</h4>
                    </div>
                  </div>
                  <div class="card-body">
                    <p>가입 시 입력한 이름과 Email을 입력해주세요.</p>
                    <div class="input-group input-group-outline my-3">
                      <input type="text" maxlength="50" id="userName" name="userName" class="form-control" placeholder="이름" v-model="user_name" required aria-label="이름">
                    </div>
                    <div class="input-group input-group-outline my-3">
                      <input type="email" maxlength="50" id="userEmail" name="memberEmail" class="form-control" placeholder="Email" v-model="user_email" required aria-label="이메일">
                    </div>
                    <div class="text-center">
                      <TouchFeedback>
                        <button type="button" class="btn btn-primary bg-gradient-primary w-100 my-4 mb-2" id="checkEmail" @click="fnFind">확인</button>
                      </TouchFeedback>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 비밀번호 찾기 모달 -->
    <div id="findPw" class="modal fade" tabindex="-1" aria-labelledby="findPwLabel" aria-modal="true" role="dialog">
      <div class="modal-dialog modal-dialog-centered modal-login">
        <div class="modal-content">
          <div class="modal-body">
            <div class="container my-auto">
              <div class="row">
                <div class="card z-index-0 fadeIn3 fadeInBottom">
                  <div class="card-header p-0 position-relative mt-n4 mx-3 z-index-2">
                    <div class="bg-gradient-primary shadow-primary border-radius-lg py-3 pe-1">
                      <h4 class="text-black font-weight-bolder text-center mt-2 mb-0" id="findPwLabel">비밀번호 찾기</h4>
                    </div>
                  </div>
                  <div class="card-body">
                    <p>입력한 이메일로 임시 비밀번호가 전송됩니다.</p>
                    <div class="input-group input-group-outline my-3">
                      <input type="email" maxlength="50" id="userEmailPw" name="memberEmail" class="form-control" placeholder="Email" v-model="user_email" required aria-label="이메일">
                    </div>
                    <div class="text-center">
                      <TouchFeedback>
                        <button type="button" class="btn btn-primary bg-gradient-primary w-100 my-4 mb-2" id="checkEmailPw" @click="checkEmail">비밀번호 발송</button>
                      </TouchFeedback>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <section class="login-body responsive-section">
      <div class="login-container responsive-container">
        <div class="site-title">
          <h1>ANIMAL HOSPITAL</h1>
          <p class="intro-text">신뢰할 수 있는 리뷰로 동물병원을 선택하세요.</p>
        </div>

        <div class="kakao-btn-container">
          <TouchFeedback>
            <button class="kakao-btn" @click="kakaoLogin" aria-label="카카오로 시작하기">
              <span>카카오로 시작하기</span>
            </button>
          </TouchFeedback>
        </div>

        <div class="divider">
          <hr />
          <span class="divider-text">이메일 로그인</span>
        </div>

        <div class="container py-4">
          <form @submit.prevent="fnLogin" autocomplete="on">
            <div class="form-group">
              <label for="exampleInputEmail1" class="fw-bold mb-1">이메일</label>
              <input type="text" maxlength="50" class="form-control" id="exampleInputEmail1" placeholder="Enter Id" v-model="user_id" aria-label="이메일" required>
            </div>

            <div class="form-group">
              <label for="exampleInputPassword1" class="fw-bold mb-1 mt-4">비밀번호</label>
              <input type="password" maxlength="50" class="form-control" id="exampleInputPassword1" placeholder="Password" autocomplete="on" v-model="user_pw" aria-label="비밀번호" required>
            </div>

            <div class="d-grid gap-2 mt-2">
              <TouchFeedback>
                <button type="submit" class="btn btn-primary mt-3 fw-bold" aria-label="로그인">로그인</button>
              </TouchFeedback>
            </div>
            <div class="d-grid gap-2 mt-2">
              <router-link to="/join" type="button" class="btn btn-secondary mt-1 fw-bold" aria-label="회원가입">회원가입</router-link>
            </div>
            <div class="mt-2">
              <TouchFeedback>
                <span type="button" class="btn btn-link a" data-bs-toggle="modal" data-bs-target="#findId" tabindex="0" aria-label="ID 찾기">ID를 잊으셨나요?</span>
              </TouchFeedback>
              <TouchFeedback>
                <span type="button" class="btn btn-link a" data-bs-toggle="modal" data-bs-target="#findPw" tabindex="0" aria-label="비밀번호 찾기">비밀번호를 잊으셨나요?</span>
              </TouchFeedback>
            </div>
          </form>
        </div>
      </div>
    </section>
    <LoadingSpinner v-if="isLoading" />
  </div>
</template>

<script>
import { mapActions } from 'vuex';
import TouchFeedback from '@/components/TouchFeedback.vue';
import LoadingSpinner from '@/components/LoadingSpinner.vue';

export default {
  components: { TouchFeedback, LoadingSpinner },
  data() {
    return {
      user_id: '',
      user_pw: '',
      user_name: '',
      user_email: '',
      userInfo: null,
      isLoading: false,
    };
  },
  methods: {
    ...mapActions(['login']),
    async fnLogin() {
      if (this.user_id === '') {
        alert('ID를 입력하세요.');
        return;
      }
      if (this.user_pw === '') {
        alert('비밀번호를 입력하세요.');
        return;
      }
      this.isLoading = true;
      try {
        let loginResult = await this.login({ user_id: this.user_id, user_pw: this.user_pw });
        if (loginResult) {
          this.goToPages();
          alert(this.user_id + '님 환영합니다.');
        }
      } catch (err) {
        if (err.message.indexOf('Network Error') > -1) {
          alert('서버에 접속할 수 없습니다. 상태를 확인해주세요.');
        } else {
          alert('로그인 정보를 확인할 수 없습니다.');
        }
      } finally {
        this.isLoading = false;
      }
    },
    async kakaoLogin() {
      try {
        const redirectUri = 'http://localhost:8080/oauth/kakao/callback';
        const clientId = '409b3fb04dd78999f86c8dbc4a19372a';
        const Auth_url = `https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=${clientId}&redirect_uri=${redirectUri}`;
        window.location.href = Auth_url;
      } catch (error) {
        console.error('카카오 로그인 URL을 가져오는 중 오류가 발생했습니다.', error);
      }
    },
    goToPages() {
      this.$router.replace({ name: 'PageHome' });
    },
    checkEmail() {
      let apiUrl = '/user/find/pw?userEmail=' + this.user_email;
      this.$axios.post(apiUrl, {
        params: {
          user_email: this.user_email,
        },
      })
        .then((res) => {
          if (res.data == '') {
            alert('일치하는 정보가 없습니다.');
          } else {
            alert('임시 비밀번호가 이메일로 전송되었습니다.\n로그인 후 반드시 비밀번호를 변경해주세요.');
          }
        })
        .catch((err) => {
          if (err.message.indexOf('Network Error') > -1) {
            alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.');
          }
        });
    },
    fnFind() {
      // ID 찾기 로직 구현 필요
      alert('ID 찾기 기능은 준비 중입니다.');
    },
  },
};
</script>

<style scoped>
@import '@/assets/common.css';
.login-root {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: #f8fafc;
}
.login-container {
  width: 100%;
  max-width: 400px;
  margin: 0 auto;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 2px 16px rgba(0,0,0,0.08);
  padding: 2rem 1.5rem 1.5rem 1.5rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}
.site-title h1 {
  font-size: 2rem;
  font-weight: bold;
  text-align: center;
  margin-bottom: 0.5rem;
}
.intro-text {
  text-align: center;
  color: #666;
  font-size: 1rem;
  margin-bottom: 1.5rem;
}
.kakao-btn-container {
  display: flex;
  justify-content: center;
  margin-bottom: 1rem;
}
.kakao-btn {
  width: 100%;
  background: #fee500;
  color: #3c1e1e;
  border: none;
  border-radius: 8px;
  padding: 0.75rem 0;
  font-size: 1.1rem;
  font-weight: bold;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
  transition: background 0.2s;
}
.kakao-btn:active {
  background: #ffe066;
}
.divider {
  display: flex;
  align-items: center;
  margin: 1.5rem 0 1rem 0;
}
.divider hr {
  flex: 1;
  border: none;
  border-top: 1px solid #eee;
}
.divider-text {
  margin: 0 1rem;
  color: #aaa;
  font-size: 0.95rem;
}
@media (max-width: 600px) {
  .login-container {
    max-width: 95vw;
    padding: 1.2rem 0.5rem 1.5rem 0.5rem;
  }
  .site-title h1 {
    font-size: 1.3rem;
  }
  .intro-text {
    font-size: 0.95rem;
  }
}
</style>