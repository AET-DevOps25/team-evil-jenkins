package matchingservice;

import matchingservice.client.GenAiClient;
import matchingservice.client.UserServiceClient;
import matchingservice.dto.Candidate;
import model.UserDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MatchingServiceTest {

    @Mock
    private GenAiClient genAiClient;
    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private MatchingService matchingService;

    @Test
    void returnsTopRankedUser() {
        UserDTO requester = new UserDTO("u0", "Alice");
        UserDTO bob = new UserDTO("u1", "Bob");
        UserDTO carol = new UserDTO("u2", "Carol");

        when(userServiceClient.getUser("u0")).thenReturn(requester);
        when(userServiceClient.getAllUsers()).thenReturn(List.of(requester, bob, carol));
        when(genAiClient.getRankedIds(eq("Alice"), anyList())).thenReturn(List.of("u2", "u1"));
        when(userServiceClient.getUser("u2")).thenReturn(carol);

        UserDTO partner = matchingService.findPartner("u0");
        assertEquals("u2", partner.id());
    }
}
