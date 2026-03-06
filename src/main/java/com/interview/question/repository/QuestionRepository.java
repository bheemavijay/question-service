package com.interview.question.repository;

import com.interview.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByCategory(String category);

    List<Question> findByDifficulty(String difficulty);

    @Query("SELECT q FROM Question q WHERE (:category IS NULL OR q.category = :category) AND (:difficulty IS NULL OR q.difficulty = :difficulty)")
    Page<Question> findByFilters(@Param("category") String category, @Param("difficulty") String difficulty, Pageable pageable);
}
