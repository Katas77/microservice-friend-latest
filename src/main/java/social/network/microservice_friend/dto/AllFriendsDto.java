package social.network.microservice_friend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import social.network.microservice_friend.model.en.StatusCode;


import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllFriendsDto {
    private Integer id;
    private String photo;
    private StatusCode statusCode;
    private String firstName;
    private String lastName;
    private String city;
    private String country;
    private LocalDate birthDate;
    private Boolean isOnline;
}
