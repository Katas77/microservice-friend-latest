package social.network.microservice_friend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import social.network.microservice_friend.aop.Logger;
import social.network.microservice_friend.dto.*;
import social.network.microservice_friend.dto.responses.FriendsRs;
import social.network.microservice_friend.dto.responses.RecommendationFriendsRs;
import social.network.microservice_friend.service.FriendServiceOne;
import social.network.microservice_friend.service.FriendServiceTwo;
import java.text.ParseException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/friends")
@RequiredArgsConstructor
@Slf4j
public class FriendController {
    private final FriendServiceOne friendService;
    private final FriendServiceTwo friendService2;

    @PutMapping("/{uuid}/approve")
    public Message approve(@PathVariable UUID uuid, @RequestHeader("authorization") String headerToken) throws ParseException {
        return friendService.approveService(uuid, headerToken);
    }

    @PutMapping("/block/{uuid}")
    public Message block(@PathVariable UUID uuid, @RequestHeader("authorization") String headerToken) throws ParseException {
        return friendService.block(uuid, headerToken);
    }
    @PutMapping("/unblock/{uuid}")
    public Message unblock(@PathVariable UUID uuid, @RequestHeader("authorization") String headerToken) throws ParseException {
        return friendService.unblock(uuid, headerToken);
    }

    @PostMapping("/{uuid}/request")
    public Message request(@PathVariable UUID uuid, @RequestHeader("authorization") String headerToken) throws ParseException {
        return friendService.request(uuid, headerToken);
    }

    @PostMapping("/subscribe/{uuid}")
    public Message subscribe(@PathVariable UUID uuid, @RequestHeader("authorization") String headerToken) throws ParseException {
        return friendService.subscribe(uuid, headerToken);
    }


    @GetMapping("/{accountId}")
    public AccountDto getFriendById(@PathVariable UUID accountId, @RequestHeader("authorization") String headerToken) {
        return friendService2.gettingFriendByIdService(accountId, headerToken);
    }


    @GetMapping("/recommendations")
    public RecommendationFriendsRs recommendations(@RequestHeader("authorization") String headerToken, Pageable pageable) throws ParseException {
        return friendService2.recommendationsService(headerToken, pageable);
    }


    @GetMapping("/friendId")
    public List<UUID> friendIds(@RequestHeader("authorization") String headerToken) throws ParseException {
        return friendService.friendIds(headerToken);
    }

    @GetMapping("/friendId/post/{userId}")
    public List<UUID> friendIdsForPost(@PathVariable UUID userId) {
        return friendService.friendIdsForPost(userId);
    }


    @GetMapping("/count")
    public Integer friendRequestCounter(@RequestHeader("authorization") String headerToken) throws ParseException {
        return friendService.friendRequestCounter(headerToken);
    }

    @Logger
    @GetMapping("/blockFriendId")
    public List<UUID> blockFriendIds(@RequestHeader("authorization") String headerToken) throws ParseException {
        return friendService.blockFriendId(headerToken);
    }


    @DeleteMapping("/{uuid}")
    public Message dell(@PathVariable UUID uuid, @RequestHeader("authorization") String headerToken) throws ParseException {
        return friendService.dell(uuid, headerToken);
    }


    @GetMapping()
    public FriendsRs gettingAllFriends(@ModelAttribute("friendSearchDto") FriendSearchDto friendSearchDto, @RequestHeader("authorization") String headerToken, Pageable pageable) throws ParseException {
        return friendService2.gettingAllFriendsService(headerToken, friendSearchDto, pageable);
    }


}
