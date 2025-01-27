<!-- PageHome.vue -->
<template>

<section class="" style="margin-top: 30px">
  <div class="container text-center mt-5 " >
    <div id="mainslider" class="mt-3" >
      <splide :options="options">
        <splide-slide>
          <img style="border-radius: 70px" src="../assets/home.jpg" alt="main2">
        </splide-slide>
        <splide-slide>
          <img style="border-radius: 70px" src="../assets/home1.jpg" alt="main3">
        </splide-slide>
        <splide-slide>
          <img style="border-radius: 70px" src="../assets/home2.jpg" alt="main3">
        </splide-slide>
      </splide>
    </div>
  </div>
  <div class="search-container mt-5">
    <form action="" method="">
      <div class="input-area p-3 input-group input-group-lg">
        <select class="styled-select" v-model="search_key">
          <option value="author" selected>병원명</option>
          <option value="title">지역명</option>
        </select>
        <input type="text" maxlength="50" class="form-control ms-1" placeholder="검색어 입력" aria-label="search"
               aria-describedby="button-addon2" v-model="search_value" >
        <button class="styled-button ms-1" id="button-addon2"  @click="fnPage1(`${search_key}`,`${search_value}`)">검색</button>
      </div>
    </form>
  </div>
  <div class="container-fluid pt-1" style="text-align: center">
    <div class="" style="width: 960px; display: inline-block">
      <div class="row text-left px-3 margin-b" style="margin-top: 48px;">

        <div class="col-4 px-2">
          <div class=" pb-2" style="font-size: 21px; color:#494949; border-bottom: 2px solid #494949">
            <i style="font-size: 21px;" class="fas fa-location">
            </i>
            부천시
          </div>

          <div class="row mt-3 px-3">
            <div class="col-4 p-0">
              <div class="fw-bold tt purple-link mb-1" @click="fnPage(`범안로`,'title')">
                범안로
              </div>
            </div>
            <div class="col-4 p-0">
              <div class="fw-bold tt purple-link mb-1" @click="fnPage('중동로','title')">
                중동로
              </div>
            </div>
            <div class="col-4 p-0">
              <div class="fw-bold tt purple-link mb-1" @click="fnPage('역곡로','title')">
                역곡로
              </div>
            </div>
          </div>
          <div class="row mt-3 px-3">
            <div class="col-4 p-0">
              <div class="fw-bold tt purple-link mb-1" @click="fnPage('상일로','title')">
                상일로
              </div>
            </div>
            <div class="col-4 p-0">
              <div class="fw-bold tt purple-link mb-1" @click="fnPage('소향로','title')">
                소향로
              </div>
            </div>
            <div class="col-4 p-0">
              <div class="fw-bold tt purple-link mb-1" @click="fnPage('송내대로','title')">
                송내대로
              </div>
            </div>
          </div>

          <div class="row mt-3 px-3">
            <div class="col-4 p-0">
              <div class="fw-bold tt purple-link mb-1" @click="fnPage('부일로','title')">
                부일로
              </div>
            </div>

            <div class="col-4 p-0">
              <div class="fw-bold tt purple-link mb-1" @click="fnPage('상동로','title')">
                상동로
              </div>
            </div>
          </div>
        </div>

        <div class="col-4 px-2">
          <div class="pb-2" style="font-size: 21px; color:#494949; border-bottom: 2px solid #494949">
            <i style="font-size: 21px;" class="fas fa-location" >
            </i>
            수원시
          </div>

          <div class="row mt-3 px-3">
            <div class="col-4 p-0">
              <div class="fw-bold tt purple-link mb-1" @click="fnPage('권선구','title')">
                권선구
              </div>
            </div>
            <div class="col-4 p-0">
              <div class="fw-bold tt purple-link mb-1" @click="fnPage('영통로','title')">
                영통구
              </div>
            </div>
            <div class="col-4 p-0">
              <div class="fw-bold tt purple-link mb-1" @click="fnPage('장안구','title')">
                장안구
              </div>
            </div>
          </div>
          <div class="row mt-3 px-3">
            <div class="col-4 p-0">
              <div class="fw-bold tt purple-link mb-1" @click="fnPage('팔달구','title')">
                팔달구
              </div>
            </div>

          </div>
        </div>

        <div class="col-4 px-2">
          <div class="pb-2" style="font-size: 21px; color:#494949; border-bottom: 2px solid #494949">
            <i style="font-size: 21px;" class="fas fa-location">
            </i>
            성남시
          </div>

          <div class="row mt-3 px-3">
            <div class="col-4 p-0">
              <div class="fw-bold tt purple-link mb-1" @click="fnPage('수정구','title')">
                수정구
              </div>
            </div>
            <div class="col-4 p-0">
              <div class="fw-bold tt purple-link mb-1" @click="fnPage('분당구','title')">
                분당구
              </div>
            </div>
            <div class="col-4 p-0">
              <div class="fw-bold tt purple-link mb-1" @click="fnPage('중원구','title')">
                중원구
              </div>
            </div>
          </div>

        </div>

        <div class="col-4 px-2 mt-5">
          <div class="pb-2" style="font-size: 21px; color:#494949; border-bottom: 2px solid #494949">
            <i style="font-size: 21px;" class="fas fa-location" >
            </i>
            용인시
          </div>

          <div class="row mt-3 px-3">
            <div class="col-4 p-0">
              <div class="fw-bold tt purple-link mb-1" @click="fnPage('수지구','title')">
                수지구
              </div>
            </div>
          </div>
        </div>

        <div class="col-4 px-2 mt-5">
          <div class="pb-2" style="font-size: 21px; color:#494949; border-bottom: 2px solid #494949">
            <i style="font-size: 21px;" class="fas fa-location">
            </i>
            시흥시
          </div>

          <div class="row mt-3 px-3">
            <div class="col-4 p-0">
              <div class="fw-bold tt purple-link mb-1" @click="fnPage('수인로','title')">
                수인로
              </div>
            </div>

            <div class="col-4 p-0">
              <div class="fw-bold tt purple-link mb-1" @click="fnPage('승지로','title')">
                승지로
              </div>
            </div>
            <div class="col-4 p-0">
              <div class="fw-bold tt purple-link mb-1" @click="fnPage('은행로','title')">
                은행로
              </div>
            </div>
          </div>

          <div class="row mt-3 px-3">
            <div class="col-4 p-0">
              <div class="fw-bold tt purple-link mb-1" @click="fnPage('함송로','title')">
                함송로
              </div>
            </div>
            <div class="col-4 p-0">
              <div class="fw-bold tt purple-link mb-1" @click="fnPage('정왕대로','title')">
                정왕대로
              </div>
            </div>
            <div class="col-4 p-0">
              <div class="fw-bold tt purple-link mb-1" @click="fnPage('호현로','title')">
                호현로
              </div>
            </div>
          </div>

          <div class="row mt-3 px-3">


            <div class="col-4 p-0">
              <div class="fw-bold tt purple-link mb-1" @click="fnPage('대은로','title')">
                대은로
              </div>
            </div>
          </div>
        </div>

        <div class="col-4 px-2 mt-5">
          <div class="pb-2" style="font-size: 21px; color:#494949; border-bottom: 2px solid #494949">
            <i style="font-size: 21px;" class="fas fa-location">
            </i>
            안산시
          </div>

          <div class="row mt-3 px-3">
            <div class="col-4 p-0">
              <div class="fw-bold tt purple-link mb-1" @click="fnPage('단원구','title')">
                단원구
              </div>
            </div>
            <div class="col-4 p-0">
              <div class="fw-bold tt purple-link mb-1" @click="fnPage('상록구','title')">
                상록구
              </div>
            </div>
          </div>
        </div>

        <div class="col-4 px-2 mt-5">
          <div class="pb-2" style="font-size: 21px; color:#494949; border-bottom: 2px solid #494949">
            <i style="font-size: 21px;" class="fas fa-location">
            </i>
            고양시
          </div>

          <div class="row mt-3 px-3">
            <div class="col-4 p-0">
              <div class="fw-bold tt purple-link mb-1" @click="fnPage('덕양구','title')">
                덕양구
              </div>
            </div>

            <div class="col-4 p-0">
              <div class="fw-bold tt purple-link mb-1" @click="fnPage('일산동구','title')">
                일산동구
              </div>
            </div>

            <div class="col-4 p-0">
              <div class="fw-bold tt purple-link mb-1" @click="fnPage('일산서구','title')">
                일산서구
              </div>
            </div>
          </div>
        </div>

        <div class="col-4 px-2 mt-5">
          <div class="pb-2" style="font-size: 21px; color:#494949; border-bottom: 2px solid #494949">
            <i style="font-size: 21px;" class="fas fa-location">
            </i>
            광명시
          </div>

          <div class="row mt-3 px-3">
            <div class="col-4 p-0">
              <div class="fw-bold tt purple-link mb-1" @click="fnPage('광덕산로','title')">
                광덕산로
              </div>
            </div>

            <div class="col-4 p-0">
              <div class="fw-bold tt purple-link mb-1" @click="fnPage('하안로','title')">
                하안로
              </div>
            </div>

            <div class="col-4 p-0">
              <div class="fw-bold tt purple-link mb-1" @click="fnPage('디지털로','title')">
                디지털로
              </div>
            </div>
          </div>

          <div class="row mt-3 px-3">
            <div class="col-4 p-0">
              <div class="fw-bold tt purple-link mb-1" @click="fnPage('소하로','title')">
                소하로
              </div>
            </div>

            <div class="col-4 p-0">
              <div class="fw-bold tt purple-link mb-1" @click="fnPage('금하로','title')">
                금하로
              </div>
            </div>

            <div class="col-4 p-0">
              <div class="fw-bold tt purple-link mb-1" @click="fnPage('오리로','title')">
                오리로
              </div>
            </div>
          </div>
        </div>



        <div class="col-4 px-2 mt-5">
          <div class="pb-2" style="font-size: 21px; color:#494949; border-bottom: 2px solid #494949">
            <i style="font-size: 21px;" class="fas fa-location">
            </i>
            평택시
          </div>

          <div class="row mt-3 px-3">
            <div class="col-4 p-0">
              <div class="fw-bold tt purple-link mb-1" @click="fnPage('송탄로','title')">
                송탄로
              </div>
            </div>

            <div class="col-4 p-0">
              <div class="fw-bold tt purple-link mb-1" @click="fnPage('평택로','title')">
                평택로
              </div>
            </div>

            <div class="col-4 p-0">
              <div class="fw-bold tt purple-link mb-1" @click="fnPage('탄현로','title')">
                탄현로
              </div>
            </div>
          </div>

          <div class="row mt-3 px-3">
            <div class="col-4 p-0">
              <div class="fw-bold tt purple-link mb-1" @click="fnPage('청북읍','title')">
                청북읍
              </div>
            </div>

            <div class="col-4 p-0">
              <div class="fw-bold tt purple-link mb-1" @click="fnPage('중앙로','title')">
                중앙로
              </div>
            </div>

            <div class="col-4 p-0">
              <div class="fw-bold tt purple-link mb-1" @click="fnPage('서정역로','title')">
                서정역로
              </div>
            </div>
          </div>

          <div class="row mt-3 px-3">
            <div class="col-4 p-0">
              <div class="fw-bold tt purple-link mb-1" @click="fnPage('송탄로','title')">
                송탄로
              </div>
            </div>

            <div class="col-4 p-0">
              <div class="fw-bold tt purple-link mb-1" @click="fnPage('안중읍','title')">
                안중읍
              </div>
            </div>


          </div>
        </div>
      </div>
      <div class="row text-left px-3" style="margin-top: 48px;">
      </div>
    </div>
  </div>
