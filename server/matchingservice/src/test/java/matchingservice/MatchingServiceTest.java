package matchingservice;

import matchingservice.client.GenAiClient;

import matchingservice.client.UserServiceClient;
import matchingservice.repository.MatchRepository;
import model.UserDTO;
import matchingservice.dto.RankedMatchDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Collections;

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
    private MatchRepository matchRepository;

    @InjectMocks
    private MatchingService matchingService;

    @Test
    void returnsTopRankedUser() {
        UserDTO requester = new UserDTO("u0", "Alice", "", "", "", java.util.Map.of(), List.of("Hiking", "Climbing"));
        UserDTO bob = new UserDTO("u1", "Bob", "", "", "", java.util.Map.of(), List.of("Swimming"));
        UserDTO carol = new UserDTO("u2", "Carol", "", "", "", java.util.Map.of(), List.of("Hiking"));

        when(userServiceClient.getUser("u0")).thenReturn(requester);
        when(userServiceClient.getNearbyUsers(eq("u0"), eq(50.0))).thenReturn(List.of(requester, bob, carol));

        List<RankedMatchDTO> ranked = List.of(
                new RankedMatchDTO("u2", 0.9, "Hiking match", List.of("Hiking")),
                new RankedMatchDTO("u1", 0.3, "Less similar", List.of())
        );
        when(genAiClient.getRankedMatches(eq(new matchingservice.dto.Candidate("u0", "Alice", List.of("Hiking", "Climbing"))), anyList())).thenReturn(ranked);

        when(userServiceClient.getUser("u2")).thenReturn(carol);
        when(userServiceClient.getUser("u1")).thenReturn(bob);

        List<UserDTO> matches = matchingService.findPartners("u0");
        assertEquals(2, matches.size());
        assertEquals("u2", matches.get(0).id());
        assertEquals("u1", matches.get(1).id());
    }
}