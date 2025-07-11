package messages;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private String senderUsername;
    private String type;
    private String content;
    private Timestamp sent_at;
}
