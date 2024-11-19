package social.network.microservice_friend.security.dto;

import lombok.Data;

@Data
public class JwtRequest {
    private String username;

    private String password;
}
