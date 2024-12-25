


package social.network.microservice_friend.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KafkaTemplateFriend {

    @Value("${app.topic.send_topic}")
    private String sendTopic;
    private final org.springframework.kafka.core.KafkaTemplate<String, Object> kafkaTemplate;

    public <T> void sendOrder(T orderEvent) {
        kafkaTemplate.send(sendTopic, String.valueOf(UUID.randomUUID()), orderEvent);
    }



}



