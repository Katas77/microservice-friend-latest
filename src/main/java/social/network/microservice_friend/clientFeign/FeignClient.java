package social.network.microservice_friend.clientFeign;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import social.network.microservice_friend.dto.AccountDto;
import social.network.microservice_friend.dto.CheckDto;

import java.util.UUID;

@org.springframework.cloud.openfeign.FeignClient(name = "accountClient", url = "http://localhost:8080/api/v1/account")
public interface FeignClient {

    @GetMapping(value = "/{uuid}")
    AccountDto getAccountById(@PathVariable(value = "uuid") UUID uuid);
    @GetMapping(value = "/email/{email}")
    AccountDto getAccountBayEmail(@PathVariable(value = "email") String email);
    @GetMapping(value = "/authentication/{token}")
    CheckDto validToken(@PathVariable(value = "token") String token);

}

