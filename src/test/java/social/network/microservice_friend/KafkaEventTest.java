package social.network.microservice_friend;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import social.network.microservice_friend.kafka.KafkaTemplateFriend;
import social.network.microservice_friend.kafka.dto.FriendRequestEvent;
import social.network.microservice_friend.kafka.en.NotificationType;

@ActiveProfiles("test")
class KafkaEventTest {

    @Value("${app.topic.send_topic}")
    private String sendTopic;

    @Test
    void sendEventKafka() {
        KafkaTemplate<String, String> kafkaTemplate = Mockito.mock(KafkaTemplate.class);
        KafkaTemplateFriend eventService = new KafkaTemplateFriend(kafkaTemplate);
        FriendRequestEvent event = createFriendRequestEvent();
        String eventJson = formatEventToJson(event);
        eventService.sendEventToNotification(event);

        Mockito.verify(kafkaTemplate).send(sendTopic, eventJson);
    }

    private FriendRequestEvent createFriendRequestEvent() {
        return FriendRequestEvent.builder()
                .authorId(UUID.fromString("7f6f2ece-7ccf-460f-ac36-db6626288a5e"))
                .userId(UUID.fromString("95674b05-3448-4d99-8a0e-0a765d06837f"))
                .notificationType(NotificationType.FRIEND_REQUEST)
                .build();
    }

    private String formatEventToJson(FriendRequestEvent event) {
        return String.format("{\"authorId\":\"%s\",\"userId\":\"%s\",\"notificationType\":\"%s\",\"content\":null}",
                event.getAuthorId(),
                event.getUserId(),
                event.getNotificationType());
    }
}