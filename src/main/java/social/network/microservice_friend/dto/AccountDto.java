package social.network.microservice_friend.dto;

import lombok.*;
import social.network.microservice_friend.dto.en.AccountStatus;
import social.network.microservice_friend.dto.en.RoleType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AccountDto {
    private UUID id;
    private String email;
    private String phone;
    private String photo;
    private String about;
    private String city;
    private String country;
    private AccountStatus statusCode;
    private String firstName;
    private String lastName;
    private LocalDateTime regDate;
    private LocalDate birthDate;
    private String messagePermission;
    private LocalDateTime lastOnlineTime;
    private Boolean isOnline;
    private Boolean isBlocked;
    private Boolean isDeleted;
    private String photoId;
    private String photoName;
    private RoleType role;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private String password;
}


