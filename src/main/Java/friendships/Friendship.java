package friendships;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Friendship {
    private Integer requester_id;
    private Integer receiver_id;
    private String status;
    private java.sql.Timestamp requested_at;
}

