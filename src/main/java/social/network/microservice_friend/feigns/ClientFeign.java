package social.network.microservice_friend.feigns;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import social.network.microservice_friend.dto.AccountDto;


import java.util.UUID;

@FeignClient(name = "validate", url = "${app.url}")
public interface ClientFeign {

    @RequestMapping(method = RequestMethod.GET, value = "/auth/validate-token")
    boolean validToken(@RequestHeader(value = "Authorization", required = true) String authorizationToken);

    @RequestMapping(method = RequestMethod.GET, value = "/account/{id}")
    AccountDto getAccountById(@PathVariable(value = "id") UUID id, @RequestHeader(value = "Authorization", required = true) String authorizationToken);

    @GetMapping(value = "/account/email/{email}")
    AccountDto getAccountByEmail(@PathVariable(value = "email") String email);

}

