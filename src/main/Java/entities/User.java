package entities;

import java.sql.Timestamp;

public class User {
    private Integer user_id;
    private String username;
    private String password_hash;
    private String salt;
    private Boolean is_admin;
    private Timestamp created_at;

    public User() {}

    public User(Integer user_id, String username, String password_hash, String salt, Boolean is_admin, Timestamp created_at) {
        this.user_id = user_id;
        this.username = username;
        this.password_hash = password_hash;
        this.salt = salt;
        this.is_admin = is_admin;
        this.created_at = created_at;
    }

    public Integer getUser_id() { return user_id; }
    public void setUser_id(Integer user_id) { this.user_id = user_id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword_hash() { return password_hash; }
    public void setPassword_hash(String password_hash) { this.password_hash = password_hash; }
    public String getSalt() { return salt; }
    public void setSalt(String salt) { this.salt = salt; }
    public Boolean getIs_admin() { return is_admin; }
    public void setIs_admin(Boolean is_admin) { this.is_admin = is_admin; }
    public Timestamp getCreated_at() { return created_at; }
    public void setCreated_at(Timestamp created_at) { this.created_at = created_at; }
} 