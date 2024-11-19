
package social.network.microservice_friend.configurationsecurity;

import io.micrometer.common.util.StringUtils;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
@Component
public class RequestHeaderAuthenticationProvider implements AuthenticationProvider {


    private final String token="Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IlJvbWFuIiwiZW1haWwiOiJrcnA3N0BtYWlsLnJ1In0.QFbiuTijoW4YsxvlYakG0_m2KY_ak9v7aAXLQRpttd4";


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String authSecretKey = String.valueOf(authentication.getPrincipal());
        System.out.println(String.valueOf(authentication.getPrincipal()));
        if(StringUtils.isBlank(authSecretKey) || !authSecretKey.equals(token)) {
            throw new BadCredentialsException("Bad Request Header Credentials");
        }
        System.out.println(StringUtils.isBlank(authSecretKey) || !authSecretKey.equals(token));
        return new PreAuthenticatedAuthenticationToken(authentication.getPrincipal(), null, new ArrayList<>());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(PreAuthenticatedAuthenticationToken.class);
    }
}




