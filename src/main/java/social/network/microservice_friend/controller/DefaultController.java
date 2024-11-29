package social.network.microservice_friend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import social.network.microservice_friend.model.Friendship;
import social.network.microservice_friend.repository.FriendshipRepository;

import java.util.List;

@RestController
@RequestMapping("/api/v1/default")
@RequiredArgsConstructor
public class DefaultController {
    private final FriendshipRepository friendService;

    @GetMapping("/hello")
    public String hello()  {
        return " I hear you! The connections from Controllers to Agents can be secured by HTTPS with TLS/SSL certificates.\n" +
                "This article describes the steps required to set up secure HTTPS communication from a Controller to an Agent. This includes using a standalone Controller or a Controller cluster with a primary and standby instance.\n" +
                "See the JS7 - System Architecture article for an overview of products and connections.\n" +
                "Follow the instructions in the JS7 - JOC Cockpit HTTPS Connections article for securing connections from clients (user browser / REST API client) to JOC Cockpit.\n" +
                "See the JS7 - Controller HTTPS Connections article for information about securing the connections between JOC Cockpit and Controller instances.";
    }

    @GetMapping("/size")
    public List<Friendship> getFriendById() {
        return friendService.findAll();
    }

}
