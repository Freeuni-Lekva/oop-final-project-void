package response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class Response {
    private Integer responseId;
    private Integer attemptId;
    private Integer questionId;
    private String responseText;
    private Boolean isCorrect;
}

