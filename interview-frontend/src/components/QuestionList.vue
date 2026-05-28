<template>
  <div class="question-list">
    <div class="filter-bar">
      <div class="filter-group">
        <el-select 
          v-model="filterCategory" 
          placeholder="选择分类"
          clearable
          class="filter-select"
          :disabled="loading"
        >
          <el-option 
            v-for="category in categories" 
            :key="category" 
            :label="category" 
            :value="category"
          />
        </el-select>
        <el-select 
          v-model="filterDifficulty" 
          placeholder="选择难度"
          clearable
          class="filter-select"
          :disabled="loading"
        >
          <el-option label="简单" value="1" />
          <el-option label="较易" value="2" />
          <el-option label="中等" value="3" />
          <el-option label="较难" value="4" />
          <el-option label="困难" value="5" />
        </el-select>
      </div>
      <el-button type="primary" @click="openAddForm" :loading="loading">
        <el-icon><Plus /></el-icon>
        添加题目
      </el-button>
    </div>

    <div v-if="loading" class="loading-state">
      <el-skeleton :rows="5" animated />
      <el-skeleton :rows="5" animated style="margin-top: 20px" />
    </div>

    <div v-else>
      <el-card v-for="question in questions" :key="question.id" class="question-card">
        <div class="card-header">
          <div class="question-title">{{ question.title }}</div>
          <div class="card-tags">
            <span class="category-tag">{{ question.category }}</span>
            <span :class="['difficulty-tag', `difficulty-${question.difficulty}`]">
              {{ getDifficultyLabel(question.difficulty) }}
            </span>
          </div>
        </div>
        <div class="card-body">
          <p class="answer-preview">{{ truncateAnswer(question.answer) }}</p>
          <div v-if="question.tags" class="tags-container">
            <span v-for="tag in question.tags.split(',')" :key="tag" class="tag-item">
              {{ tag.trim() }}
            </span>
          </div>
        </div>
        <div class="card-footer">
          <el-button size="small" @click="$emit('view-detail', question)">
            <el-icon><Eye /></el-icon>
            查看详情
          </el-button>
          <el-button size="small" type="primary" @click="$emit('edit-question', question)">
            <el-icon><Edit /></el-icon>
            编辑
          </el-button>
          <el-button size="small" type="danger" @click="handleDelete(question.id)" :loading="deletingId === question.id">
            <el-icon><Delete /></el-icon>
            删除
          </el-button>
        </div>
      </el-card>

      <div v-if="questions.length === 0" class="empty-state">
        <el-empty description="暂无面试题" />
      </div>

      <el-pagination
        v-if="total > 0"
        :current-page="currentPage + 1"
        :page-size="pageSize"
        :total="total"
        :disabled="loading"
        layout="total, prev, pager, next, jumper"
        @current-change="handlePageChange"
        class="pagination"
      />
    </div>

    <QuestionForm 
      v-if="showAddForm" 
      @close="showAddForm = false"
      @saved="handleQuestionSaved"
    />
  </div>
</template>

<script>
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Eye, Edit, Delete } from '@element-plus/icons-vue'
import { questionApi } from '../api/question'
import QuestionForm from './QuestionForm.vue'

export default {
  name: 'QuestionList',
  components: {
    Plus,
    Eye,
    Edit,
    Delete,
    QuestionForm
  },
  emits: ['view-detail', 'edit-question', 'delete-question'],
  data() {
    return {
      questions: [],
      categories: [],
      currentPage: 0,
      pageSize: 10,
      total: 0,
      filterCategory: '',
      filterDifficulty: '',
      showAddForm: false,
      loading: false,
      deletingId: null
    }
  },
  mounted() {
    this.loadCategories()
    this.loadQuestions()
  },
  watch: {
    filterCategory() {
      this.currentPage = 0
      this.loadQuestions()
    },
    filterDifficulty() {
      this.currentPage = 0
      this.loadQuestions()
    }
  },
  methods: {
    async loadCategories() {
      try {
        const response = await questionApi.getAllCategories()
        this.categories = response.data.data || []
      } catch (error) {
        console.error('加载分类失败:', error)
        ElMessage.error('加载分类失败')
      }
    },
    async loadQuestions() {
      this.loading = true
      try {
        let response
        if (this.filterCategory) {
          response = await questionApi.getQuestionsByCategory(this.filterCategory, this.currentPage, this.pageSize)
        } else if (this.filterDifficulty) {
          response = await questionApi.getQuestionsByDifficulty(this.filterDifficulty, this.currentPage, this.pageSize)
        } else {
          response = await questionApi.getAllQuestions(this.currentPage, this.pageSize)
        }
        this.questions = response.data.data?.content || []
        this.total = response.data.total || 0
      } catch (error) {
        console.error('加载题目失败:', error)
        ElMessage.error(error.response?.data?.message || '加载题目失败')
      } finally {
        this.loading = false
      }
    },
    handlePageChange(page) {
      this.currentPage = page - 1
      this.loadQuestions()
    },
    getDifficultyLabel(difficulty) {
      const labels = ['', '简单', '较易', '中等', '较难', '困难']
      return labels[difficulty] || '未知'
    },
    truncateAnswer(answer) {
      if (!answer) return ''
      if (answer.length <= 100) return answer
      return answer.substring(0, 100) + '...'
    },
    openAddForm() {
      this.showAddForm = true
    },
    handleQuestionSaved() {
      this.showAddForm = false
      this.loadQuestions()
      this.loadCategories()
    },
    async handleDelete(id) {
      try {
        await ElMessageBox.confirm('确定要删除这个题目吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        this.deletingId = id
        await questionApi.deleteQuestion(id)
        ElMessage.success('删除成功')
        this.loadQuestions()
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除失败:', error)
          ElMessage.error(error.response?.data?.message || '删除失败')
        }
      } finally {
        this.deletingId = null
      }
    }
  }
}
</script>

<style scoped>
.question-list {
  padding: 20px 0;
}

.filter-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.filter-group {
  display: flex;
  gap: 12px;
}

.filter-select {
  width: 160px;
}

.loading-state {
  padding: 20px 0;
}

.question-card {
  margin-bottom: 16px;
  transition: box-shadow 0.3s;
}

.question-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
}

.question-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  flex: 1;
  margin-right: 12px;
}

.card-tags {
  display: flex;
  gap: 8px;
}

.card-body {
  margin-bottom: 12px;
}

.answer-preview {
  color: #666;
  line-height: 1.6;
  margin-bottom: 12px;
}

.tags-container {
  display: flex;
  flex-wrap: wrap;
}

.card-footer {
  display: flex;
  gap: 8px;
}

.empty-state {
  padding: 40px 0;
}

.pagination {
  text-align: center;
  margin-top: 20px;
}
</style>