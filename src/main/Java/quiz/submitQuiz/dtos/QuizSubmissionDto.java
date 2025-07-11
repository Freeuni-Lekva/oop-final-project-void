package quiz.submitQuiz.dtos;


import java.util.List;

public class QuizSubmissionDto {
    public int quizId;
    public int totalQuestions;
    public int timeTaken;
    public String attemptedAt;
    public List<UserAnswerDto> answers;
}



