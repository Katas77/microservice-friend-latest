package social.network.microservice_friend;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import social.network.microservice_friend.dto.FriendSearchDto;
import social.network.microservice_friend.dto.Message;
import social.network.microservice_friend.service.FriendServiceOne;
import social.network.microservice_friend.service.impl.FriendServiceOneImpl;
import social.network.microservice_friend.test_utils.UtilsTests;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestFriendController extends AbstractTesFriend {

    @DisplayName("Test blockFriendIds")
    @Test
    public void blockTest() throws Exception {
        Mockito.when(friendServiceMock.blockFriendId(UtilsTests.token)).thenReturn(UtilsTests.blockTest());
        String expectedResponse = UtilsTests.readStringFromResource("response/blockFriendIds.json");
        RequestBuilder builder = MockMvcRequestBuilders.get("/api/v1/friends/blockFriendId").accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", UtilsTests.token);
        String actualResponse = mockMvc.perform(builder).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        MvcResult result = mockMvc.perform(builder).andReturn();
        log.info(" BlockFriendIds  HTTP response status: {}", result.getResponse().getStatus());
        Mockito.verify(friendServiceMock, Mockito.times(2)).blockFriendId(UtilsTests.token);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test account_by_id")
    @Test
    public void account_by_id() throws Exception {
        Mockito.when(friendService2Mock.gettingFriendByIdService(UUID.fromString("b3999ffa-2df9-469e-9793-ee65e214846e"), UtilsTests.token)).thenReturn(UtilsTests.account_by_id());
        String expectedResponse = UtilsTests.readStringFromResource("response/account_by_id.json");
        RequestBuilder builder = MockMvcRequestBuilders.get("/api/v1/friends/{accountId}", "b3999ffa-2df9-469e-9793-ee65e214846e").accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", UtilsTests.token);
        String actualResponse = mockMvc.perform(builder).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        MvcResult result = mockMvc.perform(builder).andReturn();
        log.info(" Account_by_id HTTP response status: {}", result.getResponse().getStatus());
        Mockito.verify(friendService2Mock, Mockito.times(2)).gettingFriendByIdService(UUID.fromString("b3999ffa-2df9-469e-9793-ee65e214846e"), UtilsTests.token);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("friendRequestCounter")
    @Test
    public void friendRequestCounter() throws Exception {
        Mockito.when(friendServiceMock.friendRequestCounter(UtilsTests.token)).thenReturn(3);
        Integer expectedResponse = 3;
        RequestBuilder builder = MockMvcRequestBuilders.get("/api/v1/friends/count", "b3999ffa-2df9-469e-9793-ee65e214846e").accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", UtilsTests.token);
        Integer actualResponse = Integer.valueOf(mockMvc.perform(builder).andExpect(status().isOk()).andReturn().getResponse().getContentAsString());
        Mockito.verify(friendServiceMock, Mockito.times(1)).friendRequestCounter(UtilsTests.token);
        assertEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test approve")
    @Test
    public void approve() throws Exception {
        Mockito.when(friendServiceMock.approveService(UUID.fromString("b3999ffa-2df9-469e-9793-ee65e214846e"), UtilsTests.token)).thenReturn(UtilsTests.messageApprove());
        String expectedResponse = UtilsTests.readStringFromResource("response/approve.json");
        RequestBuilder builder = MockMvcRequestBuilders.put("/api/v1/friends/{uuid}/approve", "b3999ffa-2df9-469e-9793-ee65e214846e").accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", UtilsTests.token);
        var actualResponse = mockMvc.perform(builder).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        MvcResult result = mockMvc.perform(builder).andReturn();
        log.info(" Approve HTTP response status: {}", result.getResponse().getStatus());
        Mockito.verify(friendServiceMock, Mockito.times(2)).approveService(UUID.fromString("b3999ffa-2df9-469e-9793-ee65e214846e"), UtilsTests.token);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test block ")
    @Test
    public void block() throws Exception {
        Mockito.when(friendServiceMock.block(UUID.fromString("b3999ffa-2df9-469e-9793-ee65e214846e"), UtilsTests.token)).thenReturn(UtilsTests.messageBlock());
        String expectedResponse = UtilsTests.readStringFromResource("response/block.json");
        RequestBuilder builder = MockMvcRequestBuilders.put("/api/v1/friends/block/{uuid}", "b3999ffa-2df9-469e-9793-ee65e214846e").accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", UtilsTests.token);
        var actualResponse = mockMvc.perform(builder).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        MvcResult result = mockMvc.perform(builder).andReturn();
        log.info(" Block  HTTP response status: {}", result.getResponse().getStatus());
        Mockito.verify(friendServiceMock, Mockito.times(2)).block(UUID.fromString("b3999ffa-2df9-469e-9793-ee65e214846e"), UtilsTests.token);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test  dell ")
    @Test
    public void dell() throws Exception {
        Mockito.when(friendServiceMock.dell(UUID.fromString("b3999ffa-2df9-469e-9793-ee65e214846e"), UtilsTests.token)).thenReturn(UtilsTests.messageDell());
        String expectedResponse = UtilsTests.readStringFromResource("response/dell.json");
        RequestBuilder builder = MockMvcRequestBuilders.delete("/api/v1/friends/{uuid}", "b3999ffa-2df9-469e-9793-ee65e214846e").accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", UtilsTests.token);
        var actualResponse = mockMvc.perform(builder).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        MvcResult result = mockMvc.perform(builder).andReturn();
        log.info("  Dell  HTTP response status: {}", result.getResponse().getStatus());
        Mockito.verify(friendServiceMock, Mockito.times(2)).dell(UUID.fromString("b3999ffa-2df9-469e-9793-ee65e214846e"), UtilsTests.token);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test friendIds")
    @Test
    public void friendIds() throws Exception {
        Mockito.when(friendServiceMock.friendIds(UtilsTests.token)).thenReturn(UtilsTests.friendIds());
        String expectedResponse = UtilsTests.readStringFromResource("response/friendIds.json");
        RequestBuilder builder = MockMvcRequestBuilders.get("/api/v1/friends/friendId").accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", UtilsTests.token);
        String actualResponse = mockMvc.perform(builder).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        MvcResult result = mockMvc.perform(builder).andReturn();
        log.info("FriendIds HTTP response status: {}", result.getResponse().getStatus());

        Mockito.verify(friendServiceMock, Mockito.times(2)).friendIds(UtilsTests.token);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test request")
    @Test
    public void request() throws Exception {
        Mockito.when(friendServiceMock.request(UUID.fromString("b3999ffa-2df9-469e-9793-ee65e214846e"), UtilsTests.token)).thenReturn(UtilsTests.messageRequest());
        String expectedResponse = UtilsTests.readStringFromResource("response/request.json");
        RequestBuilder builder = MockMvcRequestBuilders.post("/api/v1/friends/{uuid}/request", "b3999ffa-2df9-469e-9793-ee65e214846e").accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", UtilsTests.token);
        var actualResponse = mockMvc.perform(builder).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        MvcResult result = mockMvc.perform(builder).andReturn();
        log.info(" Request HTTP response status: {}", result.getResponse().getStatus());
        Mockito.verify(friendServiceMock, Mockito.times(2)).request(UUID.fromString("b3999ffa-2df9-469e-9793-ee65e214846e"), UtilsTests.token);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test subscribe")
    @Test
    public void subscribe() throws Exception {
        Mockito.when(friendServiceMock.subscribe(UUID.fromString("b3999ffa-2df9-469e-9793-ee65e214846e"), UtilsTests.token)).thenReturn(UtilsTests.messageSubscribe());
        String expectedResponse = UtilsTests.readStringFromResource("response/subscribe.json");
        RequestBuilder builder = MockMvcRequestBuilders.post("/api/v1/friends/subscribe/{uuid}", "b3999ffa-2df9-469e-9793-ee65e214846e").accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", UtilsTests.token);
        var actualResponse = mockMvc.perform(builder).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        MvcResult result = mockMvc.perform(builder).andReturn();
        log.info(" Subscribe HTTP response status: {}", result.getResponse().getStatus());
        Mockito.verify(friendServiceMock, Mockito.times(2)).subscribe(UUID.fromString("b3999ffa-2df9-469e-9793-ee65e214846e"), UtilsTests.token);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test recommendations")
    @Test
    public void recommendations() throws Exception {
        Pageable pageable = PageRequest.of(0, 20);
        Mockito.when(friendService2Mock.recommendationsService(UtilsTests.token, pageable)).thenReturn(UtilsTests.recommendations());
        String expectedResponse = UtilsTests.readStringFromResource("response/recommendations.json");
        RequestBuilder builder = MockMvcRequestBuilders.get("/api/v1/friends/recommendations")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", UtilsTests.token);
        var actualResponse = mockMvc.perform(builder).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        MvcResult result = mockMvc.perform(builder).andReturn();
        log.info(" recommendations HTTP response status: {}", result.getResponse().getStatus());
        Mockito.verify(friendService2Mock, Mockito.times(2)).recommendationsService(UtilsTests.token, pageable);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test  gettingAllFriends")
    @Test
    public void gettingAllFriends() throws Exception {
        FriendSearchDto friendSearchDto = new FriendSearchDto();
        Pageable pageable = PageRequest.of(0, 20);
        Mockito.when(friendService2Mock.gettingAllFriendsService(UtilsTests.token, friendSearchDto, pageable)).thenReturn(UtilsTests.gettingAllFriends());
        String expectedResponse = UtilsTests.readStringFromResource("response/gettingAllFriends.json");
        RequestBuilder builder = MockMvcRequestBuilders.get("/api/v1/friends")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", UtilsTests.token);
        var actualResponse = mockMvc.perform(builder).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        MvcResult result = mockMvc.perform(builder).andReturn();
        log.info(" gettingAllFriends HTTP response status: {}", result.getResponse().getStatus());
        Mockito.verify(friendService2Mock, Mockito.times(2)).gettingAllFriendsService(UtilsTests.token, friendSearchDto, pageable);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test Service  approveService")
    @Test
    public void approveService() throws ParseException {
        FriendServiceOne friendServiceOne=new FriendServiceOneImpl(repository);
        Message actual = friendServiceOne.approveService(UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b2a"), UtilsTests.token);
        UUID uuidTo= UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b2a");
        Message expect=Message.builder().message(MessageFormat.format("Friendship with uuidTo {0} is approve", uuidTo)).build();
        JsonAssert.assertJsonEquals(expect, actual);

    }
    @DisplayName("Test  Service blockS")
    @Test
    public void blockS() throws ParseException {
        FriendServiceOne friendServiceOne=new FriendServiceOneImpl(repository);
        Message actual = friendServiceOne.block(UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b4a"), UtilsTests.token);
        UUID uuidTo= UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b4a");
        Message expect=Message.builder()
                .message( MessageFormat.format("Friend with ID {0} is blocked", uuidTo)).build();
        JsonAssert.assertJsonEquals(expect, actual);

    }

    @DisplayName("Test Service  requestS")
    @Test
    public void requestS() throws ParseException {
        FriendServiceOne friendServiceOne=new FriendServiceOneImpl(repository);
        Message actual = friendServiceOne.request(UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b7a"), UtilsTests.token);
        UUID uuidTo= UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b7a");
        Message expect=Message.builder()
                .message(MessageFormat.format("Friendship with uuidTo {0} REQUEST_FROM", uuidTo)).build();
        JsonAssert.assertJsonEquals(expect, actual);

    }
    @DisplayName("Test Service  subscribe")
    @Test
    public void subscribeS() throws ParseException {
        FriendServiceOne friendServiceOne=new FriendServiceOneImpl(repository);
        Message actual = friendServiceOne.subscribe(UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b3a"), UtilsTests.token);
        UUID uuidTo= UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b3a");
        Message expect=Message.builder()
                .message( MessageFormat.format("Friendship with uuidTo {0} SUBSCRIBED", uuidTo)).build();
        JsonAssert.assertJsonEquals(expect, actual);

    }
    @DisplayName("Test Service  friendIds")
    @Test
    public void friendIdsS() throws ParseException {
        FriendServiceOne friendServiceOne=new FriendServiceOneImpl(repository);
        List<UUID> actual = friendServiceOne.friendIds( UtilsTests.token);
        List<UUID> expect=List.of(UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b2a"));
        JsonAssert.assertJsonEquals(expect, actual);
    }

    @DisplayName("Test Service  friendRequestCounter")
    @Test
    public void friendRequestCounterS() throws ParseException {
        FriendServiceOne friendServiceOne=new FriendServiceOneImpl(repository);
        Integer actual = friendServiceOne.friendRequestCounter( UtilsTests.token);
        Integer expect=0;
        JsonAssert.assertJsonEquals(expect, actual);
    }

    @DisplayName("Test Service  blockFriendId")
    @Test
    public void blockFriendIdS() throws ParseException {
        FriendServiceOne friendServiceOne=new FriendServiceOneImpl(repository);
        List<UUID> actual = friendServiceOne.blockFriendId( UtilsTests.token);
        List<UUID> expect=List.of(UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b4a"));
        JsonAssert.assertJsonEquals(expect, actual);
    }

    @DisplayName("Test Service  dell")
    @Test
    public void dellS() throws ParseException {
        FriendServiceOne friendServiceOne=new FriendServiceOneImpl(repository);
        Message actual = friendServiceOne.dell(UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b3a"), UtilsTests.token);
        UUID uuidTo= UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b3a");
        Message expect=Message.builder()
                .message(MessageFormat.format("friendship with uuidTo {0} is Dell", uuidTo)).build();
        JsonAssert.assertJsonEquals(expect, actual);

    }


}
