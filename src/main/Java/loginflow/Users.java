package loginflow;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Builder
@Setter
public class Users {
    private Integer user_id;
    private String username;
    private String password_hash;
    private String salt;
    private Boolean is_admin;
    private Timestamp created_at;
}

