package social.network.microservice_friend;

import lombok.RequiredArgsConstructor;
import net.javacrumbs.jsonunit.JsonAssert;
import org.aspectj.lang.JoinPoint;
import org.hibernate.NonUniqueResultException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import social.network.microservice_friend.aop.LogAspect;
import social.network.microservice_friend.clientFeign.ClientFeign;
import social.network.microservice_friend.configuration.config_security.RequestHeaderAuthenticationProvider;
import social.network.microservice_friend.dto.*;
import social.network.microservice_friend.dto.respons_friend.FriendsRs;
import social.network.microservice_friend.exception.BusinessLogicException;
import social.network.microservice_friend.exception.FriendExceptionHandler;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
class TestUnitFriend {
    FriendServiceOne friendServiceMock = Mockito.mock(FriendServiceOne.class);

    MapperDTO mapper = new MapImpl();

    FriendshipRepository repositoryMock = Mockito.mock(FriendshipRepository.class);
    KafkaTemplateFriend producer = Mockito.mock(KafkaTemplateFriend.class);
    FriendServiceTwoImpl serviceTwoMock = Mockito.mock(FriendServiceTwoImpl.class);
    ClientFeign accountClient = Mockito.mock(ClientFeign.class);
    private final FriendServiceTwoImpl serviceTwo = new FriendServiceTwoImpl(mapper, repositoryMock, accountClient, friendServiceMock);
    private final FriendServiceOneImpl friendServiceOne = new FriendServiceOneImpl(repositoryMock, producer);


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
        actual = serviceTwo.validBirthDate(null, LocalDate.of(2012, 11, 1), LocalDate.of(2024, 8, 1));
        assertEquals(expect, actual);
    }

    @DisplayName("Test for validAge method")
    @Test
    void validAge() {
        boolean expect = false;
        boolean actual = serviceTwo.validAge(LocalDate.of(2014, 10, 8), 22, 24);
        assertEquals(expect, actual);
        actual = serviceTwo.validAge(null, 11, 24);
        assertTrue(actual);
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
        FriendsRs actual = serviceTwo.statusCodeNull(UtilsT.token, pageable);
        JsonAssert.assertJsonEquals(expect, actual);

    }

    @DisplayName("Test for approveService method")
    @Test
    void approveService() throws ParseException {
        Mockito.when(repositoryMock.findToAndFrom(UUID.fromString("fa6f76bb-6be7-493e-bbdc-caf03cb7eb6a"), UUID.fromString("4a001ad4-52e8-41d2-8170-c28705c765b5"))).thenReturn(Optional.empty());
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
    @DisplayName("Test for search1 method")
    @Test
    void search1Null()  {
        FriendSearchDto friendSearchDto=new FriendSearchDto();
        friendSearchDto.setIds(null);
        List<AccountDto> dtoList= serviceTwo.search1(friendSearchDto,UtilsT.token);
        int result=dtoList.size();
        assertEquals(0, result);
    }
    @DisplayName("Test for search1 method")
    @Test
    void gettingAllFriendsService() throws ParseException {
        FriendSearchDto friendSearchDto = new FriendSearchDto();
        friendSearchDto.setStatusCode(null);
        Pageable pageable = PageRequest.of(0, 20);
        Mockito.when(serviceTwoMock.gettingAllFriendsService(UtilsT.token,friendSearchDto,pageable)).thenReturn(FriendsRs.builder().content(new ArrayList<>()).build());
        FriendsRs friendsRs= serviceTwoMock.gettingAllFriendsService(UtilsT.token,friendSearchDto,pageable);
        int result=friendsRs.getContent().size();
        assertEquals(0, result);
    }

    @DisplayName("Test for block method")
    @Test
    void block() throws ParseException {
        Mockito.when(repositoryMock.findToAndFrom(UUID.fromString("fa6f76bb-6be7-493e-bbdc-caf03cb7eb6a"), UUID.fromString("4a001ad4-52e8-41d2-8170-c28705c765b5"))).thenReturn(Optional.empty());
        String expected = "Friend with ID 4a001ad4-52e8-41d2-8170-c28705c765b5 is blocked";
        Message actual = friendServiceOne.block(UUID.fromString("4a001ad4-52e8-41d2-8170-c28705c765b5"), UtilsT.token);
        Assertions.assertEquals(expected, actual.getReport());

    }

    @DisplayName("Test for request method")
    @Test
    void requestEmpty() throws ParseException {
        Mockito.when(repositoryMock.findToAndFrom(UUID.fromString("fa6f76bb-6be7-493e-bbdc-caf03cb7eb6a"), UUID.fromString("4a001ad4-52e8-41d2-8170-c28705c765b5"))).thenReturn(Optional.empty());
       String expected = "Friendship with uuidTo 4a001ad4-52e8-41d2-8170-c28705c765b5 REQUEST_FROM";
      Message  actual = friendServiceOne.request(UUID.fromString("4a001ad4-52e8-41d2-8170-c28705c765b5"), UtilsT.token);
        Assertions.assertEquals(expected, actual.getReport());

    }
    @DisplayName("Test for request method")
    @Test
    void request() throws ParseException {
        String expected = "Friendship with uuidTo fa6f76bb-6be7-493e-bbdc-caf03cb7eb6a REQUEST_FROM";
        Friendship friendshipNew = Friendship.builder()
                .account_id_to(UUID.fromString("fa6f76bb-6be7-493e-bbdc-caf03cb7eb6a"))
                .account_id_from(UUID.fromString("4a001ad4-52e8-41d2-8170-c28705c765b5"))
                .statusBetween(StatusCode.REQUEST_FROM)
                .uuid(UUID.randomUUID())
                .build();
        Mockito.when(repositoryMock.findToAndFrom(UUID.fromString("fa6f76bb-6be7-493e-bbdc-caf03cb7eb6a"), UUID.fromString("4a001ad4-52e8-41d2-8170-c28705c765b5"))).thenReturn(Optional.of(friendshipNew));
        Message  actual = friendServiceOne.request(UUID.fromString("fa6f76bb-6be7-493e-bbdc-caf03cb7eb6a"), UtilsT.token);
        Assertions.assertEquals(expected, actual.getReport());

    }

    @DisplayName("Test for subscribe method")
    @Test
    void subscribe() throws ParseException {
        Mockito.when(repositoryMock.findToAndFrom(UUID.fromString("fa6f76bb-6be7-493e-bbdc-caf03cb7eb6a"), UUID.fromString("4a001ad4-52e8-41d2-8170-c28705c765b5"))).thenReturn(Optional.empty());
        String expected = "Friendship with uuidTo 4a001ad4-52e8-41d2-8170-c28705c765b5 SUBSCRIBED";
        Message actual = friendServiceOne.subscribe(UUID.fromString("4a001ad4-52e8-41d2-8170-c28705c765b5"), UtilsT.token);
        Assertions.assertEquals(expected, actual.getReport());

    }

    @DisplayName("Test for readStringFrom")
    @Test
    void readStringFromResource() {
        Message expectedResponse = Message.builder().report("Friendship with uuidTo b3999ffa-2df9-469e-9793-ee65e214846e is approve").build();
        String actualResponse = UtilsT.readStringFromResource("response/approve.json");
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test for LogAspect")
    @Test
    void LogAspect() {
        LogAspect logAspect = Mockito.mock(LogAspect.class);
        JoinPoint joinPoint = Mockito.mock(JoinPoint.class);
        logAspect.methodName(joinPoint);
        Mockito.verify(logAspect, Mockito.times(1)).methodName(joinPoint);
        verify(logAspect).methodName(joinPoint);
    }

    @DisplayName("Test for RequestHeaderAuthenticationProvider")
    @Test
    void RequestHeaderAuthenticationProvider() {
        String mes = "";
        RequestHeaderAuthenticationProvider provider = new RequestHeaderAuthenticationProvider(accountClient);
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(UtilsT.token);
        try {
            provider.authenticate(authentication);
        } catch (BadCredentialsException e) {
            mes = e.getMessage();
        }
        Assertions.assertEquals("Bad Request invalid or missing token", mes);
    }

    @DisplayName("Test for FriendExceptionHandler")
    @Test
    void FriendExceptionHandler() {
        FriendExceptionHandler friend = new FriendExceptionHandler();
        BusinessLogicException e = new BusinessLogicException("BusinessLogicException is    calling");
        String actual = friend.handleException(e).getBody().getReport();
        Assertions.assertEquals("BusinessLogicException is    calling", actual);
    }

    @DisplayName("Test for FriendExceptionHandler")
    @Test
    void handleExceptionNull() {
        FriendExceptionHandler friend = new FriendExceptionHandler();
        NullPointerException e = new NullPointerException("NullPointerException is    calling");
        String actual = friend.handleExceptionNull(e).getBody().getReport();
        Assertions.assertEquals("NullPointerException is    calling", actual);
    }

    @DisplayName("Test for IllegalArgumentException")
    @Test
    void IllegalArgumentExceptionF() {
        FriendExceptionHandler friend = new FriendExceptionHandler();
        IllegalArgumentException e = new IllegalArgumentException("IllegalArgumentException is    calling");
        String actual = friend.handleExceptionIllegalA(e).getBody().getReport();
        Assertions.assertEquals("IllegalArgumentException is    calling", actual);
    }

    @DisplayName("Test for FriendExceptionHandler")
    @Test
    void FriendHttpRequestMethodNotSupportedException() {
        FriendExceptionHandler friend = new FriendExceptionHandler();
        HttpRequestMethodNotSupportedException e = new HttpRequestMethodNotSupportedException("HttpRequestMethodNotSupportedException is    calling");
        String actual = friend.handleException(e).getBody().getReport();
        Assertions.assertEquals("Request method 'HttpRequestMethodNotSupportedException is    calling' is not supported", actual);
    }



    @DisplayName("Test for FriendMissingServletRequestParameterException")
    @Test
    void FriendMissingServletRequestParameterException() {
        FriendExceptionHandler friend = new FriendExceptionHandler();
        MissingServletRequestParameterException e = new MissingServletRequestParameterException(null, null);
        String actual = friend.handleExceptionServlet(e).getBody().getReport();
        Assertions.assertEquals("Required request parameter 'null' for method parameter type null is not present", actual);
    }

    @DisplayName("Test for FriendMissingServletRequestParameterException")
    @Test
    void FriendNonUniqueResultException() {
        FriendExceptionHandler friend = new FriendExceptionHandler();
        NonUniqueResultException e = new NonUniqueResultException(1);
        String actual = friend.handleExceptionServlet(e).getBody().getReport();
        Assertions.assertEquals("Query did not return a unique result: 1 results were returned", actual);
    }

}
