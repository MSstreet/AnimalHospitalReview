<script>
import axios from 'axios'
import {mapActions} from "vuex";
export default {
  data(){
    return {
      userInfo: {
        userId: null,
        nickIdx: null,
        userToken: null,
        userRole: null
      },
    };
  },
  created() {
    this.code = this.$route.query.code;
    this.getJwtToken();
  }
  ,methods: {
    ...mapActions(['socialLogin']),
    getJwtToken() {
      this.requestBody = { // 데이터 전송
        code: this.code
      }
      this.$axios.get(this.$serverUrl + "/oauth/kakao/user-info", {
        params: this.requestBody,
        headers: {}
      }).then((res) => {
        this.userInfo.userId = res.data.user_id;
        this.userInfo.userIdx = res.data.user_idx;
        this.userInfo.userToken = res.data.user_token;
        this.userInfo.userRoleole = res.data.user_role;

        localStorage.setItem('user_token', res.data.user_token);
        localStorage.setItem('user_role', res.data.user_role);
        this.fnSocialLogin();
      }).catch((err) => {
        if (err.message.indexOf('Network Error') > -1) {
          alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
        }
      })
    }
    , async fnSocialLogin() {       //async 함수로 변경
      try {
        let loginResult = await this.socialLogin({user_id: this.userInfo.userId, user_idx :this.userInfo.userIdx});
        if (loginResult) {
          this.goToPages()
          console.log(" this.userInfo.userId : "+  this.userInfo.userId)
        }
      } catch (err) {
        if (err.message.indexOf('Network Error') > -1) {
          alert('서버에 접속할 수 없습니다. 상태를 확인해주세요.')
        } else {
          alert('로그인 정보를 확인할 수 없습니다.')
        }
      }
    },goToPages() {
      this.$router.replace('/kakao-join',
          { state:
                { id: this.userInfo.userId}
          });
  }

  }
}
</script>