package com.example.interview.repository;

import com.example.interview.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    Page<Question> findByCategory(String category, Pageable pageable);

    Page<Question> findByDifficulty(Integer difficulty, Pageable pageable);

    Page<Question> findByIsActiveTrue(Pageable pageable);

    List<Question> findByCategoryAndIsActiveTrue(String category);

    List<Question> findByTagsContaining(String tag);
}