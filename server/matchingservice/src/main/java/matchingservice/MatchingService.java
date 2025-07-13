package matchingservice;

import org.springframework.stereotype.Service;

import model.UserDTO;
import matchingservice.client.GenAiClient;
import matchingservice.client.UserServiceClient;
import matchingservice.entity.Match;
import matchingservice.repository.MatchRepository;
import matchingservice.dto.Candidate;
import matchingservice.client.LocationServiceClient;
import java.util.List;

@Service
public class MatchingService {

    private final GenAiClient genAiClient;
    private final LocationServiceClient locationServiceClient;
    private final UserServiceClient userServiceClient;
    private final MatchRepository matchRepository;

    public MatchingService(GenAiClient genAiClient, UserServiceClient userServiceClient, MatchRepository matchRepository, LocationServiceClient locationServiceClient) {
        this.genAiClient = genAiClient;
        this.userServiceClient = userServiceClient;
        this.matchRepository = matchRepository;
        this.locationServiceClient = locationServiceClient;
    }

    public UserDTO findPartner(String userId) {
        // first check if we already have a recent match
        List<Match> existing = matchRepository.findTop1ByUserIdOrderByCreatedAtDesc(userId);
        if (!existing.isEmpty()) {
            return userServiceClient.getUser(existing.get(0).getMatchedUserId());
        }
        // fetch user profile from user-service
        UserDTO user = userServiceClient.getUser(userId);
        if (user == null) {
            return null;
        }
        String userProfile = user.name();
        // fetch candidate users (simple: all users except current)
        List<UserDTO> allUsers = userServiceClient.getAllUsers();
        List<Candidate> candidates = allUsers.stream()
            .filter(u -> !u.id().equals(userId))
            .map(u -> {
                var loc = locationServiceClient.getLocation(u.id());
                String locStr = loc != null ? loc.latitude()+","+loc.longitude() : "unknown";
                return new Candidate(u.id(), u.name(), locStr);
            })
            .toList();
        List<String> ranked = genAiClient.getRankedIds(userProfile, candidates);
        if (!ranked.isEmpty()) {
            // Persist matches (store up to first 5)
            int limit = Math.min(5, ranked.size());
            for (int i = 0; i < limit; i++) {
                String candidateId = ranked.get(i);
                double score = 1.0 / (i + 1); // simple descending score
                Match m = new Match(userId, candidateId, score, "Ranked by GenAI", "");
                matchRepository.save(m);
            }
            String bestId = ranked.get(0);
            return userServiceClient.getUser(bestId);
        }
        return null;
    }

    /**
     * Retrieve previously stored matches for a user, ordered by best score.
     */
    public List<UserDTO> getMatches(String userId) {
        return matchRepository.findByUserIdOrderByScoreDesc(userId).stream()
                .map(m -> userServiceClient.getUser(m.getMatchedUserId()))
                .toList();
    }
}
