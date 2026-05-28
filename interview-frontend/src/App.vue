<template>
  <div class="app-container">
    <header class="header">
      <div class="container">
        <h1 class="logo">面试题网站</h1>
        <p class="subtitle">Java后端开发 & Vue3前端开发面试题库</p>
      </div>
    </header>

    <main class="main-content">
      <div class="container">
        <QuestionList 
          @view-detail="showQuestionDetail" 
          @edit-question="editQuestion"
          @delete-question="handleDelete"
        />
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
import QuestionList from './components/QuestionList.vue'
import QuestionDetail from './components/QuestionDetail.vue'
import QuestionForm from './components/QuestionForm.vue'

export default {
  name: 'App',
  components: {
    QuestionList,
    QuestionDetail,
    QuestionForm
  },
  data() {
    return {
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
  padding: 40px 0;
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
}

.subtitle {
  font-size: 16px;
  opacity: 0.9;
}

.main-content {
  padding: 20px 0;
}
</style>