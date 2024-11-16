
package social.network.microservice_friend.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import social.network.microservice_friend.dto.AllFriendsDto;
import social.network.microservice_friend.service.FriendService;


import java.util.Map;


@RestController
@RequestMapping("/api/v1/friends")
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;


    @PutMapping("/{id}/approve")// http://localhost:8080/api/v1/friends/7/approve
    public String approve(@PathVariable Integer id,@RequestHeader Map<String, String> headers) {
        return friendService.approve(id);
    }

    @PutMapping("/block/{id}")
    public String block(@PathVariable Integer id) {
        return friendService.block(id);
    }

    @PutMapping("/{id}/request")
    public String request(@PathVariable Integer id) {
        return friendService.request(id);
    }

    @PutMapping("/subscribe/{id}")
    public String subscribe(@PathVariable Integer id) {
        return friendService.subscribe(id);
    }

    @GetMapping()
    public AllFriendsDto friendsAll() {
        return friendService.findAll();
    }

    @GetMapping("/{accountId}")
    public AllFriendsDto gettingFriendById(@PathVariable Integer accountId) {
        return friendService.gettingFriendById(accountId);
    }

    @GetMapping("/recommendations")
    public AllFriendsDto recommendations() {
        return friendService.recommendations();
    }

    @GetMapping("/friendId")
    public Integer[] friendId() {
        return friendService.friendId();
    }

    @GetMapping("/count")
    public Integer friendRequestCounter() {
        return friendService.friendRequestCounter();
    }

    @GetMapping("/blockFriendId")
    public Integer blockFriendId() {
        return friendService.blockFriendId();
    }

    @DeleteMapping("/{id}")
    public String dell(@PathVariable Integer id) {
        return friendService.dell(id);
    }




}

