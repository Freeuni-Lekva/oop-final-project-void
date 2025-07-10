package choice;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Choice {
    private Integer choice_id;
    private Integer question_id;
    private String choice_text;
    private Boolean is_correct;
}

