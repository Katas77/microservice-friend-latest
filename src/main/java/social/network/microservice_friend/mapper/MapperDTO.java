package social.network.microservice_friend.mapper;

import social.network.microservice_friend.dto.AccountDto;
import social.network.microservice_friend.dto.AllFriendsDto;
import social.network.microservice_friend.dto.en.AllFriendsDtoList;
import org.mapstruct.Mapper;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface MapperDTO {

    AllFriendsDto convertToAllFriend(AccountDto account);

    default AllFriendsDtoList accountsListToAllFriends(List<AccountDto> accounts) {
        AllFriendsDtoList response = new AllFriendsDtoList();
        response.setAllFriends(accounts.stream().map(this::convertToAllFriend).collect(Collectors.toList()));
        return response;
    }

}
