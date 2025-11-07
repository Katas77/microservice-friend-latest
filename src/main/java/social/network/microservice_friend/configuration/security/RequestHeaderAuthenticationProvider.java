package social.network.microservice_friend.configuration.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import social.network.microservice_friend.feigns.ClientFeign;

import java.util.Collections;

@Component
@RequiredArgsConstructor
@Slf4j
public class RequestHeaderAuthenticationProvider implements AuthenticationProvider {

    private final ClientFeign client;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Object principalObj = authentication.getPrincipal();
        String token = (principalObj == null) ? null : principalObj.toString();
        if (!org.springframework.util.StringUtils.hasText(token)) {
            log.warn("Missing authorization header for request (principal is empty)");
            throw new BadCredentialsException("Bad Request: invalid or missing token");
        }
        boolean valid;
        try {
            valid = client.validToken(token);
        } catch (Exception e) {
            log.error("Token validation failed (remote service error): {}", e.getMessage());
            throw new BadCredentialsException("Bad Request: token validation error");
        }

        if (!valid) {
            log.warn("Invalid token presented for authentication");
            throw new BadCredentialsException("Bad Request: invalid or missing token");
        }

        // Здесь можно получить дополнительные данные (userId/roles) от client и установить authorities
        return new PreAuthenticatedAuthenticationToken(token, null, Collections.emptyList());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PreAuthenticatedAuthenticationToken.class.isAssignableFrom(authentication);
    }
}





