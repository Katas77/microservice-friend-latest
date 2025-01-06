package social.network.microserviceFriend.service;

import org.springframework.data.domain.Pageable;
import social.network.microserviceFriend.dto.AccountDto;
import social.network.microserviceFriend.dto.FriendSearchDto;
import social.network.microserviceFriend.dto.responseFriend.FriendsRs;
import social.network.microserviceFriend.dto.responseFriend.RecommendationFriendsRs;
import java.text.ParseException;
import java.util.UUID;

public interface FriendServiceTwo {
    FriendsRs gettingAllFriendsService(String headerToken, FriendSearchDto friendSearchDto,Pageable pageable) throws ParseException;

    AccountDto gettingFriendByIdService(UUID uuidAccountId, String headerToken);

    RecommendationFriendsRs recommendationsService(String headerToken, Pageable pageable) throws ParseException;
}
