package social.network.microservice_friend.service.impl;

import com.nimbusds.jwt.SignedJWT;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import social.network.microservice_friend.aop.Logger;
import social.network.microservice_friend.aop.LoggerThrowing;
import social.network.microservice_friend.clientFeign.ClientFeign;
import social.network.microservice_friend.dto.*;
import social.network.microservice_friend.exception.BusinessLogicException;
import social.network.microservice_friend.mapper.MapperDTO;
import social.network.microservice_friend.model.Friendship;
import social.network.microservice_friend.model.en.StatusCode;
import social.network.microservice_friend.repository.FriendshipRepository;
import social.network.microservice_friend.service.FriendService;

import java.text.MessageFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendServiceImpl implements FriendService {
    private final MapperDTO mapper;
    private final FriendshipRepository repository;
    private final ClientFeign accountClient;


    @Override
    public String approve(UUID uuidTo, String headerToken) throws ParseException {
        log.info("approve is call");
        Friendship friend = repository.findToAndFrom(uuidTo, uuidFrom(headerToken)).orElseThrow(
                () -> new BusinessLogicException(MessageFormat.format("Friendship with uuidTo{0} is NOT_FOUND", uuidTo)));
        friend.setStatusBetween(StatusCode.FRIEND);
        repository.save(friend);
        return MessageFormat.format("Friendship with uuidTo {0} is approve", uuidTo);
    }

    @Override
    public String block(UUID uuidTo, String headerToken) throws ParseException {
        Friendship friend = repository.findToAndFrom(uuidTo, uuidFrom(headerToken)).orElseThrow(
                () -> new BusinessLogicException(MessageFormat.format("Friendship with uuidTo{0} is BLOCKED", uuidTo)));
        friend.setStatusBetween(StatusCode.BLOCKED);
        repository.save(friend);
        return MessageFormat.format("Friend with ID {0} is blocked", uuidTo);
    }


    @Override
    public String request(UUID uuidTo, Map<String, String> headers) throws ParseException {
        log.info("request is call");
        Friendship friendshipNew = Friendship.builder()
                .account_id_to(uuidTo)
                .account_id_from(uuidFrom(headers.get("authorization")))
                .statusBetween(StatusCode.REQUEST_FROM)
                .uuid(UUID.randomUUID())
                .build();
        repository.save(friendshipNew);
        return MessageFormat.format("Friendship with uuidTo {0} REQUEST_FROM", uuidTo);

    }

    @Override
    public String subscribe(UUID uuidTo, Map<String, String> headers) throws ParseException {
        log.info("SUBSCRIBED is call");
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
    public AllFriendsDtoList gettingAllFriends(String headerToken, SearchDto searchDto) {
        log.info(searchDto.toString());
        List<AccountDto> accountDtoList = new ArrayList<>();
        searchDto.getIds().forEach(uuid -> accountDtoList.add(accountById(uuid, headerToken)));
        List<AccountDto> filter = accountDtoList.stream()
                .filter(accountDto -> searchDto.getFirstName() == null | accountDto.getFirstName().equals(searchDto.getFirstName()))
                .filter(accountDto -> searchDto.getCity() == null | accountDto.getCity().equals(searchDto.getCity()))
                .filter(accountDto -> searchDto.getCountry() == null | accountDto.getCountry().equals(searchDto.getCountry()))
                .filter(accountDto -> searchDto.getStatusCode() == null | accountDto.getStatusCode().equals(searchDto.getStatusCode()))
                .filter(accountDto -> validBirthDate(accountDto.getBirthDate(), searchDto.getBirthDateFrom(), searchDto.getBirthDateTo()))
                .filter(accountDto -> validAge(accountDto.getBirthDate(), searchDto.getAgeFrom(), searchDto.getAgeTo()))
                .toList();
        return mapper.accountsListToAllFriends(filter);
    }


    @Override
    public AccountDto gettingFriendById(UUID accountId, String headerToken) {
        return accountById(accountId, headerToken);
    }

    @Override
    public RecommendDtoList recommendations(String headerToken) throws ParseException {
        log.info("recommendations   is  call");
        List<AccountDto> accountDtoList = new ArrayList<>();
        List<UUID> uuidFriends = uuidFriends(uuidFrom(headerToken));
        for (UUID uuid : uuidFriends) {
            List<UUID> friendsOfFriend = uuidFriends(uuid);
            for (UUID uuid2 : friendsOfFriend) {
                List<UUID> friendsOfFriend2 = uuidFriends(uuid2);
                if (mutualFriends(uuidFriends, friendsOfFriend2) >= 2) {
                    accountDtoList.add(accountById(uuid2, headerToken));
                }
            }
        }
        return mapper.accountsListToRecommends(accountDtoList);
    }

    @Override
    public UUID[] friendIds(String headerToken) throws ParseException {
        return uuidFriends(uuidFrom(headerToken)).toArray(new UUID[0]);
    }


    @Override
    public Integer friendRequestCounter(String headerToken) throws ParseException {
        log.info("friendRequestCounter    is call");
        return repository.countREQUEST_TO(uuidFrom(headerToken));
    }

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

    @Override
    public String dell(UUID uuidTo, String headerToken) throws ParseException {
        UUID uuidFrom = uuidFrom(headerToken);
        ArrayList<Friendship> friendships = (ArrayList<Friendship>) repository.findAllUudTo(uuidTo);
        Friendship friendship = friendships.stream().filter(friendship1 -> friendship1.getAccount_id_from().equals(uuidFrom)).findFirst().orElseThrow(
                () -> new BusinessLogicException(MessageFormat.format("Friendship with uuidTo{0} is NOT_FOUND", uuidTo)));
        repository.delete(friendship);
        return MessageFormat.format("friendship with uuidTo {0} is Dell", uuidTo);
    }

    @LoggerThrowing
    private AccountDto accountById(UUID id, String headerToken) {
        return Optional.ofNullable(accountClient.getAccountById(id, headerToken))
                .orElseThrow(() -> new BusinessLogicException(MessageFormat.format("Friend with ID {0} is NOT_FOUND", id)));
    }


    public UUID uuidFrom(String headerToken) throws ParseException {
        return UUID.fromString(SignedJWT.parse(headerToken.substring(7)).getPayload().toJSONObject().get("sub").toString());
    }


    private Integer mutualFriends(List<UUID> uuidFriends, List<UUID> friendsOfFriend) {
        List<UUID> all = new ArrayList<>();
        all.addAll(uuidFriends);
        all.addAll(friendsOfFriend);
        Set<UUID> set = new HashSet<>(all);
        return (all.size() - set.size());
    }

    private List<UUID> uuidFriends(UUID uuidFrom) {
        List<Friendship> friendships = repository.findFRIENDS(uuidFrom);
        List<UUID> uuidFriends = new ArrayList<>();
        friendships.stream().filter(friendship -> friendship.getAccount_id_from().equals(uuidFrom)).forEach(friendship -> uuidFriends.add(friendship.getAccount_id_to()));
        friendships.stream().filter(friendship -> friendship.getAccount_id_to().equals(uuidFrom)).forEach(friendship -> uuidFriends.add(friendship.getAccount_id_from()));
        return uuidFriends;
    }

    private boolean validBirthDate(LocalDate birthDate, LocalDate birthDateFrom, LocalDate birthDateTo) {
        if (birthDate == null) {
            return false;
        }
        birthDateFrom = birthDateFrom == null ? LocalDate.of(1, 1, 1) : birthDateFrom;
        birthDateTo = birthDateTo == null ? LocalDate.now() : birthDateTo;
        return !(birthDate.isBefore(birthDateFrom) || birthDate.isAfter(birthDateTo));
    }

    private boolean validAge(LocalDate birthDate, Integer ageFrom, Integer ageTo) {
        if (birthDate == null) {
            return false;
        }
        int age = LocalDate.now().getYear() - birthDate.getYear();
        ageFrom = ageFrom == null ? 0 : ageFrom;
        ageTo = ageTo == null ? 1000 : ageTo;
        return !(age < ageFrom || age > ageTo);
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
                .statusBetween(statusCodes[b])
                .build();
        repository.save(friendship2);
    }

}

