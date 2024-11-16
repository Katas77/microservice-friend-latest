package social.network.microservice_friend.model;

import jakarta.persistence.*;
import lombok.*;
import social.network.microservice_friend.model.en.StatusCode;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "friendship")
public class Friendship {
    @Id
    private String uuid;

    @Column(name = "account_id_Offer")
    @ToString.Exclude
    private UUID accountIdOffer;


    @Column(name = "account_id_answer")
    @ToString.Exclude
    private UUID accountIdAnswer;


    @Column(name = "status_between")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private StatusCode statusBetween;
}
