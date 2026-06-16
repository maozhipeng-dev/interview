package com.example.interview.service.impl;

import com.example.interview.dto.QuestionDTO;
import com.example.interview.entity.Question;
import com.example.interview.repository.QuestionRepository;
import com.example.interview.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    @Cacheable(value = "questions", key = "'all_'.concat(#page.toString()).concat('_').concat(#size.toString())")
    public Page<Question> getAllQuestions(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return questionRepository.findAll(pageable);
    }

    @Override
    @Cacheable(value = "questions", key = "'id_'.concat(#id.toString())")
    public Question getQuestionById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("题目不存在，ID: " + id));
    }

    @Override
    @Transactional
    @CacheEvict(value = "questions", allEntries = true)
    public Question createQuestion(QuestionDTO questionDTO) {
        Question question = new Question();
        question.setTitle(questionDTO.getTitle());
        question.setAnswer(questionDTO.getAnswer());
        question.setCategory(questionDTO.getCategory());
        question.setDifficulty(questionDTO.getDifficulty());
        question.setTags(questionDTO.getTags());
        question.setIsActive(questionDTO.getIsActive() != null ? questionDTO.getIsActive() : true);
        return questionRepository.save(question);
    }

    @Override
    @Transactional
    @CacheEvict(value = "questions", allEntries = true)
    public Question updateQuestion(Long id, QuestionDTO questionDTO) {
        Question question = getQuestionById(id);
        question.setTitle(questionDTO.getTitle());
        question.setAnswer(questionDTO.getAnswer());
        question.setCategory(questionDTO.getCategory());
        question.setDifficulty(questionDTO.getDifficulty());
        question.setTags(questionDTO.getTags());
        if (questionDTO.getIsActive() != null) {
            question.setIsActive(questionDTO.getIsActive());
        }
        return questionRepository.save(question);
    }

    @Override
    @Transactional
    @CacheEvict(value = "questions", allEntries = true)
    public void deleteQuestion(Long id) {
        if (!questionRepository.existsById(id)) {
            throw new RuntimeException("题目不存在，ID: " + id);
        }
        questionRepository.deleteById(id);
    }

    @Override
    @Cacheable(value = "questions", key = "'category_'.concat(#category).concat('_').concat(#page.toString()).concat('_').concat(#size.toString())")
    public Page<Question> getQuestionsByCategory(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return questionRepository.findByCategory(category, pageable);
    }

    @Override
    @Cacheable(value = "questions", key = "'difficulty_'.concat(#difficulty.toString()).concat('_').concat(#page.toString()).concat('_').concat(#size.toString())")
    public Page<Question> getQuestionsByDifficulty(Integer difficulty, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return questionRepository.findByDifficulty(difficulty, pageable);
    }

    @Override
    @Cacheable(value = "questions", key = "'active_category_'.concat(#category)")
    public List<Question> getActiveQuestionsByCategory(String category) {
        return questionRepository.findByCategoryAndIsActiveTrue(category);
    }

    @Override
    @Cacheable(value = "questions", key = "'categories'")
    public List<String> getAllCategories() {
        return questionRepository.findAll().stream()
                .map(Question::getCategory)
                .distinct()
                .collect(Collectors.toList());
    }
}