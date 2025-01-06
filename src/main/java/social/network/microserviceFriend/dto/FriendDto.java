package social.network.microserviceFriend.dto;

import lombok.*;
import social.network.microserviceFriend.model.en.StatusCode;

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




