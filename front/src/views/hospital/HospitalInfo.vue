<template>
  <div class="row mb-3 mt-2" style="width: 100%">

    <div class="col-6">
      <div id="map" style="width: 27rem; height: 20rem;">
      </div>
    </div>

    <div class="text-left info-pos col-6 mt-3">
      <div class="color49 mt-3" style="font-size: 14px; font-weight: 40; letter-spacing: -0.6px;">
        <div >
           <p class="text-title text-lg-start">주소</p>
        </div>
        <div>
          <p class="text-content text-lg-start">{{ addr }}</p>
        </div>
      </div>
      <div class="color49 mt-3" style="font-size: 14px; font-weight: 40; letter-spacing: -0.6px;">
        <div>
          <p class="text-title text-lg-start">전화번호</p>
        </div>
        <div>
          <p class="text-content text-lg-start">{{number }}</p>
        </div>

      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      requestBody: this.$route.query,
      idx: this.$route.query.idx,

      map: null,

      addr: '',
      number: '',
      hos_latitude: '',
      hos_longitude: ''

    }
  }
  , created() {
    this.fnGetView();
  }

  ,methods: {
    initMap() {
      const map = document.getElementById("map")

      const options = {
        center: new kakao.maps.LatLng(this.hos_latitude,this.hos_longitude),
        level: 5
      }

      this.map = new kakao.maps.Map(map, options)
      this.displayMakers(this.hos_latitude,this.hos_longitude)

    }

    ,displayMakers(latitude , longitude){
       const position = new kakao.maps.LatLng(latitude, longitude)
       const marker = new kakao.maps.Marker({
         position : position
       })
      marker.setMap(this.map)
    }

    ,fnGetView() {
      this.$axios.get(this.$serverUrl + '/hospital/' + this.idx, {
        params: this.requestBody
      }).then((res) => {
        this.addr = res.data.hospital_addr
        this.number = res.data.hospital_num

        this.hos_latitude = res.data.hos_latitude
        this.hos_longitude = res.data.hos_longitude

        const script = document.createElement("script")
        script.src = "//dapi.kakao.com/v2/maps/sdk.js?appkey=d65815a3036b82a47f46344d7cf57bdf&autoload=false"
        script.addEventListener("load", () => {
          kakao.maps.load(this.initMap)
        })
        document.head.appendChild(script)
      }).catch((err) => {
        if (err.message.indexOf('Network Error') > -1) {
          alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
        }
      })
    }
  }
  ,mounted() {

  }
}

</script>

<style>
.text-title{
  font-size: 1.8rem;
  font-weight: bold;
  color: black;
}
.text-content{
  font-size: 1.3rem;
  font-weight: bold;
  color: #d3d3d3;
}

</style>