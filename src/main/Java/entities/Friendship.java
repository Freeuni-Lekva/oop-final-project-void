package entities;

import java.sql.Timestamp;

public class Friendship {
    private Integer request_id;
    private Integer requester_id;
    private Integer receiver_id;
    private String status;
    private Timestamp requested_at;

    public Friendship() {}

    public Friendship(Integer request_id, Integer requester_id, Integer receiver_id, String status, Timestamp requested_at) {
        this.request_id = request_id;
        this.requester_id = requester_id;
        this.receiver_id = receiver_id;
        this.status = status;
        this.requested_at = requested_at;
    }

    public Integer getRequest_id() { return request_id; }
    public void setRequest_id(Integer request_id) { this.request_id = request_id; }
    public Integer getRequester_id() { return requester_id; }
    public void setRequester_id(Integer requester_id) { this.requester_id = requester_id; }
    public Integer getReceiver_id() { return receiver_id; }
    public void setReceiver_id(Integer receiver_id) { this.receiver_id = receiver_id; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Timestamp getRequested_at() { return requested_at; }
    public void setRequested_at(Timestamp requested_at) { this.requested_at = requested_at; }
} 