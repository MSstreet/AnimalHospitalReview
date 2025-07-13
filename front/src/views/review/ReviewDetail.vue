<template>
  <div class="review-detail-container modern-review-container">
    <div class="mt-5 mb-3 pt-3">
      <!-- 모달 -->
      <div v-if="isModalVisible" class="modal fade show" id="report" tabindex="-1" aria-labelledby="reportLabel" aria-hidden="true" style="display: block;">
        <div class="modal-dialog modal-dialog-centered modal-login">
          <div class="modal-content">
            <div class="modal-body">
              <div class="container my-auto">
                <div class="row">
                  <div class="card z-index-0 fadeIn3 fadeInBottom">
                    <div class="p-0 position-relative mt-n4 mx-3 z-index-2">
                      <div class="bg-gradient-danger shadow-danger border-radius-lg py-3 pe-1">
                        <h4 class="text-black font-weight-bolder text-center mt-2 mb-0">신고하기</h4>
                      </div>
                    </div>
                    <div class="card-body">
                      <p class="text-start">신고 사유를 선택하고 추가 설명을 입력해주세요.</p>

                      <!-- 신고 사유 라디오 버튼 -->
                      <div class="form-check text-start">
                        <input class="form-check-input" type="radio" name="reportReason" id="spam" value="1" v-model="reportReason">
                        <label class="form-check-label text-start" for="spam">스팸/홍보</label>
                      </div>
                      <div class="form-check text-start">
                        <input class="form-check-input" type="radio" name="reportReason" id="hateSpeech" value="2" v-model="reportReason">
                        <label class="form-check-label" for="hateSpeech">혐오 발언</label>
                      </div>
                      <div class="form-check text-start">
                        <input class="form-check-input" type="radio" name="reportReason" id="falseInfo" value="3" v-model="reportReason">
                        <label class="form-check-label" for="falseInfo">허위 정보</label>
                      </div>
                      <!-- 추가 설명 입력 -->
                      <div class="input-group input-group-outline my-3">
                        <textarea id="reportDetails" class="form-control" rows="4" placeholder="추가 설명을 입력해주세요." v-model="reportDetails"></textarea>
                      </div>
                      <!-- 신고하기 버튼 -->
                      <div class="text-center">
                        <button type="button" class="btn btn-danger bg-gradient-danger w-100 my-4 mb-2" @click="fnSubmitReport()">신고하기</button>
                      </div>
                      <!-- 취소 버튼 -->
                      <div class="text-center">
                        <button type="button" class="btn btn-secondary w-100" @click="closeModal" data-bs-dismiss="modal">취소</button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="boarder review-score-card">
        <div class="row px-3">
          <div class="col-12 px-3 pt-3 pb-0 mb-3 score-summary">
            <div style="letter-spacing: -1.05px; text-align: center; color: #9b9b9b; font-size: 18px" class="mb-2">
              평점 {{ avgScore }}
            </div>
            <span v-if="avgScore < 0.5">
              <i class="fa-regular fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
            </span>
            <span v-if="(avgScore >= 0.5 && avgScore < 1)">
              <i class="fa-solid fa-star-half-stroke fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
            </span>
            <span v-if="(avgScore >= 1 && avgScore < 1.5)">
                  <i class="fa-solid fa-star fa-lg"></i>
                  <i class="fa-regular fa-star fa-lg"></i>
                  <i class="fa-regular fa-star fa-lg"></i>
                  <i class="fa-regular fa-star fa-lg"></i>
                  <i class="fa-regular fa-star fa-lg"></i>
            </span>
            <span v-if="(avgScore >= 1.5 && avgScore < 2)">
                <i class="fa-solid fa-star fa-lg"></i>
                <i class="fa-solid fa-star-half-stroke fa-lg"></i>
                <i class="fa-regular fa-star fa-lg"></i>
                <i class="fa-regular fa-star fa-lg"></i>
                <i class="fa-regular fa-star fa-lg"></i>
            </span>
            <span v-if="(avgScore >= 2 && avgScore < 2.5)">
                 <i class="fa-solid fa-star fa-lg"></i>
                 <i class="fa-solid fa-star fa-lg"></i>
                <i class="fa-regular fa-star fa-lg"></i>
                <i class="fa-regular fa-star fa-lg"></i>
                <i class="fa-regular fa-star fa-lg"></i>
            </span>
            <span v-if="(avgScore >= 2.5 && avgScore < 3)">
                <i class="fa-solid fa-star fa-lg"></i>
                <i class="fa-solid fa-star fa-lg"></i>
                <i class="fa-solid fa-star-half-stroke fa-lg"></i>
                <i class="fa-regular fa-star fa-lg"></i>
                <i class="fa-regular fa-star fa-lg"></i>
            </span>
            <span v-if="(avgScore >= 3 && avgScore < 3.5)">
                <i class="fa-solid fa-star fa-lg"></i>
                <i class="fa-solid fa-star fa-lg"></i>
                <i class="fa-solid fa-star fa-lg"></i>
                <i class="fa-regular fa-star fa-lg"></i>
                <i class="fa-regular fa-star fa-lg"></i>
            </span>
            <span v-if="(avgScore >= 3.5 && avgScore < 4)">
                <i class="fa-solid fa-star fa-lg"></i>
                <i class="fa-solid fa-star fa-lg"></i>
                <i class="fa-solid fa-star fa-lg"></i>
                <i class="fa-solid fa-star-half-stroke fa-lg"></i>
                <i class="fa-regular fa-star fa-lg"></i>
            </span>
            <span v-if="(avgScore >= 4 && avgScore < 4.5)">
                <i class="fa-solid fa-star fa-lg"></i>
                <i class="fa-solid fa-star fa-lg"></i>
                <i class="fa-solid fa-star fa-lg"></i>
                <i class="fa-solid fa-star fa-lg"></i>
                <i class="fa-regular fa-star fa-lg"></i>
            </span>
            <span v-if="(avgScore >= 4.5 && avgScore < 5)">
                <i class="fa-solid fa-star fa-lg"></i>
                <i class="fa-solid fa-star fa-lg"></i>
                <i class="fa-solid fa-star fa-lg"></i>
                <i class="fa-solid fa-star fa-lg"></i>
                <i class="fa-solid fa-star-half-stroke fa-lg"></i>
            </span>
            <span v-if="(avgScore >= 4.8 && avgScore <= 5)">
              <i class="fa-solid fa-star fa-lg"></i>
                <i class="fa-solid fa-star fa-lg"></i>
                <i class="fa-solid fa-star fa-lg"></i>
                <i class="fa-solid fa-star fa-lg"></i>
               <i class="fa-solid fa-star fa-lg"></i>
           </span>
            <div class="row mt-3">
            <div class="col-4">
              <div>
              가격 평점
            </div>
            <div v-if="avg_price_score < 0.5">
              <i class="fa-regular fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
            </div>

            <div v-if="(avg_price_score >= 0.5 && avg_price_score < 1)">
              <i class="fa-solid fa-star-half-stroke fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
            </div>

            <div v-if="(avg_price_score >= 1 && avg_price_score < 1.5)">
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
            </div>

            <div v-if="(avg_price_score >= 1.5 && avg_price_score < 2)">
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-solid fa-star-half-stroke fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
            </div>

            <div v-if="(avg_price_score >= 2 && avg_price_score < 2.5)">
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
            </div>

            <div v-if="(avg_price_score >= 2.5 && avg_price_score < 3)">
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-solid fa-star-half-stroke fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
            </div>

            <div v-if="(avg_price_score >= 3 && avg_price_score < 3.5)">
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
            </div>

            <div v-if="(avg_price_score >= 3.5 && avg_price_score < 4)">
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-solid fa-star-half-stroke fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
            </div>

            <div v-if="(avg_price_score >= 4 && avg_price_score < 4.5)">
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
            </div>

            <div v-if="(avg_price_score >= 4.5 && avg_price_score < 5)">
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-solid fa-star-half-stroke fa-lg"></i>
            </div>

            <div v-if="(avg_price_score >= 4.8 && avg_price_score <= 5)">
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-solid fa-star fa-lg"></i>
            </div>
          </div>

          <div class="col-4">
            <div>
              친절 평점
            </div>
            <div v-if="avg_kindness_score < 0.5">
              <i class="fa-regular fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
            </div>

            <div v-if="(avg_kindness_score >= 0.5 && avg_kindness_score < 1)">
              <i class="fa-solid fa-star-half-stroke fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
            </div>

            <div v-if="(avg_kindness_score >= 1 && avg_kindness_score < 1.5)">
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
            </div>

            <div v-if="(avg_kindness_score >= 1.5 && avg_kindness_score < 2)">
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-solid fa-star-half-stroke fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
            </div>

            <div v-if="(avg_kindness_score >= 2 && avg_kindness_score < 2.5)">
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
            </div>

            <div v-if="(avg_kindness_score >= 2.5 && avg_kindness_score < 3)">
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-solid fa-star-half-stroke fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
            </div>

            <div v-if="(avg_kindness_score >= 3 && avg_kindness_score < 3.5)">
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
            </div>

            <div v-if="(avg_kindness_score >= 3.5 && avg_kindness_score < 4)">
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-solid fa-star-half-stroke fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
            </div>

            <div v-if="(avg_kindness_score >= 4 && avg_kindness_score < 4.5)">
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-regular fa-star fa-lg"></i>
            </div>

            <div v-if="(avg_kindness_score >= 4.5 && avg_kindness_score < 5)">
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-solid fa-star-half-stroke fa-lg"></i>
            </div>

            <div v-if="(avg_kindness_score >= 4.8 && avg_kindness_score <= 5)">
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-solid fa-star fa-lg"></i>
              <i class="fa-solid fa-star fa-lg"></i>
            </div>
          </div>

             <div class="col-4">
                <div>
                  효과 평점
                </div>
                <div v-if="avg_effect_score < 0.5">
                  <i class="fa-regular fa-star fa-lg"></i>
                  <i class="fa-regular fa-star fa-lg"></i>
                  <i class="fa-regular fa-star fa-lg"></i>
                  <i class="fa-regular fa-star fa-lg"></i>
                  <i class="fa-regular fa-star fa-lg"></i>
                </div>

                <div v-if="(avg_effect_score >= 0.5 && avg_effect_score < 1)">
                  <i class="fa-solid fa-star-half-stroke fa-lg"></i>
                  <i class="fa-regular fa-star fa-lg"></i>
                  <i class="fa-regular fa-star fa-lg"></i>
                  <i class="fa-regular fa-star fa-lg"></i>
                  <i class="fa-regular fa-star fa-lg"></i>
                </div>

                <div v-if="(avg_effect_score >= 1 && avg_effect_score < 1.5)">
                  <i class="fa-solid fa-star fa-lg"></i>
                  <i class="fa-regular fa-star fa-lg"></i>
                  <i class="fa-regular fa-star fa-lg"></i>
                  <i class="fa-regular fa-star fa-lg"></i>
                  <i class="fa-regular fa-star fa-lg"></i>
                </div>

                <div v-if="(avg_effect_score >= 1.5 && avg_effect_score < 2)">
                  <i class="fa-solid fa-star fa-lg"></i>
                  <i class="fa-solid fa-star-half-stroke fa-lg"></i>
                  <i class="fa-regular fa-star fa-lg"></i>
                  <i class="fa-regular fa-star fa-lg"></i>
                  <i class="fa-regular fa-star fa-lg"></i>
                </div>

                <div v-if="(avg_effect_score >= 2 && avg_effect_score < 2.5)">
                  <i class="fa-solid fa-star fa-lg"></i>
                  <i class="fa-solid fa-star fa-lg"></i>
                  <i class="fa-regular fa-star fa-lg"></i>
                  <i class="fa-regular fa-star fa-lg"></i>
                  <i class="fa-regular fa-star fa-lg"></i>
                </div>

                <div v-if="(avg_effect_score >= 2.5 && avg_effect_score < 3)">
                  <i class="fa-solid fa-star fa-lg"></i>
                  <i class="fa-solid fa-star fa-lg"></i>
                  <i class="fa-solid fa-star-half-stroke fa-lg"></i>
                  <i class="fa-regular fa-star fa-lg"></i>
                  <i class="fa-regular fa-star fa-lg"></i>
                </div>

                <div v-if="(avg_effect_score >= 3 && avg_effect_score < 3.5)">
                  <i class="fa-solid fa-star fa-lg"></i>
                  <i class="fa-solid fa-star fa-lg"></i>
                  <i class="fa-solid fa-star fa-lg"></i>
                  <i class="fa-regular fa-star fa-lg"></i>
                  <i class="fa-regular fa-star fa-lg"></i>
                </div>

                <div v-if="(avg_effect_score >= 3.5 && avg_effect_score < 4)">
                  <i class="fa-solid fa-star fa-lg"></i>
                  <i class="fa-solid fa-star fa-lg"></i>
                  <i class="fa-solid fa-star fa-lg"></i>
                  <i class="fa-solid fa-star-half-stroke fa-lg"></i>
                  <i class="fa-regular fa-star fa-lg"></i>
                </div>

                <div v-if="(avg_effect_score >= 4 && avg_effect_score < 4.5)">
                  <i class="fa-solid fa-star fa-lg"></i>
                  <i class="fa-solid fa-star fa-lg"></i>
                  <i class="fa-solid fa-star fa-lg"></i>
                  <i class="fa-solid fa-star fa-lg"></i>
                  <i class="fa-regular fa-star fa-lg"></i>
                </div>

                <div v-if="(avg_effect_score >= 4.5 && avg_effect_score < 5)">
                  <i class="fa-solid fa-star fa-lg"></i>
                  <i class="fa-solid fa-star fa-lg"></i>
                  <i class="fa-solid fa-star fa-lg"></i>
                  <i class="fa-solid fa-star fa-lg"></i>
                  <i class="fa-solid fa-star-half-stroke fa-lg"></i>
                </div>

                <div v-if="(avg_effect_score >= 4.8 && avg_effect_score <= 5)">
                  <i class="fa-solid fa-star fa-lg"></i>
                  <i class="fa-solid fa-star fa-lg"></i>
                  <i class="fa-solid fa-star fa-lg"></i>
                  <i class="fa-solid fa-star fa-lg"></i>
                  <i class="fa-solid fa-star fa-lg"></i>
                </div>
            </div>
          </div>
        </div>
      </div>

      <div class="mt-5 border-top border-bottom py-3 review-card" v-for="(row, idx) in list" :key="idx">
        <div class="row fw-semibold review-card-header">
          <div class="col-3 mb-2 receipt-badge">
            <i class="fa-solid fa-check fa-lg"></i> 영수증 인증
          </div>
        </div>
        <div class="d-flex justify-content-between align-items-center review-card-user">
          <h3 class="text-lg-start reviewer-id">{{row.user_id}}</h3>
          <div>
            <button class="btn btn-secondary me-2 btn-sm review-btn" v-if="row.user_num == log_id" @click="fnDelete(row.review_id)">삭제</button>
            <button class="btn btn-secondary btn-sm review-btn" v-if="row.user_num == log_id" @click="fnUpdate(idx, row.review_id)">수정</button>
          </div>
        </div>
        <div class="container1">
            <div v-if="row.score == 1" class="test-position1">
              <div class="align-items-center justify-content-between d-flex flex-colum">
                <div class="row my-1 text-nowrap">
                  <div class="col-7 p-0">
                    <img src="https://d23zwvh2kbhdec.cloudfront.net/static_22_11_25/img/mdd_event/Star-Full-icon.svg" alt="Responsive image" class="star-image-middle">
                    <img src="https://d23zwvh2kbhdec.cloudfront.net/static_22_11_25/img/mdd_event/Star-Empty-icon.svg" alt="Responsive image" class="star-image-middle">
                    <img src="https://d23zwvh2kbhdec.cloudfront.net/static_22_11_25/img/mdd_event/Star-Empty-icon.svg" alt="Responsive image" class="star-image-middle">
                    <img src="https://d23zwvh2kbhdec.cloudfront.net/static_22_11_25/img/mdd_event/Star-Empty-icon.svg" alt="Responsive image" class="star-image-middle">
                    <img src="https://d23zwvh2kbhdec.cloudfront.net/static_22_11_25/img/mdd_event/Star-Empty-icon.svg" alt="Responsive image" class="star-image-middle">
                  </div>
                </div>
              </div>
            </div>
            <div v-if="row.score == 2" class="test-position1">
              <div class="align-items-center justify-content-between d-flex flex-colum">
                <div class="row my-1 text-nowrap">
                  <div class="col-7 p-0">
                    <img src="https://d23zwvh2kbhdec.cloudfront.net/static_22_11_25/img/mdd_event/Star-Full-icon.svg" alt="Responsive image" class="star-image-middle">
                    <img src="https://d23zwvh2kbhdec.cloudfront.net/static_22_11_25/img/mdd_event/Star-Full-icon.svg" alt="Responsive image" class="star-image-middle">
                    <img src="https://d23zwvh2kbhdec.cloudfront.net/static_22_11_25/img/mdd_event/Star-Empty-icon.svg" alt="Responsive image" class="star-image-middle">
                    <img src="https://d23zwvh2kbhdec.cloudfront.net/static_22_11_25/img/mdd_event/Star-Empty-icon.svg" alt="Responsive image" class="star-image-middle">
                    <img src="https://d23zwvh2kbhdec.cloudfront.net/static_22_11_25/img/mdd_event/Star-Empty-icon.svg" alt="Responsive image" class="star-image-middle">
                  </div>
                </div>
              </div>
            </div>
            <div v-if="row.score == 3" class="test-position1">
              <div class="align-items-center justify-content-between d-flex flex-colum">
                <div class="row my-1 text-nowrap">
                  <div class="col-7 p-0">
                    <img src="https://d23zwvh2kbhdec.cloudfront.net/static_22_11_25/img/mdd_event/Star-Full-icon.svg" alt="Responsive image" class="star-image-middle">
                    <img src="https://d23zwvh2kbhdec.cloudfront.net/static_22_11_25/img/mdd_event/Star-Full-icon.svg" alt="Responsive image" class="star-image-middle">
                    <img src="https://d23zwvh2kbhdec.cloudfront.net/static_22_11_25/img/mdd_event/Star-Full-icon.svg" alt="Responsive image" class="star-image-middle">
                    <img src="https://d23zwvh2kbhdec.cloudfront.net/static_22_11_25/img/mdd_event/Star-Empty-icon.svg" alt="Responsive image" class="star-image-middle">
                    <img src="https://d23zwvh2kbhdec.cloudfront.net/static_22_11_25/img/mdd_event/Star-Empty-icon.svg" alt="Responsive image" class="star-image-middle">

                  </div>
                </div>
              </div>
            </div>
            <div v-if="row.score == 4" class="test-position1">
              <div class="align-items-center justify-content-between d-flex flex-colum">
                <div class="row my-1 text-nowrap">
                  <div class="col-7 p-0">
                    <img src="https://d23zwvh2kbhdec.cloudfront.net/static_22_11_25/img/mdd_event/Star-Full-icon.svg" alt="Responsive image" class="star-image-middle">
                    <img src="https://d23zwvh2kbhdec.cloudfront.net/static_22_11_25/img/mdd_event/Star-Full-icon.svg" alt="Responsive image" class="star-image-middle">
                    <img src="https://d23zwvh2kbhdec.cloudfront.net/static_22_11_25/img/mdd_event/Star-Full-icon.svg" alt="Responsive image" class="star-image-middle">
                    <img src="https://d23zwvh2kbhdec.cloudfront.net/static_22_11_25/img/mdd_event/Star-Full-icon.svg" alt="Responsive image" class="star-image-middle">
                    <img src="https://d23zwvh2kbhdec.cloudfront.net/static_22_11_25/img/mdd_event/Star-Empty-icon.svg" alt="Responsive image" class="star-image-middle">
                  </div>
                </div>
              </div>
            </div>
            <div v-if="row.score == 5" class="test-position1">
              <div class="align-items-center justify-content-between d-flex flex-colum">
                <div class="row my-1 text-nowrap">
                  <div class="col-7 p-0">
                    <img src="https://d23zwvh2kbhdec.cloudfront.net/static_22_11_25/img/mdd_event/Star-Full-icon.svg" alt="Responsive image" class="star-image-middle">
                    <img src="https://d23zwvh2kbhdec.cloudfront.net/static_22_11_25/img/mdd_event/Star-Full-icon.svg" alt="Responsive image" class="star-image-middle">
                    <img src="https://d23zwvh2kbhdec.cloudfront.net/static_22_11_25/img/mdd_event/Star-Full-icon.svg" alt="Responsive image" class="star-image-middle">
                    <img src="https://d23zwvh2kbhdec.cloudfront.net/static_22_11_25/img/mdd_event/Star-Full-icon.svg" alt="Responsive image" class="star-image-middle">
                    <img src="https://d23zwvh2kbhdec.cloudfront.net/static_22_11_25/img/mdd_event/Star-Full-icon.svg" alt="Responsive image" class="star-image-middle">
                  </div>
                </div>
              </div>
            </div>
          <div class="ms-4 created-at review-date">
            {{ new Date(row.created_at).toISOString().split('T')[0].replace(/-/g, '-')}}
          </div>
        </div>
          <div class="fs-4 fw-semibold mt-3 text-start review-content" style="word-break: break-word">
            <p>{{ row.content }}</p>
          </div>
        <div class="d-flex flex-column align-items-end mt-3 review-actions">
          <div class="mb-2">
            <button
                class="btn btn-outline-secondary btn-sm review-btn"
                @click="fnHelpful(1, row)"
                :class="{ 'active-helpful': row.help_ful === 1 }">
              도움이 돼요
            </button>
            <button
                class="btn btn-outline-secondary btn-sm review-btn"
                @click="fnHelpful(2, row)"
                :class="{ 'active-helpful': row.help_ful === 2 }">
              도움이 안 돼요
            </button>
            <button class="btn btn-outline-danger btn-sm review-btn report-btn" @click="openReportModal(row)">
              신고하기
            </button>
          </div>
        </div>
      </div>
    </div>
    <div class="test-position review-pagination">
      <nav aria-label="Page navigation example" v-if="paging.total_list_cnt > 0">
        <span class="center">
          <ul class="pagination">
            <li class="page-item"><a class="page-link" href="javascript:;" @click="fnPage(1)">처음</a></li>
            <a href="javascript:;" class="page-link" @click="fnPage(`${paging.start_page-1}`)">이전</a>

            <li v-for="(n,index) in paginavigation()" :key="index" class="page-item" :class="{ active: paging.page === n }">
              <a class="page-link" href="javascript:;" @click="fnPage(`${n}`)">{{ n }}</a>
            </li>

            <a href="javascript:;" class="page-link" v-if="paging.total_page_cnt > paging.end_page"
               @click="fnPage(`${paging.end_page+1}`)">다음</a>
            <li class="page-item"><a class="page-link" href="javascript:;" @click="fnPage(`${paging.total_page_cnt}`)">마지막</a></li>
          </ul>
        </span>
      </nav>
    </div>
  </div>
  </div>
