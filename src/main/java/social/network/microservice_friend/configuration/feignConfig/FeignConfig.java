package social.network.microservice_friend.configuration.feignConfig;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

@Configuration
public class FeignConfig {
    @Bean
    public RequestInterceptor authorizationHeaderForwardingInterceptor() {
        return template -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() != null) {
                String principal = auth.getPrincipal().toString();
                template.header("Authorization", principal);
            }
        };
    }
}
