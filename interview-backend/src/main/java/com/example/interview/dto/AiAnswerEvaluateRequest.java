package com.example.interview.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "AI答案评估请求")
public class AiAnswerEvaluateRequest {

    @NotBlank(message = "问题不能为空")
    @Schema(description = "面试问题", required = true, example = "请解释什么是Spring Boot的自动配置？")
    private String question;

    @NotBlank(message = "用户答案不能为空")
    @Schema(description = "用户回答", required = true, example = "自动配置是Spring Boot的核心功能...")
    private String userAnswer;

    @Schema(description = "期望答案要点", example = "1. @SpringBootApplication注解...")
    private String answerHint;
}
