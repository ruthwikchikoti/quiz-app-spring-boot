package com.example.quizapp.repository;

import com.example.quizapp.model.QuizSession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface QuizSessionRepository extends JpaRepository<QuizSession, Long> {
    List<QuizSession> findByActiveAndLastAccessTimeBefore(boolean active, LocalDateTime time);
}