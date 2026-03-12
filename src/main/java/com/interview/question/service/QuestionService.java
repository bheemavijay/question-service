package com.interview.question.service;

import com.interview.question.dto.QuestionRequest;
import com.interview.question.dto.QuestionResponse;
import com.interview.question.entity.Question;
import com.interview.question.exception.ResourceNotFoundException;
import com.interview.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
                .answer(request.getAnswer())
                .explanation(request.getExplanation())
                .build();
        Question savedQuestion = questionRepository.save(question);
        return mapToResponse(savedQuestion);
    }

    public List<QuestionResponse> createBulkQuestions(List<QuestionRequest> requests) {
        List<Question> questions = requests.stream()
                .map(request -> Question.builder()
                        .title(request.getTitle())
                        .description(request.getDescription())
                        .category(request.getCategory())
                        .difficulty(request.getDifficulty())
                        .answer(request.getAnswer())
                        .explanation(request.getExplanation())
                        .build())
                .collect(Collectors.toList());
        
        List<Question> savedQuestions = questionRepository.saveAll(questions);
        return savedQuestions.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public QuestionResponse updateQuestion(Long id, QuestionRequest request) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + id));
        
        question.setTitle(request.getTitle());
        question.setDescription(request.getDescription());
        question.setCategory(request.getCategory());
        question.setDifficulty(request.getDifficulty());
        question.setAnswer(request.getAnswer());
        question.setExplanation(request.getExplanation());
        
        Question updatedQuestion = questionRepository.save(question);
        return mapToResponse(updatedQuestion);
    }

    public Page<QuestionResponse> getAllQuestions(Pageable pageable) {
        return questionRepository.findAll(pageable).map(this::mapToResponse);
    }

    public Page<QuestionResponse> getQuestionsByFilter(String category, String difficulty, Pageable pageable) {
        return questionRepository.findByFilters(category, difficulty, pageable).map(this::mapToResponse);
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
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + id));
        return mapToResponse(question);
    }

    public void deleteQuestion(Long id) {
        if (!questionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Question not found with id: " + id);
        }
        questionRepository.deleteById(id);
    }

    public List<Long> getRandomQuestionIds(int limit) {
        List<Question> questions = questionRepository.findAll();
        Collections.shuffle(questions);

        return questions.stream()
                .limit(limit)
                .map(Question::getId)
                .toList();
    }

    public List<QuestionResponse> getQuestionsFromIds(List<Long> ids) {

        List<Question> questions = questionRepository.findAllById(ids);

        return questions.stream()
                .map(this::mapToResponse)
                .toList();
    }

    private QuestionResponse mapToResponse(Question question) {
        return QuestionResponse.builder()
                .id(question.getId())
                .title(question.getTitle())
                .description(question.getDescription())
                .category(question.getCategory())
                .difficulty(question.getDifficulty())
                .answer(question.getAnswer())
                .explanation(question.getExplanation())
                .createdAt(question.getCreatedAt())
                .build();
    }
}
