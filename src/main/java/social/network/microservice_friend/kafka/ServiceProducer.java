
package social.network.microservice_friend.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import social.network.microservice_friend.model.Friendship;



@Slf4j
@Component
@RequiredArgsConstructor
public class ServiceProducer {

    private final KafkaMessagingTemplate kafkaMessagingService;
    private final ModelMapper modelMapper;
    public void sendOrderEvent(Friendship friendship) {
        kafkaMessagingService.sendOrder(modelMapper.map(friendship, FriendEvent.class));
        log.info("Send order from producer {}",friendship);

    }
}
