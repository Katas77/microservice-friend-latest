package social.network.microservice_friend.dto;


import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class AccountDto {

    private String uuid;

    private String photo;

    private String firstName;

    private String lastName;

    private String city;

    private String country;

    private LocalDate birthDate;

    private Boolean isOnline;

    private LocalDate birthDateFrom;

    private LocalDate birthDateTo;

    private Integer ageTo;

    private Integer ageFrom;

}
