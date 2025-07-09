package entities;

import java.sql.Timestamp;

public class UserAchievement {
    private Integer user_id;
    private Integer achievement_id;
    private Timestamp earned_at;

    public UserAchievement() {}

    public UserAchievement(Integer user_id, Integer achievement_id, Timestamp earned_at) {
        this.user_id = user_id;
        this.achievement_id = achievement_id;
        this.earned_at = earned_at;
    }

    public Integer getUser_id() { return user_id; }
    public void setUser_id(Integer user_id) { this.user_id = user_id; }
    public Integer getAchievement_id() { return achievement_id; }
    public void setAchievement_id(Integer achievement_id) { this.achievement_id = achievement_id; }
    public Timestamp getEarned_at() { return earned_at; }
    public void setEarned_at(Timestamp earned_at) { this.earned_at = earned_at; }
} 