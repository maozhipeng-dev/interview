package com.example.interview.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {

    private Long id;

    @NotBlank(message = "题目不能为空")
    @Size(max = 500, message = "题目长度不能超过500个字符")
    private String title;

    @NotBlank(message = "答案不能为空")
    private String answer;

    @NotBlank(message = "分类不能为空")
    @Size(max = 50, message = "分类长度不能超过50个字符")
    private String category;

    @NotNull(message = "难度不能为空")
    @Min(value = 1, message = "难度最小值为1")
    @Max(value = 5, message = "难度最大值为5")
    private Integer difficulty;

    private String tags;

    private Boolean isActive;
}