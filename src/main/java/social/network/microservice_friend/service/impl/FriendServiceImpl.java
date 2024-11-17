package social.network.microservice_friend.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class FriendServiceImpl implements FriendService {
    private final FriendshipRepository repository;
    private final FeignClientAccount accountClient;


    @Override
    public String approve(String uuid) {
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
    public String request(String uuid, Map<String, String> headers) throws JsonProcessingException {
        String uuidFriendship = String.valueOf(UUID.randomUUID());
        String email = email(headers);
        AccountDto accountDto = accountUUIDGetEmail(email);
        String accountOfferUUID = accountDto.getUuid();
        Friendship friendshipNew = Friendship.builder()
                .accountOfferUUID(accountOfferUUID)
                .accountAnswerUUID(uuid)
                .statusBetween(StatusCode.SUBSCRIBED)
                .uuid(uuidFriendship)
                .build();
        System.out.println(friendshipNew.getAccountAnswerUUID() + "              friendshipNew          " + friendshipNew.getStatusBetween());
        repository.save(friendshipNew);

        return MessageFormat.format("Friendship with ID {0} is save", uuid);

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


    private AccountDto account(String uuid) {
        return Optional.ofNullable(accountClient.getAccountById(uuid))
                .orElseThrow(() -> new BusinessLogicException(MessageFormat.format("Friend with ID {0} is NOT_FOUND", uuid)));
    }

    public String email(Map<String, String> headers) throws JsonProcessingException {
        String token = headers.get("authorization").substring(7);
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String header = new String(decoder.decode(chunks[0]));
        String payload = new String(decoder.decode(chunks[1]));
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(payload);
        return jsonNode.get("email").asText();
    }

    private AccountDto accountUUIDGetEmail(String email) {
        return Optional.ofNullable(accountClient.getAccountBayEmail(email))
                .orElseThrow(() -> new BusinessLogicException(MessageFormat.format("Friend with ID {0} is NOT_FOUND", email)));
    }
}
//String token="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IlJvbWFuIiwiZW1haWwiOiJrcnA3N0BtYWlsLnJ1In0.QFbiuTijoW4YsxvlYakG0_m2KY_ak9v7aAXLQRpttd4";
