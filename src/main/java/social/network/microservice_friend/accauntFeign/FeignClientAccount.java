package social.network.microservice_friend.accauntFeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import social.network.microservice_friend.dto.AccountDto;

@FeignClient(name = "accountClient", url = "http://localhost:8080/api/v1/account")
public interface FeignClientAccount {

    @GetMapping(value = "/{uuid}")
    AccountDto getAccountById(@PathVariable(value = "uuid") String uuid);

    @GetMapping(value = "/email/{email}")
    AccountDto getAccountBayEmail(@PathVariable(value = "email") String email);


}

