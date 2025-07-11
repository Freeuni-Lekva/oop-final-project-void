package quiz;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Quiz {
    private Integer quizId;
    private String title;
    private String description;
    private Integer creatorId;
    private Boolean randomize;
    private Boolean isOnePage;
    private Boolean immediateCorrection;
    private Boolean practiceMode;
    private java.sql.Timestamp createdAt;
}

