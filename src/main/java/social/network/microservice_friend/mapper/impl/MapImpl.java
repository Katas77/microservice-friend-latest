
package social.network.microservice_friend.mapper.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import social.network.microservice_friend.dto.AccountDto;
import social.network.microservice_friend.dto.FriendDto;
import social.network.microservice_friend.dto.RecommendationFriendsDto;
import social.network.microservice_friend.mapper.MapperDTO;


@Component
@Primary
public class MapImpl implements MapperDTO {
    @Override
    public FriendDto convertToFriendDto(AccountDto account) {
        return FriendDto.builder()
                .id(account.getId())
                .photo(account.getPhoto())
                .statusCode(account.getStatusCode())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .isOnline(account.getIsOnline())
                .city(account.getCity())
                .country(account.getCountry())
                .birthDate(account.getBirthDate())
                .build();
    }

    @Override
    public RecommendationFriendsDto convertToRecommend(AccountDto account) {
        return RecommendationFriendsDto.builder()
                .id(account.getId())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .photo(account.getPhoto())
                .build();
    }


}
