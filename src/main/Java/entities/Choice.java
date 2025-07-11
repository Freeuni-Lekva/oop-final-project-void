package entities;

import java.io.Serializable;

public class Choice implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer choice_id;
    private Integer question_id;
    private String choice_text;
    private Boolean is_correct;

    public Choice() {}

    public Choice(Integer choice_id, Integer question_id, String choice_text, Boolean is_correct) {
        this.choice_id = choice_id;
        this.question_id = question_id;
        this.choice_text = choice_text;
        this.is_correct = is_correct;
    }

    public Integer getChoice_id() { return choice_id; }
    public void setChoice_id(Integer choice_id) { this.choice_id = choice_id; }
    public Integer getQuestion_id() { return question_id; }
    public void setQuestion_id(Integer question_id) { this.question_id = question_id; }
    public String getChoice_text() { return choice_text; }
    public void setChoice_text(String choice_text) { this.choice_text = choice_text; }
    public Boolean getIs_correct() { return is_correct; }
    public void setIs_correct(Boolean is_correct) { this.is_correct = is_correct; }
} 