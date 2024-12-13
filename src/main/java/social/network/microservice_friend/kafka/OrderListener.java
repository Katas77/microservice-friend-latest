/*


package social.network.microservice_friend.kafka;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderListener {

    @KafkaListener(id = "foo", topics = "${spring.topic.status-order}")
    public void receive(@Payload String data) {
        log.info("KafkaListener: {}", data);
    }

}


*/

