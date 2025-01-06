package social.network.microserviceFriend.dto.responseFriend;

import lombok.*;
import social.network.microserviceFriend.dto.FriendDto;

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