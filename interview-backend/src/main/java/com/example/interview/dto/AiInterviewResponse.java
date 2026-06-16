package com.example.interview.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "AI面试响应")
public class AiInterviewResponse {

    @Schema(description = "面试题目列表")
    private List<InterviewQuestion> questions;

    @Schema(description = "面试建议")
    private String suggestions;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "面试问题")
    public static class InterviewQuestion {

        @Schema(description = "问题内容", example = "请解释什么是Spring Boot的自动配置？")
        private String question;

        @Schema(description = "期望答案要点", example = "1. @SpringBootApplication注解的作用 2. 自动配置原理 3. 条件化注解")
        private String answerHint;

        @Schema(description = "问题难度", example = "中级")
        private String difficulty;

        @Schema(description = "知识点分类", example = "Spring Boot")
        private String category;
    }
}
