package quiz.TakeQuiz.dtos;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class TakeQuizDto {
    private Integer quizId;
    private String title;
    private String description;
    private Integer creatorId;
    private java.sql.Timestamp createdAt;
    private List<TakeQuizQuestionDto> questions;
}
