package social.network.microservice_friend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import social.network.microservice_friend.dto.AllFriendsDto;


import java.util.Map;

public interface FriendService {

    String approve(String uuid);

    String block(String uuid);

    String request(String uuid, Map<String, String> headers) throws JsonProcessingException;

    String subscribe(String uuid);

    AllFriendsDto findAll();

    AllFriendsDto gettingFriendById(String uuidAccountId);

    AllFriendsDto recommendations();

    Integer[] friendId();

    Integer friendRequestCounter();

    Integer blockFriendId();

    String dell(Integer id);

}
