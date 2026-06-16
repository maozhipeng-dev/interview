<template>
  <el-dialog 
    :title="isEdit ? '编辑题目' : '添加题目'" 
    :visible="true" 
    width="600px"
    @close="$emit('close')"
  >
    <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
      <el-form-item label="题目" prop="title">
        <el-input v-model="form.title" type="textarea" :rows="3" placeholder="请输入题目" />
      </el-form-item>

      <el-form-item label="答案" prop="answer">
        <el-input v-model="form.answer" type="textarea" :rows="6" placeholder="请输入答案" />
      </el-form-item>

      <el-form-item label="分类" prop="category">
        <el-input v-model="form.category" placeholder="请输入分类，如：Java基础" />
      </el-form-item>

      <el-form-item label="难度" prop="difficulty">
        <el-select v-model="form.difficulty" placeholder="请选择难度">
          <el-option label="简单(1)" :value="1" />
          <el-option label="较易(2)" :value="2" />
          <el-option label="中等(3)" :value="3" />
          <el-option label="较难(4)" :value="4" />
          <el-option label="困难(5)" :value="5" />
        </el-select>
      </el-form-item>

      <el-form-item label="标签">
        <el-input v-model="form.tags" placeholder="多个标签用逗号分隔" />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="$emit('close')">取消</el-button>
      <el-button type="primary" @click="handleSubmit">
        {{ isEdit ? '保存修改' : '添加题目' }}
      </el-button>
    </template>
  </el-dialog>
</template>

<script>
import { questionApi } from '../api/question'

export default {
  name: 'QuestionForm',
  props: {
    question: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      formRef: null,
      form: {
        title: '',
        answer: '',
        category: '',
        difficulty: null,
        tags: '',
        isActive: true
      },
      rules: {
        title: [
          { required: true, message: '请输入题目', trigger: 'blur' },
          { max: 500, message: '题目长度不能超过500个字符', trigger: 'blur' }
        ],
        answer: [
          { required: true, message: '请输入答案', trigger: 'blur' }
        ],
        category: [
          { required: true, message: '请输入分类', trigger: 'blur' },
          { max: 50, message: '分类长度不能超过50个字符', trigger: 'blur' }
        ],
        difficulty: [
          { required: true, message: '请选择难度', trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    isEdit() {
      return this.question !== null
    }
  },
  mounted() {
    if (this.isEdit) {
      this.form = {
        title: this.question.title,
        answer: this.question.answer,
        category: this.question.category,
        difficulty: this.question.difficulty,
        tags: this.question.tags || '',
        isActive: this.question.isActive
      }
    }
  },
  methods: {
    async handleSubmit() {
      try {
        const valid = await this.$refs.formRef.validate()
        if (!valid) return

        if (this.isEdit) {
          await questionApi.updateQuestion(this.question.id, this.form)
          this.$message.success('修改成功')
        } else {
          await questionApi.createQuestion(this.form)
          this.$message.success('添加成功')
        }

        this.$emit('saved')
      } catch (error) {
        this.$message.error(this.isEdit ? '修改失败' : '添加失败')
        console.error('保存失败:', error)
      }
    }
  }
}
</script>