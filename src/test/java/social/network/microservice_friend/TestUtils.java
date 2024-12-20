package social.network.microservice_friend;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.FileCopyUtils;
import social.network.microservice_friend.dto.AccountDto;
import social.network.microservice_friend.dto.FriendDto;
import social.network.microservice_friend.dto.Message;
import social.network.microservice_friend.dto.RecommendationFriendsDto;
import social.network.microservice_friend.dto.responsF.FriendsRs;
import social.network.microservice_friend.dto.responsF.RecommendationFriendsRs;
import social.network.microservice_friend.model.en.StatusCode;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestUtils {

    public static String readStringFromResource(String resourcePath) {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource(MessageFormat.format("classpath:{0}", resourcePath));
        try (
                Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    static String token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0YTAwMWFkNC01MmU4LTQxZDItODE3MC1jMjg3MDVjNzY1YjUiLCJpYXQiOjE3MzMzODEzODAsImV4cCI6MTczMzM4MzE4MH0.i0WFHY-378LSF9pwyT8ozEILuzKgfi6nn6hoasr4yOOG3DXrhtvOOuS_BGiOjg7fRzPU1a6WBj67sl-30wPfnA";

    static List<UUID> blockTest() {
        return List.of(UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b2a"), UUID.fromString("fa6f76bb-6be7-493e-bbdc-caf03cb7eb6a"));
    }

    static AccountDto account_by_id() {
        return AccountDto.builder()
                .id(UUID.fromString("b3999ffa-2df9-469e-9793-ee65e214846e"))
                .lastOnlineTime(null)
                .createdOn(null)
                .birthDate(null)
                .photo("best photo")
                .firstName("Ivan")
                .isOnline(true)
                .country("Niger")
                .lastName("Ivanov")
                .email("krp77@mail.ru")
                .build();
    }


    public static Message messageApprove() {
        return Message.builder()
                .message("Friendship with uuidTo b3999ffa-2df9-469e-9793-ee65e214846e is approve")
                .build();
    }

    public static Message messageBlock() {
        return Message.builder()
                .message("Friendship with uuidTob3999ffa-2df9-469e-9793-ee65e214846e is  blocked")
                .build();
    }

    public static Message messageDell() {
        return Message.builder()
                .message("friendship with uuidTo b3999ffa-2df9-469e-9793-ee65e214846e is Dell")
                .build();
    }

    public static List<UUID> friendIds() {
        return List.of(UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b2a"), UUID.fromString("fa6f76bb-6be7-493e-bbdc-caf03cb7eb6a"));
    }

    public static Message messageRequest() {
        return Message.builder()
                .message("Friendship with uuidTo b3999ffa-2df9-469e-9793-ee65e214846e REQUEST_FROM")
                .build();
    }

    public static Message messageSubscribe() {
        return Message.builder()
                .message("Friendship with uuidTo 6d175460-27bc-49a3-aa7a-bad5861f9706 SUBSCRIBED")
                .build();
    }

    public static RecommendationFriendsRs recommendations() {
        List<RecommendationFriendsDto> content = new ArrayList<>();
        RecommendationFriendsDto dt0 = RecommendationFriendsDto.builder()
                .firstName("Ivan")
                .lastName("Ivanov")
                .friendId(UUID.fromString("0bc856ad-b35a-4b19-8969-4cc848fc5198"))
                .photo("best photo")
                .build();
        content.add(dt0);

        return RecommendationFriendsRs.builder()
                .totalPages(1)
                .totalElements(1L)
                .content(content)
                .build();
    }

    public static FriendsRs gettingAllFriends() {
        List<FriendDto> content = new ArrayList<>();
        FriendDto dto = FriendDto.builder()
                .friendId(UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b2a"))
                .birthDate(LocalDate.of(2024, 12, 19))
                .statusCode(StatusCode.REQUEST_FROM)
                .photo("best photo")
                .firstName("Ivan")
                .lastName("Ivanov")
                .country("Niger")
                .isOnline(true)
                .build();
        content.add(dto);
        return FriendsRs.builder()
                .totalPages(1)
                .totalElements(1L)
                .content(content)
                .build();
    }
}
