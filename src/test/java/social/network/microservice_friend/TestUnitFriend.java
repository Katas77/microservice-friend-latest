package social.network.microservice_friend;

import lombok.RequiredArgsConstructor;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import social.network.microservice_friend.clientFeign.ClientFeign;
import social.network.microservice_friend.dto.FriendDto;
import social.network.microservice_friend.dto.RecommendationFriendsDto;
import social.network.microservice_friend.mapper.MapperDTO;
import social.network.microservice_friend.mapper.impl.MapImpl;
import social.network.microservice_friend.model.en.StatusCode;
import social.network.microservice_friend.repository.FriendshipRepository;
import social.network.microservice_friend.service.FriendServiceOne;
import social.network.microservice_friend.service.impl.FriendServiceTwoImpl;
import social.network.microservice_friend.test_utils.UtilsTests;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RequiredArgsConstructor
public class TestUnitFriend {
    FriendServiceOne friendServiceMock = Mockito.mock(FriendServiceOne.class);

    MapperDTO mapper = new MapImpl();
    @MockBean
    FriendshipRepository repositoryMock;
    @MockBean
    ClientFeign accountClient;
    private final FriendServiceTwoImpl serviceTwo = new FriendServiceTwoImpl(mapper, repositoryMock, accountClient, friendServiceMock);


    @DisplayName("Test for uuidFrom method")
    @Test
    public void uuidFrom() throws Exception {
        String expect = "4a001ad4-52e8-41d2-8170-c28705c765b5";
        String actual = String.valueOf(serviceTwo.uuidFrom(UtilsTests.token));
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

    @DisplayName("Test for accountsListToRecommends method")
    @Test
    public void accountsListToRecommends() {
        List<RecommendationFriendsDto> expect = UtilsTests.accountsListToRecommends();
        List<RecommendationFriendsDto> actual = mapper.accountsListToRecommends(UtilsTests.accountDtoList());
        JsonAssert.assertJsonEquals(expect, actual);

    }

    @DisplayName("Test for accountsListToFriendDtoList method")
    @Test
    public void accountsListToFriendDtoList() {
        List<FriendDto> expect = UtilsTests.accountsListToFriendDtoList();
        List<FriendDto> actual = mapper.accountsListToFriendDtoList(UtilsTests.accountDtoList(), StatusCode.REQUEST_FROM);
        JsonAssert.assertJsonEquals(expect, actual);
    }

    @DisplayName("Test for convertToRecommend method")
    @Test
    public void convertToRecommend() {
        RecommendationFriendsDto expect = UtilsTests.RecommendationFriendsDto();
        RecommendationFriendsDto actual = mapper.convertToRecommend(UtilsTests.accountDto());
        JsonAssert.assertJsonEquals(expect, actual);
    }

    @DisplayName("Test for convertToFriendDto method")
    @Test
    public void convertToFriendDto() {
        FriendDto expect = UtilsTests.friendDto();
        FriendDto actual = mapper.convertToFriendDto(UtilsTests.accountDto(), StatusCode.REQUEST_FROM);
        JsonAssert.assertJsonEquals(expect, actual);
    }


}
