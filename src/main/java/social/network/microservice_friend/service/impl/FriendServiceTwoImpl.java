package social.network.microservice_friend.service.impl;

import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import social.network.microservice_friend.aop.Logger;
import social.network.microservice_friend.aop.LoggerThrowing;
import social.network.microservice_friend.clientFeign.ClientFeign;
import social.network.microservice_friend.dto.AccountDto;
import social.network.microservice_friend.dto.FriendDto;
import social.network.microservice_friend.dto.FriendSearchDto;
import social.network.microservice_friend.dto.RecommendationFriendsDto;
import social.network.microservice_friend.dto.en.AccountStatus;
import social.network.microservice_friend.dto.responsF.FriendsRs;
import social.network.microservice_friend.dto.responsF.RecommendationFriendsRs;
import social.network.microservice_friend.exception.BusinessLogicException;
import social.network.microservice_friend.mapper.MapperDTO;
import social.network.microservice_friend.model.Friendship;
import social.network.microservice_friend.model.en.StatusCode;
import social.network.microservice_friend.repository.FriendshipRepository;
import social.network.microservice_friend.service.FriendServiceOne;
import social.network.microservice_friend.service.FriendServiceTwo;

import java.text.MessageFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendServiceTwoImpl implements FriendServiceTwo {
    private final MapperDTO mapper;
    private final FriendshipRepository repository;
    private final ClientFeign accountClient;
    private final FriendServiceOne friendServiceOne;

    @Logger
    @Override
    public FriendsRs gettingAllFriendsService(String headerToken, FriendSearchDto friendSearchDto, Pageable pageable) throws ParseException {
        log.info(friendSearchDto.toString());

        if (friendSearchDto.getIds() == null & friendSearchDto.getStatusCode().equals(StatusCode.REQUEST_FROM)) {
            friendSearchDto.setIds(repository.findIdStatusREQUEST_FROM(uuidFrom(headerToken)));
        }
        if (friendSearchDto.getIds() == null & friendSearchDto.getStatusCode().equals(StatusCode.WATCHING)) {
            friendSearchDto.setIds(repository.findIdStatusWATCHING(uuidFrom(headerToken)));
        }
        if (friendSearchDto.getIds() == null & friendSearchDto.getStatusCode().equals(StatusCode.REQUEST_TO)) {
            friendSearchDto.setIds(repository.findIdStatusREQUEST_TO(uuidFrom(headerToken)));
        }
        if (friendSearchDto.getIds() == null & friendSearchDto.getStatusCode().equals(StatusCode.SUBSCRIBED)) {
            friendSearchDto.setIds(repository.findIdStatus_SUBSCRIBED(uuidFrom(headerToken)));
        }
        if (friendSearchDto.getIds() == null & friendSearchDto.getStatusCode().equals(StatusCode.BLOCKED)) {
            friendSearchDto.setIds(friendServiceOne.blockFriendId(headerToken));
        }
        if (friendSearchDto.getIds() == null & friendSearchDto.getStatusCode().equals(StatusCode.FRIEND)) {
            friendSearchDto.setIds(uuidFriends(uuidFrom(headerToken)));
        }
        if (!(friendSearchDto.getIds() == null)){
        List<AccountDto> filter2 = search1(friendSearchDto, headerToken);
        List<FriendDto> friendDtoList = mapper.accountsListToFriendDtoList(filter2, friendSearchDto.getStatusCode());
        Page<FriendDto> friendPage2 = convertListToPage(friendDtoList, pageable);
        return FriendsRs.builder()
                .content(friendPage2.getContent())
                .totalElements(friendPage2.getTotalElements())
                .totalPages(friendPage2.getTotalPages())
                .build();}
            return statusCodeNull(headerToken,pageable);
        }




    @Logger
    @Override
    public RecommendationFriendsRs recommendationsService(String headerToken, Pageable pageable) throws ParseException {
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
        accountDtoList.addAll(defaultAccountDto(headerToken));
        List<RecommendationFriendsDto> list = mapper.accountsListToRecommends(accountDtoList);
        Page<RecommendationFriendsDto> friends = convertListToPage(list, pageable);
        return RecommendationFriendsRs.builder()
                .content(friends.getContent())
                .totalElements(friends.getTotalElements())
                .totalPages(friends.getTotalPages())
                .build();
    }

    @Logger
    @Override
    public AccountDto gettingFriendByIdService(UUID accountId, String headerToken) {
        return accountById(accountId, headerToken);
    }


    @Logger
    @LoggerThrowing
    private AccountDto accountById(UUID id, String headerToken) {
        return Optional.ofNullable(accountClient.getAccountById(id, headerToken))
                .orElseThrow(() -> new BusinessLogicException(MessageFormat.format("Friend with ID {0} is NOT_FOUND", id)));
    }

    private List<AccountDto> search1(FriendSearchDto friendSearchDto, String headerToken) {
        List<AccountDto> accountDtoList = new ArrayList<>();
        friendSearchDto.getIds().forEach(uuid -> accountDtoList.add(accountById(uuid, headerToken)));
        return accountDtoList.stream()
                .filter(accountDto -> friendSearchDto.getFirstName() == null || accountDto.getFirstName().equals(friendSearchDto.getFirstName()))
                .filter(accountDto -> friendSearchDto.getCity() == null || accountDto.getCity().equals(friendSearchDto.getCity()))
                .filter(accountDto -> friendSearchDto.getCountry() == null || accountDto.getCountry().equals(friendSearchDto.getCountry()))
                .filter(accountDto -> validBirthDate(accountDto.getBirthDate(), friendSearchDto.getBirthDateFrom(), friendSearchDto.getBirthDateTo()))
                .filter(accountDto -> validAge(accountDto.getBirthDate(), friendSearchDto.getAgeFrom(), friendSearchDto.getAgeTo()))
                .toList();
    }

    private boolean validBirthDate(LocalDate birthDate, LocalDate birthDateFrom, LocalDate birthDateTo) {
        if (birthDate == null) {
            return true;
        }
        birthDateFrom = birthDateFrom == null ? LocalDate.of(1, 1, 1) : birthDateFrom;
        birthDateTo = birthDateTo == null ? LocalDate.now() : birthDateTo;
        return !(birthDate.isBefore(birthDateFrom) || birthDate.isAfter(birthDateTo));
    }

    private boolean validAge(LocalDate birthDate, Integer ageFrom, Integer ageTo) {
        if (birthDate == null) {
            return true;
        }
        int age = LocalDate.now().getYear() - birthDate.getYear();
        ageFrom = ageFrom == null ? 0 : ageFrom;
        ageTo = ageTo == null ? 1000 : ageTo;
        return !(age < ageFrom || age > ageTo);
    }

    private <T> Page<T> convertListToPage(List<T> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min(pageable.getPageSize(), list.size());
        return new PageImpl<>(list.subList(start, end), pageable, list.size());
    }


    private Integer mutualFriends(List<UUID> uuidFriends, List<UUID> friendsOfFriend) {
        List<UUID> all = new ArrayList<>();
        all.addAll(uuidFriends);
        all.addAll(friendsOfFriend);
        Set<UUID> set = new HashSet<>(all);
        return (all.size() - set.size());
    }

    private UUID uuidFrom(String headerToken) throws ParseException {
        return UUID.fromString(SignedJWT.parse(headerToken.substring(7)).getPayload().toJSONObject().get("sub").toString());
    }


    private List<UUID> uuidFriends(UUID uuidFrom) {
        List<Friendship> friendships = repository.findFRIENDS(uuidFrom);
        List<UUID> uuidFriends = new ArrayList<>();
        friendships.stream().filter(friendship -> friendship.getAccount_id_from().equals(uuidFrom)).forEach(friendship -> uuidFriends.add(friendship.getAccount_id_to()));
        friendships.stream().filter(friendship -> friendship.getAccount_id_to().equals(uuidFrom)).forEach(friendship -> uuidFriends.add(friendship.getAccount_id_from()));
        return uuidFriends;
    }


    private List<AccountDto> defaultAccountDto(String headerToken) {
        List<AccountDto> filter = new ArrayList<>();
        AccountDto accountDto1 = accountById(UUID.fromString("0bc856ad-b35a-4b19-8969-4cc848fc5198"), headerToken);
        AccountDto accountDto2 = accountById(UUID.fromString("02f18c32-33a5-4b6c-811d-bc33ffc45312"), headerToken);
        AccountDto accountDto3 = accountById(UUID.fromString("2636f06b-764c-4c66-87ce-3d2a090d9897"), headerToken);
        accountDto2.setStatusCode(AccountStatus.REQUEST_FROM);
        accountDto3.setStatusCode(AccountStatus.REQUEST_FROM);
        accountDto1.setStatusCode(AccountStatus.REQUEST_FROM);
        filter.add(accountDto1);
        filter.add(accountDto2);
        filter.add(accountDto3);
        return filter;
    }
    private FriendsRs statusCodeNull(String headerToken,Pageable pageable) throws ParseException {
        List <UUID> listREQUEST_FROM=repository.findIdStatusREQUEST_FROM(uuidFrom(headerToken));
        List<AccountDto> accountREQUEST_FROM=listREQUEST_FROM.stream().map(uuid->accountById(uuid,headerToken)).toList();
        List<FriendDto> friendDtoList = mapper.accountsListToFriendDtoList(accountREQUEST_FROM,StatusCode.REQUEST_FROM);

        List <UUID> listREQUEST_TO=repository.findIdStatusREQUEST_TO(uuidFrom(headerToken));
        List<AccountDto> accountREQUEST_TO=listREQUEST_TO.stream().map(uuid->accountById(uuid,headerToken)).toList();
        friendDtoList.addAll( mapper.accountsListToFriendDtoList(accountREQUEST_TO,StatusCode.REQUEST_TO));

        List <UUID> listSUBSCRIBED=repository.findIdStatus_SUBSCRIBED(uuidFrom(headerToken));
        List<AccountDto> accountSUBSCRIBED =listSUBSCRIBED.stream().map(uuid->accountById(uuid,headerToken)).toList();
        friendDtoList.addAll( mapper.accountsListToFriendDtoList(accountSUBSCRIBED,StatusCode.SUBSCRIBED));

        List <UUID> listBLOCKED=friendServiceOne.blockFriendId(headerToken);
        List<AccountDto> accountBLOCKED =listBLOCKED.stream().map(uuid->accountById(uuid,headerToken)).toList();
        friendDtoList.addAll( mapper.accountsListToFriendDtoList(accountBLOCKED,StatusCode.BLOCKED));

        List <UUID> listFRIEND=uuidFriends(uuidFrom(headerToken));
        List<AccountDto> accountFRIEND =listFRIEND.stream().map(uuid->accountById(uuid,headerToken)).toList();
        friendDtoList.addAll( mapper.accountsListToFriendDtoList(accountFRIEND,StatusCode.FRIEND));

        Page<FriendDto> friendPage = convertListToPage(friendDtoList, pageable);
        return FriendsRs.builder()
                .content(friendPage.getContent())
                .totalElements(friendPage.getTotalElements())
                .totalPages(friendPage.getTotalPages())
                .build();}


}
