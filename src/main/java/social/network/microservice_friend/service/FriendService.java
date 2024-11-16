package social.network.microservice_friend.service;



import com.fasterxml.jackson.core.JsonProcessingException;
import social.network.microservice_friend.dto.AllFriendsDto;
import social.network.microservice_friend.exception.BusinessLogicException;

import java.util.UUID;

public interface FriendService {

   String approve(String uuid) throws BusinessLogicException, JsonProcessingException;

    String block(String uuid);



    String request(String uuid);

   String subscribe(String uuid);

  AllFriendsDto findAll();

    AllFriendsDto gettingFriendById(String uuidAccountId);

    AllFriendsDto recommendations();

    Integer[] friendId();

    Integer friendRequestCounter();

    Integer blockFriendId();

    String dell(Integer id);


}
