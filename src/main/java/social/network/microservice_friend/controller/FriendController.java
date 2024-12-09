package social.network.microservice_friend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import social.network.microservice_friend.aop.Logger;
import social.network.microservice_friend.dto.*;
import social.network.microservice_friend.dto.en.AccountStatus;
import social.network.microservice_friend.service.FriendService;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/friends")
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;

    @PutMapping("/{uuid}/approve")
    public String approve(@PathVariable UUID uuid, @RequestHeader("authorization") String headerToken) throws ParseException {
        return friendService.approveService(uuid, headerToken);
    }

    @PutMapping("/block/{uuid}")
    public String block(@PathVariable UUID uuid, @RequestHeader("authorization") String headerToken) throws ParseException {
        return friendService.block(uuid, headerToken);
    }

    @PostMapping("/{uuid}/request")
    public String request(@PathVariable UUID uuid, @RequestHeader Map<String, String> headers) throws ParseException {
        return friendService.request(uuid, headers);
    }

    @PostMapping("/subscribe/{uuid}")
    public String subscribe(@PathVariable UUID uuid, @RequestHeader Map<String, String> headers) throws ParseException {
        return friendService.subscribe(uuid, headers);
    }

    @GetMapping("/{uuid}")
    public AccountDto getFriendById(@PathVariable UUID uuid, @RequestHeader("authorization") String headerToken) {
        return friendService.gettingFriendByIdService(uuid, headerToken);
    }
    @Logger
    @GetMapping("/recommendations")
    public RecommendDtoList recommendations(@RequestHeader("authorization") String headerToken) throws ParseException {
        return friendService.recommendationsService(headerToken);
    }
    @Logger
    @GetMapping("/friendId")
    public List<UUID> friendId(@RequestHeader("authorization") String headerToken) throws ParseException {
        return friendService.friendIds(headerToken);
    }

    @GetMapping("/count")
    public Integer friendRequestCounter(@RequestHeader("authorization") String headerToken) throws ParseException {
        return friendService.friendRequestCounter(headerToken);
    }

    @GetMapping("/blockFriendId")
    public List <UUID> blockFriendId(@RequestHeader("authorization") String headerToken) throws ParseException {
        return friendService.blockFriendId(headerToken);
    }

    @DeleteMapping("/{uuid}")
    public String dell(@PathVariable UUID uuid, @RequestHeader("authorization") String headerToken) throws ParseException {
        return friendService.dell(uuid, headerToken);
    }

    @Logger
    @GetMapping()
    public AllFriendsDtoList gettingAllFriends(@RequestParam("ids") List<UUID> ids, @RequestParam("firstName") String firstName, @RequestParam("birthDateFrom") LocalDate birthDateFrom, @RequestParam("birthDateTo") LocalDate birthDateTo, @RequestHeader("authorization") String headerToken,
                                               @RequestParam("city") String city, @RequestParam("country") String country, @RequestParam("ageTo") Integer ageTo, @RequestParam("ageFrom") Integer ageFrom, @RequestParam("statusCode") AccountStatus statusCode) {
        return friendService.gettingAllFriendsService(headerToken, SearchDto.builder()
                .ids(ids)
                .firstName(firstName)
                .birthDateFrom(birthDateFrom)
                .birthDateTo(birthDateTo)
                .city(city)
                .country(country)
                .ageTo(ageTo)
                .ageFrom(ageFrom)
                .statusCode(statusCode)
                .build());
    }

}

