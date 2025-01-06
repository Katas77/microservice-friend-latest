package social.network.microserviceFriend.model;

import jakarta.persistence.*;
import lombok.*;
import social.network.microserviceFriend.model.en.StatusCode;
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
    private UUID accountIdTo;


    @Column(name = "account_id_from")
    private UUID accountIdFrom;


    @Column(name = "status_between")
    @Enumerated(value = EnumType.STRING)
    private StatusCode statusBetween;
}
