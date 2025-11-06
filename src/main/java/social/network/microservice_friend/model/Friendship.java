package social.network.microservice_friend.model;

import jakarta.persistence.*;
import lombok.*;
import social.network.microservice_friend.model.en.StatusCode;

import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "friendship", schema = "friend_schema")
public class Friendship {
    @Id
    private UUID uuid;
    @Column(name = "account_id_to")
    private UUID accountIdTo;
    @Column(name = "account_id_from")
    private UUID accountIdFrom;
    @Column(name = "status_between")
    @Enumerated(value = EnumType.STRING)
    private StatusCode statusBetween;
}
