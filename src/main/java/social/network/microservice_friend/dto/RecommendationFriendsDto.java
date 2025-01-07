package social.network.microservice_friend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;


@AllArgsConstructor
@Getter
@Setter
@Builder
public class RecommendationFriendsDto {
    private UUID friendId;
    private String photo;
    private String firstName;
    private String lastName;
}
