
package social.network.microservice_friend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import social.network.microservice_friend.dto.AccountDto;
import social.network.microservice_friend.dto.AllFriendsDto;
import social.network.microservice_friend.dto.FriendSearchDto;
import social.network.microservice_friend.service.FriendService;


import java.text.ParseException;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/friends")
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;


    @PutMapping("/{uuid}/approve")
    public String approve(@PathVariable UUID uuid,@RequestHeader("authorization") String headerToken) throws ParseException, JsonProcessingException {
        return friendService.approve(uuid,headerToken);
    }

    @PutMapping("/block/{uuid}")
    public String block(@PathVariable UUID uuid,@RequestHeader("authorization") String headerToken) throws ParseException, JsonProcessingException {
        return friendService.block(uuid,headerToken);
    }

    @PutMapping("/{uuid}/request")
    public String request(@PathVariable UUID uuid, @RequestHeader Map<String, String> headers) throws ParseException, JsonProcessingException {
        return friendService.request(uuid, headers);
    }

    @PutMapping("/subscribe/{uuid}")
    public String subscribe(@PathVariable UUID uuid, Map<String, String> headers) throws ParseException, JsonProcessingException {
        return friendService.subscribe(uuid, headers);
    }

    @GetMapping()
    public AllFriendsDto friendsAll(@RequestBody FriendSearchDto request, @RequestHeader Map<String, String> headers) {
        return friendService.findAll(request);
    }

    @GetMapping("/{accountId}")
    public AccountDto getFriendById(@PathVariable UUID uuid) {
        return friendService.gettingFriendById(uuid);
    }

    @GetMapping("/recommendations")
    public AllFriendsDto recommendations() {
        return friendService.recommendations();
    }

    @GetMapping("/friendId")
    public UUID[] friendId(@RequestHeader("authorization") String headerToken) throws ParseException {
        return friendService.friendIds(headerToken);
    }

    @GetMapping("/count")
    public Integer friendRequestCounter(@RequestHeader("authorization") String headerToken) throws ParseException, JsonProcessingException {
        return friendService.friendRequestCounter(headerToken);
    }

    @GetMapping("/blockFriendId")
    public UUID[]  blockFriendId(@RequestHeader("authorization") String headerToken)throws ParseException, JsonProcessingException {
        return friendService.blockFriendId(headerToken);
    }

    @DeleteMapping("/{uuid}")
    public String dell(@PathVariable UUID uuid, @RequestHeader("authorization") String headerToken) throws ParseException, JsonProcessingException {
        return friendService.dell(uuid, headerToken);
    }

}

