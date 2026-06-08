<template>
  <div class="ai-interview">
    <div class="interview-container">
      <!-- 配置阶段 -->
      <div v-if="!interviewStarted" class="setup-form">
        <h2>
          <el-icon><Promotion /></el-icon>
          AI模拟面试
        </h2>
        <p class="subtitle">让AI为你定制专业的面试题目</p>

        <el-form :model="interviewConfig" label-width="100px">
          <el-form-item label="应聘职位" required>
            <el-select v-model="interviewConfig.position" placeholder="请选择或输入职位" style="width: 100%" clearable filterable allow-create>
              <el-option label="Java后端开发工程师" value="Java后端开发工程师" />
              <el-option label="前端开发工程师" value="前端开发工程师" />
              <el-option label="全栈开发工程师" value="全栈开发工程师" />
              <el-option label="Python开发工程师" value="Python开发工程师" />
              <el-option label="Go开发工程师" value="Go开发工程师" />
            </el-select>
          </el-form-item>

          <el-form-item label="经验年限">
            <el-select v-model="interviewConfig.experience" placeholder="请选择" style="width: 100%" clearable>
              <el-option label="应届生" value="应届生" />
              <el-option label="1-3年" value="1-3年" />
              <el-option label="3-5年" value="3-5年" />
              <el-option label="5年以上" value="5年以上" />
            </el-select>
          </el-form-item>

          <el-form-item label="技术栈">
            <el-input v-model="interviewConfig.techStack" type="textarea" placeholder="例如: Java,Spring Boot,MySQL,Redis" :rows="2" />
          </el-form-item>

          <el-form-item label="期望级别">
            <el-select v-model="interviewConfig.level" placeholder="请选择" style="width: 100%" clearable>
              <el-option label="初级" value="初级" />
              <el-option label="中级" value="中级" />
              <el-option label="高级" value="高级" />
            </el-select>
          </el-form-item>

          <el-form-item label="特殊要求">
            <el-input v-model="interviewConfig.customRequirement" type="textarea" placeholder="有什么特殊的面试重点要求吗？" :rows="2" />
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="startInterview" :loading="generating" style="width: 100%">
              <el-icon><MagicStick /></el-icon>
              开始AI面试
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 面试阶段 -->
      <div v-else class="interview-stage">
        <div class="interview-header">
          <h2>
            <el-icon><Document /></el-icon>
            AI面试进行中
          </h2>
          <el-badge :value="currentQuestionIndex + 1" :max="interviewQuestions.length" type="primary" class="question-badge" />
        </div>

        <div v-if="interviewSuggestions" class="suggestions-card">
      <el-alert type="info" :closable="false">
        <template #title>
          <span class="suggestions-title">
            <el-icon><ReadingLamp /></el-icon>
            面试建议
          </span>
        </template>
        {{ interviewSuggestions }}
      </el-alert>
    </div>

        <div v-if="currentQuestion" class="question-card">
          <div class="question-meta">
            <el-tag :type="getDifficultyTagType(currentQuestion.difficulty)" size="small">
              {{ currentQuestion.difficulty }}
            </el-tag>
            <el-tag type="info" size="small">
              {{ currentQuestion.category }}
            </el-tag>
          </div>
          <h3 class="question-text">{{ currentQuestion.question }}</h3>
          <el-divider />
          <div class="answer-area">
            <div class="answer-hint" v-if="currentQuestion.answerHint">
              <el-tag effect="dark" type="success">
                <el-icon><Odometer /></el-icon>
                参考答案要点
              </el-tag>
              <div class="hint-content">{{ currentQuestion.answerHint }}</div>
            </div>
            <el-input
              v-model="userAnswer"
              type="textarea"
              :rows="6"
              placeholder="请输入你的回答..."
              class="answer-input"
            />
            <div class="answer-actions">
              <el-button @click="showAnswerHint = !showAnswerHint" size="small">
                <el-icon><View /></el-icon>
                {{ showAnswerHint ? '隐藏' : '显示' }}答案要点
              </el-button>
              <el-button type="primary" @click="submitAnswer" :loading="evaluating" size="small">
                <el-icon><Check /></el-icon>
                提交答案
              </el-button>
            </div>
          </div>
        </div>

        <!-- 评估结果 -->
        <div v-if="evaluationResult" class="evaluation-card">
          <el-card>
            <template #header>
              <div class="evaluation-header">
                <span>
                  <el-icon><Medal /></el-icon>
                  评估结果
                </span>
                <div class="score-display">
                  <span class="score-value" :class="getScoreClass(evaluationResult.score)">
                    {{ evaluationResult.score }}
                  </span>
                  <el-tag :type="getLevelTagType(evaluationResult.level)" size="large">
                    {{ evaluationResult.level }}
                  </el-tag>
                </div>
              </div>
            </template>

            <div class="evaluation-section">
              <h4>
                <el-icon><ChatDotRound /></el-icon>
                详细评估
              </h4>
              <p>{{ evaluationResult.evaluation }}</p>
            </div>

            <div class="evaluation-section">
              <h4>
                <el-icon><Tools /></el-icon>
                改进建议
              </h4>
              <p>{{ evaluationResult.suggestions }}</p>
            </div>

            <div class="evaluation-section">
              <h4>
                <el-icon><DocumentCopy /></el-icon>
                参考答案
              </h4>
              <p>{{ evaluationResult.referenceAnswer }}</p>
            </div>

            <div class="evaluation-actions">
              <el-button @click="resetInterview" :disabled="evaluating">
                <el-icon><RefreshLeft /></el-icon>
                重新面试
              </el-button>
              <el-button
                type="primary"
                @click="nextQuestion"
                :disabled="evaluating || currentQuestionIndex >= interviewQuestions.length - 1"
              >
                <el-icon><Right /></el-icon>
                {{ currentQuestionIndex >= interviewQuestions.length - 1 ? '面试结束' : '下一题' }}
              </el-button>
            </div>
          </el-card>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ElMessage } from 'element-plus'
