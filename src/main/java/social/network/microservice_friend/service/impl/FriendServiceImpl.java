package social.network.microservice_friend.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import social.network.microservice_friend.accauntFeign.FeignClientAccount;
import social.network.microservice_friend.dto.AccountDto;
import social.network.microservice_friend.dto.AllFriendsDto;
import social.network.microservice_friend.exception.BusinessLogicException;
import social.network.microservice_friend.model.Friendship;
import social.network.microservice_friend.model.en.StatusCode;
import social.network.microservice_friend.repository.FriendshipRepository;
import social.network.microservice_friend.service.FriendService;
import java.text.MessageFormat;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class FriendServiceImpl implements FriendService {
    private final FriendshipRepository repository;
   private final FeignClientAccount accountClient;


    @Override
    public String approve(String uuid) throws BusinessLogicException, JsonProcessingException {
      Optional <AccountDto>  account= Optional.ofNullable(accountClient.getAccountById(uuid));
        System.out.println(account.orElseThrow(() -> new BusinessLogicException(MessageFormat.format("Friend with ID {0} is NOT_FOUND", uuid))).getCountry());

        Friendship friend = repository.findById(uuid).orElseThrow(
                () -> new BusinessLogicException(MessageFormat.format("Friend with ID {0} is NOT_FOUND", uuid)));
        friend.setStatusBetween(StatusCode.FRIEND);
        repository.save(friend);
        return MessageFormat.format("Friend with ID {0} is approve", uuid);
    }

    @Override
    public String block(String uuid) {
        Friendship friend = repository.findById(uuid).orElseThrow();
        friend.setStatusBetween(StatusCode.BLOCKED);
        repository.save(friend);
        return MessageFormat.format("Friend with ID {0} is blocked", uuid);
    }


    @Override
    public String request(String uuid) {
        return null;
    }

    @Override
    public String subscribe(String uuid) {
        return null;
    }

    @Override
    public AllFriendsDto findAll() {
        return null;
    }

    @Override
    public AllFriendsDto gettingFriendById(String accountId) {
        return null;
    }

    @Override
    public AllFriendsDto recommendations() {
        return null;
    }

    @Override
    public Integer[] friendId() {
        return null;
    }

    @Override
    public Integer friendRequestCounter() {
        return null;
    }

    @Override
    public Integer blockFriendId() {
        return null;
    }

    @Override
    public String dell(Integer id) {
        return null;
    }
}
