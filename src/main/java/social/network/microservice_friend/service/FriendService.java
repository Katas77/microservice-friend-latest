package social.network.microservice_friend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import social.network.microservice_friend.dto.AllFriendsDto;


import java.text.ParseException;
import java.util.Map;
import java.util.UUID;

public interface FriendService {

    String approve(UUID uuid,String headerToken) throws JsonProcessingException, ParseException;

    String block(UUID  uuid,String headerToken) throws JsonProcessingException, ParseException;

    String request(UUID  uuid, Map<String, String> headers) throws JsonProcessingException, ParseException;

    String subscribe(UUID  uuid, Map<String, String> headers) throws JsonProcessingException, ParseException;

    AllFriendsDto findAll();

    AllFriendsDto gettingFriendById(UUID uuidAccountId);

    AllFriendsDto recommendations();

    Integer[] friendId();

    Integer friendRequestCounter(String headerToken) throws JsonProcessingException, ParseException;

    Integer blockFriendId();

    String dell(UUID uuid,String headerToken) throws JsonProcessingException, ParseException;

}
