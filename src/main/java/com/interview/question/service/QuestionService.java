package com.interview.question.service;

import com.interview.question.dto.QuestionRequest;
import com.interview.question.dto.QuestionResponse;
import com.interview.question.entity.Question;
import com.interview.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionResponse createQuestion(QuestionRequest request) {
        Question question = Question.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .category(request.getCategory())
                .difficulty(request.getDifficulty())
                .build();
        Question savedQuestion = questionRepository.save(question);
        return mapToResponse(savedQuestion);
    }

    public Page<QuestionResponse> getAllQuestions(Pageable pageable) {
        return questionRepository.findAll(pageable).map(this::mapToResponse);
    }

    public List<QuestionResponse> getQuestionsByCategory(String category) {
        return questionRepository.findByCategory(category).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<QuestionResponse> getQuestionsByDifficulty(String difficulty) {
        return questionRepository.findByDifficulty(difficulty).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public QuestionResponse getQuestionById(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + id));
        return mapToResponse(question);
    }

    public void deleteQuestion(Long id) {
        if (!questionRepository.existsById(id)) {
            throw new RuntimeException("Question not found with id: " + id);
        }
        questionRepository.deleteById(id);
    }

    public QuestionResponse getRandomQuestion() {
        long count = questionRepository.count();
        if (count == 0) {
            throw new RuntimeException("No questions available");
        }
        Random random = new Random();
        int index = random.nextInt((int) count);
        Page<Question> questionPage = questionRepository.findAll(PageRequest.of(index, 1));
        
        if (questionPage.hasContent()) {
            return mapToResponse(questionPage.getContent().get(0));
        }
        throw new RuntimeException("No questions available");
    }

    private QuestionResponse mapToResponse(Question question) {
        return QuestionResponse.builder()
                .id(question.getId())
                .title(question.getTitle())
                .description(question.getDescription())
                .category(question.getCategory())
                .difficulty(question.getDifficulty())
                .createdAt(question.getCreatedAt())
                .build();
    }
}