import {
  Promotion,
  MagicStick,
  Document,
  ReadingLamp,
  Odometer,
  View,
  Check,
  Medal,
  ChatDotRound,
  Tools,
  DocumentCopy,
  RefreshLeft,
  Right
} from '@element-plus/icons-vue'
import { aiInterviewApi } from '../api/question'

export default {
  name: 'AiInterview',
  components: {
    Promotion,
    MagicStick,
    Document,
    ReadingLamp,
    Odometer,
    View,
    Check,
    Medal,
    ChatDotRound,
    Tools,
    DocumentCopy,
    RefreshLeft,
    Right
  },
  data() {
    return {
      interviewStarted: false,
      generating: false,
      evaluating: false,
      showAnswerHint: false,
      interviewConfig: {
        position: '',
        experience: '',
        techStack: '',
        level: '',
        customRequirement: ''
      },
      interviewQuestions: [],
      interviewSuggestions: '',
      currentQuestionIndex: 0,
      userAnswer: '',
      evaluationResult: null
    }
  },
  computed: {
    currentQuestion() {
      return this.interviewQuestions[this.currentQuestionIndex]
    }
  },
  methods: {
    async startInterview() {
      if (!this.interviewConfig.position) {
        ElMessage.warning('请输入应聘职位')
        return
      }

      this.generating = true
      try {
        const res = await aiInterviewApi.generateQuestions(this.interviewConfig)
        this.interviewQuestions = res.data.data.questions
        this.interviewSuggestions = res.data.data.suggestions
        this.interviewStarted = true
        this.currentQuestionIndex = 0
        ElMessage.success('面试题目生成成功！')
      } catch (error) {
        ElMessage.error(error.response?.data?.message || '生成题目失败')
      } finally {
        this.generating = false
      }
    },

    async submitAnswer() {
      if (!this.userAnswer.trim()) {
        ElMessage.warning('请输入你的答案')
        return
      }

      this.evaluating = true
      try {
        const res = await aiInterviewApi.evaluateAnswer({
          question: this.currentQuestion.question,
          userAnswer: this.userAnswer,
          answerHint: this.currentQuestion.answerHint
        })
        this.evaluationResult = res.data.data
        ElMessage.success('评估完成！')
      } catch (error) {
        ElMessage.error(error.response?.data?.message || '评估失败')
      } finally {
        this.evaluating = false
      }
    },

    nextQuestion() {
      if (this.currentQuestionIndex < this.interviewQuestions.length - 1) {
        this.currentQuestionIndex++
        this.userAnswer = ''
        this.evaluationResult = null
        this.showAnswerHint = false
      } else {
        ElMessage.success('恭喜完成所有面试题目！')
      }
    },

    resetInterview() {
      this.interviewStarted = false
      this.interviewQuestions = []
      this.currentQuestionIndex = 0
      this.userAnswer = ''
      this.evaluationResult = null
    },

    getDifficultyTagType(difficulty) {
      const types = {
        '简单': 'success',
        '中等': 'warning',
        '高级': 'danger'
      }
      return types[difficulty] || 'info'
    },

    getScoreClass(score) {
      if (score >= 90) return 'score-excellent'
      if (score >= 80) return 'score-good'
      if (score >= 60) return 'score-ok'
      return 'score-low'
    },

    getLevelTagType(level) {
      const types = {
        '优秀': 'success',
        '良好': 'warning',
        '合格': 'info',
        '需要改进': 'danger'
      }
      return types[level] || 'info'
    }
  }
}
</script>

