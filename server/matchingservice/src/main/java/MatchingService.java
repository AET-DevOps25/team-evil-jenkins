package matchingservice;

import org.springframework.stereotype.Service;

@Service
public class MatchingService {
    // Add matching logic here
    public String findMatch(String userId) {
        // TODO: Implement matching logic
        return "Matched user for " + userId;
    }
}
