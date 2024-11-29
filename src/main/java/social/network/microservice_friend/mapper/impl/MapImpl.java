
package social.network.microservice_friend.mapper.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import social.network.microservice_friend.dto.AccountDto;
import social.network.microservice_friend.dto.AllFriendsDto;
import social.network.microservice_friend.mapper.MapperDTO;


@Component
@Primary
public class MapImpl implements MapperDTO {
    @Override
    public AllFriendsDto convertToAllFriend(AccountDto account) {
        return AllFriendsDto.builder()
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


}
