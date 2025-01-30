<template>

<div class="hos-detail-container">
    <div class="hos-info-detail">
        <div class="hos-info">
          <div class=" ">
            <div style="font-weight: 300;">
              {{ addr1[1] }}
              >
              {{ addr1[2] }}
            </div>
          </div>
          <div class="hos-text">
            <div>
              <h1>{{hospital_name }}</h1>
            </div>
            <div class="hos-heart">
              <a style="text-decoration-line: none;" id="check" @click="changeHeart(`${wish_state}`)">
                <span style="font-size: 3rem" id="heart" class=" " v-if="wish_state != 1">🤍</span>
                <span style="font-size: 3rem" id="heart" class=" ms-2" v-else-if="wish_state == 1">🧡</span>
              </a>
            </div>
          </div>

          <div class="hos-review-btn">
            <div class="" >
              <span class="mr-1" style="font-weight: 700;">{{hospital_score}}</span>
              | <span class="ml-1">리뷰 ({{review_count}})</span>
            </div>
            <div class="hos-detail-btn" >
              <a class="btn btn-success" v-on:click="fnHosList">목록</a>
              <a class="btn btn-primary" v-on:click="reviewWrite(`${hospital_id}`)">리뷰</a>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="border px-3 middle-tab" style="top:72px !important;">
      <div class="position">
        <button class="btn fs-3" @click="changeComponent('HospitalInfo')">병원 정보
        </button>
        <button class="btn fs-3" @click="changeComponent('ReviewDetail')">리뷰
        </button>
      </div>
      <keep-alive>
        <component v-bind:is="comp"></component>
      </keep-alive>
    </div>


</template>

<script>
import ReviewDetail from "@/views/review/ReviewDetail";
import HospitalInfo from "@/views/hospital/HospitalInfo";

export default {
  components:{
    ReviewDetail,
    HospitalInfo
  },
  data(){
    return {
      comp:'HospitalInfo',

      requestBody: this.$route.query,

      idx: this.$route.query.idx,
      user_idx : this.$store.state.userIdx,

      heartval: 0,
      clicked : 0,
      wish_check:0,

      addr1:'',
      hospital_id:'',
      hospital_name: '',
      hospital_num: '',
      hospital_addr:'',
      sigun_name: '',
      oper_state: '',
      hospital_score:'',
      review_count:'',

      wish_state: ''
    }
  }
  , mounted() {
    this.wishCheck()
    this.fnGetView()
  },
  methods:{
    changeComponent: function (componentName){
      this.comp = componentName
    }
    ,fnGetReviewAvg(){
      this.$axios.get(this.$serverUrl + '/review/avg/' +this.idx)
          .then((res) => {

            this.hospital_score = res.data
      }).catch((err) => {
        if (err.message.indexOf('Network Error') > -1) {
          alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
        }
      })
    }
    ,changeHeart(heart){
      if(heart == 1){
        this.heartval = 0
        document.getElementById("heart").innerText = "🤍";

      }else{
        this.heartval = 1
        document.getElementById("heart").innerText = "🧡"
      }

      let apiUrl = this.$serverUrl + '/wish'

      this.form = {
        "user_num" : this. user_idx,
        "pet_hospital_num": this.idx,
        "wish_state1" : this.heartval
      }

      this.$axios.post(apiUrl, this.form)
          .then((res) => {
            this.wish_state = res.data.wish_state1

            console.log(this.wish_state)

          }).catch((err) => {
        if (err.message.indexOf('Network Error') > -1) {
          alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
        }
      })
    }
    ,fnView(idx) {
      this.requestBody.idx = idx
      this.$router.push({
        path: '/review/detail',
        query: this.requestBody
      })
    }
    ,fnGetView() {
      this.$axios.get(this.$serverUrl + '/hospital/' + this.idx, {
        params: this.requestBody
      }).then((res) => {

        this.hospital_id = res.data.hospital_id
        this.hospital_name = res.data.hospital_name
        this.hospital_num = res.data.hospital_num
        this.hospital_addr = res.data.hospital_addr
        this.hospital_score = res.data.hospital_score
        this.sigun_name = res.data.sigun_name
        this.oper_state = res.data.oper_state
        this.review_count = res.data.review_count

        this.addr1 = this.hospital_addr.split(' ',3)

        console.log(this.hospital_name)
        console.log(this.hospital_id )

      }).catch((err) => {
        if (err.message.indexOf('Network Error') > -1) {
          alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
        }
      })
    },fnList() {
      delete this.requestBody.idx

      this.$router.push({
        path: './list',
        query: this.requestBody
      })
    },reviewWrite(idx){
      this.requestBody.idx = idx

      this.$router.push({
        path: '/review/write',
        query: this.requestBody
      })
    }
    ,wishCheck(){

      this.$axios.get(this.$serverUrl + "/wish/one/" + this.user_idx + "/" + this.idx,{
        params: this.requestBody,
        headers: {}
      }).then((res) => {
            console.log(res.data)

        this.wish_state = res.data.wish_state1

          }).catch((err) => {
        if (err.message.indexOf('Network Error') > -1) {

          alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
        }
      })
    }
    ,fnHosList(){
      delete this.requestBody.idx

      this.$router.push({
        path: './list',
        query: this.requestBody
      })
    }
  }
}

</script>

<style>
.hos-detail-container{
  display: flex;
  flex-direction: column;
  align-items: center; /* 가로 중앙 정렬 */
  margin-top: 10px;
}

.hos-info-detail{
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.hos-review-btn{
  display: flex;
  justify-content: space-between;
}

.position{
  display: flex;
  flex-direction:row;
  justify-content: space-evenly;
}

.hos-info{
 display: flex;
 justify-content: space-between;
}

.hos-text{
display: flex;
justify-content: space-between;
}
.hos-detail-btn {
  display: flex;
  justify-content: space-between; /* 버튼을 양쪽으로 정렬 */
  gap: 10px; /* 버튼 간 간격 */
  margin-top: 20px; /* 상단 여백 */
}

.hos-detail-btn .btn {
  display: inline-flex; /* 버튼 안의 텍스트를 정렬하기 위해 inline-flex 사용 */
  align-items: center; /* 세로 정렬 */
  justify-content: center; /* 가로 정렬 (필요시) */
  line-height: 1.5; /* 텍스트 높이 조정 */
  padding: 10px 20px; /* 버튼 크기 조정 */
  font-size: 16px; /* 텍스트 크기 */
  vertical-align: middle; /* 텍스트 정렬 보정 */
  border-radius: 8px; /* 둥근 버튼 */

}
</style>