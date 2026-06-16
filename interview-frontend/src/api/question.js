import axios from 'axios'

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api'
const BASE_URL = `${API_BASE_URL}/questions`
const AI_BASE_URL = `${API_BASE_URL}/ai-interview`

export const questionApi = {
  getAllQuestions(page = 0, size = 10) {
    return axios.get(`${BASE_URL}`, {
      params: { page, size }
    })
  },

  getQuestionById(id) {
    return axios.get(`${BASE_URL}/${id}`)
  },

  createQuestion(data) {
    return axios.post(`${BASE_URL}`, data)
  },

  updateQuestion(id, data) {
    return axios.put(`${BASE_URL}/${id}`, data)
  },

  deleteQuestion(id) {
    return axios.delete(`${BASE_URL}/${id}`)
  },

  getQuestionsByCategory(category, page = 0, size = 10) {
    return axios.get(`${BASE_URL}/category/${category}`, {
      params: { page, size }
    })
  },

  getQuestionsByDifficulty(difficulty, page = 0, size = 10) {
    return axios.get(`${BASE_URL}/difficulty/${difficulty}`, {
      params: { page, size }
    })
  },

  getAllCategories() {
    return axios.get(`${BASE_URL}/categories`)
  }
}

export const aiInterviewApi = {
  generateQuestions(data) {
    return axios.post(`${AI_BASE_URL}/generate`, data)
  },

  evaluateAnswer(data) {
    return axios.post(`${AI_BASE_URL}/evaluate`, data)
  }
}