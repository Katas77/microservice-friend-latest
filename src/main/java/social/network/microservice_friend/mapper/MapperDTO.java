package social.network.microservice_friend.mapper;

import social.network.microservice_friend.dto.*;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface MapperDTO {

    AllFriendsDto convertToAllFriend(AccountDto account);

    RecommendDto convertToRecommend(AccountDto account);

    default AllFriendsDtoList accountsListToAllFriends(List<AccountDto> accounts) {
        AllFriendsDtoList response = new AllFriendsDtoList();
        response.setAllFriends(accounts.stream().map(this::convertToAllFriend).collect(Collectors.toList()));
        return response;
    }

    default RecommendDtoList accountsListToRecommends(List<AccountDto> accounts) {
        RecommendDtoList response = new RecommendDtoList();
        response.setRecommendDtoList(accounts.stream().map(this::convertToRecommend).collect(Collectors.toList()));
        return response;
    }

}
