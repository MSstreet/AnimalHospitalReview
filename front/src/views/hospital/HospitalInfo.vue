<template>
  <div class="row mb-3 mt-2" style="width: 100%">
    <div class="col-6">
      <div id="map" style="width: 27rem; height: 20rem;"></div>
    </div>
    <div class="text-left info-pos col-6 mt-3">
      <div class="color49 mt-3" style="font-size: 14px; font-weight: 400; letter-spacing: -0.6px;" v-if="addr">
        <div>
          <p class="text-title text-lg-start">주소</p>
        </div>
        <div>
          <p class="text-content text-lg-start">{{ addr }}</p>
        </div>
      </div>
      <div class="color49 mt-3" style="font-size: 14px; font-weight: 400; letter-spacing: -0.6px;" v-if="number">
        <div>
          <p class="text-title text-lg-start">전화번호</p>
        </div>
        <div>
          <p class="text-content text-lg-start">{{ number }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
console.log('=== HospitalInfo.vue 파일이 파싱됨 ===');
export default {
  props: {
    idx: {
      type: [String, Number],
      default: null
    },
    requestBody: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      localRequestBody: this.requestBody || this.$route.query,
      localIdx: this.idx || this.$route.query.idx,
      map: null,
      addr: '',
      number: '',
      hos_latitude: '',
      hos_longitude: ''
    }
  },
  created() {
    console.log('=== HospitalInfo 컴포넌트 created 시작 ===');
    console.log('HospitalInfo 컴포넌트 created - idx:', this.localIdx, 'requestBody:', this.localRequestBody);
    console.log('props idx:', this.idx);
    console.log('props requestBody:', this.requestBody);
    console.log('$route.query:', this.$route.query);
    console.log('=== HospitalInfo 컴포넌트 created 완료 ===');
    this.fnGetView();
  },
  methods: {
    initMap() {
      const map = document.getElementById("map");
      if (!map) {
        console.error('지도 컨테이너를 찾을 수 없습니다.');
        return;
      }
      if (isNaN(this.hos_latitude) || isNaN(this.hos_longitude)) {
        console.error('지도 초기화 실패: 유효하지 않은 좌표값');
        return;
      }
      const options = {
        center: new kakao.maps.LatLng(Number(this.hos_latitude), Number(this.hos_longitude)),
        level: 5
      };
      try {
        this.map = new kakao.maps.Map(map, options);
        this.displayMakers(Number(this.hos_latitude), Number(this.hos_longitude));
      } catch (error) {
        console.error('지도 생성 실패:', error);
      }
    },
    displayMakers(latitude, longitude) {
      if (isNaN(latitude) || isNaN(longitude)) {
        console.error('마커 표시 실패: 유효하지 않은 좌표값');
        return;
      }
      try {
        const position = new kakao.maps.LatLng(Number(latitude), Number(longitude));
        const marker = new kakao.maps.Marker({ position });
        marker.setMap(this.map);
      } catch (error) {
        console.error('마커 표시 실패:', error);
      }
    },
    fnGetView() {
      this.$axios.get('/hospital/' + this.localIdx, {
        params: this.localRequestBody
      }).then((res) => {
        this.addr = res.data.hospital_addr;
        this.number = res.data.hospital_num;

        // 좌표값 콘솔 출력
        console.log('API 응답 위도:', res.data.hos_latitude);
        console.log('API 응답 경도:', res.data.hos_longitude);

        this.hos_latitude = Number(res.data.hos_latitude);
        this.hos_longitude = Number(res.data.hos_longitude);

        // 변환된 값 콘솔 출력
        console.log('변환된 위도:', this.hos_latitude);
        console.log('변환된 경도:', this.hos_longitude);

        if (isNaN(this.hos_latitude) || isNaN(this.hos_longitude)) {
          alert('병원 위치 정보를 가져올 수 없습니다.');
          return;
        }
        if (!window.kakao || !window.kakao.maps) {
          const script = document.createElement("script");
          script.src = "//dapi.kakao.com/v2/maps/sdk.js?appkey=d65815a3036b82a47f46344d7cf57bdf&autoload=false";
          script.addEventListener("load", () => {
            kakao.maps.load(this.initMap);
          });
          document.head.appendChild(script);
        } else {
          kakao.maps.load(this.initMap);
        }
      }).catch((err) => {
        if (err.message.indexOf('Network Error') > -1) {
          alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.');
        }
      });
    }
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