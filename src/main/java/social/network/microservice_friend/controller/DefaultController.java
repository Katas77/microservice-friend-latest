package social.network.microservice_friend.controller;

import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import social.network.microservice_friend.repository.FriendshipRepository;


import java.text.ParseException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/default")
@RequiredArgsConstructor
public class DefaultController {
    private final FriendshipRepository friendService;

    @GetMapping("/hello")
    public String hello(@RequestHeader("authorization") String headerToken) throws ParseException {
        System.out.println(   SignedJWT.parse(headerToken.substring(7)).getPayload().toJSONObject().get("id").toString());
        return "I hear you";
    }

    @GetMapping("/size")
    public Integer getFriendById() {
        return friendService.findAll().size();
    }

}
