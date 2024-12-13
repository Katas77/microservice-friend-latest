package social.network.microservice_friend.dto;

import lombok.*;
import social.network.microservice_friend.dto.en.AccountStatus;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FriendDto {
    private UUID id;
    private String photo;
    private AccountStatus statusCode;
    private String firstName;
    private String lastName;
    private String city;
    private String country;
    private LocalDate birthDate;
    private Boolean isOnline;
}




