package social.network.microservice_friend.service;



import social.network.microservice_friend.dto.AllFriendsDto;
import social.network.microservice_friend.exception.BusinessLogicException;

public interface FriendService {

   String approve(Integer id) throws BusinessLogicException;

    String block(Integer id);



    String request(Integer id);

   String subscribe(Integer id);

  AllFriendsDto findAll();

    AllFriendsDto gettingFriendById(Integer accountId);

    AllFriendsDto recommendations();

    Integer[] friendId();

    Integer friendRequestCounter();

    Integer blockFriendId();

    String dell(Integer id);


}
