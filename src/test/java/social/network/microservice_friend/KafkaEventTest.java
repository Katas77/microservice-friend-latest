package social.network.microservice_friend;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.test.context.ActiveProfiles;
import social.network.microservice_friend.kafka.KafkaTemplateFriend;
import social.network.microservice_friend.kafka.dto.FriendRequestEvent;
import social.network.microservice_friend.kafka.en.NotificationType;


@ActiveProfiles
public class KafkaEventTest {
    @Value("${app.topic.send_topic}")
    private String sendTopic;
    @Test
    void sendEvenKafka() throws JsonProcessingException {
        KafkaTemplate<String, String> kafkaTemplate= Mockito.mock(KafkaTemplate.class);
        KafkaTemplateFriend eventService=new KafkaTemplateFriend(kafkaTemplate);
        FriendRequestEvent event1 = FriendRequestEvent.builder()
                .authorId(UUID.fromString("7f6f2ece-7ccf-460f-ac36-db6626288a5e"))
                .userId(UUID.fromString("95674b05-3448-4d99-8a0e-0a765d06837f"))
                .notificationType(NotificationType.FRIEND_REQUEST)
                .build();
        String event = "{\"authorId\":\"7f6f2ece-7ccf-460f-ac36-db6626288a5e\",\"userId\":\"95674b05-3448-4d99-8a0e-0a765d06837f\",\"notificationType\":\"FRIEND_REQUEST\",\"content\":null}";
        eventService.sendEventToNotification(event1);
        Mockito.verify(kafkaTemplate).send(null, event);
    }


}