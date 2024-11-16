package social.network.microservice_friend.service;



import social.network.microservice_friend.dto.AllFriendsDto;
import social.network.microservice_friend.exception.BusinessLogicException;

import java.util.UUID;

public interface FriendService {

   String approve(UUID id) throws BusinessLogicException;

    String block(UUID id);



    String request(UUID id);

   String subscribe(UUID id);

  AllFriendsDto findAll();

    AllFriendsDto gettingFriendById(Integer accountId);

    AllFriendsDto recommendations();

    Integer[] friendId();

    Integer friendRequestCounter();

    Integer blockFriendId();

    String dell(Integer id);


}
