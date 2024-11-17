package social.network.microservice_friend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import social.network.microservice_friend.model.en.StatusCode;
import java.time.LocalDate;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendSearchDto {
    private String[] uuids;
    private String firstName;
    private LocalDate birthDateFrom;
    private LocalDate birthDateTo;
    private String city;
    private String country;
    private Integer ageTo;
    private Integer ageFrom;
    private StatusCode statusCode;

}
