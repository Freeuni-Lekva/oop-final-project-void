package entities;

import java.sql.Timestamp;

public class Quiz {
    private Integer quiz_id;
    private String title;
    private String description;
    private Integer creator_id;
    private Boolean randomize;

    public Quiz(Integer quiz_id, String title, String description, Integer creator_id, Boolean randomize, Boolean is_one_page, Boolean immediate_correction, Boolean practice_mode, Timestamp created_at) {
        this.quiz_id = quiz_id;
        this.title = title;
        this.description = description;
        this.creator_id = creator_id;
        this.randomize = randomize;
        this.is_one_page = is_one_page;
        this.immediate_correction = immediate_correction;
        this.practice_mode = practice_mode;
        this.created_at = created_at;
    }

    private Boolean is_one_page;
    private Boolean immediate_correction;

    public Integer getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(Integer quiz_id) {
        this.quiz_id = quiz_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(Integer creator_id) {
        this.creator_id = creator_id;
    }

    public Boolean getRandomize() {
        return randomize;
    }

    public void setRandomize(Boolean randomize) {
        this.randomize = randomize;
    }

    public Boolean getIs_one_page() {
        return is_one_page;
    }

    public void setIs_one_page(Boolean is_one_page) {
        this.is_one_page = is_one_page;
    }

    public Boolean getImmediate_correction() {
        return immediate_correction;
    }

    public void setImmediate_correction(Boolean immediate_correction) {
        this.immediate_correction = immediate_correction;
    }

    public Boolean getPractice_mode() {
        return practice_mode;
    }

    public void setPractice_mode(Boolean practice_mode) {
        this.practice_mode = practice_mode;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    private Boolean practice_mode;
    private java.sql.Timestamp created_at;
}

