package social.network.microservice_friend.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.SignedJWT;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import social.network.microservice_friend.clientFeign.ClientFeign;
import social.network.microservice_friend.dto.AccountDto;
import social.network.microservice_friend.dto.AllFriendsDto;
import social.network.microservice_friend.dto.FriendSearchDto;
import social.network.microservice_friend.exception.BusinessLogicException;
import social.network.microservice_friend.model.Friendship;
import social.network.microservice_friend.model.en.StatusCode;
import social.network.microservice_friend.repository.FriendshipRepository;
import social.network.microservice_friend.service.FriendService;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class FriendServiceImpl implements FriendService {
    private final FriendshipRepository repository;
    private final ClientFeign accountClient;

    @Override
    public String approve(UUID uuidTo, String headerToken) throws ParseException {
        UUID uuidFrom = UUID.fromString(idByHeaders(headerToken));
        log.info(String.valueOf(uuidFrom));
        Friendship friend = repository.findToAndFrom(uuidTo, uuidFrom).orElseThrow(
                () -> new BusinessLogicException(MessageFormat.format("Friendship with uuidTo{0} is NOT_FOUND", uuidTo)));
        friend.setStatusBetween(StatusCode.FRIEND);
        repository.save(friend);
        return MessageFormat.format("Friendship with ID {0} is approve", uuidTo);
    }

    @Override
    public String block(UUID uuidTo, String headerToken) throws ParseException {
        UUID uuidFrom = UUID.fromString(idByHeaders(headerToken));
        Friendship friend = repository.findToAndFrom(uuidTo, uuidFrom).orElseThrow(
                () -> new BusinessLogicException(MessageFormat.format("Friendship with uuidTo{0} is BLOCKED", uuidTo)));
        friend.setStatusBetween(StatusCode.BLOCKED);
        repository.save(friend);
        return MessageFormat.format("Friend with ID {0} is blocked", uuidTo);
    }


    @Override
    public String request(UUID uuidTo, Map<String, String> headers) throws ParseException {
        UUID uuidFrom = UUID.fromString(idByHeaders(headers.get("authorization")));
        Friendship friendshipNew = Friendship.builder()
                .account_id_to(uuidTo)
                .account_id_from(uuidFrom)
                .statusBetween(StatusCode.REQUEST_FROM)
                .uuid(UUID.randomUUID())
                .build();
        repository.save(friendshipNew);
        return MessageFormat.format("Friendship with ID {0} REQUEST_FROM", uuidTo);

    }

    @Override
    public String subscribe(UUID uuidTo, Map<String, String> headers) throws ParseException {
        UUID uuidFrom = UUID.fromString(idByHeaders(headers.get("authorization")));
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
    public AllFriendsDto findAll(FriendSearchDto request) {
        return null;
    }

    @Override
    public AccountDto gettingFriendById(UUID accountId) {
        return accountById(accountId);
    }

    @Override
    public AllFriendsDto recommendations() {
        return null;
    }

    @Override
    public UUID[] friendIds(String headerToken) throws ParseException {
        UUID uuidFrom = UUID.fromString(idByHeaders(headerToken));
        List<Friendship> friendships = repository.findFRIENDS(uuidFrom);
        List<UUID> uuids = new ArrayList<>();
        friendships.stream().filter(friendship -> friendship.getAccount_id_from().equals(uuidFrom)).forEach(friendship -> uuids.add(friendship.getAccount_id_to()));
        friendships.stream().filter(friendship -> friendship.getAccount_id_to().equals(uuidFrom)).forEach(friendship -> uuids.add(friendship.getAccount_id_from()));
        return uuids.toArray(new UUID[0]);
    }


    @Override
    public Integer friendRequestCounter(String headerToken) throws ParseException {
        UUID uuidFrom = UUID.fromString(idByHeaders(headerToken));
        return repository.countREQUEST_TO(uuidFrom);
    }

    @Override
    public UUID[] blockFriendId(String headerToken) throws ParseException {
        UUID uuidFrom = UUID.fromString(idByHeaders(headerToken));
        List<Friendship> friendships = repository.findsBLOCKED(uuidFrom);
        List<UUID> uuids = new ArrayList<>();
        friendships.stream().filter(friendship -> friendship.getAccount_id_from().equals(uuidFrom)).forEach(friendship -> uuids.add(friendship.getAccount_id_to()));
        friendships.stream().filter(friendship -> friendship.getAccount_id_to().equals(uuidFrom)).forEach(friendship -> uuids.add(friendship.getAccount_id_from()));
        return uuids.toArray(new UUID[0]);
    }

    @Override
    public String dell(UUID uuidTo, String headerToken) throws ParseException {
        UUID uuidFrom = UUID.fromString(idByHeaders(headerToken));
        ArrayList<Friendship> friendships = (ArrayList<Friendship>) repository.findAllUudTo(uuidTo);
        System.out.println(friendships.size());
        Friendship friendship = friendships.stream().filter(friendship1 -> friendship1.getAccount_id_from().equals(uuidFrom)).findFirst().orElseThrow(
                () -> new BusinessLogicException(MessageFormat.format("Friendship with uuidTo{0} is NOT_FOUND", uuidTo)));
        repository.delete(friendship);
        return MessageFormat.format("friendship with ID {0} is Dell", uuidTo);
    }


    private AccountDto accountById(UUID id) {
        return Optional.ofNullable(accountClient.getAccountById(id))
                .orElseThrow(() -> new BusinessLogicException(MessageFormat.format("Friend with ID {0} is NOT_FOUND", id)));
    }


    public String idByHeaders(String headerToken) throws ParseException {
        return SignedJWT.parse(headerToken.substring(7)).getPayload().toJSONObject().get("id").toString();
    }


    @PostConstruct
    public void great() {
        Random random = new Random();
        int b = random.nextInt(0, 3);
        StatusCode[] statusCodes = {StatusCode.FRIEND, StatusCode.BLOCKED, StatusCode.REQUEST_TO, StatusCode.SUBSCRIBED};
        Friendship friendship = Friendship.builder()
                .uuid(UUID.randomUUID())
                .account_id_to(UUID.randomUUID())
                .account_id_from(UUID.randomUUID())
                .statusBetween(statusCodes[b])
                .build();
        repository.save(friendship);
        Friendship friendship2 = Friendship.builder()
                .uuid(UUID.randomUUID())
                .account_id_to(UUID.fromString("b3999ffa-2df9-469e-9793-ee65e214846e"))
                .account_id_from(UUID.randomUUID())
                .statusBetween(StatusCode.BLOCKED)
                .build();
        repository.save(friendship2);
    }

    public String emailByHeaders(Map<String, String> headers) throws JsonProcessingException {
        String token = headers.get("authorization").substring(7);
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String header = new String(decoder.decode(chunks[0]));
        String payload = new String(decoder.decode(chunks[1]));
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(payload).get("email").asText();
    }

    private AccountDto accountByEmail(String email) {
        return Optional.ofNullable(accountClient.getAccountBayEmail(email))
                .orElseThrow(() -> new BusinessLogicException(MessageFormat.format("Friend with email{0} is NOT_FOUND", email)));
    }
}

