package service;

import dtos.quiz.QuizCreateDto;
import repository.QuizRepository;

public class QuizService {
    private final QuizRepository quizRepository;

    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    public int createQuiz(QuizCreateDto dto) {
        // Add any business logic/validation here if needed
        return quizRepository.create(dto);
    }
} 