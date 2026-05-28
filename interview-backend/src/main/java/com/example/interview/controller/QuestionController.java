package com.example.interview.controller;

import com.example.interview.dto.QuestionDTO;
import com.example.interview.dto.ResponseDTO;
import com.example.interview.entity.Question;
import com.example.interview.service.QuestionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping
    public ResponseDTO<Page<Question>> getAllQuestions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Question> questions = questionService.getAllQuestions(page, size);
        return ResponseDTO.success(questions, questions.getTotalElements());
    }

    @GetMapping("/{id}")
    public ResponseDTO<Question> getQuestionById(@PathVariable Long id) {
        try {
            Question question = questionService.getQuestionById(id);
            return ResponseDTO.success(question);
        } catch (RuntimeException e) {
            return ResponseDTO.notFound(e.getMessage());
        }
    }

    @PostMapping
    public ResponseDTO<Question> createQuestion(@Valid @RequestBody QuestionDTO questionDTO) {
        Question question = questionService.createQuestion(questionDTO);
        return ResponseDTO.success(question);
    }

    @PutMapping("/{id}")
    public ResponseDTO<Question> updateQuestion(
            @PathVariable Long id,
            @Valid @RequestBody QuestionDTO questionDTO) {
        try {
            Question question = questionService.updateQuestion(id, questionDTO);
            return ResponseDTO.success(question);
        } catch (RuntimeException e) {
            return ResponseDTO.notFound(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseDTO<Void> deleteQuestion(@PathVariable Long id) {
        try {
            questionService.deleteQuestion(id);
            return ResponseDTO.success(null);
        } catch (RuntimeException e) {
            return ResponseDTO.notFound(e.getMessage());
        }
    }

    @GetMapping("/category/{category}")
    public ResponseDTO<Page<Question>> getQuestionsByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Question> questions = questionService.getQuestionsByCategory(category, page, size);
        return ResponseDTO.success(questions, questions.getTotalElements());
    }

    @GetMapping("/difficulty/{difficulty}")
    public ResponseDTO<Page<Question>> getQuestionsByDifficulty(
            @PathVariable Integer difficulty,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Question> questions = questionService.getQuestionsByDifficulty(difficulty, page, size);
        return ResponseDTO.success(questions, questions.getTotalElements());
    }

    @GetMapping("/categories")
    public ResponseDTO<List<String>> getAllCategories() {
        List<String> categories = questionService.getAllCategories();
        return ResponseDTO.success(categories);
    }
}