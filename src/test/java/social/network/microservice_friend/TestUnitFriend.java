package social.network.microservice_friend;

import lombok.RequiredArgsConstructor;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import social.network.microservice_friend.clientFeign.ClientFeign;
import social.network.microservice_friend.dto.FriendDto;
import social.network.microservice_friend.dto.Message;
import social.network.microservice_friend.dto.RecommendationFriendsDto;
import social.network.microservice_friend.dto.respons_friend.FriendsRs;
import social.network.microservice_friend.exception.BusinessLogicException;
import social.network.microservice_friend.kafka.KafkaTemplateFriend;
import social.network.microservice_friend.mapper.MapperDTO;
import social.network.microservice_friend.mapper.impl.MapImpl;
import social.network.microservice_friend.model.Friendship;
import social.network.microservice_friend.model.en.StatusCode;
import social.network.microservice_friend.repository.FriendshipRepository;
import social.network.microservice_friend.service.FriendServiceOne;
import social.network.microservice_friend.service.impl.FriendServiceOneImpl;
import social.network.microservice_friend.service.impl.FriendServiceTwoImpl;
import social.network.microservice_friend.test_utils.UtilsT;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


import static org.junit.jupiter.api.Assertions.assertEquals;

@RequiredArgsConstructor
 class TestUnitFriend {
    FriendServiceOne friendServiceMock = Mockito.mock(FriendServiceOne.class);

    MapperDTO mapper = new MapImpl();

    FriendshipRepository repositoryMock= Mockito.mock(FriendshipRepository .class);
    KafkaTemplateFriend producer = Mockito.mock(KafkaTemplateFriend.class);

    ClientFeign accountClient= Mockito.mock(ClientFeign.class);
    private final FriendServiceTwoImpl serviceTwo = new FriendServiceTwoImpl(mapper, repositoryMock, accountClient, friendServiceMock);
    private final     FriendServiceOneImpl friendServiceOne=new FriendServiceOneImpl(repositoryMock,producer);



    @DisplayName("Test for uuidFrom method")
    @Test
    void uuidFrom() throws Exception {
        String expect = "4a001ad4-52e8-41d2-8170-c28705c765b5";
        String actual = String.valueOf(serviceTwo.uuidFrom(UtilsT.token));
        assertEquals(expect, actual);
    }

    @DisplayName("Test for validBirthDate method")
    @Test
     void validBirthDate() {
        boolean expect = true;
        boolean actual = serviceTwo.validBirthDate(LocalDate.of(2014, 10, 8), LocalDate.of(2012, 11, 1), LocalDate.of(2024, 8, 1));
        assertEquals(expect, actual);
        actual=serviceTwo.validBirthDate(null, LocalDate.of(2012, 11, 1), LocalDate.of(2024, 8, 1));
        assertEquals(expect, actual);
    }

    @DisplayName("Test for validAge method")
    @Test
     void validAge() {
        boolean expect = false;
        boolean actual = serviceTwo.validAge(LocalDate.of(2014, 10, 8), 11, 24);
        assertEquals(expect, actual);
        actual=serviceTwo.validAge(null,11, 24);
        assertEquals(true, actual);
    }

    @DisplayName("Test for accountsListToRecommends method")
    @Test
    void accountsListToRecommends() {
        List<RecommendationFriendsDto> expect = UtilsT.accountsListToRecommends();
        List<RecommendationFriendsDto> actual = mapper.accountsListToRecommends(UtilsT.accountDtoList());
        JsonAssert.assertJsonEquals(expect, actual);

    }

    @DisplayName("Test for accountsListToFriendDtoList method")
    @Test
     void accountsListToFriendDtoList() {
        List<FriendDto> expect = UtilsT.accountsListToFriendDtoList();
        List<FriendDto> actual = mapper.accountsListToFriendDtoList(UtilsT.accountDtoList(), StatusCode.REQUEST_FROM);
        JsonAssert.assertJsonEquals(expect, actual);
    }

    @DisplayName("Test for convertToRecommend method")
    @Test
   void convertToRecommend() {
        RecommendationFriendsDto expect = UtilsT.RecommendationFriendsDto();
        RecommendationFriendsDto actual = mapper.convertToRecommend(UtilsT.accountDto());
        JsonAssert.assertJsonEquals(expect, actual);
    }

    @DisplayName("Test for convertToFriendDto method")
    @Test
     void convertToFriendDto() {
        FriendDto expect = UtilsT.friendDto();
        FriendDto actual = mapper.convertToFriendDto(UtilsT.accountDto(), StatusCode.REQUEST_FROM);
        JsonAssert.assertJsonEquals(expect, actual);
    }
    @DisplayName("Test for statusCodeNull method")
    @Test
    void statusCodeNull() throws ParseException {
        Pageable pageable = PageRequest.of(0, 20);
        FriendsRs expect = UtilsT.statusCodeNull();
        Mockito.when(repositoryMock.findIdStatusREQUEST_FROM(serviceTwo.uuidFrom(UtilsT.token))).thenReturn(UtilsT.friendIds());
        Mockito.when(accountClient.getAccountById(UUID.fromString("fa6f76bb-6be7-493e-bbdc-caf03cb7eb6a"), UtilsT.token)).thenReturn(UtilsT.account_by_id());
        Mockito.when(accountClient.getAccountById(UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b2a"), UtilsT.token)).thenReturn(UtilsT.account_by_id());
        FriendsRs actual = serviceTwo.statusCodeNull(UtilsT.token,pageable);
        System.out.println(expect);
        System.out.println(actual);
        JsonAssert.assertJsonEquals(expect, actual);

    }

    @DisplayName("Test for search1 method")
    @Test
    void search1() throws ParseException {
        Mockito.when(repositoryMock.findToAndFrom(UUID.fromString("fa6f76bb-6be7-493e-bbdc-caf03cb7eb6a"),UUID.fromString("4a001ad4-52e8-41d2-8170-c28705c765b5"))).thenReturn(Optional.empty());
        String mes = "";
        try {
            friendServiceOne.approveService(UUID.fromString("4a001ad4-52e8-41d2-8170-c28705c765b5"), UtilsT.token);
        } catch (BusinessLogicException e) {
            mes = e.getMessage();
        }

        Assertions.assertEquals("You can't add yourself as a friend", mes);

        try {
            friendServiceOne.approveService(UUID.fromString("4a001ad4-52e8-41d2-8170-c28705c765b5"), UtilsT.token);
        } catch (BusinessLogicException e) {
            mes = e.getMessage();
        }

        Assertions.assertEquals("You can't add yourself as a friend", mes);
        try {
            friendServiceOne.approveService(UUID.fromString("fa6f76bb-6be7-493e-bbdc-caf03cb7eb6a"), UtilsT.token);
        } catch (BusinessLogicException e) {
            mes = e.getMessage();
        }
        Assertions.assertEquals("Friendship with uuidTofa6f76bb-6be7-493e-bbdc-caf03cb7eb6a is NOT_FOUND", mes);
    }
    @DisplayName("Test for block method")
    @Test
    void block() throws ParseException {
        Mockito.when(repositoryMock.findToAndFrom(UUID.fromString("fa6f76bb-6be7-493e-bbdc-caf03cb7eb6a"),UUID.fromString("4a001ad4-52e8-41d2-8170-c28705c765b5"))).thenReturn(Optional.empty());
        String  expected = "Friend with ID 4a001ad4-52e8-41d2-8170-c28705c765b5 is blocked";
        Message actual= friendServiceOne.block(UUID.fromString("4a001ad4-52e8-41d2-8170-c28705c765b5"), UtilsT.token);
        Assertions.assertEquals(expected, actual.getReport());

    }

    @DisplayName("Test for request method")
    @Test
    void request() throws ParseException {
        Mockito.when(repositoryMock.findToAndFrom(UUID.fromString("fa6f76bb-6be7-493e-bbdc-caf03cb7eb6a"),UUID.fromString("5a001ad4-52e8-41d2-8170-c28705c765b5"))).thenReturn(Optional.empty());
        String  expected = "Friendship with uuidTo 5a001ad4-52e8-41d2-8170-c28705c765b5 REQUEST_FROM";
        Message actual= friendServiceOne.request(UUID.fromString("5a001ad4-52e8-41d2-8170-c28705c765b5"), UtilsT.token);
        Assertions.assertEquals(expected, actual.getReport());

        Friendship friendshipNew = Friendship.builder()
                .account_id_to(UUID.fromString("fa6f76bb-6be7-493e-bbdc-caf03cb7eb6a"))
                .account_id_from(UUID.fromString("4a001ad4-52e8-41d2-8170-c28705c765b5"))
                .statusBetween(StatusCode.REQUEST_FROM)
                .uuid(UUID.randomUUID())
                .build();

        Mockito.when(repositoryMock.findToAndFrom(UUID.fromString("fa6f76bb-6be7-493e-bbdc-caf03cb7eb6a"),UUID.fromString("4a001ad4-52e8-41d2-8170-c28705c765b5"))).thenReturn(Optional.of(friendshipNew));
      expected = "Friendship with uuidTo 4a001ad4-52e8-41d2-8170-c28705c765b5 REQUEST_FROM";
       actual= friendServiceOne.request(UUID.fromString("4a001ad4-52e8-41d2-8170-c28705c765b5"), UtilsT.token);
        Assertions.assertEquals(expected, actual.getReport());

    }
    @DisplayName("Test for subscribe method")
    @Test
    void   subscribe() throws ParseException {
        Mockito.when(repositoryMock.findToAndFrom(UUID.fromString("fa6f76bb-6be7-493e-bbdc-caf03cb7eb6a"),UUID.fromString("4a001ad4-52e8-41d2-8170-c28705c765b5"))).thenReturn(Optional.empty());
        String  expected = "Friendship with uuidTo 4a001ad4-52e8-41d2-8170-c28705c765b5 SUBSCRIBED";
        Message actual= friendServiceOne.  subscribe(UUID.fromString("4a001ad4-52e8-41d2-8170-c28705c765b5"), UtilsT.token);
        Assertions.assertEquals(expected, actual.getReport());

    }
    @DisplayName("Test for readStringFrom")
    @Test
    void   readStringFromResource(){
        Message expectedResponse=Message.builder().report("Friendship with uuidTo b3999ffa-2df9-469e-9793-ee65e214846e is approve").build();
        String  actualResponse= UtilsT.readStringFromResource("response/approve.json");
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);

    }


}
