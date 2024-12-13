package social.network.microservice_friend.service.impl;

import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import social.network.microservice_friend.aop.Logger;
import social.network.microservice_friend.exception.BusinessLogicException;

import social.network.microservice_friend.kafka.ServiceProducer;
import social.network.microservice_friend.model.Friendship;
import social.network.microservice_friend.model.en.StatusCode;
import social.network.microservice_friend.repository.FriendshipRepository;
import social.network.microservice_friend.service.FriendServiceOne;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendServiceOneImpl implements FriendServiceOne {
    private final ServiceProducer producer;
    private final FriendshipRepository repository;


    @Logger
    @Override
    public String approveService(UUID uuidTo, String headerToken) throws ParseException {
        Friendship friend = repository.findToAndFrom(uuidTo, uuidFrom(headerToken)).orElseThrow(
                () -> new BusinessLogicException(MessageFormat.format("Friendship with uuidTo{0} is NOT_FOUND", uuidTo)));
        friend.setStatusBetween(StatusCode.FRIEND);
        repository.save(friend);
       // producer.sendOrderEvent(friend);
        return MessageFormat.format("Friendship with uuidTo {0} is approve", uuidTo);
    }

    @Logger
    @Override
    public String block(UUID uuidTo, String headerToken) throws ParseException {
        Optional<Friendship> friendshipOptional = repository.findToAndFrom(uuidTo, uuidFrom(headerToken));
        if (friendshipOptional.isEmpty()) {
            Friendship friendshipNew = Friendship.builder()
                    .account_id_to(uuidTo)
                    .account_id_from(uuidFrom(headerToken))
                    .statusBetween(StatusCode.BLOCKED)
                    .uuid(UUID.randomUUID())
                    .build();
            repository.save(friendshipNew);
        } else {
            Friendship friendship2 = friendshipOptional.get();
            friendship2.setStatusBetween(StatusCode.BLOCKED);
            repository.save(friendship2);
        }
        return MessageFormat.format("Friend with ID {0} is blocked", uuidTo);
    }

    @Logger
    @Override
    public String request(UUID uuidTo, Map<String, String> headers) throws ParseException {
        Optional<Friendship> friendshipOptional = repository.findToAndFrom(uuidTo, uuidFrom(headers.get("authorization")));
        if (friendshipOptional.isEmpty()) {
            Friendship friendshipNew = Friendship.builder()
                    .account_id_to(uuidTo)
                    .account_id_from(uuidFrom(headers.get("authorization")))
                    .statusBetween(StatusCode.REQUEST_FROM)
                    .uuid(UUID.randomUUID())
                    .build();
            repository.save(friendshipNew);
        } else {
            Friendship friendship2 = friendshipOptional.get();
            friendship2.setStatusBetween(StatusCode.REQUEST_FROM);
            repository.save(friendship2);
        }
        return MessageFormat.format("Friendship with uuidTo {0} REQUEST_FROM", uuidTo);

    }

    @Logger
    @Override
    public String subscribe(UUID uuidTo, Map<String, String> headers) throws ParseException {
        Optional<Friendship> friendshipOptional = repository.findToAndFrom(uuidTo, uuidFrom(headers.get("authorization")));
        if (friendshipOptional.isEmpty()) {
            Friendship friendship = Friendship.builder()
                    .account_id_to(uuidTo)
                    .account_id_from(uuidFrom(headers.get("authorization")))
                    .statusBetween(StatusCode.SUBSCRIBED)
                    .uuid(UUID.randomUUID())
                    .build();
            repository.save(friendship);
        } else {
            Friendship friendship2 = friendshipOptional.get();
            friendship2.setStatusBetween(StatusCode.SUBSCRIBED);
            repository.save(friendship2);
        }
        return MessageFormat.format("Friendship with uuidTo {0} SUBSCRIBED", uuidTo);
    }

    @Logger
    @Override
    public UUID[] friendIds(String headerToken) throws ParseException {
        return uuidFriends(uuidFrom(headerToken)).toArray(new UUID[0]);
    }

    @Logger
    @Override
    public Integer friendRequestCounter(String headerToken) throws ParseException {
        return repository.countREQUEST_TO(uuidFrom(headerToken));
    }

    @Logger
    @Override
    public UUID[] blockFriendId(String headerToken) throws ParseException {
        UUID uuidFrom = uuidFrom(headerToken);
        List<Friendship> friendships = repository.findsBLOCKED(uuidFrom);
        List<UUID> uuids = new ArrayList<>();
        for (Friendship friendship : friendships) {
            if (friendship.getAccount_id_from().equals(uuidFrom)) {
                uuids.add(friendship.getAccount_id_to());
            } else uuids.add(friendship.getAccount_id_from());
        }
        return uuids.toArray(new UUID[0]);
    }

    @Logger
    @Override
    public String dell(UUID uuidTo, String headerToken) throws ParseException {
        UUID uuidFrom = uuidFrom(headerToken);
        Friendship friendship = repository.findToAndFrom(uuidTo, uuidFrom).orElseThrow(() -> new BusinessLogicException("Friendship  is NOT_FOUND"));
        repository.delete(friendship);
        return MessageFormat.format("friendship with uuidTo {0} is Dell", uuidTo);
    }

    public UUID uuidFrom(String headerToken) throws ParseException {
        return UUID.fromString(SignedJWT.parse(headerToken.substring(7)).getPayload().toJSONObject().get("sub").toString());
    }

    @Logger
    private List<UUID> uuidFriends(UUID uuidFrom) {
        List<Friendship> friendships = repository.findFRIENDS(uuidFrom);
        List<UUID> uuidFriends = new ArrayList<>();
        friendships.stream().filter(friendship -> friendship.getAccount_id_from().equals(uuidFrom)).forEach(friendship -> uuidFriends.add(friendship.getAccount_id_to()));
        friendships.stream().filter(friendship -> friendship.getAccount_id_to().equals(uuidFrom)).forEach(friendship -> uuidFriends.add(friendship.getAccount_id_from()));
        return uuidFriends;
    }

}

