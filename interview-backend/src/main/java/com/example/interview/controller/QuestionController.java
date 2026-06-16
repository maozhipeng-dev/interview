package com.example.interview.controller;

import com.example.interview.dto.QuestionDTO;
import com.example.interview.dto.ResponseDTO;
import com.example.interview.entity.Question;
import com.example.interview.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@Tag(name = "面试题管理", description = "面试题CRUD接口")
public class QuestionController {

    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping
    @Operation(summary = "获取所有题目", description = "分页获取所有面试题")
    public ResponseDTO<Page<Question>> getAllQuestions(
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size) {
        Page<Question> questions = questionService.getAllQuestions(page, size);
        return ResponseDTO.success(questions, questions.getTotalElements());
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取题目详情", description = "根据ID获取面试题详情")
    public ResponseDTO<Question> getQuestionById(
            @Parameter(description = "题目ID") @PathVariable Long id) {
        try {
            Question question = questionService.getQuestionById(id);
            return ResponseDTO.success(question);
        } catch (RuntimeException e) {
            return ResponseDTO.notFound(e.getMessage());
        }
    }

    @PostMapping
    @Operation(summary = "创建题目", description = "创建新的面试题")
    public ResponseDTO<Question> createQuestion(
            @Parameter(description = "题目信息") @Valid @RequestBody QuestionDTO questionDTO) {
        Question question = questionService.createQuestion(questionDTO);
        return ResponseDTO.success(question);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新题目", description = "根据ID更新面试题")
    public ResponseDTO<Question> updateQuestion(
            @Parameter(description = "题目ID") @PathVariable Long id,
            @Parameter(description = "题目信息") @Valid @RequestBody QuestionDTO questionDTO) {
        try {
            Question question = questionService.updateQuestion(id, questionDTO);
            return ResponseDTO.success(question);
        } catch (RuntimeException e) {
            return ResponseDTO.notFound(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除题目", description = "根据ID删除面试题")
    public ResponseDTO<Void> deleteQuestion(
            @Parameter(description = "题目ID") @PathVariable Long id) {
        try {
            questionService.deleteQuestion(id);
            return ResponseDTO.success(null);
        } catch (RuntimeException e) {
            return ResponseDTO.notFound(e.getMessage());
        }
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "按分类获取", description = "根据分类获取面试题")
    public ResponseDTO<Page<Question>> getQuestionsByCategory(
            @Parameter(description = "分类名称") @PathVariable String category,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size) {
        Page<Question> questions = questionService.getQuestionsByCategory(category, page, size);
        return ResponseDTO.success(questions, questions.getTotalElements());
    }

    @GetMapping("/difficulty/{difficulty}")
    @Operation(summary = "按难度获取", description = "根据难度获取面试题")
    public ResponseDTO<Page<Question>> getQuestionsByDifficulty(
            @Parameter(description = "难度级别") @PathVariable Integer difficulty,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size) {
        Page<Question> questions = questionService.getQuestionsByDifficulty(difficulty, page, size);
        return ResponseDTO.success(questions, questions.getTotalElements());
    }

    @GetMapping("/categories")
    @Operation(summary = "获取所有分类", description = "获取所有面试题分类")
    public ResponseDTO<List<String>> getAllCategories() {
        List<String> categories = questionService.getAllCategories();
        return ResponseDTO.success(categories);
    }
}