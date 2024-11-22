
package social.network.microservice_friend.configurationsecurity;

import io.micrometer.common.util.StringUtils;

import lombok.RequiredArgsConstructor;
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
public class RequestHeaderAuthenticationProvider implements AuthenticationProvider {

    private final ClientFeign client;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String authSecretToken = String.valueOf(authentication.getPrincipal());
        if (StringUtils.isBlank(authSecretToken) || !client.validToken(authSecretToken)) {//authSecretToken.equals(token)
            throw new BadCredentialsException("Bad Request invalid or missing token");
        }
        return new PreAuthenticatedAuthenticationToken(authentication.getPrincipal(), null, new ArrayList<>());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(PreAuthenticatedAuthenticationToken.class);
    }
}




