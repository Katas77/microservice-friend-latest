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
import social.network.microservice_friend.dto.AccountDto;
import social.network.microservice_friend.dto.FriendSearchDto;
import social.network.microservice_friend.dto.Message;
import social.network.microservice_friend.dto.respons_friend.FriendsRs;
import social.network.microservice_friend.dto.respons_friend.RecommendationFriendsRs;
import social.network.microservice_friend.mapper.MapperDTO;
import social.network.microservice_friend.mapper.impl.MapImpl;
import social.network.microservice_friend.model.en.StatusCode;
import social.network.microservice_friend.service.FriendServiceOne;
import social.network.microservice_friend.service.impl.FriendServiceOneImpl;
import social.network.microservice_friend.service.impl.FriendServiceTwoImpl;
import social.network.microservice_friend.test_utils.UtilsT;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestFriendControllerIntegrationBD extends AbstractTesFriend {

    @DisplayName("Test for controller emergent method blockFriendIds")
    @Test
    void blockTest() throws Exception {
        Mockito.when(friendServiceMock.blockFriendId(UtilsT.token)).thenReturn(UtilsT.blockTest());
        String expectedResponse = UtilsT.readStringFromResource("response/blockFriendIds.json");
        RequestBuilder builder = MockMvcRequestBuilders.get("/api/v1/friends/blockFriendId").accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", UtilsT.token);
        String actualResponse = mockMvc.perform(builder).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        MvcResult result = mockMvc.perform(builder).andReturn();
        log.info(" BlockFriendIds  HTTP response status: {}", result.getResponse().getStatus());
        Mockito.verify(friendServiceMock, Mockito.times(2)).blockFriendId(UtilsT.token);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test for controller emergent method account_by_id")
    @Test
    void account_by_id() throws Exception {
        Mockito.when(friendService2Mock.gettingFriendByIdService(UUID.fromString("b3999ffa-2df9-469e-9793-ee65e214846e"), UtilsT.token)).thenReturn(UtilsT.account_by_id());
        String expectedResponse = UtilsT.readStringFromResource("response/account_by_id.json");
        RequestBuilder builder = MockMvcRequestBuilders.get("/api/v1/friends/{accountId}", "b3999ffa-2df9-469e-9793-ee65e214846e").accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", UtilsT.token);
        String actualResponse = mockMvc.perform(builder).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        MvcResult result = mockMvc.perform(builder).andReturn();
        log.info(" Account_by_id HTTP response status: {}", result.getResponse().getStatus());
        Mockito.verify(friendService2Mock, Mockito.times(2)).gettingFriendByIdService(UUID.fromString("b3999ffa-2df9-469e-9793-ee65e214846e"), UtilsT.token);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test for controller emergent method friendRequestCounter")
    @Test
     void friendRequestCounter() throws Exception {
        Mockito.when(friendServiceMock.friendRequestCounter(UtilsT.token)).thenReturn(3);
        Integer expectedResponse = 3;
        RequestBuilder builder = MockMvcRequestBuilders.get("/api/v1/friends/count", "b3999ffa-2df9-469e-9793-ee65e214846e").accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", UtilsT.token);
        Integer actualResponse = Integer.valueOf(mockMvc.perform(builder).andExpect(status().isOk()).andReturn().getResponse().getContentAsString());
        Mockito.verify(friendServiceMock, Mockito.times(1)).friendRequestCounter(UtilsT.token);
        assertEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test for controller emergent method approve")
    @Test
     void approve() throws Exception {
        Mockito.when(friendServiceMock.approveService(UUID.fromString("b3999ffa-2df9-469e-9793-ee65e214846e"), UtilsT.token)).thenReturn(UtilsT.messageApprove());
        String expectedResponse = UtilsT.readStringFromResource("response/approve.json");
        RequestBuilder builder = MockMvcRequestBuilders.put("/api/v1/friends/{uuid}/approve", "b3999ffa-2df9-469e-9793-ee65e214846e").accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", UtilsT.token);
        var actualResponse = mockMvc.perform(builder).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        MvcResult result = mockMvc.perform(builder).andReturn();
        log.info(" Approve HTTP response status: {}", result.getResponse().getStatus());
        Mockito.verify(friendServiceMock, Mockito.times(2)).approveService(UUID.fromString("b3999ffa-2df9-469e-9793-ee65e214846e"), UtilsT.token);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test for controller emergent method block ")
    @Test
    void block() throws Exception {
        Mockito.when(friendServiceMock.block(UUID.fromString("b3999ffa-2df9-469e-9793-ee65e214846e"), UtilsT.token)).thenReturn(UtilsT.messageBlock());
        String expectedResponse = UtilsT.readStringFromResource("response/block.json");
        RequestBuilder builder = MockMvcRequestBuilders.put("/api/v1/friends/block/{uuid}", "b3999ffa-2df9-469e-9793-ee65e214846e").accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", UtilsT.token);
        var actualResponse = mockMvc.perform(builder).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        MvcResult result = mockMvc.perform(builder).andReturn();
        log.info(" Block  HTTP response status: {}", result.getResponse().getStatus());
        Mockito.verify(friendServiceMock, Mockito.times(2)).block(UUID.fromString("b3999ffa-2df9-469e-9793-ee65e214846e"), UtilsT.token);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test for controller emergent method  dell ")
    @Test
     void dell() throws Exception {
        Mockito.when(friendServiceMock.dell(UUID.fromString("b3999ffa-2df9-469e-9793-ee65e214846e"), UtilsT.token)).thenReturn(UtilsT.messageDell());
        String expectedResponse = UtilsT.readStringFromResource("response/dell.json");
        RequestBuilder builder = MockMvcRequestBuilders.delete("/api/v1/friends/{uuid}", "b3999ffa-2df9-469e-9793-ee65e214846e").accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", UtilsT.token);
        var actualResponse = mockMvc.perform(builder).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        MvcResult result = mockMvc.perform(builder).andReturn();
        log.info("  Dell  HTTP response status: {}", result.getResponse().getStatus());
        Mockito.verify(friendServiceMock, Mockito.times(2)).dell(UUID.fromString("b3999ffa-2df9-469e-9793-ee65e214846e"), UtilsT.token);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test for controller emergent method friendIds")
    @Test
    void friendIds() throws Exception {
        Mockito.when(friendServiceMock.friendIds(UtilsT.token)).thenReturn(UtilsT.friendIds());
        String expectedResponse = UtilsT.readStringFromResource("response/friendIds.json");
        RequestBuilder builder = MockMvcRequestBuilders.get("/api/v1/friends/friendId").accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", UtilsT.token);
        String actualResponse = mockMvc.perform(builder).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        MvcResult result = mockMvc.perform(builder).andReturn();
        log.info("FriendIds HTTP response status: {}", result.getResponse().getStatus());

        Mockito.verify(friendServiceMock, Mockito.times(2)).friendIds(UtilsT.token);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }
    @DisplayName("Test for controller emergent method friendIdsForPost")
    @Test
    void friendIdsForPost() throws Exception {
        Mockito.when(friendServiceMock.friendIdsForPost(UUID.fromString("b3999ffa-2df9-469e-9793-ee65e214846e"))).thenReturn(UtilsT.friendIds());
        String expectedResponse = UtilsT.readStringFromResource("response/friendIds.json");
        RequestBuilder builder = MockMvcRequestBuilders.get("/api/v1/friends/friendId/post/{userId}","b3999ffa-2df9-469e-9793-ee65e214846e").accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", UtilsT.token);
        String actualResponse = mockMvc.perform(builder).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        Mockito.verify(friendServiceMock, Mockito.times(1)).friendIdsForPost(UUID.fromString("b3999ffa-2df9-469e-9793-ee65e214846e"));
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test for controller emergent method request")
    @Test
     void request() throws Exception {
        Mockito.when(friendServiceMock.request(UUID.fromString("b3999ffa-2df9-469e-9793-ee65e214846e"), UtilsT.token)).thenReturn(UtilsT.messageRequest());
        String expectedResponse = UtilsT.readStringFromResource("response/request.json");
        RequestBuilder builder = MockMvcRequestBuilders.post("/api/v1/friends/{uuid}/request", "b3999ffa-2df9-469e-9793-ee65e214846e").accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", UtilsT.token);
        var actualResponse = mockMvc.perform(builder).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        MvcResult result = mockMvc.perform(builder).andReturn();
        log.info(" Request HTTP response status: {}", result.getResponse().getStatus());
        Mockito.verify(friendServiceMock, Mockito.times(2)).request(UUID.fromString("b3999ffa-2df9-469e-9793-ee65e214846e"), UtilsT.token);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test for controller emergent method subscribe")
    @Test
    void subscribe() throws Exception {
        Mockito.when(friendServiceMock.subscribe(UUID.fromString("b3999ffa-2df9-469e-9793-ee65e214846e"), UtilsT.token)).thenReturn(UtilsT.messageSubscribe());
        String expectedResponse = UtilsT.readStringFromResource("response/subscribe.json");
        RequestBuilder builder = MockMvcRequestBuilders.post("/api/v1/friends/subscribe/{uuid}", "b3999ffa-2df9-469e-9793-ee65e214846e").accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", UtilsT.token);
        var actualResponse = mockMvc.perform(builder).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        MvcResult result = mockMvc.perform(builder).andReturn();
        log.info(" Subscribe HTTP response status: {}", result.getResponse().getStatus());
        Mockito.verify(friendServiceMock, Mockito.times(2)).subscribe(UUID.fromString("b3999ffa-2df9-469e-9793-ee65e214846e"), UtilsT.token);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test for controller emergent method recommendations")
    @Test
     void recommendations() throws Exception {
        Pageable pageable = PageRequest.of(0, 20);
        Mockito.when(friendService2Mock.recommendationsService(UtilsT.token, pageable)).thenReturn(UtilsT.recommendations());
        String expectedResponse = UtilsT.readStringFromResource("response/recommendations.json");
        RequestBuilder builder = MockMvcRequestBuilders.get("/api/v1/friends/recommendations")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", UtilsT.token);
        var actualResponse = mockMvc.perform(builder).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        MvcResult result = mockMvc.perform(builder).andReturn();
        log.info(" recommendations HTTP response status: {}", result.getResponse().getStatus());
        Mockito.verify(friendService2Mock, Mockito.times(2)).recommendationsService(UtilsT.token, pageable);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test for controller emergent method gettingAllFriends")
    @Test
     void gettingAllFriends() throws Exception {
        FriendSearchDto friendSearchDto = new FriendSearchDto();
        Pageable pageable = PageRequest.of(0, 20);
        Mockito.when(friendService2Mock.gettingAllFriendsService(UtilsT.token, friendSearchDto, pageable)).thenReturn(UtilsT.gettingAllFriends());
        String expectedResponse = UtilsT.readStringFromResource("response/gettingAllFriends.json");
        RequestBuilder builder = MockMvcRequestBuilders.get("/api/v1/friends")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", UtilsT.token);
        var actualResponse = mockMvc.perform(builder).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        MvcResult result = mockMvc.perform(builder).andReturn();
        log.info(" gettingAllFriends HTTP response status: {}", result.getResponse().getStatus());
        Mockito.verify(friendService2Mock, Mockito.times(2)).gettingAllFriendsService(UtilsT.token, friendSearchDto, pageable);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test for service layer arising in method approveService")
    @Test
     void approveService() throws ParseException {
        FriendServiceOne friendServiceOne=new FriendServiceOneImpl(repository,producer);
        Message actual = friendServiceOne.approveService(UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b2a"), UtilsT.token);
        UUID uuidTo= UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b2a");
        Message expect=Message.builder().report(MessageFormat.format("Friendship with uuidTo {0} is approve", uuidTo)).build();
        JsonAssert.assertJsonEquals(expect, actual);

    }
    @DisplayName("Test for service layer arising in method blockS")
    @Test
   void blockS() throws ParseException {
        FriendServiceOne friendServiceOne=new FriendServiceOneImpl(repository,producer);
        Message actual = friendServiceOne.block(UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b4a"), UtilsT.token);
        UUID uuidTo= UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b4a");
        Message expect=Message.builder()
                .report( MessageFormat.format("Friend with ID {0} is blocked", uuidTo)).build();
        JsonAssert.assertJsonEquals(expect, actual);

    }

    @DisplayName("Test for service layer arising in method  requestS")
    @Test
     void requestS() throws ParseException {
        FriendServiceOne friendServiceOne=new FriendServiceOneImpl(repository,producer);
        Message actual = friendServiceOne.request(UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b7a"), UtilsT.token);
        UUID uuidTo= UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b7a");
        Message expect=Message.builder()
                .report(MessageFormat.format("Friendship with uuidTo {0} REQUEST_FROM", uuidTo)).build();
        JsonAssert.assertJsonEquals(expect, actual);

    }
    @DisplayName("Test for service layer arising in method  subscribe")
    @Test
     void subscribeS() throws ParseException {
        FriendServiceOne friendServiceOne=new FriendServiceOneImpl(repository,producer);
        Message actual = friendServiceOne.subscribe(UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b3a"), UtilsT.token);
        UUID uuidTo= UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b3a");
        Message expect=Message.builder()
                .report( MessageFormat.format("Friendship with uuidTo {0} SUBSCRIBED", uuidTo)).build();
        JsonAssert.assertJsonEquals(expect, actual);

    }
    @DisplayName("Test for service layer arising in method  friendIds")
    @Test
    void friendIdsS() throws ParseException {
        FriendServiceOne friendServiceOne=new FriendServiceOneImpl(repository,producer);
        List<UUID> actual = friendServiceOne.friendIds( UtilsT.token);
        List<UUID> expect=List.of(UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b2a"));
        JsonAssert.assertJsonEquals(expect, actual);
    }
    @DisplayName("Test for service layer arising in method   friendIdsForPost")
    @Test
     void  friendIdsForPostS()  {
        FriendServiceOne friendServiceOne=new FriendServiceOneImpl(repository,producer);
        List<UUID> actual = friendServiceOne. friendIdsForPost( (UUID.fromString("4a001ad4-52e8-41d2-8170-c28705c765b5")));
        List<UUID> expect=List.of(UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b2a"));
        JsonAssert.assertJsonEquals(expect, actual);
    }

    @DisplayName("Test for service layer arising in method  friendRequestCounter")
    @Test
     void friendRequestCounterS() throws ParseException {
        FriendServiceOne friendServiceOne=new FriendServiceOneImpl(repository,producer);
        Integer actual = friendServiceOne.friendRequestCounter( UtilsT.token);
        Integer expect=0;
        JsonAssert.assertJsonEquals(expect, actual);
    }

    @DisplayName("Test for service layer arising in method blockFriendId")
    @Test
    void blockFriendIdS() throws ParseException {
        FriendServiceOne friendServiceOne=new FriendServiceOneImpl(repository,producer);
        List<UUID> actual = friendServiceOne.blockFriendId( UtilsT.token);
        List<UUID> expect=List.of(UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b4a"));
        JsonAssert.assertJsonEquals(expect, actual);
    }

    @DisplayName("Test for service layer arising in method  dell")
    @Test
    void dellS() throws ParseException {
        FriendServiceOne friendServiceOne=new FriendServiceOneImpl(repository,producer);
        Message actual = friendServiceOne.dell(UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b3a"), UtilsT.token);
        UUID uuidTo= UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b3a");
        Message expect=Message.builder()
                .report(MessageFormat.format("friendship with uuidTo {0} is Dell", uuidTo)).build();
        JsonAssert.assertJsonEquals(expect, actual);

    }

    @DisplayName("Test for service layer arising in method gettingAllFriends")
    @Test
     void gettingAllFriendsS() throws Exception {
        MapperDTO mapper=new MapImpl();
        FriendServiceTwoImpl serviceTwo = new FriendServiceTwoImpl(mapper, repository, accountClient, friendServiceMock);
        FriendSearchDto friendSearchDto = new FriendSearchDto();
        friendSearchDto.setStatusCode(StatusCode.REQUEST_FROM);
        Pageable pageable = PageRequest.of(0, 20);
        FriendsRs expectedResponse=FriendsRs.builder().totalElements(0L).totalPages(0).content(new ArrayList<>()).build();
        Mockito.when(accountClient.getAccountById(UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b3a"), UtilsT.token)).thenReturn(UtilsT.accountDto());
        FriendsRs  actualResponse = serviceTwo.gettingAllFriendsService(UtilsT.token,friendSearchDto,pageable);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test for service layer arising in method recommendationsService")
    @Test
     void recommendationsService() throws Exception {
        MapperDTO mapper=new MapImpl();
        FriendServiceTwoImpl serviceTwo = new FriendServiceTwoImpl(mapper, repository, accountClient, friendServiceMock);
        Pageable pageable = PageRequest.of(0, 20);
        RecommendationFriendsRs  expectedResponse= UtilsT.recommendations();
        Mockito.when(accountClient.getAccountById(UUID.fromString("0bc856ad-b35a-4b19-8969-4cc848fc5198"), UtilsT.token)).thenReturn(UtilsT.accountDto());
        RecommendationFriendsRs actualResponse = serviceTwo.recommendationsService(UtilsT.token,pageable);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }
    @DisplayName("Test for service layer arising in method gettingFriendByIdService")
    @Test
  void gettingFriendByIdService(){
        MapperDTO mapper=new MapImpl();
        FriendServiceTwoImpl serviceTwo = new FriendServiceTwoImpl(mapper, repository, accountClient, friendServiceMock);
        AccountDto expectedResponse= UtilsT.accountDto();
        Mockito.when(accountClient.getAccountById(UUID.fromString("0bc856ad-b35a-4b19-8969-4cc848fc5198"), UtilsT.token)).thenReturn(UtilsT.accountDto());
        AccountDto  actualResponse = serviceTwo.gettingFriendByIdService(UUID.fromString("0bc856ad-b35a-4b19-8969-4cc848fc5198"), UtilsT.token);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }


}
