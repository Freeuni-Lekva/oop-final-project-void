package friendships;

import loginflow.Users;
import lombok.*;

import java.sql.Timestamp;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FriendshipDto {
    private String friend_name;
    private String status;
    private String friend_requested_at;

    public static FriendshipDto fromUser(Users user, Timestamp time) {
        FriendshipDto friendshipDto = new FriendshipDto();
        friendshipDto.setFriend_name(user.getUsername());
        friendshipDto.setFriend_requested_at(time.toString());

        return friendshipDto;
    }
}
