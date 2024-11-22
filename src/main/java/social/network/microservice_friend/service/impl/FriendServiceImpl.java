package social.network.microservice_friend.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import social.network.microservice_friend.clientFeign.ClientFeign;
import social.network.microservice_friend.dto.AccountDto;
import social.network.microservice_friend.dto.AllFriendsDto;
import social.network.microservice_friend.exception.BusinessLogicException;
import social.network.microservice_friend.model.Friendship;
import social.network.microservice_friend.model.en.StatusCode;
import social.network.microservice_friend.repository.FriendshipRepository;
import social.network.microservice_friend.service.FriendService;


import java.text.MessageFormat;
import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class FriendServiceImpl implements FriendService {
    private final FriendshipRepository repository;
    private final ClientFeign accountClient;


    @Override
    public String approve(UUID uuidTo) {
        Friendship friend = repository.findById(uuidTo).orElseThrow(
                () -> new BusinessLogicException(MessageFormat.format("Friend with ID {0} is NOT_FOUND", uuidTo)));
        friend.setStatusBetween(StatusCode.FRIEND);
        repository.save(friend);
        return MessageFormat.format("Friend with ID {0} is approve", uuidTo);
    }

    @Override
    public String block(UUID uuidTo) {
        Friendship friend = repository.findById(uuidTo).orElseThrow(
                () -> new BusinessLogicException(MessageFormat.format("Friend with ID {0} is NOT_FOUND", uuidTo)));
        friend.setStatusBetween(StatusCode.BLOCKED);
        repository.save(friend);
        return MessageFormat.format("Friend with ID {0} is blocked", uuidTo);
    }


    @Override
    public String request(UUID uuidTo, Map<String, String> headers) throws JsonProcessingException {
        String email = emailByHeaders(headers);
        AccountDto accountFrom = accountByEmail(email);
        System.out.println(accountFrom.toString() + "    accountByEmail");
        Friendship friendshipNew = Friendship.builder()
                .account_id_to(uuidTo)
                .account_id_from(accountFrom.getUuid())
                .statusBetween(StatusCode.REQUEST_TO)
                .uuid(UUID.randomUUID())
                .build();
        repository.save(friendshipNew);
        return MessageFormat.format("Friendship with ID {0} is save", uuidTo);

    }

    @Override
    public String subscribe(UUID uuidTo, Map<String, String> headers) throws JsonProcessingException {
        String token = headers.get("authorization").substring(7);
        UUID uuidFrom = UUID.fromString(idByHeaders(token));
        Friendship friendship = Friendship.builder()
                .account_id_to(uuidTo)
                .account_id_from(uuidFrom)
                .statusBetween(StatusCode.REQUEST_TO)
                .uuid(UUID.randomUUID())
                .build();
        repository.save(friendship);
        return null;
    }

    @Override
    public AllFriendsDto findAll() {
        return null;
    }

    @Override
    public AllFriendsDto gettingFriendById(UUID accountId) {
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
        return repository.findAllStatus_between();
    }

    @Override
    public Integer blockFriendId() {
        return null;
    }

    @Override
    public String dell(UUID uuid, String headerToken) throws JsonProcessingException {
        UUID uuidFrom = UUID.fromString(idByHeaders(headerToken));
        ArrayList<Friendship> friendships = (ArrayList<Friendship>) repository.findAllUudTo(uuid);
        Friendship friendship = friendships.stream().filter(friendship1 -> friendship1.equals(uuidFrom)).findFirst().orElseThrow();
        repository.delete(friendship);
        return MessageFormat.format("friendship with ID {0} is Dell", uuid);
    }


    private AccountDto accountById(UUID uuid) {
        return Optional.ofNullable(accountClient.getAccountById(uuid))
                .orElseThrow(() -> new BusinessLogicException(MessageFormat.format("Friend with ID {0} is NOT_FOUND", uuid)));
    }

    private AccountDto accountByEmail(String email) {
        return Optional.ofNullable(accountClient.getAccountBayEmail(email))
                .orElseThrow(() -> new BusinessLogicException(MessageFormat.format("Friend with email{0} is NOT_FOUND", email)));
    }

    public String emailByHeaders(Map<String, String> headers) throws JsonProcessingException {
        String token = headers.get("authorization").substring(7);
        String[] chunks = token.split("\\.");
        String payload = new String(Base64.getUrlDecoder().decode(chunks[1]));
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(payload).get("email").asText();
    }

    public String idByHeaders(String headerToken) throws JsonProcessingException {
        String token = headerToken.substring(7);
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(chunks[1]));
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(payload);
        return jsonNode.get("id").asText();
    }


    @PostConstruct
    public void great() {
        Friendship friendship = Friendship.builder()
                .uuid(UUID.randomUUID())
                .account_id_to(UUID.randomUUID())
                .account_id_from(UUID.randomUUID())
                .statusBetween(StatusCode.BLOCKED)
                .build();
        repository.save(friendship);
    }


}

