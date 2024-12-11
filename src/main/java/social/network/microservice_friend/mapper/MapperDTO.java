package social.network.microservice_friend.mapper;

import social.network.microservice_friend.dto.*;
import org.mapstruct.Mapper;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface MapperDTO {

    AllFriendsDto convertToAllFriend(AccountDto account);

    RecommendDto convertToRecommend(AccountDto account);

    default List<AllFriendsDto> accountsListToAllFriends(List<AccountDto> accounts) {
        return accounts.stream().map(this::convertToAllFriend).collect(Collectors.toList());
    }

    default List<RecommendDto> accountsListToRecommends(List<AccountDto> accounts) {

        return accounts.stream().map(this::convertToRecommend).collect(Collectors.toList());
    }

}
