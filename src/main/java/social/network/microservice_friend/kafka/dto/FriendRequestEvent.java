package social.network.microservice_friend.kafka.dto;

import social.network.microservice_friend.kafka.en.NotificationType;
import java.util.UUID;
import lombok.Builder;

@Builder
public record FriendRequestEvent(
        UUID authorId,
        UUID userId,
        NotificationType notificationType,
        String content
) {}