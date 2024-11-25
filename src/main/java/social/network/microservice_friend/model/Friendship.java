package social.network.microservice_friend.model;

import jakarta.persistence.*;
import lombok.*;
import social.network.microservice_friend.model.en.StatusCode;

import java.util.UUID;

@Getter
@ToString
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "friendship")
public class Friendship {
    @Id
    private UUID uuid;

    @Column(name = "account_id_to")
    private UUID account_id_to;


    @Column(name = "account_id_from")
    private UUID account_id_from;


    @Column(name = "status_between")
    @Enumerated(value = EnumType.STRING)
    private StatusCode statusBetween;
}
