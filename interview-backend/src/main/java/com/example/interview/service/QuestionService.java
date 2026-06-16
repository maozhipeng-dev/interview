package com.example.interview.service;

import com.example.interview.dto.QuestionDTO;
import com.example.interview.entity.Question;
import org.springframework.data.domain.Page;

import java.util.List;

public interface QuestionService {

    Page<Question> getAllQuestions(int page, int size);

    Question getQuestionById(Long id);

    Question createQuestion(QuestionDTO questionDTO);

    Question updateQuestion(Long id, QuestionDTO questionDTO);

    void deleteQuestion(Long id);

    Page<Question> getQuestionsByCategory(String category, int page, int size);

    Page<Question> getQuestionsByDifficulty(Integer difficulty, int page, int size);

    List<Question> getActiveQuestionsByCategory(String category);

    List<String> getAllCategories();
}