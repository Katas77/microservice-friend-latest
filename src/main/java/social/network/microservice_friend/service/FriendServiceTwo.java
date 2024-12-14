package social.network.microservice_friend.service;

import org.springframework.data.domain.Pageable;
import social.network.microservice_friend.dto.AccountDto;
import social.network.microservice_friend.dto.FriendSearchDto;
import social.network.microservice_friend.dto.responsF.FriendsRs;
import social.network.microservice_friend.dto.responsF.RecommendationFriendsRs;
import java.text.ParseException;
import java.util.UUID;

public interface FriendServiceTwo {
    FriendsRs gettingAllFriendsService(String headerToken, FriendSearchDto friendSearchDto,Pageable pageable) throws ParseException;

    AccountDto gettingFriendByIdService(UUID uuidAccountId, String headerToken);

    RecommendationFriendsRs recommendationsService(String headerToken, Pageable pageable) throws ParseException;
}
