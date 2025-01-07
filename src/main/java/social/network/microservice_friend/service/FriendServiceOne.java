package social.network.microservice_friend.service;


import social.network.microservice_friend.dto.Message;

import java.text.ParseException;
import java.util.List;
import java.util.UUID;

public interface FriendServiceOne {
    Message approveService(UUID uuid, String headerToken) throws ParseException;

    Message block(UUID uuid, String headerToken) throws ParseException;

    Message request(UUID uuid, String headerToken) throws ParseException;

    Message subscribe(UUID uuid, String headerToken) throws ParseException;

    List<UUID> friendIds(String headerToken) throws ParseException;

    List<UUID> friendIdsForPost(UUID userId);

    Integer friendRequestCounter(String headerToken) throws ParseException;

    List<UUID> blockFriendId(String headerToken) throws ParseException;

    Message dell(UUID uuid, String headerToken) throws ParseException;


}
