package social.network.microservice_friend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import social.network.microservice_friend.dto.en.AccountStatus;


import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchDto {
    private List <UUID> ids;
    private String firstName;
    private LocalDate birthDateFrom;
    private LocalDate birthDateTo;
    private String city;
    private String country;
    private Integer ageTo;
    private Integer ageFrom;
    private AccountStatus statusCode;

}