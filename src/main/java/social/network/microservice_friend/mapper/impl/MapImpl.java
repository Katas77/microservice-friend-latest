package social.network.microservice_friend.mapper.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import social.network.microservice_friend.dto.AccountDto;
import social.network.microservice_friend.dto.FriendDto;
import social.network.microservice_friend.dto.RecommendationFriendsDto;
import social.network.microservice_friend.mapper.MapperDTO;
import social.network.microservice_friend.model.en.StatusCode;


@Component
@Primary
public class MapImpl implements MapperDTO {
    @Override
    public FriendDto convertToFriendDto(AccountDto account, StatusCode statusBetween) {
        return FriendDto.builder()
                .friendId(account.getId())
                .photo(account.getPhoto())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .isOnline(account.getIsOnline())
                .city(account.getCity())
                .country(account.getCountry())
                .birthDate(account.getBirthDate())
                .statusCode(statusBetween)
                .build();
    }

    @Override
    public RecommendationFriendsDto convertToRecommend(AccountDto account) {
        return RecommendationFriendsDto.builder()
                .friendId(account.getId())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .photo(account.getPhoto())
                .build();
    }


}
