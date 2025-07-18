package matchingservice;

import org.springframework.stereotype.Service;

import model.UserDTO;
import model.MatcherDTO;
import matchingservice.dto.RankedMatchDTO;
import matchingservice.client.GenAiClient;
import matchingservice.client.UserServiceClient;
import matchingservice.entity.Match;
import matchingservice.repository.MatchRepository;
import matchingservice.dto.Candidate;

import java.util.List;
import java.util.Collections;

@Service
public class MatchingService {

    private final GenAiClient genAiClient;
    
    private final UserServiceClient userServiceClient;
    private final MatchRepository matchRepository;

    public MatchingService(GenAiClient genAiClient, UserServiceClient userServiceClient, MatchRepository matchRepository) {
        this.genAiClient = genAiClient;
        this.userServiceClient = userServiceClient;
        this.matchRepository = matchRepository;
        
    }

    public List<UserDTO> findPartners(String userId) {
        
        // fetch user profile from user-service
        UserDTO user = userServiceClient.getUser(userId);
        if (user == null) {
            return null;
        }
        Candidate userCandidate = new Candidate(user.id(), user.name(), user.sportInterests());
        // fetch candidate users (nearby)
        List<UserDTO> allUsers = userServiceClient.getNearbyUsers(userId, 50.0); // 50km radius
        // Ensure active user is included in candidates
        boolean hasActiveUser = allUsers.stream().anyMatch(u -> u.id().equals(userId));
        if (!hasActiveUser) {
            allUsers = new java.util.ArrayList<>(allUsers);
            allUsers.add(user);
        }
        List<Candidate> candidates = allUsers.stream()
            .filter(u -> !u.id().equals(userId)) // exclude the main user from candidates
            .map(u -> new Candidate(u.id(), u.name(), u.sportInterests()))
            .toList();
        List<RankedMatchDTO> ranked = genAiClient.getRankedMatches(userCandidate, candidates);
        if (ranked == null || ranked.isEmpty()) {
            System.err.println("WARNING: GenAI returned null or empty ranked match list. Returning no matches.");
            return Collections.emptyList();
        }
        // Overwrite: delete previous matches for this user
        matchRepository.deleteByUserId(userId);
        // Persist top-N matches
        ranked.forEach(dto -> {
            Match match = new Match(userId, dto.id(), dto.score(), dto.explanation(), String.join(",", dto.commonPreferences()));
            matchRepository.save(match);
        });
        // Return all matched users in order (just UserDTOs for compatibility)
        return ranked.stream()
                .map(dto -> userServiceClient.getUser(dto.id()))
                .filter(java.util.Objects::nonNull)
                .toList();
    }

    /**
     * Retrieve previously stored matches for a user, ordered by best score.
     */
    public List<MatcherDTO> getMatches(String userId) {
        return matchRepository.findByUserIdOrderByScoreDesc(userId).stream()
                .map(matchingservice.mapper.MatchMapper::toDto)
                .toList();
    }
}
