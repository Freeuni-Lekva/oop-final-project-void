package choice;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class Choice implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Integer choiceId;
    private Integer questionId;
    private String choiceText;
    private Boolean isCorrect;

    public Choice() {

    }
}

