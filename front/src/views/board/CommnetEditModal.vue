<template>
  <div v-if="visible" class="custom-modal-overlay" @click.self="close">
    <div class="custom-modal-dialog">
      <div class="custom-modal-content">
        <div class="custom-modal-header">
          <h5 class="custom-modal-title">댓글 수정</h5>
          <button type="button" class="custom-modal-close" @click="close">&times;</button>
        </div>
        <div class="custom-modal-body">
          <textarea
            v-model="editContent"
            class="custom-modal-textarea"
            rows="4"
            placeholder="댓글을 입력하세요."
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
  name: 'CommentEditModal',
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
  position: fixed;
  top: 0; left: 0; right: 0; bottom: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0,0,0,0.45);
  z-index: 2000;
  display: flex;
  align-items: center;
  justify-content: center;
}
.custom-modal-dialog {
  background: none;
  border: none;
  box-shadow: none;
  width: 95vw;
  max-width: 420px;
  margin: 0;
  display: flex;
  align-items: center;
  justify-content: center;
}
.custom-modal-content {
  background: #fff;
  border-radius: 18px;
  box-shadow: 0 8px 32px rgba(44,62,80,0.12);
  width: 100%;
  padding: 0;
  overflow: hidden;
}
.custom-modal-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1.1rem 1.5rem 0.5rem 1.5rem;
  border-bottom: 1px solid #f0f0f0;
}
.custom-modal-title {
  font-size: 1.18rem;
  font-weight: 700;
  color: #222;
  margin: 0;
}
.custom-modal-close {
  background: none;
  border: none;
  font-size: 1.7rem;
  color: #888;
  cursor: pointer;
  line-height: 1;
  padding: 0 0.2em;
}
.custom-modal-body {
  padding: 1.2rem 1.5rem 1.5rem 1.5rem;
}
.custom-modal-textarea {
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
.custom-modal-textarea:focus {
  border: 1.5px solid #3498db;
  box-shadow: 0 2px 8px rgba(52,152,219,0.08);
  outline: none;
}
.custom-modal-btn-group {
  display: flex;
  gap: 1rem;
  justify-content: center;
}
.custom-modal-btn {
  min-width: 100px;
  padding: 0.7rem 0;
  border-radius: 22px;
  font-size: 1.08rem;
  font-weight: 600;
  border: none;
  transition: background 0.18s, color 0.18s;
  cursor: pointer;
  box-shadow: 0 1px 4px rgba(0,0,0,0.04);
}
.custom-modal-btn.confirm {
  background: #3498db;
  color: #fff;
}
.custom-modal-btn.confirm:hover {
  background: #217dbb;
}
.custom-modal-btn.cancel {
  background: #f4f7fd;
  color: #333;
}
.custom-modal-btn.cancel:hover {
  background: #e0e0e0;
}
@media (max-width: 600px) {
  .custom-modal-dialog {
    max-width: 98vw;
  }
  .custom-modal-content {
    border-radius: 10px;
  }
  .custom-modal-header, .custom-modal-body {
    padding-left: 0.7rem;
    padding-right: 0.7rem;
  }
}
</style>
