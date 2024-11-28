package social.network.microservice_friend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import social.network.microservice_friend.dto.AccountDto;
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

    AccountDto gettingFriendById(UUID uuidAccountId);

    AllFriendsDto recommendations();

    UUID[] friendIds(String headerToken) throws ParseException;

    Integer friendRequestCounter(String headerToken) throws JsonProcessingException, ParseException;

    public UUID[]  blockFriendId(String headerToken) throws ParseException;

    String dell(UUID uuid,String headerToken) throws JsonProcessingException, ParseException;

}
