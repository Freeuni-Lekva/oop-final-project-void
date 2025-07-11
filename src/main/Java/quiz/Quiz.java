package quiz;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class Quiz {
    private Integer quizId;
    @Getter
    @Setter
    private String title;
    private String description;
    private Integer creatorId;
    private java.sql.Timestamp createdAt;
    public Quiz() {}
}

