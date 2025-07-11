package quiz.submitQuiz.mapper;

import quiz.submitQuiz.dtos.QuizSubmissionDto;
import quiz.submitQuiz.dtos.UserAnswerDto;
import quizAttempt.QuizAttempt;
import response.Response;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class QuizSubmissionMapper {

    public static QuizAttempt mapToQuizAttempt(QuizSubmissionDto dto, int userId) {
        return QuizAttempt.builder()
                .quizId(dto.quizId)
                .userId(userId)
                .totalQuestions(dto.answers.size())
                .attemptedAt(Timestamp.from(Instant.parse(dto.attemptedAt)))
                .score(0)
                .timeTaken(dto.timeTaken)
                .build();
    }

    public static List<Response> mapToResponses(QuizSubmissionDto dto, int attemptId) {
        List<Response> responses = new ArrayList<>();
        int order = 0;

        for (UserAnswerDto answer : dto.answers) {
            Response response = Response.builder()
                    .attemptId(attemptId)
                    .questionId(answer.questionId)
                    .responseText(answer.answerText)
                    .isCorrect(false)
                    .build();

            responses.add(response);
        }

        return responses;
    }
}
