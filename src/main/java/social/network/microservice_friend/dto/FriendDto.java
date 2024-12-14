package social.network.microservice_friend.dto;

import lombok.*;
import social.network.microservice_friend.dto.en.AccountStatus;
import social.network.microservice_friend.model.en.StatusCode;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FriendDto {
    private UUID friendId;
    private String photo;
    private StatusCode statusCode;
    private String firstName;
    private String lastName;
    private String city;
    private String country;
    private LocalDate birthDate;
    private Boolean isOnline;
}




