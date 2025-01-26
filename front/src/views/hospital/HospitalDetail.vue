<template>

<div class="text-center">
  <div style="width: 960px; display: inline-block">
    <div class="row p-3">
      <div class="col-12 border-0">
          <div class=" ml-3 col-20 text-left d-flex align-items-center justify-content-between pt-2">
            <div style="font-weight: 300;">
              {{ addr1[1] }}
               >
              {{ addr1[2] }}
            </div>
          </div>

        <div class="hos-info">
          <div>
             {{hospital_name }}
            <a style="text-decoration-line: none;" id="check" @click="changeHeart(`${wish_state}`)">
              <span style="font-size: 3rem" id="heart" class=" " v-if="wish_state != 1">🤍</span>
              <div style="font-size: 1rem" id="heart" class=" " v-if="wish_state != 1">찜하기</div>
              <span style="font-size: 3rem" id="heart" class=" ms-2" v-else-if="wish_state == 1">🧡</span>
              <div style="font-size: 1rem" id="heart" class=" ms-2" v-else-if="wish_state == 1">찜완료!</div>
            </a>
          </div>
          <div class="" >
            <a class=" btn btn-success  " v-on:click="fnHosList">목록</a>
            <a class="btn btn-primary " v-on:click="reviewWrite(`${hospital_id}`)"><i class="fas fa-edit"></i> 리뷰 남기기</a>
          </div>
        </div>

        <div class="d-flex align-items-start py-2 color49 pb-1">
          <div class="review" style="position: relative; font-size: 1.5rem;letter-spacing: 1px;">
            <span class="mr-1" style="font-weight: 700;">{{hospital_score}}</span>
            | <span class="ml-1">리뷰 ({{review_count}})</span>
          </div>
        </div>

        <div class=" pb-2 pt-2 btn-pos">

        </div>


      </div>
    </div>

    <div class="row border px-3 middle-tab" style="top:72px !important;">
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
  </div>
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
  .review{
    left: 0.2rem;
  }

  .name{
    position: relative;
    right: 0.5rem;
    top: 0.2rem;
  }

  .position{
    display: flex;
    flex-direction:row;
    justify-content: space-evenly;
  }

  .btn-pos{
    text-align: left;
  }
  .info-pos{
    text-align: left;
  }
 .heart-position{
    position: relative;
    right: 17rem;
    bottom:4.2rem;
}
 .hos-info{
   display: flex;
 }

</style>