package social.network.microservice_friend.dto.responsF;

import lombok.*;
import social.network.microservice_friend.dto.RecommendationFriendsDto;

import java.util.List;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationFriendsRs {
    private Long totalElements;
    private Integer totalPages;
    private List<RecommendationFriendsDto> content;
}
