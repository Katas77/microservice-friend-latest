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
import social.network.microservice_friend.dto.responsF.FriendsRs;
import social.network.microservice_friend.dto.responsF.RecommendationFriendsRs;
import social.network.microservice_friend.exception.BusinessLogicException;
import social.network.microservice_friend.mapper.MapperDTO;
import social.network.microservice_friend.model.Friendship;
import social.network.microservice_friend.repository.FriendshipRepository;
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

    @Logger
    @Override
    public FriendsRs gettingAllFriendsService(String headerToken, FriendSearchDto friendSearchDto, Pageable pageable) {
        log.info(friendSearchDto.toString());
        if (friendSearchDto.getIds() == null) {
            List<FriendDto> friendDtoList = mapper.accountsListToFriendDtoList(defaultAccountDto(headerToken));
            Page<FriendDto> friendPage = convertListToPage(friendDtoList, pageable);
            return FriendsRs.builder()
                    .content(friendPage.getContent())
                    .totalElements(friendPage.getTotalElements())
                    .totalPages(friendPage.getTotalPages())
                    .build();
        } else {
            List<AccountDto> filter2 = search1(friendSearchDto, headerToken);
            List<FriendDto> list2 = mapper.accountsListToFriendDtoList(filter2);
            Page<FriendDto> friendPage2 = convertListToPage(list2, pageable);
            return FriendsRs.builder()
                    .content(friendPage2.getContent())
                    .totalElements(friendPage2.getTotalElements())
                    .totalPages(friendPage2.getTotalPages())
                    .build();

        }
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

    private List<AccountDto> search1(FriendSearchDto friendSearchDto, String headerToken) {
        List<AccountDto> accountDtoList = new ArrayList<>();
        Arrays.stream(friendSearchDto.getIds()).forEach(uuid -> accountDtoList.add(accountById(uuid, headerToken)));
        return accountDtoList.stream()
                .filter(accountDto -> friendSearchDto.getFirstName() == null | accountDto.getFirstName().equals(friendSearchDto.getFirstName()))
                .filter(accountDto -> friendSearchDto.getCity() == null | accountDto.getCity().equals(friendSearchDto.getCity()))
                .filter(accountDto -> friendSearchDto.getCountry() == null | accountDto.getCountry().equals(friendSearchDto.getCountry()))
                .filter(accountDto -> friendSearchDto.getStatusCode() == null | accountDto.getStatusCode().equals(friendSearchDto.getStatusCode()))
                .filter(accountDto -> validBirthDate(accountDto.getBirthDate(), friendSearchDto.getBirthDateFrom(), friendSearchDto.getBirthDateTo()))
                .filter(accountDto -> validAge(accountDto.getBirthDate(), friendSearchDto.getAgeFrom(), friendSearchDto.getAgeTo()))
                .toList();
    }

    private List<AccountDto> defaultAccountDto(String headerToken) {
        List<AccountDto> filter = new ArrayList<>();
        filter.add(accountById(UUID.fromString("0bc856ad-b35a-4b19-8969-4cc848fc5198"), headerToken));
        filter.add(accountById(UUID.fromString("02f18c32-33a5-4b6c-811d-bc33ffc45312"), headerToken));
        filter.add(accountById(UUID.fromString("2636f06b-764c-4c66-87ce-3d2a090d9897"), headerToken));
        return filter;
    }


}
