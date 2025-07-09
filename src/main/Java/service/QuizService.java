package service;

import entities.*;
import repository.*;

import java.util.List;

public class QuizService {
    private final QuizRepository quizRepo;
    private final QuestionService questionService;

    public QuizService(
            QuizRepository quizRepo,
            QuestionService questionService
    ) {
        this.quizRepo = quizRepo;
        this.questionService = questionService;
    }

    public void createQuizWithQuestions(Quiz quiz, List<QuestionBundle> bundles) throws Exception {
        quizRepo.create(quiz);
        int quizId = quiz.getQuiz_id();

        for (QuestionBundle bundle : bundles) {
            questionService.createQuestionWithAnswersAndChoices(bundle, quizId);
        }
    }


    public void updateQuizWithQuestions(Quiz quiz, List<QuestionBundle> updatedBundles) throws Exception {
        quizRepo.update(quiz);

        List<Question> existingQuestions = quizRepo.getQuestionsByQuizId(quiz.getQuiz_id());
        for (Question q : existingQuestions) {
            questionService.deleteQuestion(q.getQuestion_id());
        }


        for (QuestionBundle bundle : updatedBundles) {
            questionService.createQuestionWithAnswersAndChoices(bundle, quiz.getQuiz_id());
        }
    }
}