</template>
<script>

export default {
  data() { //변수생성
    return {
      reportDetails:'',
      reportReason:'0',
      idx:this.$route.query.idx,
      log_id: this.$store.state.userIdx,
      hos_score:'',

      requestBody: {}, //리스트 페이지 데이터전송
      list: {}, //리스트 데이터
      no: '', //게시판 숫자처리

      avg_price_score:'',
      avg_kindness_score:'',
      avg_effect_score:'',

      helpfulState: 0,
      helpfulCount : 0,

      isModalVisible: false, // 모달이 보이는지 여부

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

      page:1,
      size: this.$route.query.size ? this.$route.query.size : 10,
      search_key: this.$route.query.sk ? this.$route.query.sk : '',
      search_value: this.$route.query.sv ? this.$route.query.sv : '',

      paginavigation: function () { //페이징 처리 for문 커스텀
        let pageNumber = [] //;
        let start_page = this.paging.start_page;
        let end_page = this.paging.end_page;
        for (let i = start_page; i <= end_page; i++) pageNumber.push(i);
        return pageNumber;
      }
    }
  },
  mounted() {
    this.fnGetList()
    this.fnGetReviewScore()
  },
  computed: {
    avgScore() {
      // 평균값이 모두 숫자일 때만 계산
      const p = parseFloat(this.avg_price_score);
      const k = parseFloat(this.avg_kindness_score);
      const e = parseFloat(this.avg_effect_score);
      if (!isNaN(p) && !isNaN(k) && !isNaN(e)) {
        return ((p + k + e) / 3).toFixed(1);
      }
      return 0;
    }
  },
  methods: {
    fnPage(n) {
      if (this.page !== n) {
        this.page = n
      }
      this.fnGetList()
    }
    ,fnGetList() {
      this.requestBody = { // 데이터 전송
        page: this.page,
        size: this.size
      }
              this.$axios.get('/review/list/'+ this.idx + "/" + this.log_id, {
        params: this.requestBody,
        headers: {}
      }).then((res) => {
        if (res.data.result_code === "OK") {
          this.list = res.data.data
          this.paging = res.data.pagination
          this.no = this.paging.total_list_cnt - ((this.paging.page - 1) * this.paging.page_size)
        }

        const k_sum = res.data.data.reduce((acc,obj) => {
          if("kindness_score" in obj){
            return acc + obj.kindness_score;
          }else{
            return acc;
          }
        },0);
        const k_count = res.data.data.filter(obj => "kindness_score" in obj).length;
        this.avg_kindness_score = k_sum / k_count

        const p_sum = res.data.data.reduce((acc,obj) => {
          if("price_score" in obj){
            return acc + obj.price_score;
          }else{
            return acc;
          }
        },0);
        const p_count = res.data.data.filter(obj => "price_score" in obj).length;
        this.avg_price_score = p_sum / p_count

        const e_sum = res.data.data.reduce((acc,obj) => {
          if("effect_score" in obj){
            return acc + obj.effect_score;
          }else{
            return acc;
          }
        },0);
        const e_count = res.data.data.filter(obj => "effect_score" in obj).length;
        this.avg_effect_score = e_sum / e_count

        this.avg_effect_score = this.avg_effect_score.toFixed(1)
        this.avg_kindness_score = this.avg_kindness_score.toFixed(1)
        this.avg_price_score = this.avg_price_score.toFixed(1)

        console.log("친절평균:" + this.avg_kindness_score)
        console.log("가격평균:" + this.avg_price_score)
        console.log("효과평균:" + this.avg_effect_score)

      }).catch((err) => {
        if (err.message.indexOf('Network Error') > -1) {
          alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
        }
      })
    }
    ,fnGetReviewScore() {
              this.$axios.get('/review/hos/'+ this.idx, {
        headers: {}
      }).then((res) => {
        this.hos_score = res.data;
      }).catch((err) => {
        if (err.message.indexOf('Network Error') > -1) {
          alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
        }
      })
    }
    ,fnDelete(idx){
      if (!confirm("삭제하시겠습니까?")) return

              this.$axios.delete('/review/' + idx, {})
          .then(() => {
            alert('삭제되었습니다.')

            this.fnGetList()
            location.reload()

          }).catch((err) => {
        console.log(err);
      })
    }
    ,fnUpdate(hos_idx,rev_idx){
      this.requestBody.hos_idx = hos_idx
      this.requestBody.rev_idx = rev_idx
      this.$router.push({
        path: '/review/update',
        query: this.requestBody
      })
    }
    ,fnHelpful(helpful,row) {
        // 토글 로직: 같은 버튼을 다시 누르면 해제 (0으로 설정)
        let finalHelpful = helpful;
        if(row.help_ful === helpful){
          finalHelpful = 0; // 같은 버튼을 다시 누르면 해제
        }
        
        let apiUrl = '/help';
        this.form = {
          reviewNum: row.review_id,
          userNum: this.log_id,
          helpFul: finalHelpful,
        }
        
        console.log('Sending helpful data:', this.form); // 디버깅용 로그
        
        this.$axios.post(apiUrl, this.form, {
          headers: {
            'Content-Type': 'application/json'
          }
        })
            .then((res) => {
             console.log('Helpful response:', res.data); // 디버깅용 로그
             row.help_ful = res.data.data.helpFul
            }).catch((err) => {
          console.error('Helpful error:', err); // 디버깅용 로그
          if (err.message.indexOf('Network Error') > -1) {
            alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
          }
        })
      }
    , openReportModal(row) {
      this.row = row; // row 데이터를 모달에 전달
      this.isModalVisible = true; // 모달 열기
      this.reportReason = null; // 신고 사유 초기화
      this.reportDetails = ""; // 추가 설명 초기화
    }
    , closeModal() {
      this.isModalVisible = false; // 모달 닫기
    }
    ,fnSubmitReport() {
      if (!this.reportReason) {
        alert("신고 사유를 선택해주세요.");
        return;
      }
      let apiUrl = '/report/insert'
      this.form = {
        "review_num": this.row.review_id,
        "user_num": this.log_id,
        "report_reason": Number(this.reportReason),
        "report_detail": this.reportDetails
      }

      this.$axios.post(apiUrl, this.form)
          .then(() => {
            alert('신고가 완료되었습니다.')
            this.closeModal(); // 신고 완료 후 모달 닫기
          }).catch((err) => {
        if (err.message.indexOf('Network Error') > -1) {
          alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
        }
      })
    }
  }
}
</script>

