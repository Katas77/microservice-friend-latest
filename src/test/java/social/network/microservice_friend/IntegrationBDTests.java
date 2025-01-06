package social.network.microservice_friend;

import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

public class IntegrationBDTests extends FriendAbstractTests {
    @DisplayName("Test for service layer arising in method approveService")
    @Test
    void approveService() throws ParseException {
        FriendServiceOne friendServiceOne = new FriendServiceOneImpl(repository, producer);
        Message actual = friendServiceOne.approveService(UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b2a"), UtilsT.token);
        UUID uuidTo = UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b2a");
        Message expect = Message.builder().report(MessageFormat.format("Friendship with uuidTo {0} is approve", uuidTo)).build();
        JsonAssert.assertJsonEquals(expect, actual);

    }

    @DisplayName("Test for service layer arising in method blockS")
    @Test
    void blockS() throws ParseException {
        FriendServiceOne friendServiceOne = new FriendServiceOneImpl(repository, producer);
        Message actual = friendServiceOne.block(UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b4a"), UtilsT.token);
        UUID uuidTo = UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b4a");
        Message expect = Message.builder()
                .report(MessageFormat.format("Friend with ID {0} is blocked", uuidTo)).build();
        JsonAssert.assertJsonEquals(expect, actual);

    }

    @DisplayName("Test for service layer arising in method  requestS")
    @Test
    void requestS() throws ParseException {
        FriendServiceOne friendServiceOne = new FriendServiceOneImpl(repository, producer);
        Message actual = friendServiceOne.request(UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b7a"), UtilsT.token);
        UUID uuidTo = UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b7a");
        Message expect = Message.builder()
                .report(MessageFormat.format("Friendship with uuidTo {0} REQUEST_FROM", uuidTo)).build();
        JsonAssert.assertJsonEquals(expect, actual);

    }

    @DisplayName("Test for service layer arising in method  subscribe")
    @Test
    void subscribeS() throws ParseException {
        FriendServiceOne friendServiceOne = new FriendServiceOneImpl(repository, producer);
        Message actual = friendServiceOne.subscribe(UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b3a"), UtilsT.token);
        UUID uuidTo = UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b3a");
        Message expect = Message.builder()
                .report(MessageFormat.format("Friendship with uuidTo {0} SUBSCRIBED", uuidTo)).build();
        JsonAssert.assertJsonEquals(expect, actual);

    }

    @DisplayName("Test for service layer arising in method  friendIds")
    @Test
    void friendIdsS() throws ParseException {
        FriendServiceOne friendServiceOne = new FriendServiceOneImpl(repository, producer);
        List<UUID> actual = friendServiceOne.friendIds(UtilsT.token);
        List<UUID> expect = List.of(UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b2a"));
        JsonAssert.assertJsonEquals(expect, actual);
    }

    @DisplayName("Test for service layer arising in method   friendIdsForPost")
    @Test
    void friendIdsForPostS() {
        FriendServiceOne friendServiceOne = new FriendServiceOneImpl(repository, producer);
        List<UUID> actual = friendServiceOne.friendIdsForPost((UUID.fromString("4a001ad4-52e8-41d2-8170-c28705c765b5")));
        List<UUID> expect = List.of(UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b2a"));
        JsonAssert.assertJsonEquals(expect, actual);
    }

    @DisplayName("Test for service layer arising in method  friendRequestCounter")
    @Test
    void friendRequestCounterS() throws ParseException {
        FriendServiceOne friendServiceOne = new FriendServiceOneImpl(repository, producer);
        Integer actual = friendServiceOne.friendRequestCounter(UtilsT.token);
        Integer expect = 0;
        JsonAssert.assertJsonEquals(expect, actual);
    }

    @DisplayName("Test for service layer arising in method blockFriendId")
    @Test
    void blockFriendIdS() throws ParseException {
        FriendServiceOne friendServiceOne = new FriendServiceOneImpl(repository, producer);
        List<UUID> actual = friendServiceOne.blockFriendId(UtilsT.token);
        List<UUID> expect = List.of(UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b4a"));
        JsonAssert.assertJsonEquals(expect, actual);
    }

    @DisplayName("Test for service layer arising in method  dell")
    @Test
    void dellS() throws ParseException {
        FriendServiceOne friendServiceOne = new FriendServiceOneImpl(repository, producer);
        Message actual = friendServiceOne.dell(UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b3a"), UtilsT.token);
        UUID uuidTo = UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b3a");
        Message expect = Message.builder()
                .report(MessageFormat.format("friendship with uuidTo {0} is Dell", uuidTo)).build();
        JsonAssert.assertJsonEquals(expect, actual);

    }

    @DisplayName("Test for service layer arising in method gettingAllFriends")
    @Test
    void gettingAllFriendsS() throws Exception {
        MapperDTO mapper = new MapImpl();
        FriendServiceTwoImpl serviceTwo = new FriendServiceTwoImpl(mapper, repository, accountClient, friendServiceMock);
        FriendSearchDto friendSearchDto = new FriendSearchDto();
        friendSearchDto.setStatusCode(StatusCode.REQUEST_FROM);
        Pageable pageable = PageRequest.of(0, 20);
        FriendsRs expectedResponse = FriendsRs.builder().totalElements(0L).totalPages(0).content(new ArrayList<>()).build();
        Mockito.when(accountClient.getAccountById(UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b3a"), UtilsT.token)).thenReturn(UtilsT.accountDto());
        FriendsRs actualResponse = serviceTwo.gettingAllFriendsService(UtilsT.token, friendSearchDto, pageable);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test for service layer arising in method gettingAllFriends")
    @Test
    void gettingAllFriendsWATCHING() throws Exception {
        MapperDTO mapper = new MapImpl();
        FriendServiceTwoImpl serviceTwo = new FriendServiceTwoImpl(mapper, repository, accountClient, friendServiceMock);
        FriendSearchDto friendSearchDto = new FriendSearchDto();
        friendSearchDto.setStatusCode(StatusCode.WATCHING);
        Pageable pageable = PageRequest.of(0, 20);
        FriendsRs expectedResponse = FriendsRs.builder().totalElements(0L).totalPages(0).content(new ArrayList<>()).build();
        Mockito.when(accountClient.getAccountById(UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b3a"), UtilsT.token)).thenReturn(UtilsT.accountDto());
        FriendsRs actualResponse = serviceTwo.gettingAllFriendsService(UtilsT.token, friendSearchDto, pageable);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test for service layer arising in method gettingAllFriends")
    @Test
    void gettingAllFriendsREQUEST_TO() throws Exception {
        MapperDTO mapper = new MapImpl();
        FriendServiceTwoImpl serviceTwo = new FriendServiceTwoImpl(mapper, repository, accountClient, friendServiceMock);
        FriendSearchDto friendSearchDto = new FriendSearchDto();
        friendSearchDto.setStatusCode(StatusCode.REQUEST_TO);
        Pageable pageable = PageRequest.of(0, 20);
        FriendsRs expectedResponse = FriendsRs.builder().totalElements(1L).totalPages(1).content(List.of(mapper.convertToFriendDto(UtilsT.accountDto(), StatusCode.REQUEST_TO))).build();
        Mockito.when(accountClient.getAccountById(UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b5a"), UtilsT.token)).thenReturn(UtilsT.accountDto());
        FriendsRs actualResponse = serviceTwo.gettingAllFriendsService(UtilsT.token, friendSearchDto, pageable);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test for service layer arising in method gettingAllFriends")
    @Test
    void gettingAllFriendsSUBSCRIBED() throws Exception {
        MapperDTO mapper = new MapImpl();
        FriendServiceTwoImpl serviceTwo = new FriendServiceTwoImpl(mapper, repository, accountClient, friendServiceMock);
        FriendSearchDto friendSearchDto = new FriendSearchDto();
        friendSearchDto.setStatusCode(StatusCode.SUBSCRIBED);
        Pageable pageable = PageRequest.of(0, 20);
        FriendsRs expectedResponse = FriendsRs.builder().totalElements(1L).totalPages(1).content(List.of(mapper.convertToFriendDto(UtilsT.accountDto(), StatusCode.SUBSCRIBED))).build();
        Mockito.when(accountClient.getAccountById(UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b3a"), UtilsT.token)).thenReturn(UtilsT.accountDto());
        FriendsRs actualResponse = serviceTwo.gettingAllFriendsService(UtilsT.token, friendSearchDto, pageable);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test for service layer arising in method gettingAllFriends")
    @Test
    void gettingAllFriendsBLOCKED() throws Exception {
        MapperDTO mapper = new MapImpl();
        FriendServiceTwoImpl serviceTwo = new FriendServiceTwoImpl(mapper, repository, accountClient, friendServiceMock);
        FriendSearchDto friendSearchDto = new FriendSearchDto();
        friendSearchDto.setStatusCode(StatusCode.BLOCKED);
        Pageable pageable = PageRequest.of(0, 20);
        FriendsRs expectedResponse = FriendsRs.builder().totalElements(0L).totalPages(0).content(new ArrayList<>()).build();
        Mockito.when(accountClient.getAccountById(UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b3a"), UtilsT.token)).thenReturn(UtilsT.accountDto());
        FriendsRs actualResponse = serviceTwo.gettingAllFriendsService(UtilsT.token, friendSearchDto, pageable);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test for service layer arising in method gettingAllFriends")
    @Test
    void gettingAllFriendsFRIEND() throws Exception {
        MapperDTO mapper = new MapImpl();
        FriendServiceTwoImpl serviceTwo = new FriendServiceTwoImpl(mapper, repository, accountClient, friendServiceMock);
        FriendSearchDto friendSearchDto = new FriendSearchDto();
        friendSearchDto.setStatusCode(StatusCode.FRIEND);
        Pageable pageable = PageRequest.of(0, 20);
        FriendsRs expectedResponse = FriendsRs.builder().totalElements(1L).totalPages(1).content(List.of(mapper.convertToFriendDto(UtilsT.accountDto(), StatusCode.FRIEND))).build();
        Mockito.when(accountClient.getAccountById(UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b2a"), UtilsT.token)).thenReturn(UtilsT.accountDto());
        FriendsRs actualResponse = serviceTwo.gettingAllFriendsService(UtilsT.token, friendSearchDto, pageable);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test for service layer arising in method recommendationsService")
    @Test
    void recommendationsService() throws Exception {
        MapperDTO mapper = new MapImpl();
        FriendServiceTwoImpl serviceTwo = new FriendServiceTwoImpl(mapper, repository, accountClient, friendServiceMock);
        Pageable pageable = PageRequest.of(0, 20);
        RecommendationFriendsRs expectedResponse = UtilsT.recommendations();
        Mockito.when(accountClient.getAccountById(UUID.fromString("0bc856ad-b35a-4b19-8969-4cc848fc5198"), UtilsT.token)).thenReturn(UtilsT.accountDto());
        RecommendationFriendsRs actualResponse = serviceTwo.recommendationsService(UtilsT.token, pageable);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test for service layer arising in method gettingFriendByIdService")
    @Test
    void gettingFriendByIdService() {
        MapperDTO mapper = new MapImpl();
        FriendServiceTwoImpl serviceTwo = new FriendServiceTwoImpl(mapper, repository, accountClient, friendServiceMock);
        AccountDto expectedResponse = UtilsT.accountDto();
        Mockito.when(accountClient.getAccountById(UUID.fromString("0bc856ad-b35a-4b19-8969-4cc848fc5198"), UtilsT.token)).thenReturn(UtilsT.accountDto());
        AccountDto actualResponse = serviceTwo.gettingFriendByIdService(UUID.fromString("0bc856ad-b35a-4b19-8969-4cc848fc5198"), UtilsT.token);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

}
