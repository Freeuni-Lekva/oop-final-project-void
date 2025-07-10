package friendships;

import entities.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FriendshipDto {
    private String friend_name;
    private String friend_requested_at;

    public static FriendshipDto fromUser(Users user, Timestamp time) {
        FriendshipDto friendshipDto = new FriendshipDto();
        friendshipDto.setFriend_name(user.getUsername());
        friendshipDto.setFriend_requested_at(time.toString());

        return friendshipDto;
    }
}
