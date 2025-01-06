package social.network.microserviceFriend.mapper;

import social.network.microserviceFriend.dto.*;
import social.network.microserviceFriend.model.en.StatusCode;
import java.util.List;
import java.util.stream.Collectors;


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
