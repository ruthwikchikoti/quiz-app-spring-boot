package com.example.quizapp.repository;

import com.example.quizapp.model.QuizSession;
import com.example.quizapp.model.UserResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserResponseRepository extends JpaRepository<UserResponse, Long> {
    List<UserResponse> findByQuizSession(QuizSession session);
    List<UserResponse> findByQuizSessionOrderByAnsweredAtDesc(QuizSession session);
}