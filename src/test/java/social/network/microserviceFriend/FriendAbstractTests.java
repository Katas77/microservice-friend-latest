package social.network.microserviceFriend;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import social.network.microserviceFriend.clientFeign.ClientFeign;
import social.network.microserviceFriend.kafka.KafkaTemplateFriend;
import social.network.microserviceFriend.model.Friendship;
import social.network.microserviceFriend.model.en.StatusCode;
import social.network.microserviceFriend.repository.FriendshipRepository;
import social.network.microserviceFriend.service.FriendServiceOne;


import java.util.UUID;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class FriendAbstractTests {
    FriendServiceOne friendServiceMock = Mockito.mock(FriendServiceOne.class);
    KafkaTemplateFriend producer = Mockito.mock(KafkaTemplateFriend.class);


    @MockBean
    ClientFeign accountClient;
    @Autowired
    public FriendshipRepository repository;

    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14")
            .withDatabaseName("friend_db")
            .withUsername("friend")
            .withPassword("friend")
            .withInitScript("db.sql");

    @DynamicPropertySource
    public static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    void init() {
        repository.deleteAll();
        Friendship friendship = Friendship.builder()
                .uuid(UUID.randomUUID())
                .accountIdTo(UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b2a"))
                .accountIdFrom(UUID.fromString("4a001ad4-52e8-41d2-8170-c28705c765b5"))
                .statusBetween(StatusCode.FRIEND)
                .build();
        Friendship friendship2 = Friendship.builder()
                .uuid(UUID.randomUUID())
                .accountIdTo(UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b3a"))
                .accountIdFrom(UUID.fromString("4a001ad4-52e8-41d2-8170-c28705c765b5"))
                .statusBetween(StatusCode.SUBSCRIBED)
                .build();
        Friendship friendship3 = Friendship.builder()
                .uuid(UUID.randomUUID())
                .accountIdTo(UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b4a"))
                .accountIdFrom(UUID.fromString("4a001ad4-52e8-41d2-8170-c28705c765b5"))
                .statusBetween(StatusCode.BLOCKED)
                .build();
        Friendship friendship4 = Friendship.builder()
                .uuid(UUID.randomUUID())
                .accountIdTo(UUID.fromString("494e2d92-26bb-4524-aaeb-46308a412b5a"))
                .accountIdFrom(UUID.fromString("4a001ad4-52e8-41d2-8170-c28705c765b5"))
                .statusBetween(StatusCode.REQUEST_FROM)
                .build();
        repository.save(friendship);
        repository.save(friendship2);
        repository.save(friendship3);
        repository.save(friendship4);
        System.out.println(repository.count());
    }
}


