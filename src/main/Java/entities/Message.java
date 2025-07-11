package entities;

import java.sql.Timestamp;

public class Message {
    private Integer message_id;
    private Integer sender_id;
    private Integer receiver_id;
    private String type;
    private String content;
    private Integer quiz_id;
    private Timestamp sent_at;

    public Message() {}

    public Message(Integer message_id, Integer sender_id, Integer receiver_id, String type, String content, Integer quiz_id, Timestamp sent_at) {
        this.message_id = message_id;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.type = type;
        this.content = content;
        this.quiz_id = quiz_id;
        this.sent_at = sent_at;
    }

    public Integer getMessage_id() { return message_id; }
    public void setMessage_id(Integer message_id) { this.message_id = message_id; }
    public Integer getSender_id() { return sender_id; }
    public void setSender_id(Integer sender_id) { this.sender_id = sender_id; }
    public Integer getReceiver_id() { return receiver_id; }
    public void setReceiver_id(Integer receiver_id) { this.receiver_id = receiver_id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Integer getQuiz_id() { return quiz_id; }
    public void setQuiz_id(Integer quiz_id) { this.quiz_id = quiz_id; }
    public Timestamp getSent_at() { return sent_at; }
    public void setSent_at(Timestamp sent_at) { this.sent_at = sent_at; }
} 