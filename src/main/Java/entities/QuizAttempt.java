package entities;

import java.sql.Timestamp;

public class QuizAttempt {
    private Integer attempt_id;
    private Integer quiz_id;
    private Integer user_id;
    private Float score;
    private Integer total_questions;
    private Integer time_taken;
    private Boolean is_practice;
    private Timestamp attempted_at;

    public QuizAttempt() {}

    public QuizAttempt(Integer attempt_id, Integer quiz_id, Integer user_id, Float score, Integer total_questions, Integer time_taken, Boolean is_practice, Timestamp attempted_at) {
        this.attempt_id = attempt_id;
        this.quiz_id = quiz_id;
        this.user_id = user_id;
        this.score = score;
        this.total_questions = total_questions;
        this.time_taken = time_taken;
        this.is_practice = is_practice;
        this.attempted_at = attempted_at;
    }

    public Integer getAttempt_id() { return attempt_id; }
    public void setAttempt_id(Integer attempt_id) { this.attempt_id = attempt_id; }
    public Integer getQuiz_id() { return quiz_id; }
    public void setQuiz_id(Integer quiz_id) { this.quiz_id = quiz_id; }
    public Integer getUser_id() { return user_id; }
    public void setUser_id(Integer user_id) { this.user_id = user_id; }
    public Float getScore() { return score; }
    public void setScore(Float score) { this.score = score; }
    public Integer getTotal_questions() { return total_questions; }
    public void setTotal_questions(Integer total_questions) { this.total_questions = total_questions; }
    public Integer getTime_taken() { return time_taken; }
    public void setTime_taken(Integer time_taken) { this.time_taken = time_taken; }
    public Boolean getIs_practice() { return is_practice; }
    public void setIs_practice(Boolean is_practice) { this.is_practice = is_practice; }
    public Timestamp getAttempted_at() { return attempted_at; }
    public void setAttempted_at(Timestamp attempted_at) { this.attempted_at = attempted_at; }
} 