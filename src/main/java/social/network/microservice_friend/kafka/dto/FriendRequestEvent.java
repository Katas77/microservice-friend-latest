
package social.network.microservice_friend.kafka.dto;


import lombok.*;
import social.network.microservice_friend.kafka.en.NotificationType;


import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FriendRequestEvent {
    private UUID uuid;
    private UUID account_id_to;
    private UUID account_id_from;
    private NotificationType notificationType;
}
