package matchingservice;

import org.springframework.stereotype.Service;

import model.User;
import matchingservice.client.GenAiClient;
import matchingservice.client.UserServiceClient;
import matchingservice.dto.Candidate;
import java.util.List;

@Service
public class MatchingService {

    private final GenAiClient genAiClient;
    private final UserServiceClient userServiceClient;

    public MatchingService(GenAiClient genAiClient, UserServiceClient userServiceClient) {
                this.genAiClient = genAiClient;
        this.userServiceClient = userServiceClient;
    }

    public User findPartner(String userId) {
        // fetch user profile from user-service
        User user = userServiceClient.getUser(userId);
        if (user == null) {
            return new User();
        }
        String userProfile = user.getName();
        // fetch candidate users (simple: all users except current)
        List<User> allUsers = userServiceClient.getAllUsers();
        List<Candidate> candidates = allUsers.stream()
                .filter(u -> !u.getId().equals(userId))
                .map(u -> new Candidate(u.getId(), u.getName()))
                .toList();
        List<String> ranked = genAiClient.getRankedIds(userProfile, candidates);
        if (!ranked.isEmpty()) {
            String bestId = ranked.get(0);
            return userServiceClient.getUser(bestId);
        }
        return new User();
    }
}
