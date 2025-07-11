package question;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Question {
    private Integer questionId;
    private Integer quizId;
    private String questionText;
    private String type;
    private String imageUrl;
    private Integer questionOrder;
}

