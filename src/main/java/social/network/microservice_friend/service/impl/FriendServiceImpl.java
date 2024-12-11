package social.network.microservice_friend.service.impl;

import com.nimbusds.jwt.SignedJWT;
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

    @Logger
    @Override
    public String approveService(UUID uuidTo, String headerToken) throws ParseException {
        Friendship friend = repository.findToAndFrom(uuidTo, uuidFrom(headerToken)).orElseThrow(
                () -> new BusinessLogicException(MessageFormat.format("Friendship with uuidTo{0} is NOT_FOUND", uuidTo)));
        friend.setStatusBetween(StatusCode.FRIEND);
        repository.save(friend);
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
    public List<AllFriendsDto> gettingAllFriendsService(String headerToken, FriendSearchDto friendSearchDto) {
        log.info(friendSearchDto.toString());
        List<AccountDto> accountDtoList = new ArrayList<>();
        List<AccountDto> filter2 = new ArrayList<>();
        filter2.add(accountById(UUID.fromString("4a001ad4-52e8-41d2-8170-c28705c765b5"), headerToken));
        if (friendSearchDto.getIds() == null) {
            return mapper.accountsListToAllFriends(filter2);
        } else {
            Arrays.stream(friendSearchDto.getIds()).forEach(uuid -> accountDtoList.add(accountById(uuid, headerToken)));
            List<AccountDto> filter = accountDtoList.stream()
                    .filter(accountDto -> friendSearchDto.getFirstName() == null | accountDto.getFirstName().equals(friendSearchDto.getFirstName()))
                    .filter(accountDto -> friendSearchDto.getCity() == null | accountDto.getCity().equals(friendSearchDto.getCity()))
                    .filter(accountDto -> friendSearchDto.getCountry() == null | accountDto.getCountry().equals(friendSearchDto.getCountry()))
                    .filter(accountDto -> friendSearchDto.getStatusCode() == null | accountDto.getStatusCode().equals(friendSearchDto.getStatusCode()))
                    .filter(accountDto -> validBirthDate(accountDto.getBirthDate(), friendSearchDto.getBirthDateFrom(), friendSearchDto.getBirthDateTo()))
                    .filter(accountDto -> validAge(accountDto.getBirthDate(), friendSearchDto.getAgeFrom(), friendSearchDto.getAgeTo()))
                    .toList();
            return mapper.accountsListToAllFriends(filter);
        }
    }

    @Logger
    @Override
    public AccountDto gettingFriendByIdService(UUID accountId, String headerToken) {
        return accountById(accountId, headerToken);
    }

    @Logger
    @Override
    public List<RecommendDto> recommendationsService(String headerToken) throws ParseException {
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
        accountDtoList.add(accountById(UUID.fromString("4a001ad4-52e8-41d2-8170-c28705c765b5"), headerToken));
        return mapper.accountsListToRecommends(accountDtoList);
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

    @Logger
    @LoggerThrowing
    private AccountDto accountById(UUID id, String headerToken) {
        return Optional.ofNullable(accountClient.getAccountById(id, headerToken))
                .orElseThrow(() -> new BusinessLogicException(MessageFormat.format("Friend with ID {0} is NOT_FOUND", id)));
    }


    public UUID uuidFrom(String headerToken) throws ParseException {
        return UUID.fromString(SignedJWT.parse(headerToken.substring(7)).getPayload().toJSONObject().get("sub").toString());
    }

    @Logger
    private Integer mutualFriends(List<UUID> uuidFriends, List<UUID> friendsOfFriend) {
        List<UUID> all = new ArrayList<>();
        all.addAll(uuidFriends);
        all.addAll(friendsOfFriend);
        Set<UUID> set = new HashSet<>(all);
        return (all.size() - set.size());
    }

    @Logger
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


}

