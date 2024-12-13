package social.network.microservice_friend.kafka;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import social.network.microservice_friend.model.en.StatusCode;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendEvent {
    private UUID uuid;
    private UUID account_id_to;
    private UUID account_id_from;
    private StatusCode statusBetween;
}