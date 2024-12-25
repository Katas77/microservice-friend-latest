


package social.network.microservice_friend.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KafkaTemplateFriend {
    public static final String ANSI_RESET = "\u001B[0m";

    public static final String ANSI_RED = "\u001B[31m";
    @Value("${app.topic.send_topic}")
    private String sendTopic;
    private final org.springframework.kafka.core.KafkaTemplate<String, Object> kafkaTemplate;

    public <T> void sendOrder(T orderEvent) {
        kafkaTemplate.send(sendTopic, String.valueOf(UUID.randomUUID()), orderEvent);
        System.out.println(ANSI_RED + "Warning! app.topic.send_topic: " + ANSI_RESET);
    }



}



