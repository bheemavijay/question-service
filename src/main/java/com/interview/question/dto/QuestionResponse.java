package com.interview.question.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponse {
    private Long id;
    private String title;
    private String description;
    private String category;
    private String difficulty;
    private String answer;
    private String explanation;
    private LocalDateTime createdAt;
}
