package social.network.microservice_friend.mapper;

import social.network.microservice_friend.dto.*;
import org.mapstruct.Mapper;
import social.network.microservice_friend.model.en.StatusCode;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface MapperDTO {

    FriendDto convertToFriendDto(AccountDto account, StatusCode statusBetween);

    RecommendationFriendsDto convertToRecommend(AccountDto account);

    default List<FriendDto> accountsListToFriendDtoList(List<AccountDto> accounts,StatusCode statusBetween) {
        return accounts.stream().map(accountDto -> convertToFriendDto(accountDto,statusBetween)).collect(Collectors.toList());
    }

    default List<RecommendationFriendsDto> accountsListToRecommends(List<AccountDto> accounts) {

        return accounts.stream().map(this::convertToRecommend).collect(Collectors.toList());
    }

}
