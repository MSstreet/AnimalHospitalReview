<script>
import {mapActions} from "vuex";
export default {
  data(){
    return {
      userInfo: {
        userId: null,
        userIdx: null,
        userToken: null,
        userRole: null
      },
    };
  },
  created() {
    this.code = this.$route.query.code;
    this.getUserInfo();
  },
  methods: {
    ...mapActions(['socialLogin']),
    getUserInfo() {
      this.requestBody = { code: this.code };
      this.$axios.get("/oauth/kakao/user-info", {
        params: this.requestBody
      }).then((res) => {
        this.userInfo.userIdx = res.data.user_idx;
        this.userInfo.userId = res.data.user_id;
        this.userInfo.userToken = res.data.user_token;
        this.userInfo.userRole = res.data.user_role;
        localStorage.setItem('user_token', res.data.user_token);
        localStorage.setItem('user_role', res.data.user_role);
        localStorage.setItem('user_idx', res.data.user_idx);  // idx도 저장
        localStorage.setItem('user_id', res.data.user_id);     // userId도 저장
        this.fnSocialLogin();
      }).catch((err) => {
        if (err.message.indexOf('Network Error') > -1) {
          alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
        }
      })
    },
    async fnSocialLogin() {
      try {
        let loginResult = await this.socialLogin({user_id: this.userInfo.userId, user_idx: this.userInfo.userIdx});
        if (loginResult) {
          this.goToPages();
          console.log("this.userInfo.userId : "+  this.userInfo.userId)
        }
      } catch (err) {
        if (err.message.indexOf('Network Error') > -1) {
          alert('서버에 접속할 수 없습니다. 상태를 확인해주세요.')
        } else {
          alert('로그인 정보를 확인할 수 없습니다.')
        }
      }
    },
    goToPages() {
      this.$router.replace({
        name: 'kakaoJoin'
      })
    }
  }
}
</script>