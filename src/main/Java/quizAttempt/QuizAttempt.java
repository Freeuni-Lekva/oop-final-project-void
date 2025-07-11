package quizAttempt;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class QuizAttempt {
    private Integer attemptId;
    private Integer quizId;
    private Integer userId;
    private Integer score;
    private Integer totalQuestions;
    private Integer timeTaken;
    private java.sql.Timestamp attemptedAt;
}
