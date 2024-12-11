package social.network.microservice_friend.service;

import social.network.microservice_friend.dto.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface FriendService {
    String approveService(UUID uuid, String headerToken) throws ParseException;

    String block(UUID uuid, String headerToken) throws ParseException;

    String request(UUID uuid, Map<String, String> headers) throws ParseException;

    String subscribe(UUID uuid, Map<String, String> headers) throws ParseException;

    List<AllFriendsDto> gettingAllFriendsService(String headerToken, FriendSearchDto friendSearchDto);

    AccountDto gettingFriendByIdService(UUID uuidAccountId, String headerToken);

    List<RecommendDto> recommendationsService(String headerToken) throws ParseException;

    UUID[] friendIds(String headerToken) throws ParseException;

    Integer friendRequestCounter(String headerToken) throws ParseException;

    UUID[] blockFriendId(String headerToken) throws ParseException;

    String dell(UUID uuid, String headerToken) throws ParseException;

}
