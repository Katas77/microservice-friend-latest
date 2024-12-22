/*


package social.network.microservice_friend.kafka;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaMessagingTemplate {

    @Value("${spring.topic.send-order}")
    private String sendClientTopic;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendOrder(FriendEvent orderEvent) {
        kafkaTemplate.send(sendClientTopic, String.valueOf(orderEvent.getUuid()), orderEvent);
    }

}


*/
