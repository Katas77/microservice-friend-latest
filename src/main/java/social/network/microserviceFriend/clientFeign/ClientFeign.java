package social.network.microserviceFriend.clientFeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import social.network.microserviceFriend.dto.AccountDto;


import java.util.UUID;

@FeignClient(name = "accountClient", url = "${app.url}")
public interface ClientFeign {

    @RequestMapping(method = RequestMethod.GET, value = "/auth/validate-token")
    boolean validToken(@RequestHeader(value = "Authorization", required = true) String authorizationToken);

    @RequestMapping(method = RequestMethod.GET, value = "/account/{id}")
    AccountDto getAccountById(@PathVariable(value = "id") UUID id, @RequestHeader(value = "Authorization", required = true) String authorizationToken);

    @GetMapping(value = "/account/email/{email}")
    AccountDto getAccountBayEmail(@PathVariable(value = "email") String email);

}

