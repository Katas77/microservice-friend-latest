package social.network.microservice_friend;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import social.network.microservice_friend.clientFeign.ClientFeign;
import social.network.microservice_friend.controller.FriendController;
import social.network.microservice_friend.dto.FriendSearchDto;
import social.network.microservice_friend.mapper.MapperDTO;
import social.network.microservice_friend.repository.FriendshipRepository;
import social.network.microservice_friend.service.FriendServiceOne;
import social.network.microservice_friend.service.FriendServiceTwo;
import social.network.microservice_friend.service.impl.FriendServiceTwoImpl;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FriendControllerTest {
    FriendServiceOne friendServiceMock = Mockito.mock(FriendServiceOne.class);
    FriendServiceTwo friendService2Mock = Mockito.mock(FriendServiceTwo.class);
    @MockBean
    MapperDTO mapper;
    @MockBean
    FriendshipRepository repositoryMock;
    @MockBean
    ClientFeign accountClient;
    MockMvc mockMvc;
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14");

    @BeforeAll
    public static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    public static void afterAll() {
        postgres.stop();
    }


    private final FriendServiceTwoImpl serviceTwo = new FriendServiceTwoImpl(mapper, repositoryMock, accountClient, friendServiceMock);

    @DynamicPropertySource
    public static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }


    @BeforeEach
    void setup() {
        this.mockMvc = standaloneSetup(new FriendController(friendServiceMock, friendService2Mock)).setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).build();
    }


    @DisplayName("Test blockFriendIds")
    @Test
    public void blockTest() throws Exception {
        Mockito.when(friendServiceMock.blockFriendId(TestUtils.token)).thenReturn(TestUtils.blockTest());
        String expectedResponse = TestUtils.readStringFromResource("response/blockFriendIds.json");
        RequestBuilder builder = MockMvcRequestBuilders.get("/api/v1/friends/blockFriendId").accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", TestUtils.token);
        String actualResponse = mockMvc.perform(builder).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        MvcResult result = mockMvc.perform(builder).andReturn();
        log.info(" BlockFriendIds  HTTP response status: {}", result.getResponse().getStatus());
        Mockito.verify(friendServiceMock, Mockito.times(2)).blockFriendId(TestUtils.token);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test account_by_id")
    @Test
    public void account_by_id() throws Exception {
        Mockito.when(friendService2Mock.gettingFriendByIdService(UUID.fromString("b3999ffa-2df9-469e-9793-ee65e214846e"), TestUtils.token)).thenReturn(TestUtils.account_by_id());
        String expectedResponse = TestUtils.readStringFromResource("response/account_by_id.json");
        RequestBuilder builder = MockMvcRequestBuilders.get("/api/v1/friends/{accountId}", "b3999ffa-2df9-469e-9793-ee65e214846e").accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", TestUtils.token);
        String actualResponse = mockMvc.perform(builder).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        MvcResult result = mockMvc.perform(builder).andReturn();
        log.info(" Account_by_id HTTP response status: {}", result.getResponse().getStatus());
        Mockito.verify(friendService2Mock, Mockito.times(2)).gettingFriendByIdService(UUID.fromString("b3999ffa-2df9-469e-9793-ee65e214846e"), TestUtils.token);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("friendRequestCounter")
    @Test
    public void friendRequestCounter() throws Exception {
        Mockito.when(friendServiceMock.friendRequestCounter(TestUtils.token)).thenReturn(3);
        Integer expectedResponse = 3;
        RequestBuilder builder = MockMvcRequestBuilders.get("/api/v1/friends/count", "b3999ffa-2df9-469e-9793-ee65e214846e").accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", TestUtils.token);
        Integer actualResponse = Integer.valueOf(mockMvc.perform(builder).andExpect(status().isOk()).andReturn().getResponse().getContentAsString());
        Mockito.verify(friendServiceMock, Mockito.times(1)).friendRequestCounter(TestUtils.token);
        assertEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test approve")
    @Test
    public void approve() throws Exception {
        Mockito.when(friendServiceMock.approveService(UUID.fromString("b3999ffa-2df9-469e-9793-ee65e214846e"), TestUtils.token)).thenReturn(TestUtils.messageApprove());
        String expectedResponse = TestUtils.readStringFromResource("response/approve.json");
        RequestBuilder builder = MockMvcRequestBuilders.put("/api/v1/friends/{uuid}/approve", "b3999ffa-2df9-469e-9793-ee65e214846e").accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", TestUtils.token);
        var actualResponse = mockMvc.perform(builder).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        MvcResult result = mockMvc.perform(builder).andReturn();
        log.info(" Approve HTTP response status: {}", result.getResponse().getStatus());
        Mockito.verify(friendServiceMock, Mockito.times(2)).approveService(UUID.fromString("b3999ffa-2df9-469e-9793-ee65e214846e"), TestUtils.token);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test block ")
    @Test
    public void block() throws Exception {
        Mockito.when(friendServiceMock.block(UUID.fromString("b3999ffa-2df9-469e-9793-ee65e214846e"), TestUtils.token)).thenReturn(TestUtils.messageBlock());
        String expectedResponse = TestUtils.readStringFromResource("response/block.json");
        RequestBuilder builder = MockMvcRequestBuilders.put("/api/v1/friends/block/{uuid}", "b3999ffa-2df9-469e-9793-ee65e214846e").accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", TestUtils.token);
        var actualResponse = mockMvc.perform(builder).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        MvcResult result = mockMvc.perform(builder).andReturn();
        log.info(" Block  HTTP response status: {}", result.getResponse().getStatus());
        Mockito.verify(friendServiceMock, Mockito.times(2)).block(UUID.fromString("b3999ffa-2df9-469e-9793-ee65e214846e"), TestUtils.token);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test  dell ")
    @Test
    public void dell() throws Exception {
        Mockito.when(friendServiceMock.dell(UUID.fromString("b3999ffa-2df9-469e-9793-ee65e214846e"), TestUtils.token)).thenReturn(TestUtils.messageDell());
        String expectedResponse = TestUtils.readStringFromResource("response/dell.json");
        RequestBuilder builder = MockMvcRequestBuilders.delete("/api/v1/friends/{uuid}", "b3999ffa-2df9-469e-9793-ee65e214846e").accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", TestUtils.token);
        var actualResponse = mockMvc.perform(builder).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        MvcResult result = mockMvc.perform(builder).andReturn();
        log.info("  Dell  HTTP response status: {}", result.getResponse().getStatus());
        Mockito.verify(friendServiceMock, Mockito.times(2)).dell(UUID.fromString("b3999ffa-2df9-469e-9793-ee65e214846e"), TestUtils.token);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test friendIds")
    @Test
    public void friendIds() throws Exception {
        Mockito.when(friendServiceMock.friendIds(TestUtils.token)).thenReturn(TestUtils.friendIds());
        String expectedResponse = TestUtils.readStringFromResource("response/friendIds.json");
        RequestBuilder builder = MockMvcRequestBuilders.get("/api/v1/friends/friendId").accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", TestUtils.token);
        String actualResponse = mockMvc.perform(builder).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        MvcResult result = mockMvc.perform(builder).andReturn();
        log.info("FriendIds HTTP response status: {}", result.getResponse().getStatus());

        Mockito.verify(friendServiceMock, Mockito.times(2)).friendIds(TestUtils.token);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test request")
    @Test
    public void request() throws Exception {
        Mockito.when(friendServiceMock.request(UUID.fromString("b3999ffa-2df9-469e-9793-ee65e214846e"), TestUtils.token)).thenReturn(TestUtils.messageRequest());
        String expectedResponse = TestUtils.readStringFromResource("response/request.json");
        RequestBuilder builder = MockMvcRequestBuilders.post("/api/v1/friends/{uuid}/request", "b3999ffa-2df9-469e-9793-ee65e214846e").accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", TestUtils.token);
        var actualResponse = mockMvc.perform(builder).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        MvcResult result = mockMvc.perform(builder).andReturn();
        log.info(" Request HTTP response status: {}", result.getResponse().getStatus());
        Mockito.verify(friendServiceMock, Mockito.times(2)).request(UUID.fromString("b3999ffa-2df9-469e-9793-ee65e214846e"), TestUtils.token);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test subscribe")
    @Test
    public void subscribe() throws Exception {
        Mockito.when(friendServiceMock.subscribe(UUID.fromString("b3999ffa-2df9-469e-9793-ee65e214846e"), TestUtils.token)).thenReturn(TestUtils.messageSubscribe());
        String expectedResponse = TestUtils.readStringFromResource("response/subscribe.json");
        RequestBuilder builder = MockMvcRequestBuilders.post("/api/v1/friends/subscribe/{uuid}", "b3999ffa-2df9-469e-9793-ee65e214846e").accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", TestUtils.token);
        var actualResponse = mockMvc.perform(builder).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        MvcResult result = mockMvc.perform(builder).andReturn();
        log.info(" Subscribe HTTP response status: {}", result.getResponse().getStatus());
        Mockito.verify(friendServiceMock, Mockito.times(2)).subscribe(UUID.fromString("b3999ffa-2df9-469e-9793-ee65e214846e"), TestUtils.token);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test recommendations")
    @Test
    public void recommendations() throws Exception {
        Pageable pageable = PageRequest.of(0, 20);
        Mockito.when(friendService2Mock.recommendationsService(TestUtils.token, pageable)).thenReturn(TestUtils.recommendations());
        String expectedResponse = TestUtils.readStringFromResource("response/recommendations.json");
        RequestBuilder builder = MockMvcRequestBuilders.get("/api/v1/friends/recommendations")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", TestUtils.token);
        var actualResponse = mockMvc.perform(builder).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        MvcResult result = mockMvc.perform(builder).andReturn();
        log.info(" recommendations HTTP response status: {}", result.getResponse().getStatus());
        Mockito.verify(friendService2Mock, Mockito.times(2)).recommendationsService(TestUtils.token, pageable);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test  gettingAllFriends")
    @Test
    public void gettingAllFriends() throws Exception {
        FriendSearchDto friendSearchDto = new FriendSearchDto();
        Pageable pageable = PageRequest.of(0, 20);
        Mockito.when(friendService2Mock.gettingAllFriendsService(TestUtils.token, friendSearchDto, pageable)).thenReturn(TestUtils.gettingAllFriends());
        String expectedResponse = TestUtils.readStringFromResource("response/gettingAllFriends.json");
        RequestBuilder builder = MockMvcRequestBuilders.get("/api/v1/friends")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization", TestUtils.token);
        var actualResponse = mockMvc.perform(builder).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        MvcResult result = mockMvc.perform(builder).andReturn();
        log.info(" gettingAllFriends HTTP response status: {}", result.getResponse().getStatus());
        Mockito.verify(friendService2Mock, Mockito.times(2)).gettingAllFriendsService(TestUtils.token, friendSearchDto, pageable);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test for uuidFrom method")
    @Test
    public void uuidFrom() throws Exception {
        String expect = "4a001ad4-52e8-41d2-8170-c28705c765b5";
        String actual = String.valueOf(serviceTwo.uuidFrom(TestUtils.token));
        assertEquals(expect, actual);
    }

    @DisplayName("Test for validBirthDate method")
    @Test
    public void validBirthDate() {
        Boolean expect = true;
        Boolean actual = serviceTwo.validBirthDate(LocalDate.of(2014, 10, 8), LocalDate.of(2012, 11, 1), LocalDate.of(2024, 8, 1));
        assertEquals(expect, actual);
    }

    @DisplayName("Test for validAge method")
    @Test
    public void validAge() {
        Boolean expect = false;
        Boolean actual = serviceTwo.validAge(LocalDate.of(2014, 10, 8), 11, 24);
        assertEquals(expect, actual);
    }

}
 /* .param("offset", String.valueOf(pageable.getOffset()))   // <-- no space after comma!!!
        .param("sort", String.valueOf(pageable.getSort()))
        .param("pageSize", String.valueOf(pageable.getPageSize()))
        .param("pageNumber", String.valueOf(pageable.getPageNumber()))*/