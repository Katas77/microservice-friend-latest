

package social.network.microservice_friend.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import social.network.microservice_friend.kafka.dto.FriendBirthdayEvent;
import social.network.microservice_friend.kafka.dto.FriendRequestEvent;
import social.network.microservice_friend.kafka.model.FriendBirthday;
import social.network.microservice_friend.kafka.model.FriendRequest;


@Slf4j
@Component
@RequiredArgsConstructor
public class ServiceProducer {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    private final KafkaTemplateFriend template;
    private final ModelMapper modelMapper;
    public void sendOrderEvent(FriendRequest friendRequest) {
        template.sendOrder(modelMapper.map(friendRequest, FriendRequestEvent.class));
        log.info("Send order from producer {}",friendRequest);
        System.out.println(ANSI_RED + "Warning! sendOrderEvent" + ANSI_RESET);

    }

    public void sendOrderEvent2(FriendBirthday friendBirthday) {
        template.sendOrder(modelMapper.map(friendBirthday, FriendBirthdayEvent.class));
        log.info("Send FriendBirthday from producer {}",friendBirthday);
        System.out.println(ANSI_RED + "Warning! sendOrderEvent" + ANSI_RESET);

    }
}

