package social.network.microservice_friend.service;

import social.network.microservice_friend.dto.*;

import java.text.ParseException;
import java.util.Map;
import java.util.UUID;

public interface FriendService {
    String approve(UUID uuid, String headerToken) throws ParseException;

    String block(UUID uuid, String headerToken) throws ParseException;

    String request(UUID uuid, Map<String, String> headers) throws ParseException;

    String subscribe(UUID uuid, Map<String, String> headers) throws ParseException;

    AllFriendsDtoList gettingAllFriends(String headerToken, SearchDto searchDto);

    AccountDto gettingFriendById(UUID uuidAccountId, String headerToken);

    RecommendDtoList recommendations(String headerToken) throws ParseException;

    UUID[] friendIds(String headerToken) throws ParseException;

    Integer friendRequestCounter(String headerToken) throws ParseException;

    UUID[] blockFriendId(String headerToken) throws ParseException;

    String dell(UUID uuid, String headerToken) throws ParseException;

}
