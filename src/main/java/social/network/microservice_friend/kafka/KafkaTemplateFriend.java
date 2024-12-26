package social.network.microservice_friend.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import social.network.microservice_friend.kafka.dto.FriendRequestEvent;


import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaTemplateFriend {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    @Value("${app.topic.send_topic}")
    private String sendTopic;
    private final KafkaTemplate<String, String> kafkaTemplate;
    public void sendEventToNotification(FriendRequestEvent friendRequestEvent) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String eventJson = objectMapper.writeValueAsString(friendRequestEvent);
            kafkaTemplate.send(sendTopic, eventJson);
            log.info("Send event: {}", eventJson);
            System.out.println(ANSI_RED + "Warning ! sendOrderEvent"+eventJson + ANSI_RESET);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert account to JSON: {} - don't send", friendRequestEvent.toString(), e);
        }

    }


}



