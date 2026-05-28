package com.example.interview.service.impl;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.ResultCallback;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.example.interview.config.AiConfig;
import com.example.interview.dto.*;
import com.example.interview.service.AiInterviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiInterviewServiceImpl implements AiInterviewService {

    private final AiConfig aiConfig;
    private final ObjectMapper objectMapper;

    @Override
    public AiInterviewResponse generateInterviewQuestions(AiInterviewRequest request) {
        String prompt = buildInterviewPrompt(request);
        String response = callAi(prompt);
        return parseInterviewResponse(response);
    }

    @Override
    public AiAnswerEvaluateResponse evaluateAnswer(AiAnswerEvaluateRequest request) {
        String prompt = buildEvaluationPrompt(request);
        String response = callAi(prompt);
        return parseEvaluationResponse(response);
    }

    private String buildInterviewPrompt(AiInterviewRequest request) {
        return String.format("""
            你是一位专业的面试官。请根据以下信息生成5道面试题目：
            
            应聘职位：%s
            经验年限：%s
            技术栈：%s
            职位级别：%s
            特殊要求：%s
            
            请以JSON格式返回，包含以下字段：
            - questions: 数组，每个题目包含：
              - question: 问题内容
              - answerHint: 期望的答案要点
              - difficulty: 难度(简单/中级/高级)
              - category: 知识点分类
            - suggestions: 整体面试建议
            
            只返回JSON，不要包含其他文字说明。
            """,
            request.getPosition(),
            request.getExperience() != null ? request.getExperience() : "不限",
            request.getTechStack() != null ? request.getTechStack() : "根据职位生成",
            request.getLevel() != null ? request.getLevel() : "中级",
            request.getCustomRequirement() != null ? request.getCustomRequirement() : "无");
    }

    private String buildEvaluationPrompt(AiAnswerEvaluateRequest request) {
        return String.format("""
            请作为面试官评估以下答案：
            
            面试问题：%s
            用户回答：%s
            %s
            
            请以JSON格式返回，包含以下字段：
            - score: 0-100的整数分数
            - level: 评估等级(优秀/良好/合格/需要改进)
            - evaluation: 详细评估
            - suggestions: 改进建议
            - referenceAnswer: 参考答案
            
            只返回JSON，不要包含其他文字说明。
            """,
            request.getQuestion(),
            request.getUserAnswer(),
            request.getAnswerHint() != null ? "期望答案要点：" + request.getAnswerHint() : "");
    }

    private String callAi(String prompt) {
        try {
            Generation gen = new Generation();
            Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content("你是一位专业的技术面试官，擅长评估候选人的技术能力。请严格按照要求返回JSON格式的结果。")
                .build();
            Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content(prompt)
                .build();

            GenerationParam param = GenerationParam.builder()
                .apiKey(aiConfig.getApiKey())
                .model(aiConfig.getModel())
                .messages(Arrays.asList(systemMsg, userMsg))
                .temperature(aiConfig.getTemperature())
                .maxTokens(aiConfig.getMaxTokens())
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .build();

            GenerationResult result = gen.call(param);
            String content = result.getOutput().getChoices().get(0).getMessage().getContent();
            log.info("AI响应成功");
            return extractJson(content);
        } catch (ApiException | NoApiKeyException | InputRequiredException e) {
            log.error("AI调用失败", e);
            throw new RuntimeException("AI服务调用失败：" + e.getMessage());
        }
    }

    private String extractJson(String content) {
        int start = content.indexOf('{');
        int end = content.lastIndexOf('}');
        if (start != -1 && end != -1) {
            return content.substring(start, end + 1);
        }
        return content;
    }

    private AiInterviewResponse parseInterviewResponse(String json) {
        try {
            return objectMapper.readValue(json, AiInterviewResponse.class);
        } catch (Exception e) {
            log.error("解析AI响应失败", e);
            AiInterviewResponse fallback = new AiInterviewResponse();
            AiInterviewResponse.InterviewQuestion q = new AiInterviewResponse.InterviewQuestion();
            q.setQuestion("请解释你对这个职位的理解？");
            q.setAnswerHint("1. 职位职责 2. 技术要求 3. 发展方向");
            q.setDifficulty("简单");
            q.setCategory("职业规划");
            fallback.setQuestions(List.of(q));
            fallback.setSuggestions("由于AI服务暂不可用，这是一个简单的面试题示例。请配置正确的API Key后重试。");
            return fallback;
        }
    }

    private AiAnswerEvaluateResponse parseEvaluationResponse(String json) {
        try {
            return objectMapper.readValue(json, AiAnswerEvaluateResponse.class);
        } catch (Exception e) {
            log.error("解析AI响应失败", e);
            AiAnswerEvaluateResponse fallback = new AiAnswerEvaluateResponse();
            fallback.setScore(60);
            fallback.setLevel("合格");
            fallback.setEvaluation("感谢你的回答！建议结合更多实际项目经验进行说明。");
            fallback.setSuggestions("1. 补充具体案例 2. 突出技术细节 3. 说明遇到的挑战和解决方案");
            fallback.setReferenceAnswer("这个问题的完整回答应该包括...");
            return fallback;
        }
    }
}
