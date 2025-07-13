<template>
  <div class="review-card shadow-lg rounded-4 p-4 mt-5 bg-white mx-auto" style="max-width: 500px;">
    <h2 class="mb-4 text-center fw-bold">{{ hospital_name }} 후기</h2>
    <form enctype="multipart/form-data" method="post" id="revieform" class="reviewform">
      <div class="mb-4">
        <label class="form-label fw-bold text-center w-100">별점과 리뷰를 남겨주세요.</label>
        <div class="row g-3">
          <div class="col-4 text-end pt-2">가격</div>
          <div class="col-8">
            <div class="star-rating star-rating-1">
              <input type="radio" id="5-stars-price" name="priceScore" v-model="priceScore" value="5" />
              <label for="5-stars-price" class="star">★</label>
              <input type="radio" id="4-stars-price" name="priceScore" v-model="priceScore" value="4" />
              <label for="4-stars-price" class="star">★</label>
              <input type="radio" id="3-stars-price" name="priceScore" v-model="priceScore" value="3" />
              <label for="3-stars-price" class="star">★</label>
              <input type="radio" id="2-stars-price" name="priceScore" v-model="priceScore" value="2" />
              <label for="2-stars-price" class="star">★</label>
              <input type="radio" id="1-stars-price" name="priceScore" v-model="priceScore" value="1" />
              <label for="1-stars-price" class="star">★</label>
            </div>
          </div>
        </div>
        <div class="row g-3 mt-2">
          <div class="col-4 text-end pt-2">친절</div>
          <div class="col-8">
            <div class="star-rating star-rating-2">
              <input type="radio" id="5-stars-kindness" name="kindnessScore" v-model="kindnessScore" value="5" />
              <label for="5-stars-kindness" class="star">★</label>
              <input type="radio" id="4-stars-kindness" name="kindnessScore" v-model="kindnessScore" value="4" />
              <label for="4-stars-kindness" class="star">★</label>
              <input type="radio" id="3-stars-kindness" name="kindnessScore" v-model="kindnessScore" value="3" />
              <label for="3-stars-kindness" class="star">★</label>
              <input type="radio" id="2-stars-kindness" name="kindnessScore" v-model="kindnessScore" value="2" />
              <label for="2-stars-kindness" class="star">★</label>
              <input type="radio" id="1-stars-kindness" name="kindnessScore" v-model="kindnessScore" value="1" />
              <label for="1-stars-kindness" class="star">★</label>
            </div>
          </div>
        </div>
        <div class="row g-3 mt-2">
          <div class="col-4 text-end pt-2">효과</div>
          <div class="col-8">
            <div class="star-rating star-rating-3">
              <input type="radio" id="5-stars-effect" name="effectScore" v-model="effectScore" value="5" />
              <label for="5-stars-effect" class="star">★</label>
              <input type="radio" id="4-stars-effect" name="effectScore" v-model="effectScore" value="4" />
              <label for="4-stars-effect" class="star">★</label>
              <input type="radio" id="3-stars-effect" name="effectScore" v-model="effectScore" value="3" />
              <label for="3-stars-effect" class="star">★</label>
              <input type="radio" id="2-stars-effect" name="effectScore" v-model="effectScore" value="2" />
              <label for="2-stars-effect" class="star">★</label>
              <input type="radio" id="1-stars-effect" name="effectScore" v-model="effectScore" value="1" />
              <label for="1-stars-effect" class="star">★</label>
            </div>
          </div>
        </div>
      </div>
      <div class="mb-4">
        <label class="form-label fw-bold">리뷰 내용</label>
        <textarea rows="6" class="form-control review_textarea" name="content" v-model="contents" placeholder="가격, 서비스, 효과 등을 자세히 기술해주세요." required></textarea>
        <div v-if="contents.length > 0 && contents.length < 5" class="text-danger mt-1">5자 이상으로 작성해 주세요.</div>
      </div>
      <div class="mb-4">
        <label class="form-label fw-bold">영수증 업로드</label>
        <input type="file" class="form-control" name="file" accept="image/*">
      </div>
      <div class="d-grid gap-2">
        <button type="button" class="btn btn-primary btn-lg" @click="fnTest()">저장</button>
      </div>
    </form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      requestBody: this.$route.query,
      pet_hospital_num: this.$route.query.idx,
      user_idx: this.$store.state.userIdx,

      priceScore:'',
      kindnessScore:'',
      effectScore:'',

      contents:'',
      addr1:'',
      hospital_id:'',
      hospital_name: '',
      hospital_num: '',
      hospital_addr:'',
      sigun_name: '',
      oper_state: '',
      hospital_score:'',
      review_count:'',

      error:false
    };
  },
  mounted() {
    //this.GetView()
    // console.log(this.idx)
    console.log("check체크!!!!!" + this.user_idx)
    console.log("check체크!!!!!" + this.user_idx)
    console.log(this.requestBody)
    this.fnGetView()
  },

  methods:{
    fnTest(){
      if (!this.priceScore) {
        alert("평점을 선택해주세요.")
        return false
      }
      if (!this.kindnessScore) {
        alert("평점을 선택해주세요.")
        return false
      }
      if (!this.effectScore) {
        alert("평점을 선택해주세요.")
        return false
      }
      if(!this.contents){
        alert("리뷰 내용을 입력해주세요.")
        return false
      }

      console.log(this.priceScore)
      console.log(this.kindnessScore)
      console.log(this.effectScore)

      const formData = new FormData(document.getElementById("revieform"))
      formData.append('petHospitalNum',this.pet_hospital_num)
      formData.append('userNum',this.user_idx)
      let apiUrl = '/review/join'
      if (this.idx === undefined) {
        //INSERT
        this.$axios.post(apiUrl, formData)
            .then((res) => {
              if(res.data.errorCode == 401){
                alert('올바른 이미지 파일을 등록해주세요.');
                return false
              }
              alert('글이 저장되었습니다.');
              this.fnView(res.data.pet_hospital_entity.hospital_id)

            }).catch((err) => {
          if (err.message.indexOf('Network Error') > -1) {
            alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.');
          }
        })
      } else {
        //UPDATE
        this.$axios.patch(apiUrl, this.form)
            .then((res) => {
              alert('글이 저장되었습니다.');
              this.fnView(res.data.idx)
            }).catch((err) => {
          if (err.message.indexOf('Network Error') > -1) {
            alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.');
          }
        })
      }
    }
    ,fnSave() {
      let apiUrl = '/review/join'

      this.form = {
        "user_num" : this.user_idx,
        "pet_hospital_num": this.pet_hospital_num,
        "content": this.contents,
        "score": this.score
      }

      if (this.idx === undefined) {
        //INSERT
        this.$axios.post(apiUrl, this.form)
            .then((res) => {
              alert('글이 저장되었습니다.')
              this.fnView(res.data.pet_hospital_entity.hospital_id)
              //console.log("확인!!!"+res.data.pet_hospital_entity.hospital_id)
            }).catch((err) => {
          if (err.message.indexOf('Network Error') > -1) {
            alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
          }
        })
      } else {
        //UPDATE
        this.$axios.patch(apiUrl, this.form)
            .then((res) => {
              alert('글이 저장되었습니다.')
              this.fnView(res.data.idx)
            }).catch((err) => {
          if (err.message.indexOf('Network Error') > -1) {
            alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
          }
        })
      }
    }
    ,fnView(idx) {
      this.requestBody.idx = idx
      this.$router.push({
        path: '/hospital/detail',
        query: this.requestBody
      })
    }
    ,fnGetView() {
              this.$axios.get('/hospital/' + this.pet_hospital_num, {
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
    }
  }
};

</script>

<style scoped>
.review-card {
  background: #fff;
  border-radius: 1.5rem;
  box-shadow: 0 4px 24px rgba(0,0,0,0.08);
  padding: 2rem;
  margin-top: 2rem;
}
.star-rating {
  display: flex;
  flex-direction: row-reverse;
  justify-content: flex-end;
  font-size: 2.2rem;
  gap: 0.2rem;
}
.star-rating input {
  display: none;
}
.star-rating label {
  color: #ccc;
  cursor: pointer;
  transition: color 0.2s;
}
.star-rating input:checked ~ label,
.star-rating label:hover,
.star-rating label:hover ~ label {
  color: #ffc107;
}
.form-label {
  font-size: 1.1rem;
}
.review_textarea {
  min-height: 120px;
  font-size: 1rem;
  border-radius: 0.5rem;
  border: 1px solid #e0e0e0;
  padding: 1rem;
}
.btn-primary {
  font-size: 1.2rem;
  padding: 0.75rem 0;
  border-radius: 0.7rem;
}
@media (max-width: 600px) {
  .review-card {
    padding: 1rem;
    margin-top: 1rem;
  }
  .star-rating {
    font-size: 1.5rem;
  }
}
.form-label.text-center {
  display: block;
  text-align: center;
}
</style>