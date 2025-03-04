package social.network.microservice_friend.service.impl;

import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import social.network.microservice_friend.aop.Logger;
import social.network.microservice_friend.feigns.ClientFeign;
import social.network.microservice_friend.dto.AccountDto;
import social.network.microservice_friend.dto.FriendDto;
import social.network.microservice_friend.dto.FriendSearchDto;
import social.network.microservice_friend.dto.RecommendationFriendsDto;
import social.network.microservice_friend.dto.responses.FriendsRs;
import social.network.microservice_friend.dto.responses.RecommendationFriendsRs;
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
        if (friendSearchDto.getStatusCode() == null) {
            return statusCodeNull(headerToken, pageable);
        }

        if (friendSearchDto.getIds() == null && friendSearchDto.getStatusCode().equals(StatusCode.REQUEST_FROM)) {
            friendSearchDto.setIds(repository.findIdStatusREQUESTFROM(uuidFrom(headerToken)));
        }
        if (friendSearchDto.getIds() == null && friendSearchDto.getStatusCode().equals(StatusCode.WATCHING)) {
            friendSearchDto.setIds(repository.findIdStatusWATCHING(uuidFrom(headerToken)));
        }
        if (friendSearchDto.getIds() == null && friendSearchDto.getStatusCode().equals(StatusCode.REQUEST_TO)) {
            friendSearchDto.setIds(repository.findIdStatusREQUESTTO(uuidFrom(headerToken)));
        }
        if (friendSearchDto.getIds() == null && friendSearchDto.getStatusCode().equals(StatusCode.SUBSCRIBED)) {
            friendSearchDto.setIds(repository.findIdStatusSUBSCRIBED(uuidFrom(headerToken)));
        }
        if (friendSearchDto.getIds() == null && friendSearchDto.getStatusCode().equals(StatusCode.BLOCKED)) {
            friendSearchDto.setIds(friendServiceOne.blockFriendId(headerToken));
        }
        if (friendSearchDto.getIds() == null && friendSearchDto.getStatusCode().equals(StatusCode.FRIEND)) {
            friendSearchDto.setIds(uuidFriends(uuidFrom(headerToken)));
        }

        List<AccountDto> filteredAccounts = search1(friendSearchDto, headerToken);

        List<FriendDto> friendDtoList = mapper.accountsListToFriendDtoList(filteredAccounts, friendSearchDto.getStatusCode());
        Page<FriendDto> friendPage = convertListToPage(friendDtoList, pageable);
        return FriendsRs.builder()
                .content(friendPage.getContent())
                .totalElements(friendPage.getTotalElements())
                .totalPages(friendPage.getTotalPages())
                .build();
    }

    @Logger
    @Override
    public RecommendationFriendsRs recommendationsService(String headerToken, Pageable pageable) throws ParseException {
        List<AccountDto> recommendedFriends = new ArrayList<>();
        List<UUID> uuidFriends = uuidFriends(uuidFrom(headerToken));
        for (UUID uuid : uuidFriends) {
            List<UUID> friendsOfFriend = uuidFriends(uuid);
            for (UUID uuid2 : friendsOfFriend) {
                List<UUID> friendsOfFriend2 = uuidFriends(uuid2);
                if (mutualFriends(uuidFriends, friendsOfFriend2) >= 2) {
                    recommendedFriends.add(accountById(uuid2, headerToken));
                }
            }
        }
        List<RecommendationFriendsDto> recommendationFriendsDtos = mapper.accountsListToRecommends(recommendedFriends);
        Page<RecommendationFriendsDto> friendsPage = convertListToPage(recommendationFriendsDtos, pageable);
        return RecommendationFriendsRs.builder()
                .content(friendsPage.getContent())
                .totalElements(friendsPage.getTotalElements())
                .totalPages(friendsPage.getTotalPages())
                .build();
    }

    @Logger
    @Override
    public AccountDto gettingFriendByIdService(UUID accountId, String headerToken) {
        return accountById(accountId, headerToken);
    }

    @Logger
    public AccountDto accountById(UUID id, String headerToken) {
        return Optional.ofNullable(accountClient.getAccountById(id, headerToken))
                .orElseThrow(() -> new BusinessLogicException(MessageFormat.format("Friend with ID {0} is NOT_FOUND", id)));
    }

    public List<AccountDto> search1(FriendSearchDto friendSearchDto, String headerToken) {
        if (friendSearchDto.getIds() == null) {
            friendSearchDto.setIds(new ArrayList<>());
        }

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

    /**
     * Метод для проверки даты рождения в пределах диапазона.
     *
     * @param birthDate     Дата рождения пользователя.
     * @param birthDateFrom Начальная дата диапазона.
     * @param birthDateTo   Конечная дата диапазона.
     * @return True, если дата рождения находится в диапазоне, иначе False.
     */
    public boolean validBirthDate(LocalDate birthDate, LocalDate birthDateFrom, LocalDate birthDateTo) {
        if (birthDate == null) {
            return true;
        }
        birthDateFrom = birthDateFrom == null ? LocalDate.of(1, 1, 1) : birthDateFrom;
        birthDateTo = birthDateTo == null ? LocalDate.now() : birthDateTo;
        return !(birthDate.isBefore(birthDateFrom) || birthDate.isAfter(birthDateTo));
    }

    /**
     * Метод для проверки возраста в пределах диапазона.
     *
     * @param birthDate Дата рождения пользователя.
     * @param ageFrom   Минимальный возраст.
     * @param ageTo     Максимальный возраст.
     * @return True, если возраст находится в диапазоне, иначе False.
     */
    public boolean validAge(LocalDate birthDate, Integer ageFrom, Integer ageTo) {
        if (birthDate == null) {
            return true;
        }
        int age = LocalDate.now().getYear() - birthDate.getYear();
        ageFrom = ageFrom == null ? 0 : ageFrom;
        ageTo = ageTo == null ? 1000 : ageTo;
        return !(age < ageFrom || age > ageTo);
    }

    /**
     * Метод для преобразования списка в страницу с учетом параметров пагинации.
     *
     * @param list     Исходный список элементов.
     * @param pageable Параметры пагинации.
     * @return Страница с элементами согласно параметрам пагинации.
     */
    public <T> Page<T> convertListToPage(List<T> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((int) pageable.getPageSize(), list.size());
        return new PageImpl<>(list.subList(start, end), pageable, list.size());
    }

    /**
     * Метод для вычисления количества общих друзей между двумя пользователями.
     *
     * @param uuidFriends     Список друзей первого пользователя.
     * @param friendsOfFriend Список друзей второго пользователя.
     * @return Количество общих друзей.
     */
    public Integer mutualFriends(List<UUID> uuidFriends, List<UUID> friendsOfFriend) {
        List<UUID> all = new ArrayList<>();
        all.addAll(uuidFriends);
        all.addAll(friendsOfFriend);
        Set<UUID> uniqueFriends = new HashSet<>(all);
        return (all.size() - uniqueFriends.size());
    }

    public UUID uuidFrom(String headerToken) throws ParseException {
        return UUID.fromString(SignedJWT.parse(headerToken.substring(7)).getPayload().toJSONObject().get("sub").toString());
    }

    public List<UUID> uuidFriends(UUID uuidFrom) {
        List<Friendship> friendships = repository.findFRIENDS(uuidFrom);
        List<UUID> uuidFriends = new ArrayList<>();
        friendships.stream().filter(friendship -> friendship.getAccountIdFrom().equals(uuidFrom)).forEach(friendship -> uuidFriends.add(friendship.getAccountIdTo()));
        friendships.stream().filter(friendship -> friendship.getAccountIdTo().equals(uuidFrom)).forEach(friendship -> uuidFriends.add(friendship.getAccountIdFrom()));
        return uuidFriends;
    }

    public FriendsRs statusCodeNull(String headerToken, Pageable pageable) throws ParseException {
        List<UUID> listREQUESTFROM = repository.findIdStatusREQUESTFROM(uuidFrom(headerToken));
        List<AccountDto> accountREQUESTFROM = listREQUESTFROM.stream().map(uuid -> accountById(uuid, headerToken)).toList();
        List<FriendDto> friendDtoList = mapper.accountsListToFriendDtoList(accountREQUESTFROM, StatusCode.REQUEST_FROM);
        List<UUID> listREQUESTTO = repository.findIdStatusREQUESTTO(uuidFrom(headerToken));
        List<AccountDto> accountREQUESTTO = listREQUESTTO.stream().map(uuid -> accountById(uuid, headerToken)).toList();
        friendDtoList.addAll(mapper.accountsListToFriendDtoList(accountREQUESTTO, StatusCode.REQUEST_TO));
        List<UUID> listSUBSCRIBED = repository.findIdStatusSUBSCRIBED(uuidFrom(headerToken));
        List<AccountDto> accountSUBSCRIBED = listSUBSCRIBED.stream().map(uuid -> accountById(uuid, headerToken)).toList();
        friendDtoList.addAll(mapper.accountsListToFriendDtoList(accountSUBSCRIBED, StatusCode.SUBSCRIBED));
        List<UUID> listBLOCKED = friendServiceOne.blockFriendId(headerToken);
        List<AccountDto> accountBLOCKED = listBLOCKED.stream().map(uuid -> accountById(uuid, headerToken)).toList();
        friendDtoList.addAll(mapper.accountsListToFriendDtoList(accountBLOCKED, StatusCode.BLOCKED));
        List<UUID> listFRIEND = uuidFriends(uuidFrom(headerToken));
        List<AccountDto> accountFRIEND = listFRIEND.stream().map(uuid -> accountById(uuid, headerToken)).toList();
        friendDtoList.addAll(mapper.accountsListToFriendDtoList(accountFRIEND, StatusCode.FRIEND));
        Page<FriendDto> friendPage = convertListToPage(friendDtoList, pageable);
        return FriendsRs.builder()
                .content(friendPage.getContent())
                .totalElements(friendPage.getTotalElements())
                .totalPages(friendPage.getTotalPages())
                .build(); // Формируем ответ.
    }
}