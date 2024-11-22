package social.network.microservice_friend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import social.network.microservice_friend.dto.AllFriendsDto;


import java.util.Map;
import java.util.UUID;

public interface FriendService {

    String approve(UUID uuid);

    String block(UUID  uuid);

    String request(UUID  uuid, Map<String, String> headers) throws JsonProcessingException;

    String subscribe(UUID  uuid, Map<String, String> headers) throws JsonProcessingException;

    AllFriendsDto findAll();

    AllFriendsDto gettingFriendById(UUID uuidAccountId);

    AllFriendsDto recommendations();

    Integer[] friendId();

    Integer friendRequestCounter();

    Integer blockFriendId();

    String dell(UUID uuid,String headerToken) throws JsonProcessingException;

}
