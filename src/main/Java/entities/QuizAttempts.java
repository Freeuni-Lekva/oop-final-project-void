package entities;

public class QuizAttempts {
    private Integer attempt_id;
    private Integer quiz_id;
    private Integer user_id;
    private Float score;
    private Integer total_questions;
    private Integer time_taken;
    private Boolean is_practice;
    private java.sql.Timestamp attempted_at;
}
