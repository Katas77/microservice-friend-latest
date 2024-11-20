package social.network.microservice_friend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
public class CheckDto {
    private boolean validToken;
}
