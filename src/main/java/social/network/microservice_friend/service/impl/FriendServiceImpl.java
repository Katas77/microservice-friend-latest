package social.network.microservice_friend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
;
import org.springframework.stereotype.Service;
import social.network.microservice_friend.dto.AllFriendsDto;
import social.network.microservice_friend.exception.BusinessLogicException;
import social.network.microservice_friend.model.Friendship;
import social.network.microservice_friend.model.en.StatusCode;
import social.network.microservice_friend.repository.FriendshipRepository;
import social.network.microservice_friend.service.FriendService;

import java.text.MessageFormat;


@Service
@RequiredArgsConstructor
@Slf4j
public class FriendServiceImpl implements FriendService {
    private final FriendshipRepository repository;


    @Override
    public String approve(Integer id) throws BusinessLogicException {
        Friendship friend = repository.findById(id).orElseThrow(
                () -> new BusinessLogicException(MessageFormat.format("Friend with ID {0} is NOT_FOUND", id)));
        friend.setStatusBetween(StatusCode.FRIEND);
        repository.save(friend);
        return MessageFormat.format("Friend with ID {0} is approve", id);
    }

    @Override
    public String block(Integer id) {
        Friendship friend = repository.findById(id).orElseThrow();
        friend.setStatusBetween(StatusCode.BLOCKED);
        repository.save(friend);
        return MessageFormat.format("Friend with ID {0} is blocked", id);
    }


    @Override
    public String request(Integer id) {
        return null;
    }

    @Override
    public String subscribe(Integer id) {
        return null;
    }

    @Override
    public AllFriendsDto findAll() {
        return null;
    }

    @Override
    public AllFriendsDto gettingFriendById(Integer accountId) {
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
