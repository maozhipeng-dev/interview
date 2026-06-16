<template>
  <div class="app-container">
    <header class="header">
      <div class="container">
        <h1 class="logo">面试题网站</h1>
        <p class="subtitle">Java后端开发 & Vue3前端开发面试题库</p>
        <div class="nav-tabs">
          <el-radio-group v-model="currentTab" size="large">
            <el-radio-button value="questions">
              <el-icon><Document /></el-icon>
              面试题库
            </el-radio-button>
            <el-radio-button value="ai-interview">
              <el-icon><MagicStick /></el-icon>
              AI模拟面试
            </el-radio-button>
          </el-radio-group>
        </div>
      </div>
    </header>

    <main class="main-content">
      <div class="container" v-if="currentTab === 'questions'">
        <QuestionList
          @view-detail="showQuestionDetail"
          @edit-question="editQuestion"
        />
      </div>
      <div v-else>
        <AiInterview />
      </div>
    </main>

    <QuestionDetail
      v-if="showDetail"
      :question="selectedQuestion"
      @close="showDetail = false"
      @updated="handleQuestionUpdated"
    />

    <QuestionForm
      v-if="showForm"
      :question="editingQuestion"
      @close="closeForm"
      @saved="handleQuestionSaved"
    />
  </div>
</template>

<script>
import { Document, MagicStick } from '@element-plus/icons-vue'
import QuestionList from './components/QuestionList.vue'
import QuestionDetail from './components/QuestionDetail.vue'
import QuestionForm from './components/QuestionForm.vue'
import AiInterview from './components/AiInterview.vue'

export default {
  name: 'App',
  components: {
    Document,
    MagicStick,
    QuestionList,
    QuestionDetail,
    QuestionForm,
    AiInterview
  },
  data() {
    return {
      currentTab: 'questions',
      showDetail: false,
      showForm: false,
      selectedQuestion: null,
      editingQuestion: null
    }
  },
  methods: {
    showQuestionDetail(question) {
      this.selectedQuestion = question
      this.showDetail = true
    },
    editQuestion(question) {
      this.editingQuestion = question
      this.showForm = true
    },
    closeForm() {
      this.showForm = false
      this.editingQuestion = null
    },
    handleQuestionSaved() {
      this.showForm = false
      this.editingQuestion = null
    },
    handleQuestionUpdated() {
      this.showDetail = false
      this.selectedQuestion = null
    }
  }
}
</script>

<style scoped>
.app-container {
  min-height: 100vh;
  background-color: #f5f7fa;
}

.header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 40px 0 30px;
  color: white;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.header .container {
  text-align: center;
}

.logo {
  font-size: 36px;
  font-weight: 700;
  margin-bottom: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.subtitle {
  font-size: 16px;
  opacity: 0.9;
  margin-bottom: 20px;
}

.nav-tabs {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.main-content {
  padding: 20px 0;
}
</style>
