package com.example.interview.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "面试题数据传输对象")
public class QuestionDTO {

    @Schema(description = "题目ID", example = "1")
    private Long id;

    @NotBlank(message = "题目不能为空")
    @Size(max = 500, message = "题目长度不能超过500个字符")
    @Schema(description = "题目标题", required = true, example = "什么是Java中的多态？")
    private String title;

    @NotBlank(message = "答案不能为空")
    @Schema(description = "题目答案", required = true, example = "多态是指...")
    private String answer;

    @NotBlank(message = "分类不能为空")
    @Size(max = 50, message = "分类长度不能超过50个字符")
    @Schema(description = "题目分类", required = true, example = "Java基础")
    private String category;

    @NotNull(message = "难度不能为空")
    @Min(value = 1, message = "难度最小值为1")
    @Max(value = 5, message = "难度最大值为5")
    @Schema(description = "难度等级(1-5)", required = true, example = "3")
    private Integer difficulty;

    @Schema(description = "标签(逗号分隔)", example = "面向对象,继承")
    private String tags;

    @Schema(description = "是否启用", example = "true")
    private Boolean isActive;
}