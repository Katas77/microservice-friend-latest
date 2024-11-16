package social.network.microservice_friend.accaunt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import social.network.microservice_friend.exception.BusinessLogicException;
import social.network.microservice_friend.dto.AccountDto;

import java.io.IOException;
import java.util.Optional;


@Component
public class AccountClient {

    @Autowired
    private OkHttpClient client;

    @Value("${account.url}")
    private String accUrl;

    public AccountDto getAccount(String pathVariable) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json=getJson(pathVariable).orElseThrow(() -> new BusinessLogicException("Не удалось получить Json"));
        return objectMapper.readValue
                (json, AccountDto.class);
    }

    public Optional<String> getJson(String pathVariable) {
        accUrl = accUrl + "/" + pathVariable;
        var request = new Request.Builder()
                .url(accUrl)
                .build();
        try (var response = client.newCall(request).execute()) {
            var body = response.body();
            return body == null ? Optional.empty() : Optional.of(body.string());
        } catch (IOException e) {
            throw new BusinessLogicException("Ошибка подключения", e);
        }
    }
}