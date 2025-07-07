package main.Java.Entities;

import java.sql.Timestamp;

public class Users {
    private Integer user_id;
    private String username;
    private String password_hash;
    private String salt;
    private Boolean is_admin;
    private Timestamp created_at;
}

