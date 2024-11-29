package social.network.microservice_friend.dto.en;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import social.network.microservice_friend.dto.AllFriendsDto;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllFriendsDtoList {
    private List<AllFriendsDto> allFriends = new ArrayList<>();
}