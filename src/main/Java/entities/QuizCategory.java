package entities;

public class QuizCategory {
    private Integer quiz_id;
    private Integer category_id;

    public QuizCategory() {}

    public QuizCategory(Integer quiz_id, Integer category_id) {
        this.quiz_id = quiz_id;
        this.category_id = category_id;
    }

    public Integer getQuiz_id() { return quiz_id; }
    public void setQuiz_id(Integer quiz_id) { this.quiz_id = quiz_id; }
    public Integer getCategory_id() { return category_id; }
    public void setCategory_id(Integer category_id) { this.category_id = category_id; }
} 