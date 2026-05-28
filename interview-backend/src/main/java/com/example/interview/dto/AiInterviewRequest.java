package com.example.interview.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "AI面试请求")
public class AiInterviewRequest {

    @NotBlank(message = "职位不能为空")
    @Schema(description = "应聘职位", required = true, example = "Java后端开发工程师")
    private String position;

    @Schema(description = "经验年限", example = "3年")
    private String experience;

    @Schema(description = "技术栈", example = "Java,Spring Boot,MySQL,Redis")
    private String techStack;

    @Schema(description = "期望职位级别", example = "中级")
    private String level;

    @Size(max = 500, message = "自定义要求长度不能超过500个字符")
    @Schema(description = "自定义面试要求", example = "重点考察微服务和高并发相关知识")
    private String customRequirement;
}
