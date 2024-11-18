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
    private String uuid;

    @Column(name = "account_id_offer")
    private String accountOfferUUID;


    @Column(name = "account_id_answer")
    private String accountAnswerUUID;


    @Column(name = "status_between")
    @Enumerated(EnumType.STRING)
    private StatusCode statusBetween;
}
