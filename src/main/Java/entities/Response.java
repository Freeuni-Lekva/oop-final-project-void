package entities;

public class Response {
    private Integer response_id;
    private Integer attempt_id;
    private Integer question_id;
    private String response_text;
    private Boolean is_correct;
    private Integer response_order;

    public Response() {}

    public Response(Integer response_id, Integer attempt_id, Integer question_id, String response_text, Boolean is_correct, Integer response_order) {
        this.response_id = response_id;
        this.attempt_id = attempt_id;
        this.question_id = question_id;
        this.response_text = response_text;
        this.is_correct = is_correct;
        this.response_order = response_order;
    }

    public Integer getResponse_id() { return response_id; }
    public void setResponse_id(Integer response_id) { this.response_id = response_id; }
    public Integer getAttempt_id() { return attempt_id; }
    public void setAttempt_id(Integer attempt_id) { this.attempt_id = attempt_id; }
    public Integer getQuestion_id() { return question_id; }
    public void setQuestion_id(Integer question_id) { this.question_id = question_id; }
    public String getResponse_text() { return response_text; }
    public void setResponse_text(String response_text) { this.response_text = response_text; }
    public Boolean getIs_correct() { return is_correct; }
    public void setIs_correct(Boolean is_correct) { this.is_correct = is_correct; }
    public Integer getResponse_order() { return response_order; }
    public void setResponse_order(Integer response_order) { this.response_order = response_order; }
} 