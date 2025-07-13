package matchingservice;

import matchingservice.client.GenAiClient;
import matchingservice.client.LocationServiceClient;
import matchingservice.client.UserServiceClient;
import matchingservice.repository.MatchRepository;
import model.UserDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
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
    @Mock
    private LocationServiceClient locationServiceClient;
    @Mock
    private MatchRepository matchRepository;

    @InjectMocks
    private MatchingService matchingService;

    @Test
    void returnsTopRankedUser() {
        UserDTO requester = new UserDTO("u0", "Alice", List.of("Hiking", "Climbing"));
        UserDTO bob = new UserDTO("u1", "Bob", List.of("Swimming"));
        UserDTO carol = new UserDTO("u2", "Carol", List.of("Hiking"));

        when(userServiceClient.getUser("u0")).thenReturn(requester);
        when(userServiceClient.getAllUsers()).thenReturn(List.of(requester, bob, carol));
        
        String expectedProfile = "Alice who is interested in Hiking, Climbing";
        when(genAiClient.getRankedIds(eq(expectedProfile), anyList())).thenReturn(List.of("u2", "u1"));

        when(userServiceClient.getUser("u2")).thenReturn(carol);

        when(matchRepository.findTop1ByUserIdOrderByCreatedAtDesc("u0")).thenReturn(Collections.emptyList());

        UserDTO partner = matchingService.findPartner("u0");
        assertEquals("u2", partner.id());
    }
}