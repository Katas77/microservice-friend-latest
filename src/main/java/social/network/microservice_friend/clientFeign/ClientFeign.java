package social.network.microservice_friend.clientFeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import social.network.microservice_friend.dto.AccountDto;


import java.util.UUID;

@FeignClient(name = "accountClient", url ="${api.url}")
public interface ClientFeign {

    @GetMapping(value = "/account/{id}")
    AccountDto getAccountById(@PathVariable(value = "id") UUID id);

    @RequestMapping(method = RequestMethod.GET, value = "/auth/validate-token")
    boolean validToken(@RequestHeader(value = "authorization", required = true) String authorizationToken);
    @GetMapping(value = "/account/email/{email}")
    AccountDto getAccountBayEmail(@PathVariable(value = "email") String email);

}

