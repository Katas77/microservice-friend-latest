
package social.network.microservice_friend.configuration.configurationsecurity;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import social.network.microservice_friend.clientFeign.ClientFeign;

import java.util.ArrayList;


@Component
@RequiredArgsConstructor
@Slf4j
public class RequestHeaderAuthenticationProvider implements AuthenticationProvider {

    private final ClientFeign client;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String authToken = String.valueOf(authentication.getPrincipal());
        log.info(authToken);
        if (StringUtils.isBlank(authToken) || !client.validToken(authToken)) {
            throw new BadCredentialsException("Bad Request invalid or missing token");
        }
        return new PreAuthenticatedAuthenticationToken(authentication.getPrincipal(), null, new ArrayList<>());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(PreAuthenticatedAuthenticationToken.class);
    }
}





