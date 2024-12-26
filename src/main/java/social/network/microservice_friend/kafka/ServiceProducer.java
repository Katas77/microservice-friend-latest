
package social.network.microservice_friend.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import social.network.microservice_friend.kafka.dto.FriendRequestEvent;
import social.network.microservice_friend.kafka.model.FriendRequest;


@Slf4j
@Component
@RequiredArgsConstructor
public class ServiceProducer {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    private final KafkaTemplateFriend template;
    private final ModelMapper modelMapper;



}

