package social.network.microservice_friend;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import social.network.microservice_friend.kafka.KafkaTemplateFriend;
import social.network.microservice_friend.kafka.dto.FriendRequestEvent;
import social.network.microservice_friend.kafka.en.NotificationType;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.concurrent.ListenableFuture;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaTemplateFriendTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    private KafkaTemplateFriend kafkaTemplateFriend;

    @BeforeEach
    void setUp() {
        kafkaTemplateFriend = new KafkaTemplateFriend(kafkaTemplate);
        ReflectionTestUtils.setField(kafkaTemplateFriend, "sendTopic", "test-topic");
    }

    @Test
    void shouldSerializeEventAndSendToKafka() throws Exception {

        UUID authorId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        NotificationType type = NotificationType.FRIEND_REQUEST;
        FriendRequestEvent event = new FriendRequestEvent(authorId, userId, type, "Hello");

        when(kafkaTemplate.send(anyString(), anyString())).thenReturn(Mockito.<CompletableFuture<SendResult<String, String>>>mock(String.valueOf(ListenableFuture.class)));

        kafkaTemplateFriend.sendEventToNotification(event);

        ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);
        verify(kafkaTemplate, times(1)).send(topicCaptor.capture(), payloadCaptor.capture());

        assertEquals("test-topic", topicCaptor.getValue());

        String json = payloadCaptor.getValue();
        assertNotNull(json);

        ObjectMapper mapper = new ObjectMapper();
        FriendRequestEvent parsed = mapper.readValue(json, FriendRequestEvent.class);

        assertEquals(event.authorId(), parsed.authorId());
        assertEquals(event.userId(), parsed.userId());
        assertEquals(event.notificationType(), parsed.notificationType());
        assertEquals(event.content(), parsed.content());
    }
}