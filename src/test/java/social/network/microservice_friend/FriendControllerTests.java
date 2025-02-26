package social.network.microservice_friend;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import social.network.microservice_friend.controller.FriendController;
import social.network.microservice_friend.dto.FriendSearchDto;
import social.network.microservice_friend.service.FriendServiceOne;
import social.network.microservice_friend.service.FriendServiceTwo;
import social.network.microservice_friend.test_utils.UtilsT;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
@Slf4j
class FriendControllerTests {

    private static final UUID TEST_UUID = UUID.fromString("b3999ffa-2df9-469e-9793-ee65e214846e");
    private static final String API_FRIENDS_URL = "/api/v1/friends";

    private FriendServiceOne friendServiceMock;
    private FriendServiceTwo friendService2Mock;
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        friendServiceMock = Mockito.mock(FriendServiceOne.class);
        friendService2Mock = Mockito.mock(FriendServiceTwo.class);
        mockMvc = standaloneSetup(new FriendController(friendServiceMock, friendService2Mock))
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @DisplayName("Test for controller emergent method blockFriendIds")
    @Test
    void blockTest() throws Exception {
        Mockito.when(friendServiceMock.blockFriendId(UtilsT.token)).thenReturn(UtilsT.blockTest());

        String expectedResponse = UtilsT.readStringFromResource("response/blockFriendIds.json");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(API_FRIENDS_URL + "/blockFriendId")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", UtilsT.token);

        String actualResponse = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        log.info("BlockFriendIds HTTP response status: {}", result.getResponse().getStatus());

        Mockito.verify(friendServiceMock, Mockito.times(2)).blockFriendId(UtilsT.token);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test for controller emergent method account_by_id")
    @Test
    void account_by_id() throws Exception {
        Mockito.when(friendService2Mock.gettingFriendByIdService(TEST_UUID, UtilsT.token)).thenReturn(UtilsT.account_by_id());

        String expectedResponse = UtilsT.readStringFromResource("response/account_by_id.json");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(API_FRIENDS_URL + "/{accountId}", TEST_UUID.toString())
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", UtilsT.token);

        String actualResponse = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        log.info("Account_by_id HTTP response status: {}", result.getResponse().getStatus());

        Mockito.verify(friendService2Mock, Mockito.times(2)).gettingFriendByIdService(TEST_UUID, UtilsT.token);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test for controller emergent method friendRequestCounter")
    @Test
    void friendRequestCounter() throws Exception {
        Mockito.when(friendServiceMock.friendRequestCounter(UtilsT.token)).thenReturn(3);

        int expectedResponse = 3;
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(API_FRIENDS_URL + "/count")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", UtilsT.token);

        int actualResponse = Integer.valueOf(mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString());

        Mockito.verify(friendServiceMock, Mockito.times(1)).friendRequestCounter(UtilsT.token);
        assertEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test for controller emergent method approve")
    @Test
    void approve() throws Exception {
        Mockito.when(friendServiceMock.approveService(TEST_UUID, UtilsT.token)).thenReturn(UtilsT.messageApprove());

        String expectedResponse = UtilsT.readStringFromResource("response/approve.json");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(API_FRIENDS_URL + "/{uuid}/approve", TEST_UUID.toString())
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", UtilsT.token);

        String actualResponse = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        log.info("Approve HTTP response status: {}", result.getResponse().getStatus());

        Mockito.verify(friendServiceMock, Mockito.times(2)).approveService(TEST_UUID, UtilsT.token);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test for controller emergent method block")
    @Test
    void block() throws Exception {
        Mockito.when(friendServiceMock.block(TEST_UUID, UtilsT.token)).thenReturn(UtilsT.messageBlock());

        String expectedResponse = UtilsT.readStringFromResource("response/block.json");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(API_FRIENDS_URL + "/block/{uuid}", TEST_UUID.toString())
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", UtilsT.token);

        String actualResponse = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        log.info("Block HTTP response status: {}", result.getResponse().getStatus());

        Mockito.verify(friendServiceMock, Mockito.times(2)).block(TEST_UUID, UtilsT.token);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test for controller emergent method unblock")
    @Test
    void unblock() throws Exception {
        Mockito.when(friendServiceMock.unblock(TEST_UUID, UtilsT.token)).thenReturn(UtilsT.messageBlock());

        String expectedResponse = UtilsT.readStringFromResource("response/block.json");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(API_FRIENDS_URL + "/unblock/{uuid}", TEST_UUID.toString())
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", UtilsT.token);

        String actualResponse = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        log.info("Unblock HTTP response status: {}", result.getResponse().getStatus());

        Mockito.verify(friendServiceMock, Mockito.times(2)).unblock(TEST_UUID, UtilsT.token);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test for controller emergent method delete (dell)")
    @Test
    void delete() throws Exception {
        Mockito.when(friendServiceMock.dell(TEST_UUID, UtilsT.token)).thenReturn(UtilsT.messageDell());

        String expectedResponse = UtilsT.readStringFromResource("response/dell.json");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(API_FRIENDS_URL + "/{uuid}", TEST_UUID.toString())
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", UtilsT.token);

        String actualResponse = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        log.info("Delete HTTP response status: {}", result.getResponse().getStatus());

        Mockito.verify(friendServiceMock, Mockito.times(2)).dell(TEST_UUID, UtilsT.token);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test for controller emergent method friendIds")
    @Test
    void friendIds() throws Exception {
        Mockito.when(friendServiceMock.friendIds(UtilsT.token)).thenReturn(UtilsT.friendIds());

        String expectedResponse = UtilsT.readStringFromResource("response/friendIds.json");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(API_FRIENDS_URL + "/friendId")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", UtilsT.token);

        String actualResponse = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        log.info("FriendIds HTTP response status: {}", result.getResponse().getStatus());

        Mockito.verify(friendServiceMock, Mockito.times(2)).friendIds(UtilsT.token);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }
    @DisplayName("Test for controller emergent method gettingAllFriends")
    @Test
    void gettingAllFriends() throws Exception {
        FriendSearchDto friendSearchDto = new FriendSearchDto();
        Pageable pageable = PageRequest.of(0, 20);
        Mockito.when(friendService2Mock.gettingAllFriendsService(UtilsT.token, friendSearchDto, pageable))
                .thenReturn(UtilsT.gettingAllFriends());

        String expectedResponse = UtilsT.readStringFromResource("response/gettingAllFriends.json");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(API_FRIENDS_URL)
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", UtilsT.token);

        String actualResponse = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        log.info("Getting all friends HTTP response status: {}", result.getResponse().getStatus());

        Mockito.verify(friendService2Mock, Mockito.times(2))
                .gettingAllFriendsService(UtilsT.token, friendSearchDto, pageable);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test for controller emergent method recommendations")
    @Test
    void recommendations() throws Exception {
        Pageable pageable = PageRequest.of(0, 20);
        Mockito.when(friendService2Mock.recommendationsService(UtilsT.token, pageable))
                .thenReturn(UtilsT.recommendations());

        String expectedResponse = UtilsT.readStringFromResource("response/recommendations.json");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(API_FRIENDS_URL + "/recommendations")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", UtilsT.token);

        String actualResponse = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        log.info("Recommendations HTTP response status: {}", result.getResponse().getStatus());

        Mockito.verify(friendService2Mock, Mockito.times(2))
                .recommendationsService(UtilsT.token, pageable);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test for controller emergent method subscribe")
    @Test
    void subscribe() throws Exception {
        Mockito.when(friendServiceMock.subscribe(TEST_UUID, UtilsT.token))
                .thenReturn(UtilsT.messageSubscribe());

        String expectedResponse = UtilsT.readStringFromResource("response/subscribe.json");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(API_FRIENDS_URL + "/subscribe/" + TEST_UUID)
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", UtilsT.token);

        String actualResponse = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        log.info("Subscribe HTTP response status: {}", result.getResponse().getStatus());

        Mockito.verify(friendServiceMock, Mockito.times(2))
                .subscribe(TEST_UUID, UtilsT.token);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test for controller emergent method friendIdsForPost")
    @Test
    void friendIdsForPost() throws Exception {
        Mockito.when(friendServiceMock.friendIdsForPost(TEST_UUID))
                .thenReturn(UtilsT.friendIds());

        String expectedResponse = UtilsT.readStringFromResource("response/friendIds.json");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(API_FRIENDS_URL + "/friendId/post/" + TEST_UUID)
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", UtilsT.token);

        String actualResponse = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Mockito.verify(friendServiceMock, Mockito.times(1))
                .friendIdsForPost(TEST_UUID);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test for controller emergent method request")
    @Test
    void request() throws Exception {
        Mockito.when(friendServiceMock.request(TEST_UUID, UtilsT.token))
                .thenReturn(UtilsT.messageRequest());

        String expectedResponse = UtilsT.readStringFromResource("response/request.json");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(API_FRIENDS_URL + "/" + TEST_UUID + "/request")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", UtilsT.token);

        String actualResponse = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        log.info("Request HTTP response status: {}", result.getResponse().getStatus());

        Mockito.verify(friendServiceMock, Mockito.times(2))
                .request(TEST_UUID, UtilsT.token);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }
}