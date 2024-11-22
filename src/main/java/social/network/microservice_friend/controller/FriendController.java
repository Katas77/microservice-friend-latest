
package social.network.microservice_friend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import social.network.microservice_friend.dto.AllFriendsDto;
import social.network.microservice_friend.service.FriendService;


import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/friends")
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;


    @PutMapping("/{uuid}/approve")
    public String approve(@PathVariable UUID uuid) {
        return friendService.approve(uuid);
    }

    @PutMapping("/block/{uuid}")
    public String block(@PathVariable UUID uuid) {
        return friendService.block(uuid);
    }

    @PutMapping("/{uuid}/request")
    public String request(@PathVariable UUID uuid, @RequestHeader Map<String, String> headers) throws JsonProcessingException {
        return friendService.request(uuid, headers);
    }

    @PutMapping("/subscribe/{uuid}")
    public String subscribe(@PathVariable UUID uuid, Map<String, String> headers) throws JsonProcessingException {
        return friendService.subscribe(uuid, headers);
    }

    @GetMapping()
    public AllFriendsDto friendsAll(@RequestHeader Map<String, String> headers) {
        return friendService.findAll();
    }

    @GetMapping("/{accountId}")
    public AllFriendsDto getFriendById(@PathVariable UUID uuid) {
        return friendService.gettingFriendById(uuid);
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

    @DeleteMapping("/{uuid}")
    public String dell(@PathVariable UUID uuid, @RequestHeader("authorization") String headerToken) throws JsonProcessingException {
        return friendService.dell(uuid, headerToken);
    }

}

