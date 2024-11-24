package social.network.microservice_friend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import social.network.microservice_friend.repository.FriendshipRepository;


import java.util.UUID;

@RestController
@RequestMapping("/api/v1/default")
@RequiredArgsConstructor
public class DefaultController {
    private final FriendshipRepository friendService;

    @GetMapping("/hello")
    public String hello() {
        return "I hear you";
    }

    @GetMapping("/size")
    public Integer getFriendById() {
        return friendService.findAll().size();
    }

}
