<template>
  <el-dialog 
    title="题目详情" 
    :visible="true" 
    :width="dialogWidth"
    @close="$emit('close')"
  >
    <div v-if="question" class="detail-content">
      <div class="detail-header">
        <h3 class="title">{{ question.title }}</h3>
        <div class="tags">
          <span class="category-tag">{{ question.category }}</span>
          <span :class="['difficulty-tag', `difficulty-${question.difficulty}`]">
            {{ getDifficultyLabel(question.difficulty) }}
          </span>
        </div>
      </div>

      <div class="detail-section">
        <h4 class="section-title">答案</h4>
        <div class="answer-content">{{ question.answer }}</div>
      </div>

      <div v-if="question.tags" class="detail-section">
        <h4 class="section-title">标签</h4>
        <div class="tags-container">
          <span v-for="tag in question.tags.split(',')" :key="tag" class="tag-item">
            {{ tag.trim() }}
          </span>
        </div>
      </div>
    </div>

    <template #footer>
      <el-button @click="$emit('close')">关闭</el-button>
    </template>
  </el-dialog>
</template>

<script>
export default {
  name: 'QuestionDetail',
  props: {
    question: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      dialogWidth: '800px'
    }
  },
  methods: {
    getDifficultyLabel(difficulty) {
      const labels = ['', '简单', '较易', '中等', '较难', '困难']
      return labels[difficulty] || '未知'
    }
  }
}
</script>

<style scoped>
.detail-content {
  padding: 16px 0;
}

.detail-header {
  margin-bottom: 24px;
}

.title {
  font-size: 20px;
  font-weight: 600;
  color: #333;
  margin-bottom: 12px;
}

.tags {
  display: flex;
  gap: 8px;
}

.detail-section {
  margin-bottom: 24px;
}

.section-title {
  font-size: 16px;
  font-weight: 500;
  color: #666;
  margin-bottom: 12px;
}

.answer-content {
  line-height: 1.8;
  color: #333;
  background-color: #fafafa;
  padding: 16px;
  border-radius: 8px;
  white-space: pre-wrap;
}

.tags-container {
  display: flex;
  flex-wrap: wrap;
}
</style>