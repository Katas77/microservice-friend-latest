package social.network.microservice_friend.service;


import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface FriendServiceOne {
    String approveService(UUID uuid, String headerToken) throws ParseException;

    String block(UUID uuid, String headerToken) throws ParseException;

    String request(UUID uuid, Map<String, String> headers) throws ParseException;

    String subscribe(UUID uuid, Map<String, String> headers) throws ParseException;

    List<UUID> friendIds(String headerToken) throws ParseException;

    Integer friendRequestCounter(String headerToken) throws ParseException;

    List<UUID>  blockFriendId(String headerToken) throws ParseException;

    String dell(UUID uuid, String headerToken) throws ParseException;

}
