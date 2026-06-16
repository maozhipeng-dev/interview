package com.example.interview.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "questions", indexes = {
    @Index(name = "idx_category", columnList = "category"),
    @Index(name = "idx_difficulty", columnList = "difficulty"),
    @Index(name = "idx_is_active", columnList = "isActive"),
    @Index(name = "idx_created_at", columnList = "createdAt")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String answer;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(nullable = false)
    private Integer difficulty;

    @Column(length = 500)
    private String tags;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(length = 255)
    private String sourceUrl;

    @Column(length = 100)
    private String sourceName;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}