<style>
.container1 {
  display: flex;
  align-items: center;
}

.test-position1 {
  display: flex;
  align-items: center;
  margin-left: 5px;
}

.test-position {
  display: flex;
  align-items: center;
}

.created-at {
  font-size: 1rem;
  color: #666;
  white-space: nowrap;
  margin-left: 20px;
}

.custom-btn {
  border: 2px solid transparent;
  border-radius: 0.25rem;
  padding: 0.375rem 0.75rem;
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.2s ease-in-out;
}

.custom-btn-info {
  background-color: transparent;
  border-color: #17a2b8;
  color: #17a2b8;
}

.custom-btn-secondary {
  background-color: transparent;
  border-color: #6c757d;
  color: #6c757d;
}

.custom-btn-sm {
  font-size: 0.875rem;
  padding: 0.25rem 0.5rem;
}

.custom-mr-2 {
  margin-right: 0.5rem;
}

.custom-text-muted {
  color: #6c757d !important;
}

.modal.show {
  display: block;
}

.modern-review-container {
  max-width: 900px;
  margin: 0 auto;
  padding: 2rem 1rem 3rem 1rem;
  background: #f8f9fb;
  border-radius: 18px;
  box-shadow: 0 4px 24px rgba(0,0,0,0.07);
}
.review-score-card {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
  margin-bottom: 2rem;
  padding: 2rem 1.5rem 1.5rem 1.5rem;
}
.score-summary {
  background: none !important;
  border: none !important;
  color: #444;
  font-size: 1.1rem;
  text-align: center;
}
.review-card {
  background: #fff;
  border-radius: 14px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
  margin-bottom: 2rem;
  padding: 1.5rem 1.2rem 1.2rem 1.2rem;
  transition: box-shadow 0.2s;
}
.review-card:hover {
  box-shadow: 0 6px 24px rgba(0,0,0,0.10);
}
.review-card-header {
  margin-bottom: 0.5rem;
}
.receipt-badge {
  background: #4c1192;
  border-radius: 12px;
  color: #fff;
  padding: 0.3rem 1rem;
  font-size: 1rem;
  display: inline-block;
  width: auto;
}
.review-card-user {
  margin-bottom: 0.5rem;
}
.reviewer-id {
  font-size: 1.2rem;
  font-weight: 600;
  color: #222;
}
.review-date {
  font-size: 0.95rem;
  color: #888;
  margin-left: 10px;
}
.review-content {
  font-size: 1.1rem;
  color: #333;
  margin-top: 0.7rem;
}
.review-actions {
  margin-top: 1.2rem;
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}
.review-actions .review-btn {
  margin-bottom: 0;
}
.review-actions .review-btn:not(:last-child) {
  margin-bottom: 0;
}
.review-actions .report-btn {
  margin-top: 0.7rem;
}
.review-btn {
  border-radius: 8px !important;
  font-weight: 500;
  font-size: 1rem !important;
  margin-right: 0.5rem;
  padding: 0.35rem 1.1rem !important;
  transition: background 0.18s, color 0.18s, border 0.18s;
}
.review-btn:last-child {
  margin-right: 0;
}
.review-btn:hover, .review-btn:focus {
  background: #e3e8f0 !important;
  color: #222 !important;
}
.btn-outline-danger.review-btn:hover {
  background: #ffebee !important;
  color: #d32f2f !important;
  border-color: #d32f2f !important;
}
.review-pagination {
  justify-content: center;
  margin-top: 2.5rem;
}
.pagination {
  justify-content: center;
  margin: 0 auto;
}
.page-link {
  border-radius: 6px !important;
  margin: 0 2px;
  color: #4c1192;
  font-weight: 500;
  transition: background 0.18s, color 0.18s;
}
.page-item.active .page-link, .page-link:hover {
  background: #4c1192;
  color: #fff !important;
  border-color: #4c1192;
}
/* 신고하기 모달 라디오 버튼 modern 스타일 */
.form-check {
  display: flex;
  align-items: center;
  margin-bottom: 0.7rem;
}
.form-check-input[type="radio"] {
  width: 1.3em;
  height: 1.3em;
  accent-color: #4c1192;
  margin-right: 0.7em;
  border: 2px solid #bbb;
  box-shadow: 0 1px 2px rgba(0,0,0,0.04);
  transition: border 0.2s, box-shadow 0.2s;
}
.form-check-input[type="radio"]:hover,
.form-check-input[type="radio"]:focus {
  border: 2px solid #4c1192;
  box-shadow: 0 0 0 2px #e3d6f7;
}
.form-check-label {
  font-size: 1.08rem;
  color: #222;
  font-weight: 500;
  cursor: pointer;
}
.review-btn.active-helpful {
  background-color: #1976d2 !important;
  color: #fff !important;
  border-color: #1976d2 !important;
  box-shadow: 0 2px 8px rgba(25, 118, 210, 0.08);
}
@media (max-width: 700px) {
  .modern-review-container {
    padding: 0.5rem 0.2rem 2rem 0.2rem;
  }
  .review-score-card {
    padding: 1rem 0.5rem 1rem 0.5rem;
  }
  .review-card {
    padding: 1rem 0.5rem 1rem 0.5rem;
  }
  .review-content {
    font-size: 1rem;
  }
  .review-btn {
    font-size: 0.95rem !important;
    padding: 0.3rem 0.7rem !important;
  }
}
</style>