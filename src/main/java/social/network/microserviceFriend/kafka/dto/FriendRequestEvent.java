
package social.network.microserviceFriend.kafka.dto;


import lombok.*;
import social.network.microserviceFriend.kafka.en.NotificationType;

import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class FriendRequestEvent {
    private UUID authorId;
    private UUID userId;
    private NotificationType notificationType;
    private String content;

}
