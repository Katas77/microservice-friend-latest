package social.network.microservice_friend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import social.network.microservice_friend.clientFeign.ClientFeign;
import social.network.microservice_friend.model.Friendship;
import social.network.microservice_friend.repository.FriendshipRepository;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/default")
@RequiredArgsConstructor
public class DefaultController {
    private final FriendshipRepository friendService;
    private final ClientFeign clientFeign;

    @GetMapping("/hello")
    public String hello()  {
        return " I hear you!  "+clientFeign.getAccountById(UUID.fromString("6d175460-27bc-49a3-aa7a-bad5861f9706"),"Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0YTAwMWFkNC01MmU4LTQxZDItODE3MC1jMjg3MDVjNzY1YjUiLCJpYXQiOjE3MzM0NzU0MzYsImV4cCI6MTczMzQ3NzIzNn0.8xQ_OUY7Al5Gro9G74bu28B6qQbDETICyZSsXWLyckI0zRQKLFAG3nb7zbbjdex5ieCg004Hia-LMOG2fAThvw").toString();
    }

    @GetMapping("/size")
    public List<Friendship> getFriendById() {
        return friendService.findAll();
    }

}
