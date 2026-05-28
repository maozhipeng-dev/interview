package com.example.interview.controller;

import com.example.interview.dto.*;
import com.example.interview.service.AiInterviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/ai-interview")
@RequiredArgsConstructor
@Tag(name = "AI面试", description = "AI面试官相关接口")
public class AiInterviewController {

    private final AiInterviewService aiInterviewService;

    @PostMapping("/generate")
    @Operation(summary = "生成面试题", description = "根据职位信息生成AI面试题目")
    public ResponseDTO<AiInterviewResponse> generateQuestions(@Valid @RequestBody AiInterviewRequest request) {
        log.info("收到生成面试题请求，职位：{}", request.getPosition());
        AiInterviewResponse response = aiInterviewService.generateInterviewQuestions(request);
        return ResponseDTO.success(response);
    }

    @PostMapping("/evaluate")
    @Operation(summary = "评估答案", description = "AI评估用户回答并给出分数和建议")
    public ResponseDTO<AiAnswerEvaluateResponse> evaluateAnswer(@Valid @RequestBody AiAnswerEvaluateRequest request) {
        log.info("收到答案评估请求");
        AiAnswerEvaluateResponse response = aiInterviewService.evaluateAnswer(request);
        return ResponseDTO.success(response);
    }
}
