
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


    @PutMapping("/{uuid}/approve")//                     http://localhost:8080/api/v1/friends/b3999ffa-2df9-469e-9793-ee65e214846e/approve
    public String approve(@PathVariable UUID uuid) throws JsonProcessingException {
        return friendService.approve(uuid);
    }

    @PutMapping("/block/{uuid}")
    public String block(@PathVariable UUID uuid) {
        return friendService.block(uuid);
    }

    @PutMapping("/{uuid}/request")//         http://localhost:8080/api/v1/friends/b3999ffa-2df9-469e-9793-ee65e214846e/request
    public String request(@PathVariable UUID uuid, @RequestHeader Map<String, String> headers) throws JsonProcessingException {

        return friendService.request(uuid,headers);
    }

    @PutMapping("/subscribe/{uuid}")
    public String subscribe(@PathVariable UUID uuid) {
        return friendService.subscribe(uuid);
    }

    @GetMapping()
    public AllFriendsDto friendsAll( @RequestHeader Map<String, String> headers) {
        for (Map.Entry entry:headers.entrySet())
        { System.out.println(entry.getKey()+"              "+entry.getValue());}

        return friendService.findAll();
    }

    @GetMapping("/{accountId}")
    public AllFriendsDto gettingFriendById(@PathVariable UUID uuid) {
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

    @DeleteMapping("/{id}")
    public String dell(@PathVariable Integer id) {
        return friendService.dell(id);
    }

}

