package com.interview.question.controller;

import com.interview.question.dto.QuestionRequest;
import com.interview.question.dto.QuestionResponse;
import com.interview.question.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping
    public ResponseEntity<QuestionResponse> createQuestion(@Valid @RequestBody QuestionRequest request) {
        return new ResponseEntity<>(questionService.createQuestion(request), HttpStatus.CREATED);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<QuestionResponse>> createBulkQuestions(@RequestBody List<QuestionRequest> requests) {
        return new ResponseEntity<>(questionService.createBulkQuestions(requests), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionResponse> updateQuestion(@PathVariable Long id, @Valid @RequestBody QuestionRequest request) {
        return ResponseEntity.ok(questionService.updateQuestion(id, request));
    }

    @GetMapping
    public ResponseEntity<Page<QuestionResponse>> getAllQuestions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(questionService.getAllQuestions(pageable));
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<QuestionResponse>> getQuestionsByFilter(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String difficulty,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(questionService.getQuestionsByFilter(category, difficulty, pageable));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<QuestionResponse>> getQuestionsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(questionService.getQuestionsByCategory(category));
    }

    @GetMapping("/difficulty/{difficulty}")
    public ResponseEntity<List<QuestionResponse>> getQuestionsByDifficulty(@PathVariable String difficulty) {
        return ResponseEntity.ok(questionService.getQuestionsByDifficulty(difficulty));
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionResponse> getQuestionById(@PathVariable Long id) {
        return ResponseEntity.ok(questionService.getQuestionById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/random")
    public ResponseEntity<QuestionResponse> getRandomQuestion() {
        return ResponseEntity.ok(questionService.getRandomQuestion());
    }
}
