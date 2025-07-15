<template>
  <div id="replyCreateModal" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-dialog-centered">
      <div class="modal-content modal-edit-comment">
        <div class="modal-header border-0 pb-0">
          <h5 class="modal-title modal-edit-title">대댓글 작성</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
        </div>
        <div class="modal-body pt-2 pb-4 px-4">
          <textarea
              v-model="replyContent"
              class="modal-edit-textarea"
              rows="4"
              placeholder="대댓글을 입력하세요."
          ></textarea>
          <div class="d-flex justify-content-between align-items-center mt-2 mb-3">
            <small class="text-muted">
              <i class="fas fa-info-circle me-1"></i>최대 500자까지 입력 가능합니다
            </small>
            <small class="text-muted">{{ replyContent.length }}/500</small>
          </div>
          <div class="modal-edit-btn-group">
            <button
                type="button"
                class="modal-edit-btn cancel"
                data-bs-dismiss="modal"
            >
              취소
            </button>
            <button
                type="button"
                class="modal-edit-btn confirm"
                @click="handleCreate"
                data-bs-dismiss="modal"
            >
              작성
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ReplyCreateModal',
  props: {
    modelValue: {
      type: String,
      default: ''
    }
  },
  emits: ['update:modelValue', 'create'],
  computed: {
    replyContent: {
      get() {
        return this.modelValue;
      },
      set(value) {
        this.$emit('update:modelValue', value);
      }
    }
  },
  methods: {
    handleCreate() {
      this.$emit('create');
    }
  }
}
</script>

<style scoped>
.modal-edit-comment {
  border-radius: 18px;
  box-shadow: 0 8px 32px rgba(44,62,80,0.12);
  border: none;
  background: #fff;
}

.modal-edit-title {
  font-size: 1.25rem;
  font-weight: 700;
  color: #222;
  margin: 0 auto 0.5rem auto;
  text-align: center;
  letter-spacing: -0.5px;
}

.modal-edit-textarea {
  width: 100%;
  min-height: 90px;
  border-radius: 12px;
  border: 1.5px solid #e0e0e0;
  font-size: 1.08rem;
  padding: 1.1rem 1rem;
  margin-bottom: 1.5rem;
  box-shadow: 0 1px 4px rgba(0,0,0,0.03);
  transition: border 0.2s, box-shadow 0.2s;
  resize: none;
  background: #fafbfc;
}

.modal-edit-textarea:focus {
  border: 1.5px solid #3498db;
  box-shadow: 0 2px 8px rgba(52,152,219,0.08);
  outline: none;
}

.modal-edit-btn-group {
  display: flex;
  gap: 1rem;
  justify-content: center;
}

.modal-edit-btn {
  min-width: 120px;
  padding: 0.7rem 0;
  border-radius: 22px;
  font-size: 1.08rem;
  font-weight: 600;
  border: none;
  transition: background 0.18s, color 0.18s;
  cursor: pointer;
  box-shadow: 0 1px 4px rgba(0,0,0,0.04);
}

.modal-edit-btn.confirm {
  background: #3498db;
  color: #fff;
}

.modal-edit-btn.confirm:hover {
  background: #217dbb;
}

.modal-edit-btn.cancel {
  background: #f4f7fd;
  color: #333;
}

.modal-edit-btn.cancel:hover {
  background: #e0e0e0;
}
</style>