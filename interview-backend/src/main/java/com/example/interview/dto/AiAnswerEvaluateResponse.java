package com.example.interview.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "AI答案评估响应")
public class AiAnswerEvaluateResponse {

    @Schema(description = "评分(0-100)", example = "85")
    private Integer score;

    @Schema(description = "评估等级", example = "良好")
    private String level;

    @Schema(description = "详细评估", example = "回答基本正确，但可以更详细地说明条件注解的使用")
    private String evaluation;

    @Schema(description = "改进建议", example = "建议补充说明@ConditionalOnProperty注解的用法")
    private String suggestions;

    @Schema(description = "参考答案", example = "Spring Boot的自动配置通过...")
    private String referenceAnswer;
}
