package matchingservice.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MatchResponse(@JsonProperty("ranked_ids") List<String> rankedIds) {
}
