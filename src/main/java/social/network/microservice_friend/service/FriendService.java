package social.network.microservice_friend.service;

import social.network.microservice_friend.dto.AccountDto;
import social.network.microservice_friend.dto.AllFriendsDto;
import social.network.microservice_friend.dto.FriendSearchDto;

import java.text.ParseException;
import java.util.Map;
import java.util.UUID;

public interface FriendService {
    String approve(UUID uuid, String headerToken) throws ParseException;
    String block(UUID uuid, String headerToken) throws ParseException;
    String request(UUID uuid, Map<String, String> headers) throws ParseException;
    String subscribe(UUID uuid, Map<String, String> headers) throws ParseException;
    AllFriendsDto findAll(FriendSearchDto request);
    AccountDto gettingFriendById(UUID uuidAccountId);
    AllFriendsDto recommendations();
    UUID[] friendIds(String headerToken) throws ParseException;
    Integer friendRequestCounter(String headerToken) throws ParseException;
    UUID[] blockFriendId(String headerToken) throws ParseException;
    String dell(UUID uuid, String headerToken) throws ParseException;

}
