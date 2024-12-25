/*

package social.network.microservice_friend.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderListener {

    @KafkaListener(topics = "${app.topic.send_topic}",
            groupId = "${app.groupId.send_groupId}")
    public void receive(@Payload String data) throws JsonProcessingException {
        log.info("KafkaListener: {}", data);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(data, new TypeReference<Map<String, Object>>() {
        });
        String userId = map.get("notificationType").toString();
        log.info("Settings for User with notificationType: {} created", userId);
    }

    @KafkaListener(topics = "${app.topic.account_topic}",
            groupId = "${app.groupId.account_groupId}")
    public void receiveAccount(@Payload String data) throws JsonProcessingException {
        log.info("Account: {}", data);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(data, new TypeReference<Map<String, Object>>() {
        });

    }


}


*/





