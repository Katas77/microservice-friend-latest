package social.network.microservice_friend.model;

import jakarta.persistence.*;
import lombok.*;
import social.network.microservice_friend.model.en.StatusCode;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "friendship")
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "account_id_Offer")
    @ToString.Exclude
    private Integer accountIdOffer;


    @Column(name = "account_id_answer")
    @ToString.Exclude
    private Integer accountIdAnswer;


    @Column(name = "status_between")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private StatusCode statusBetween;
}
