package social.network.microservice_friend.clientFeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import social.network.microservice_friend.dto.AccountDto;


import java.util.UUID;

@FeignClient(name = "accountClient", url = "http://localhost:8080/api/v1")
public interface ClientFeign {

    @GetMapping(value = "/account/{uuid}")
    AccountDto getAccountById(@PathVariable(value = "uuid") UUID uuid);

    @GetMapping(value = "/account/email/{email}")
    AccountDto getAccountBayEmail(@PathVariable(value = "email") String email);

    @RequestMapping(method = RequestMethod.GET, value = "/auth/validate-token")
    boolean validToken(@RequestHeader(value = "authorization", required = true) String authorizationToken);;

}

