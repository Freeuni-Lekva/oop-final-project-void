package entities;

import java.io.Serializable;

public class Question implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer question_id;
    private Integer quiz_id;
    private String question_text;
    private String type;
    private String image_url;
    private Integer question_order;

    public Question() {}

    public Question(Integer question_id, String question_text, Integer quiz_id, String type, String image_url, Integer question_order) {
        this.question_id = question_id;
        this.question_text = question_text;
        this.quiz_id = quiz_id;
        this.type = type;
        this.image_url = image_url;
        this.question_order = question_order;
    }

    public Integer getQuestion_id() { return question_id; }
    public void setQuestion_id(Integer question_id) { this.question_id = question_id; }
    public Integer getQuiz_id() { return quiz_id; }
    public void setQuiz_id(Integer quiz_id) { this.quiz_id = quiz_id; }
    public String getQuestion_text() { return question_text; }
    public void setQuestion_text(String question_text) { this.question_text = question_text; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getImage_url() { return image_url; }
    public void setImage_url(String image_url) { this.image_url = image_url; }
    public Integer getQuestion_order() { return question_order; }
    public void setQuestion_order(Integer question_order) { this.question_order = question_order; }
} 