</section>
</template>

<script>
// @ is an alias to /src
import HelloWorld from '@/components/HelloWorld.vue'
import { Splide, SplideSlide } from '@splidejs/vue-splide'
import '@splidejs/splide/dist/css/themes/splide-default.min.css';
export default {
  name: 'PageHome',
  components: {
    HelloWorld,
    Splide,
    SplideSlide
  },
  data() {
    return {
      userInfo: {
        userId: null,
        nickname: null,
      },
      options: {
        type: 'fade',
        rewind: true,
        perPage: 1,
        autoplay: true,
        pauseOnHover: false,
        arrows: false,
        dots: true,
        animatedDots: true
      }
      ,map: null,
      markers: [],
      latitude: 0,
      longitude: 0,
      requestBody: {}, //리스트 페이지 데이터전송
      list: {}, //리스트 데이터
      no: '', //게시판 숫자처리
      paging: {
        block: 0,
        end_page: 0,
        next_block: 0,
        page: 0,
        page_size: 0,
        prev_block: 0,
        start_index: 0,
        start_page: 0,
        total_block_cnt: 0,
        total_list_cnt: 0,
        total_page_cnt: 0,
      }, //페이징 데이터
      page: this.$route.query.page ? this.$route.query.page : 1,
      size: this.$route.query.size ? this.$route.query.size : 10,
      //search_key: this.$route.query.sk ? this.$route.query.sk : '',
      search_key:"author",
      //search_key:this.search_key,
      search_value: this.$route.query.sv ? this.$route.query.sv : '',
      paginavigation: function () { //페이징 처리 for문 커스텀
        let pageNumber = [] //;
        let start_page = this.paging.start_page;
        let end_page = this.paging.end_page;
        for (let i = start_page; i <= end_page; i++) pageNumber.push(i);
        return pageNumber;
      }
    }
  }
  ,created(){
  }
  ,methods: {
    fnGetList() {
      this.requestBody = { // 데이터 전송
        sk: this.search_key,
        sv: this.search_value,
        page: this.page,
        size: this.size
      }
      this.$axios.get(this.$serverUrl + "/hospital/list", {
        params: this.requestBody,
        headers: {}
      }).then((res) => {
        if (res.data.result_code === "OK") {
          this.list = res.data.data
          this.paging = res.data.pagination
          this.no = this.paging.total_list_cnt - ((this.paging.page - 1) * this.paging.page_size)
        }
        // this.fnView()
        console.log(res.data.data);
        // console.log(res.data.pagination);
      }).catch((err) => {
        if (err.message.indexOf('Network Error') > -1) {
          alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
        }
      })
    }, fnGetUserInfo() {
      this.requestBody = { // 데이터 전송
        email: this.userInfo.userId,
        nickname: this.userInfo.nickname,
      }
      this.$axios.get(this.$serverUrl + "/oauth/kakao/user-info", {
        params: this.requestBody,
        headers: {}
      }).then((res) => {
        console.log(res.data);

      }).catch((err) => {
        if (err.message.indexOf('Network Error') > -1) {
          alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
        }
      })
    }
    ,fnPage(n,m) {
      console.log(n)
      this.search_key = m
      this.search_value = n
      console.log(this.search_value)
      this.$router.push({
        path: '/hospital/list1',
        query:{
          sk : this.search_key,
          sv : this.search_value
        }
      })
    }
    ,fnPage1(n,m) {
      console.log("@@@@@search_value@@@@@"+m)
      console.log("@@@@@search_key@@@@@"+n)
      this.search_value = m
      this.search_key = n
      console.log("!!!!!!!!!!search_value!!!!!!!!!!!"+this.search_value)
      this.$router.push({
        //path: '/hospital/list',
        path: '/hospital/list1',
        query:{
          sk :this.search_key,
          sv :this.search_value
        }
      })
    }
  }
}
</script>

<style scoped>
.home {
  text-align: center;
  margin: 10px;
}
.test-position{
  display: flex;
  justify-content: center;
  align-items: center;
}
.img-container{
  height: 200px;
  width: 100%;
  /*margin: auto;*/
}

.margin-b{
  margin-bottom: 7rem;
}
.root_cacao_roughmap {
  margin: auto !important;
}
.container {
  padding-right: 15px;
  padding-left: 15px;
  margin-right: auto;
  margin-left: auto;
}

.tt:hover{
  text-decoration: underline;
  text-decoration-thickness: 1px;
  text-decoration-color: #ff9900;
  color:#ff9900;
  cursor: pointer
}

.input-area{
  display: flex;
  justify-content: center;
  width: 600px;
}

.search-container {
  display: flex;
  justify-content: center;
  align-items: center;
}

</style>