<style scoped>
.ai-interview {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 40px 20px;
}

.interview-container {
  max-width: 800px;
  margin: 0 auto;
}

.setup-form {
  background: white;
  border-radius: 16px;
  padding: 40px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
}

.setup-form h2 {
  text-align: center;
  color: #333;
  margin-bottom: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.subtitle {
  text-align: center;
  color: #666;
  margin-bottom: 30px;
}

.interview-stage {
  background: white;
  border-radius: 16px;
  padding: 30px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
}

.interview-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.interview-header h2 {
  color: #333;
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 0;
}

.question-badge {
  font-size: 14px;
}

.suggestions-card {
  margin-bottom: 20px;
}

.suggestions-title {
  display: flex;
  align-items: center;
  gap: 6px;
  font-weight: 600;
}

.question-card {
  margin-bottom: 20px;
}

.question-meta {
  display: flex;
  gap: 10px;
  margin-bottom: 16px;
}

.question-text {
  font-size: 20px;
  color: #333;
  line-height: 1.6;
  margin: 0;
}

.answer-hint {
  margin-bottom: 16px;
}

.hint-content {
  margin-top: 8px;
  padding: 12px;
  background: #f0f9eb;
  border-radius: 8px;
  color: #67c23a;
  line-height: 1.6;
}

.answer-input {
  margin-bottom: 16px;
}

.answer-actions {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
}

.evaluation-card {
  margin-top: 20px;
}

.evaluation-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.score-display {
  display: flex;
  align-items: center;
  gap: 12px;
}

.score-value {
  font-size: 32px;
  font-weight: bold;
}

.score-excellent {
  color: #67c23a;
}

.score-good {
  color: #e6a23c;
}

.score-ok {
  color: #409eff;
}

.score-low {
  color: #f56c6c;
}

.evaluation-section {
  margin-bottom: 20px;
}

.evaluation-section h4 {
  color: #333;
  display: flex;
  align-items: center;
  gap: 6px;
  margin-bottom: 8px;
}

.evaluation-section p {
  color: #666;
  line-height: 1.6;
  margin: 0;
  padding: 12px;
  background: #f5f7fa;
  border-radius: 8px;
}

.evaluation-actions {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
  margin-top: 20px;
}
</style>
