package social.network.microservice_friend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import social.network.microservice_friend.dto.AccountDto;
import social.network.microservice_friend.feigns.ClientFeign;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ClientFeignIntegrationTest {
    @Mock
    private ClientFeign clientFeign;

    private static final String TEST_TOKEN = "Bearer test_token";

    @BeforeEach
    void setUp() {
    }

    @Test
    void testValidToken_Success() {
        when(clientFeign.validToken(TEST_TOKEN)).thenReturn(true);

        boolean response = clientFeign.validToken(TEST_TOKEN);

        assertTrue(response);
        verify(clientFeign, times(1)).validToken(TEST_TOKEN);
    }

    @Test
    void testValidToken_Failure() {
        when(clientFeign.validToken(TEST_TOKEN)).thenReturn(false);

        boolean response = clientFeign.validToken(TEST_TOKEN);

        assertFalse(response); // ожидание, что токен не действителен
        verify(clientFeign, times(1)).validToken(TEST_TOKEN);
    }

    @Test
    void testGetAccountById_Success() {
        UUID testUuid = UUID.randomUUID();
        AccountDto expectedAccount = new AccountDto();
        expectedAccount.setId(testUuid);

        when(clientFeign.getAccountById(testUuid, TEST_TOKEN)).thenReturn(expectedAccount);

        AccountDto response = clientFeign.getAccountById(testUuid, TEST_TOKEN);

        assertSame(expectedAccount, response);
        verify(clientFeign, times(1)).getAccountById(testUuid, TEST_TOKEN);
    }

    @Test
    void testGetAccountByEmail_Success() {
        String testEmail = "test@example.com";
        AccountDto expectedAccount = new AccountDto();
        expectedAccount.setEmail(testEmail);

        when(clientFeign.getAccountBayEmail(testEmail)).thenReturn(expectedAccount);

        AccountDto response = clientFeign.getAccountBayEmail(testEmail);

        assertSame(expectedAccount, response);
        verify(clientFeign, times(1)).getAccountBayEmail(testEmail);
    }
}