<template>
  <div class="login-root">
    <div class="container py-5">
      <div class="login-container">
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
              <div class="input-wrapper">
                <input 
                  type="email" 
                  maxlength="50" 
                  class="form-control" 
                  :class="{ 'is-invalid': showEmailError }"
                  id="exampleInputEmail1" 
                  placeholder="example@email.com" 
                  v-model="user_id" 
                  @blur="validateEmailInput"
                  aria-label="이메일" 
                  required
                >
                <div v-if="showEmailError" class="validation-popup">
                  올바른 이메일 형식이 아닙니다.
                </div>
              </div>
            </div>
            <div class="form-group">
              <label for="exampleInputPassword1" class="fw-bold mb-1">비밀번호</label>
              <input type="password" maxlength="50" class="form-control" id="exampleInputPassword1" placeholder="Password" autocomplete="on" v-model="user_pw" aria-label="비밀번호" required>
            </div>
            <div class="login-action-group">
              <button type="submit" class="login-btn" aria-label="로그인">로그인</button>
              <router-link to="/join" class="signup-btn" aria-label="회원가입">회원가입</router-link>
              <div class="find-links">
                <button type="button" class="find-link" data-bs-toggle="modal" data-bs-target="#findId" tabindex="0" aria-label="ID 찾기">ID를 잊으셨나요?</button>
                <button type="button" class="find-link" data-bs-toggle="modal" data-bs-target="#findPw" tabindex="0" aria-label="비밀번호 찾기">비밀번호를 잊으셨나요?</button>
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>
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
      showEmailError: false,
    };
  },
  methods: {
    ...mapActions(['login']),
    validateEmail(email) {
      const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
      return emailRegex.test(email);
    },
    validateEmailInput() {
      if (this.user_id) {
        this.showEmailError = !this.validateEmail(this.user_id);
      }
    },
    async fnLogin() {
      if (this.user_id === '') {
        alert('이메일을 입력하세요.');
        return;
      }
      if (!this.validateEmail(this.user_id)) {
        alert('올바른 이메일 형식이 아닙니다.');
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
  background-color: #f8f9fa;
  padding: 2rem 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
.login-container {
  width: 100%;
  max-width: 800px;
  /*max-width: 600px;*/
  margin: 0 auto;
  padding: 2rem 10rem;
  background: white;
  border-radius: 1rem;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
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
  width: 100%;
  margin-bottom: 1.2rem;
}
.kakao-btn {
  width: 100%;
  display: block;
  background: #fee500;
  color: #3c1e1e;
  border: none;
  border-radius: 8px;
  padding: 0.85rem 0;
  font-size: 1.08rem;
  font-weight: 600;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
  transition: background 0.2s;
  margin-bottom: 0.5rem;
  letter-spacing: 0.01em;
}
.kakao-btn:active {
  background: #ffe066;
}

.form-group {
  width: 100%;
  margin-bottom: 0.7rem;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}
.form-group label {
  margin-bottom: 0.3rem;
  width: auto;
}
.form-group input {
  width: 100%;
  box-sizing: border-box;
  font-size: 1rem;
  padding: 0.7rem 1rem;
  border-radius: 8px;
  border: 1.5px solid #e0e0e0;
  margin-bottom: 0.2rem;
}

.login-action-group {
  display: flex;
  flex-direction: column;
  gap: 0.7rem;
  margin-top: 1.2rem;
}
.login-btn, .signup-btn {
  width: 100%;
  border-radius: 8px;
  font-size: 1.08rem;
  font-weight: 600;
  padding: 0.85rem 0;
  border: none;
  transition: background 0.2s;
}
.login-btn {
  background: #3498db;
  color: #fff;
  margin-bottom: 0.2rem;
}
.login-btn:active {
  background: #217dbb;
}
.signup-btn {
  background: #eaf3fb;
  color: #3498db;
  margin-bottom: 0.2rem;
  text-align: center;
}
.signup-btn:active {
  background: #d4e6f7;
}
.find-links {
  display: flex;
  justify-content: space-between;
  gap: 0.5rem;
  margin-top: 0.5rem;
}
.find-link {
  color: #3498db;
  background: none;
  border: none;
  font-size: 0.98rem;
  text-decoration: underline;
  cursor: pointer;
  padding: 0.2rem 0.1rem;
  transition: color 0.2s;
}
.find-link:active {
  color: #217dbb;
}

.input-wrapper {
  position: relative;
  width: 100%;
}

.validation-popup {
  position: absolute;
  top: calc(100% + 5px);
  left: 0;
  background-color: #dc3545;
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  font-size: 0.875rem;
  z-index: 1000;
  animation: fadeIn 0.2s ease-in-out;
}

.validation-popup::before {
  content: '';
  position: absolute;
  top: -4px;
  left: 10px;
  width: 8px;
  height: 8px;
  background-color: #dc3545;
  transform: rotate(45deg);
}

.is-invalid {
  border-color: #dc3545;
}

.is-invalid:focus {
  border-color: #dc3545;
  box-shadow: 0 0 0 0.2rem rgba(220, 53, 69, 0.25);
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
.divider {
  display: flex;
  align-items: center;
  margin: 1.5rem 0 1rem 0;
  width: 100%;
  position: relative;
}
.divider hr {
  flex: 1;
  border: none;
  border-top: 1px solid #ccc; /* #eee에서 #ddd로 더 진하게 변경 */
  margin: 0;
}
.divider-text {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  background: #fff;
  padding: 0 0.7rem;
  color: #aaa;
  font-size: 0.98rem;
  font-weight: 500;
  letter-spacing: 0.01em;
  z-index: 1;
}
@media (max-width: 600px) {
  .login-container {
    max-width: 98vw;
    padding: 1.2rem 0.5rem 1.5rem 0.5rem;
  }
  .site-title h1 {
    font-size: 1.3rem;
  }
  .intro-text {
    font-size: 0.95rem;
  }
  .kakao-btn {
    font-size: 1rem;
    padding: 0.8rem 0;
  }
  .login-btn, .signup-btn {
    font-size: 1rem;
    padding: 0.8rem 0;
  }
  .find-links {
    flex-direction: column;
    gap: 0.2rem;
    align-items: flex-start;
  }
}
</style>