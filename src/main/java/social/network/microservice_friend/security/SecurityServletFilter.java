package social.network.microservice_friend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.util.Map;

public class SecurityServletFilter {

    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      Map<String,String> hds= (Map<String, String>) request.getHeaderNames();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails2 = (UserDetails) auth.getPrincipal();
    }
}
