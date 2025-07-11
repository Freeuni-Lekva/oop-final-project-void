package entities;

import java.sql.Timestamp;

public class Announcement {
    private Integer announcement_id;
    private Integer admin_id;
    private String title;
    private String content;
    private Timestamp posted_at;

    public Announcement() {}

    public Announcement(Integer announcement_id, Integer admin_id, String title, String content, Timestamp posted_at) {
        this.announcement_id = announcement_id;
        this.admin_id = admin_id;
        this.title = title;
        this.content = content;
        this.posted_at = posted_at;
    }

    public Integer getAnnouncement_id() { return announcement_id; }
    public void setAnnouncement_id(Integer announcement_id) { this.announcement_id = announcement_id; }
    public Integer getAdmin_id() { return admin_id; }
    public void setAdmin_id(Integer admin_id) { this.admin_id = admin_id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Timestamp getPosted_at() { return posted_at; }
    public void setPosted_at(Timestamp posted_at) { this.posted_at = posted_at; }
} 