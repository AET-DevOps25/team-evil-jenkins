package model;

import java.time.Instant;
import java.util.List;

public record MatcherDTO(String matchedUserId,
                         Double score,
                         String explanation,
                         List<String> commonPreferences,
                         Instant createdAt) {
}
