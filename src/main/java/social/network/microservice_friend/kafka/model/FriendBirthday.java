package social.network.microservice_friend.kafka.model;

import lombok.*;
import social.network.microservice_friend.kafka.en.NotificationType;

import java.util.UUID;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FriendBirthday {
    private UUID uuid;
    private UUID account_id_to;
    private UUID account_id_from;
    private NotificationType notificationType;}
