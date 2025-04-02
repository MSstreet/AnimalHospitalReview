<template>
<div class="hospital-detail-container">
  <div class="hospital-content">
    <!-- Breadcrumb -->
    <div class="breadcrumb">
      <span class="breadcrumb-item">{{ addr1[1] }}</span>
      <span class="breadcrumb-separator">/</span>
      <span class="breadcrumb-item">{{ addr1[2] }}</span>
    </div>

    <!-- Hospital Header -->
    <div class="hospital-header">
      <div class="hospital-title-section">
        <h1 class="hospital-name">{{hospital_name}}</h1>
        <div class="hospital-rating">
          <div class="rating-score">
            <i class="fas fa-star"></i>
            <span class="score">{{hospital_score}}</span>
          </div>
          <div class="review-count">
            <i class="fas fa-comment"></i>
            <span>리뷰 ({{review_count}})</span>
          </div>
        </div>
      </div>
      <div class="hospital-actions">
        <button class="wish-button" @click="changeHeart(`${wish_state}`)">
          <span class="heart-icon" :class="{ 'active': wish_state == 1 }">
            {{ wish_state != 1 ? '🤍' : '🧡' }}
          </span>
        </button>
      </div>
    </div>

    <!-- Action Buttons -->
    <div class="action-buttons">
      <button class="btn btn-outline-secondary" @click="fnHosList">
        <i class="fas fa-list"></i> 목록
      </button>
      <button class="btn btn-primary" @click="reviewWrite(`${hospital_id}`)">
        <i class="fas fa-pen"></i> 리뷰 작성
      </button>
    </div>

    <!-- Tab Navigation -->
    <div class="tab-navigation">
      <button 
        class="tab-button" 
        :class="{ active: comp === 'HospitalInfo' }"
        @click="changeComponent('HospitalInfo')"
      >
        <i class="fas fa-hospital"></i> 병원 정보
      </button>
      <button 
        class="tab-button"
        :class="{ active: comp === 'ReviewDetail' }"
        @click="changeComponent('ReviewDetail')"
      >
        <i class="fas fa-comments"></i> 리뷰
      </button>
    </div>

    <!-- Content Area -->
    <div class="content-area">
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
.hospital-detail-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
}

.hospital-content {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 2rem;
}

.breadcrumb {
  margin-bottom: 1.5rem;
  color: #666;
  font-size: 0.9rem;
}

.breadcrumb-separator {
  margin: 0 0.5rem;
  color: #999;
}

.hospital-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 2rem;
  padding-bottom: 1rem;
  border-bottom: 1px solid #eee;
}

.hospital-title-section {
  flex: 1;
}

.hospital-name {
  font-size: 2rem;
  font-weight: 700;
  color: #333;
  margin-bottom: 0.5rem;
}

.hospital-rating {
  display: flex;
  gap: 1rem;
  color: #666;
}

.rating-score, .review-count {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.rating-score i {
  color: #ffc107;
}

.wish-button {
  background: none;
  border: none;
  cursor: pointer;
  padding: 0.5rem;
  transition: transform 0.2s;
}

.wish-button:hover {
  transform: scale(1.1);
}

.heart-icon {
  font-size: 2.5rem;
  transition: all 0.3s ease;
}

.heart-icon.active {
  transform: scale(1.1);
}

.action-buttons {
  display: flex;
  gap: 1rem;
  margin-bottom: 2rem;
}

.action-buttons .btn {
  padding: 0.5rem 1.5rem;
  border-radius: 8px;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  transition: all 0.3s ease;
}

.tab-navigation {
  display: flex;
  gap: 1rem;
  margin-bottom: 2rem;
  border-bottom: 2px solid #eee;
  padding-bottom: 1rem;
}

.tab-button {
  background: none;
  border: none;
  padding: 0.8rem 1.5rem;
  font-size: 1.1rem;
  color: #666;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.tab-button:hover {
  color: #007bff;
}

.tab-button.active {
  color: #007bff;
  font-weight: 600;
  border-bottom: 2px solid #007bff;
  margin-bottom: -1rem;
}

.content-area {
  min-height: 400px;
}

@media (max-width: 768px) {
  .hospital-detail-container {
    padding: 1rem;
  }

  .hospital-content {
    padding: 1rem;
  }

  .hospital-header {
    flex-direction: column;
    gap: 1rem;
  }

  .hospital-name {
    font-size: 1.5rem;
  }

  .action-buttons {
    flex-direction: column;
  }

  .tab-navigation {
    flex-direction: column;
    gap: 0.5rem;
  }

  .tab-button {
    width: 100%;
    justify-content: center;
  }
}
</style>