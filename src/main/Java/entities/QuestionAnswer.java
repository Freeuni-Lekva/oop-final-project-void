package entities;

import java.io.Serializable;

public class QuestionAnswer implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer answer_id;
    private Integer question_id;
    private String answer_text;
    private Integer answer_order;

    public QuestionAnswer() {}

    public QuestionAnswer(Integer answer_id, Integer question_id, String answer_text, Integer answer_order) {
        this.answer_id = answer_id;
        this.question_id = question_id;
        this.answer_text = answer_text;
        this.answer_order = answer_order;
    }

    public Integer getAnswer_id() { return answer_id; }
    public void setAnswer_id(Integer answer_id) { this.answer_id = answer_id; }
    public Integer getQuestion_id() { return question_id; }
    public void setQuestion_id(Integer question_id) { this.question_id = question_id; }
    public String getAnswer_text() { return answer_text; }
    public void setAnswer_text(String answer_text) { this.answer_text = answer_text; }
    public Integer getAnswer_order() { return answer_order; }
    public void setAnswer_order(Integer answer_order) { this.answer_order = answer_order; }
} 