package social.network.microservice_friend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@AllArgsConstructor
@Getter
@Setter
@ToString
public class CheckDto {
    private  Boolean validToken;
    private String error;
}
