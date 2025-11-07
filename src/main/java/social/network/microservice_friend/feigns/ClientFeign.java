package social.network.microservice_friend.feigns;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import social.network.microservice_friend.dto.AccountDto;

import java.util.UUID;

@FeignClient(name = "validate", url = "${app.url}")
public interface ClientFeign {

    @GetMapping(value = "/auth/validate-token", produces = "application/json")
    boolean validToken(@RequestHeader("Authorization") String authorizationToken);

    @GetMapping(value = "/account/{id}", produces = "application/json")
    AccountDto getAccountById(@PathVariable("id") UUID id, @RequestHeader("Authorization") String authorizationToken);

    @GetMapping(value = "/account/email/{email}", produces = "application/json")
    AccountDto getAccountByEmail(@PathVariable("email") String email);
}

