package com.example.interview.service;

import com.example.interview.dto.AiAnswerEvaluateRequest;
import com.example.interview.dto.AiAnswerEvaluateResponse;
import com.example.interview.dto.AiInterviewRequest;
import com.example.interview.dto.AiInterviewResponse;

public interface AiInterviewService {

    AiInterviewResponse generateInterviewQuestions(AiInterviewRequest request);

    AiAnswerEvaluateResponse evaluateAnswer(AiAnswerEvaluateRequest request);
}
