<template>
  <div v-if="visible" class="custom-modal-overlay" @click.self="close">
    <div class="custom-modal-dialog">
      <div class="custom-modal-content">
        <div class="custom-modal-header">
          <h5 class="custom-modal-title">답글 작성</h5>
          <button type="button" class="custom-modal-close" @click="close" aria-label="닫기">&times;</button>
        </div>
        <div class="custom-modal-body">
          <textarea
              v-model="editContent"
              class="custom-modal-textarea"
              rows="4"
              placeholder="답글을 입력하세요."
          ></textarea>
          <div class="custom-modal-btn-group">
            <button type="button" class="custom-modal-btn confirm" @click="handleConfirm">확인</button>
            <button type="button" class="custom-modal-btn cancel" @click="close">닫기</button>
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
    },
    show: {
      type: Boolean,
      default: false
    }
  },
  emits: ['update:modelValue', 'confirm', 'update:show'],
  computed: {
    visible() {
      return this.show;
    },
    editContent: {
      get() {
        return this.modelValue;
      },
      set(value) {
        this.$emit('update:modelValue', value);
      }
    }
  },
  methods: {
    handleConfirm() {
      this.$emit('confirm');
      this.close();
    },
    close() {
      this.$emit('update:show', false);
    }
  }
};
</script>

<style scoped>
.custom-modal-overlay {
  position: fixed !important;
  top: 0 !important; left: 0 !important; right: 0 !important; bottom: 0 !important;
  width: 100vw !important;
  background: rgba(0,0,0,0.45) !important;
  z-index: 2000 !important;
  display: flex !important;
  align-items: center !important;
  justify-content: center !important;
}
.custom-modal-dialog {
  background: none !important;
  border: none !important;
  box-shadow: none !important;
  width: 95vw !important;
  max-width: 420px !important;
  margin: 0 !important;
  display: flex !important;
  align-items: center !important;
  justify-content: center !important;
}
.custom-modal-content {
  background: #fff !important;
  border-radius: 18px !important;
  box-shadow: 0 8px 32px rgba(44,62,80,0.12) !important;
  width: 100% !important;
  padding: 0 !important;
  overflow: hidden !important;
  position: relative !important;
}
.custom-modal-header {
  display: flex !important;
  align-items: center !important;
  justify-content: space-between !important;
  padding: 1.1rem 1.5rem 0.5rem 1.5rem !important;
  border-bottom: 1px solid #f0f0f0 !important;
  position: relative !important;
}
.custom-modal-title {
  font-size: 1.18rem !important;
  font-weight: 700 !important;
  color: #222 !important;
  margin: 0 !important;
}
.custom-modal-close {
  background: #f4f7fd !important;
  border: none !important;
  font-size: 1.3rem !important;
  color: #888 !important;
  cursor: pointer !important;
  line-height: 1 !important;
  padding: 0.3em 0.7em !important;
  border-radius: 50% !important;
  box-shadow: 0 1px 4px rgba(0,0,0,0.07) !important;
  position: absolute !important;
  right: 1.1rem !important;
  top: 1.1rem !important;
  transition: background 0.18s, color 0.18s !important;
}
.custom-modal-close:hover {
  background: #eaf4fb !important;
  color: #217dbb !important;
}
.custom-modal-body {
  padding: 1.2rem 1.5rem 1.5rem 1.5rem !important;
}
.custom-modal-textarea {
  width: 100% !important;
  min-height: 90px !important;
  border-radius: 12px !important;
  border: 1.5px solid #e0e0e0 !important;
  font-size: 1.08rem !important;
  padding: 1.1rem 1rem !important;
  margin-bottom: 1.5rem !important;
  box-shadow: 0 1px 4px rgba(0,0,0,0.03) !important;
  transition: border 0.2s, box-shadow 0.2s !important;
  resize: none !important;
  background: #fafbfc !important;
}
.custom-modal-textarea:focus {
  border: 1.5px solid #3498db !important;
  box-shadow: 0 2px 8px rgba(52,152,219,0.08) !important;
  outline: none !important;
}
.custom-modal-btn-group {
  display: flex !important;
  gap: 1rem !important;
  justify-content: center !important;
}
.custom-modal-btn {
  min-width: 100px !important;
  padding: 0.7rem 0 !important;
  border-radius: 22px !important;
  font-size: 1.08rem !important;
  font-weight: 600 !important;
  border: none !important;
  transition: background 0.18s, color 0.18s !important;
  cursor: pointer !important;
  box-shadow: 0 1px 4px rgba(0,0,0,0.04) !important;
}
.custom-modal-btn.confirm {
  background: #3498db !important;
  color: #fff !important;
}
.custom-modal-btn.confirm:hover {
  background: #217dbb !important;
}
.custom-modal-btn.cancel {
  background: #f4f7fd !important;
  color: #333 !important;
}
.custom-modal-btn.cancel:hover {
  background: #e0e0e0 !important;
}
@media (max-width: 600px) {
  .custom-modal-dialog {
    max-width: 98vw !important;
  }
  .custom-modal-content {
    border-radius: 10px !important;
  }
  .custom-modal-header, .custom-modal-body {
    padding-left: 0.7rem !important;
    padding-right: 0.7rem !important;
  }
}
</style>
