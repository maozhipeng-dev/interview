package com.example.interview.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "questions")
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
}