package social.network.microservice_friend.dto.responses;

import lombok.*;
import social.network.microservice_friend.dto.FriendDto;

import java.util.List;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FriendsRs {
    private Long totalElements;
    private Integer totalPages;
    private List<FriendDto> content;

}